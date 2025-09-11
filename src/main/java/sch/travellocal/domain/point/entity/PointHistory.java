package sch.travellocal.domain.point.entity;

import jakarta.persistence.*;
import lombok.*;
import sch.travellocal.domain.point.enums.PointTransactionActionType;
import sch.travellocal.domain.point.enums.PointTransactionSubjectType;
import sch.travellocal.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "point_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointTransactionActionType actionType; // EARN, USE 등 핵심 행위

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointTransactionSubjectType subjectType; // 미션, 컨텐츠 열람 등 사유

    @Column(nullable = false)
    private int pointAmount; // 변경된 포인트 양 (적립 시 +, 차감 시 -)

    @Column(nullable = false)
    private long remainPointAfter; // 결제 직후 잔여 포인트

    private Long targetId; // 관련 엔티티 ID (PointTransactionSubjectType와 연결, 참고로 관리자는 보안을 위해 id 안넣어줄거임)

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public PointHistory(
            PointTransactionActionType actionType,
            PointTransactionSubjectType subjectType,
            int pointAmount,
            long remainPointAfter,
            Long targetId,
            User user)
    {
        this.actionType = actionType;
        this.subjectType = subjectType;
        this.pointAmount = pointAmount;
        this.remainPointAfter = remainPointAfter;
        this.targetId = targetId;
        this.user = user;
    }
}
