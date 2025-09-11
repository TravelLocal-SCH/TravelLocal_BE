package sch.travellocal.domain.user.dto.response;

import lombok.*;
import sch.travellocal.domain.user.enums.UserRole;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserInfoResponseDto {

    private Long id;
    private String username;
    private String name;
    private String email;
    private String gender;
    private String birthYear;
    private String mobile;
    private UserRole role;
    private String protectNumber;
}
