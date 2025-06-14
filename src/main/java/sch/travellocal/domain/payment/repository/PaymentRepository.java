package sch.travellocal.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sch.travellocal.domain.payment.entity.PaymentEntity;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByImpUid(String impUid);

    boolean existsByImpUid(String impUid);
}