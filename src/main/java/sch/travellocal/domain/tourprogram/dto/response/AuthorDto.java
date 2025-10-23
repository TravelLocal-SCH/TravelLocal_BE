package sch.travellocal.domain.tourprogram.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class AuthorDto {

    private Long id;
    // 사용자 닉네임
    private String name;
}
