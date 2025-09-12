package sch.travellocal.domain.place.entity;

import jakarta.persistence.*;
import lombok.*;
import sch.travellocal.domain.user.entity.User;

@Entity
@Table(name = "place_user_permission")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
// GPS로 Place에 위치한 사용자에게 리뷰 작성 권한을 부여하는 엔티티
public class PlaceUserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;
}
