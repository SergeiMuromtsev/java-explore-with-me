package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.requests.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestsRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findParticipationRequestsByRequester_Id(Long userId);

    Optional<ParticipationRequest> findParticipationRequestByIdAndRequester_Id(Long requestId, Long userId);

    List<ParticipationRequest> findParticipationRequestsByEventsWithRequests_IdAndEventsWithRequests_Initiator_Id(Long eventId, Long userId);
}