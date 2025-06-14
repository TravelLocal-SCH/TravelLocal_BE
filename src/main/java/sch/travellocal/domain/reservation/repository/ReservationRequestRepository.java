package sch.travellocal.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sch.travellocal.domain.reservation.entity.ReservationRequest;
import sch.travellocal.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRequestRepository extends JpaRepository<ReservationRequest, Long> {

    List<ReservationRequest> findByGuideStartDateBetween(LocalDateTime start, LocalDateTime end);

    List<ReservationRequest> findByGuideAndGuideStartDateBetween(User guide, LocalDateTime start, LocalDateTime end);


}