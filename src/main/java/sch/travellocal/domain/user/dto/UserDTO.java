package sch.travellocal.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import sch.travellocal.domain.user.enums.UserRole;

@Getter
@Builder
public class UserDTO {

    private String name;
    private String username;
    private UserRole role;
}
