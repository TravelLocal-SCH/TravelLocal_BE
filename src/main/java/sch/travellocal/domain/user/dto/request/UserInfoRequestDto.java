package sch.travellocal.domain.user.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import sch.travellocal.domain.user.enums.UserRole;

@Getter
public class UserInfoRequestDto {

    private String name;
    private String email;
    private String gender;
    private String birthYear;
    private String mobile;
    private UserRole role;
    private String protectNumber;
}
