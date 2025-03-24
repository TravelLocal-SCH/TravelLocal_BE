package sch.travellocal.domain.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import sch.travellocal.domain.payment.enums.PaymentStatus;
import sch.travellocal.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_method", length = 100, nullable = false)
    private String paymentMethod;

    @Column(name = "imp_uid", unique = true, nullable = false)
    private String impUid;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "refunded_at")
    private LocalDateTime refundedAt;

    @ManyToOne
    @JoinColumn(name = "reservation_request_id", nullable = false)
    private ReservationRequest reservationRequest;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
