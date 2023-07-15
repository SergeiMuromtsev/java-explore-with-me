package ru.practicum.requests.priv.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventStatus;
import ru.practicum.events.priv.service.PrivateEventsService;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.requests.dto.ConfirmedAndRejectedRequestsListsDto;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.EventUpdateRequestResultStatus;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.repository.RequestsRepository;
import ru.practicum.users.admin.service.AdminUserService;
import ru.practicum.users.model.User;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.requests.mapper.RequestsMapper.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestServiceImpl implements PrivateRequestsService{

    private final RequestsRepository repository;

    private final AdminUserService usersService;

    private final PrivateEventsService eventsService;

    @Override
    public List<ParticipationRequestDto> getRequestsByUserOtherEvents(int userId) {
        usersService.getUserById(userId);
        return toListParticipationRequestDto(repository.findParticipationRequestsByRequester_Id(userId));
    }

    @Override
    public ParticipationRequestDto createRequestsByUserOtherEvents(int userId, int eventId) {
        User user = usersService.getUserById(userId);
        Event event = eventsService.getEventById(eventId);
        List<ParticipationRequestDto> requestDtoList = getRequestsByUserOtherEvents(userId);

        boolean conditionOne = false;

        if (requestDtoList != null && !requestDtoList.isEmpty()) {
            for (ParticipationRequestDto requestDto : requestDtoList) {
                if (requestDto.getEvent() == eventId) {
                    conditionOne = true;
                    break;
                }
            }
        }
// TODO: 15.07.2023 refactor
        boolean conditionTwo = event.getInitiator().getId() == user.getId();
        boolean conditionThree = event.getState().equals(EventStatus.PENDING) || event.getState().equals(EventStatus.CANCELED);
        boolean conditionFour = (event.getConfirmedRequests() >= event.getParticipantLimit()) && event.getParticipantLimit() != 0;
        boolean conditionFive = !event.isRequestModeration();
        boolean conditionSix = event.getParticipantLimit() == 0;

        if (conditionOne || conditionTwo || conditionThree || conditionFour) {
            throw new ConflictException("CONFLICT creating requestdto");
        }

        ParticipationRequest request = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .eventsWithRequests(event)
                .requester(user)
                .build();

        if (conditionFive || conditionSix) {
            request.setStatus(EventUpdateRequestResultStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        } else {
            request.setStatus(EventUpdateRequestResultStatus.PENDING);
        }
        return toParticipationRequestDto(repository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequestsByUserOtherEvents(int userId, int requestId) {
        ParticipationRequest request = repository.findParticipationRequestByIdAndRequester_Id(requestId, userId)
                .orElseThrow(() -> new NotFoundException("participation request not found"));
        request.setStatus(EventUpdateRequestResultStatus.CANCELED);
        return toParticipationRequestDto(repository.save(request));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getRequestsByUser(int userId, int eventId) {
        return toListParticipationRequestDto(repository.findParticipationRequestsByEventsWithRequests_IdAndEventsWithRequests_Initiator_Id(eventId, userId));
    }

    @Override
    public ConfirmedAndRejectedRequestsListsDto changeStatusRequestsByUser(int userId, int eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        List<ParticipationRequest> requestList = repository.findParticipationRequestsByEventsWithRequests_IdAndEventsWithRequests_Initiator_Id(eventId, userId);

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
                repository.save(request);
            }

            if (conditionThree) {
                if (conditionTwo) {
                    request.setStatus(EventUpdateRequestResultStatus.REJECTED);
                    repository.save(request);
                } else {
                    request.setStatus(eventRequestStatusUpdateRequest.getStatus());
                    if (request.getStatus().equals(EventUpdateRequestResultStatus.CONFIRMED)) {
                        request.getEventsWithRequests().setConfirmedRequests(request.getEventsWithRequests().getConfirmedRequests() + 1);
                    }
                    repository.save(request);
                }
            } else {
                throw new ConflictException("");
            }
        }
        return toEventRequestStatusUpdateResult(requestList);
    }
}
