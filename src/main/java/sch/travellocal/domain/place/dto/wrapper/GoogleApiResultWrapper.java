package sch.travellocal.domain.place.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sch.travellocal.domain.place.dto.GoogleApiResultDto;

/**
 * google map api의 result 래핑
 *
 * google map api가 반환하는 JSON의 구조
 * {
 *   "result": { ... }
 * }
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleApiResultWrapper {

    private GoogleApiResultDto result;
}
