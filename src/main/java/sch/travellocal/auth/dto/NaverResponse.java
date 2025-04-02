package sch.travellocal.auth.dto;

import java.util.Map;

public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    public NaverResponse(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getGender() {
        return attributes.get("gender").toString();
    }

    @Override
    public String getBirthYear() {
        return attributes.get("birthyear").toString();
    }

    @Override
    public String getMobile() {
        return attributes.get("mobile_e164").toString();
    }
}
