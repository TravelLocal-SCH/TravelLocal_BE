package sch.travellocal.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import sch.travellocal.common.entity.BaseTimeEntity;
import sch.travellocal.domain.user.enums.UserRole;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 공급자가 포함된 고유 이름
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String gender;

    @Column(name = "birth_year", nullable = false)
    private String birthYear;

    @Column(nullable = false, length = 15, unique = true)
    private String mobile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "protect_number", length = 15)
    private String protectNumber;

    // 신분증의 현재 주소를 사진으로 검증 가능하다면 추가할 컬럼
    //private String location;

    @Builder
    public User(String username, String name, String email, String gender, String birthYear, String mobile, UserRole role, String protectNumber) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthYear = birthYear;
        this.mobile = mobile;
        this.role = role;
        this.protectNumber = protectNumber;
    }
}
