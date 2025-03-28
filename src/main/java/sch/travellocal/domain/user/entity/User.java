package sch.travellocal.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sch.travellocal.domain.user.enums.UserRole;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
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

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

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
