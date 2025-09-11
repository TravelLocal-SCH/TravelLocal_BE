package sch.travellocal.domain.place.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/**
 * 필요한 데이터만 바인딩
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleApiResultDto {

    // front에게 제공할 데이터
    private String name;

    @JsonProperty("user_ratings_total")
    private int reviewCount;

    @JsonProperty("formatted_phone_number")
    private String formattedPhoneNumber;

    private double rating;

    private String url;

    @JsonProperty("address_components")
    private List<AddressComponent> addressComponents;

    @JsonProperty("opening_hours")
    private OpeningHours openingHours;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressComponent {

        @JsonProperty("long_name")
        private String longName;

        private List<String> types;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpeningHours {

        @JsonProperty("weekday_text")
        private List<String> weekdayText;
    }
}


