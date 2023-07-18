package ru.practicum.requests.mapper;

import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.dto.ConfirmedAndRejectedRequestsListsDto;
import ru.practicum.requests.model.EventUpdateRequestResultStatus;
import ru.practicum.requests.model.ParticipationRequest;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RequestsMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .created(participationRequest.getCreated().toString().replace("T", " ").substring(0, participationRequest.getCreated().toString().indexOf(".")))
                .event(participationRequest.getEventsWithRequests().getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus())
                .build();
    }

    public static List<ParticipationRequestDto> toListParticipationRequestDto(List<ParticipationRequest> listParticipationRequest) {
        List<ParticipationRequestDto> listParticipationRequestDto = new ArrayList<>();
        for (ParticipationRequest request : listParticipationRequest) {
            listParticipationRequestDto.add(toParticipationRequestDto(request));
        }
        return listParticipationRequestDto;
    }

    public static ConfirmedAndRejectedRequestsListsDto toEventRequestStatusUpdateResult(List<ParticipationRequest> requestList) {
        List<ParticipationRequestDto> requestsConfirmed = new ArrayList<>();
        List<ParticipationRequestDto> requestsRejected = new ArrayList<>();

        for (ParticipationRequest request : requestList) {
            if (request.getStatus().equals(EventUpdateRequestResultStatus.CONFIRMED)) {
                requestsConfirmed.add(toParticipationRequestDto(request));
            }
            if (request.getStatus().equals(EventUpdateRequestResultStatus.REJECTED)) {
                requestsRejected.add(toParticipationRequestDto(request));
            }
        }

        return ConfirmedAndRejectedRequestsListsDto
                .builder()
                .confirmedRequests(requestsConfirmed)
                .rejectedRequests(requestsRejected)
                .build();
    }
}
