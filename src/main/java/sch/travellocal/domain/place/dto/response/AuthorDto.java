package sch.travellocal.domain.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AuthorDto {

    private Long id;
    // 사용자 닉네임
    private String name;
}
