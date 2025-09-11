package sch.travellocal.domain.point.entity;

import jakarta.persistence.*;
import lombok.*;
import sch.travellocal.common.exception.custom.ApiException;
import sch.travellocal.common.exception.error.ErrorCode;
import sch.travellocal.domain.user.entity.User;

@Entity
@Table(name = "user_point")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long point;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Builder
    public UserPoint(long point, User user) {
        this.point = point;
        this.user = user;
    }

    public void updateBalance(int amount) {
        if (this.point + amount < 0) {
            throw new ApiException(ErrorCode.INVALID_OPERATION, "포인트가 부족합니다.");
        }
        this.point += amount;
    }
}
