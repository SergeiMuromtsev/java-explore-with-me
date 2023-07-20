package ru.practicum.requests.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventStatus;
import ru.practicum.events.priv.PrivateEventsService;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.requests.dto.ConfirmedAndRejectedRequestsListsDto;
import ru.practicum.requests.dto.EventStatusUpdateRequest;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.EventUpdateRequestResultStatus;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.repository.RequestsRepository;
import ru.practicum.users.admin.AdminUserService;
import ru.practicum.users.model.User;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.requests.mapper.RequestsMapper.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestServiceImpl implements PrivateRequestsService {

    private final RequestsRepository requestsRepository;

    private final AdminUserService adminUserService;

    private final PrivateEventsService privateEventsService;

    @Override
    public List<ParticipationRequestDto> getRequestsByUserSomeonesEvent(Long userId) {
        adminUserService.getUserById(userId);
        return toListParticipationRequestDto(requestsRepository.findParticipationRequestsByRequester_Id(userId));
    }

    @Override
    public ParticipationRequestDto createRequestsByUserSomeonesEvent(Long userId, Long eventId) {
        User user = adminUserService.getUserById(userId);
        Event event = privateEventsService.getEventById(eventId);
        List<ParticipationRequestDto> requestDtoList = getRequestsByUserSomeonesEvent(userId);

        boolean flag = false;

        if (requestDtoList != null && !requestDtoList.isEmpty()) {
            for (ParticipationRequestDto requestDto : requestDtoList) {
                if (requestDto.getEvent().equals(eventId)) {
                    flag = true;
                    break;
                }
            }
        }

        if (flag || event.getInitiator().getId().equals(user.getId()) ||
                (event.getState().equals(EventStatus.PENDING) || event.getState().equals(EventStatus.CANCELED)) ||
                ((event.getConfirmedRequests() >= event.getParticipantLimit()) && event.getParticipantLimit() != 0)) {
            throw new ConflictException("CONFLICT while creating requestDto");
        }

        ParticipationRequest request = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .eventsWithRequests(event)
                .requester(user)
                .build();

        if (!event.isRequestModeration() || (event.getParticipantLimit() == 0)) {
            request.setStatus(EventUpdateRequestResultStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        } else {
            request.setStatus(EventUpdateRequestResultStatus.PENDING);
        }
        return toParticipationRequestDto(requestsRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequestsByUserSomeonesEvent(Long userId, Long requestId) {
        ParticipationRequest request = requestsRepository.findParticipationRequestByIdAndRequester_Id(requestId, userId)
                .orElseThrow(() -> new NotFoundException("participation request not found"));
        request.setStatus(EventUpdateRequestResultStatus.CANCELED);
        return toParticipationRequestDto(requestsRepository.save(request));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getRequestsByUser(Long userId, Long eventId) {
        return toListParticipationRequestDto(requestsRepository.findParticipationRequestsByEventsWithRequests_IdAndEventsWithRequests_Initiator_Id(eventId, userId));
    }

    @Override
    public ConfirmedAndRejectedRequestsListsDto changeStatusRequestsByUser(Long userId, Long eventId, EventStatusUpdateRequest eventRequestStatusUpdateRequest) {
        List<ParticipationRequest> requestList = requestsRepository.findParticipationRequestsByEventsWithRequests_IdAndEventsWithRequests_Initiator_Id(eventId, userId);

        if (requestList.isEmpty()) {
            throw new NotFoundException("request list is empty");
        }

        for (ParticipationRequest request : requestList) {
// TODO: 15.07.2023 refactor
            boolean conditionOne = (request.getEventsWithRequests().getParticipantLimit() == 0) || !request.getEventsWithRequests().isRequestModeration();
            boolean conditionTwo = request.getEventsWithRequests().getConfirmedRequests() >= request.getEventsWithRequests().getParticipantLimit();
            boolean conditionThree = request.getStatus().equals(EventUpdateRequestResultStatus.PENDING);

            if (conditionOne) {
                request.setStatus(EventUpdateRequestResultStatus.CONFIRMED);
                request.getEventsWithRequests().setConfirmedRequests(request.getEventsWithRequests().getConfirmedRequests() + 1);
                requestsRepository.save(request);
            }

            if (conditionThree) {
                if (conditionTwo) {
                    request.setStatus(EventUpdateRequestResultStatus.REJECTED);
                    requestsRepository.save(request);
                } else {
                    request.setStatus(eventRequestStatusUpdateRequest.getStatus());
                    if (request.getStatus().equals(EventUpdateRequestResultStatus.CONFIRMED)) {
                        request.getEventsWithRequests().setConfirmedRequests(request.getEventsWithRequests().getConfirmedRequests() + 1);
                    }
                    requestsRepository.save(request);
                }
            } else {
                throw new ConflictException("");
            }
        }
        return toEventRequestStatusUpdateResult(requestList);
    }
}
