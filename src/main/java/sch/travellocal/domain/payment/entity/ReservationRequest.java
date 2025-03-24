package sch.travellocal.domain.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import sch.travellocal.domain.payment.enums.RequestStatus;
import sch.travellocal.domain.tourprogram.entity.TourProgram;
import sch.travellocal.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_request")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "num_of_people", nullable = false)
    private int numOfPeople;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Column(name = "guide_start_date", nullable = false)
    private LocalDateTime guideStartDate;

    @Column(name = "guide_end_date", nullable = false)
    private LocalDateTime guideEndDate;

    @Column(name = "payment_method", length = 100, nullable = false)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", nullable = false)
    @ColumnDefault("'PENDING'")
    private RequestStatus requestStatus;

    @Column(name = "guide_response")
    private String guideResponse;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "tour_program_id", nullable = false)
    private TourProgram tourProgram;
}
