package sch.travellocal.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sch.travellocal.domain.reservation.entity.ReservationRequest;

public interface ReservationRequestRepository extends JpaRepository<ReservationRequest, Long> {

}