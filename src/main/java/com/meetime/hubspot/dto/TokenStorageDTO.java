package com.meetime.hubspot.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TokenStorageDTO {
    private String accessToken;

    public boolean hasToken() {
        return accessToken != null && !accessToken.isEmpty();
    }

    public void clearTokens() {
        this.accessToken = null;
    }
}
