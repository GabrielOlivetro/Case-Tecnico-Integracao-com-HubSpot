package com.meetime.hubspot.service;

import com.meetime.hubspot.config.HubspotPropertiesConfig;
import com.meetime.hubspot.dto.RespostaTokenOAuthDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OAuthService {

    private final HubspotPropertiesConfig properties;
    private final WebClient webClient;

    public OAuthService(HubspotPropertiesConfig properties) {
        this.properties = properties;
        this.webClient = WebClient.builder().build();
    }

    public String gerarUrlAutenticacao() {
        return UriComponentsBuilder.fromHttpUrl(properties.getAuthorizationUrl())
                .queryParam("client_id", properties.getClientId())
                .queryParam("redirect_uri", properties.getRedirectUri())
                .queryParam("scope", properties.getScopes())
                .build()
                .toUriString();
    }

    public RespostaTokenOAuthDTO trocarCodigoPorToken(String code) {
        return webClient.post()
                .uri(properties.getTokenUrl())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters
                        .fromFormData("grant_type", "authorization_code")
                        .with("client_id", properties.getClientId())
                        .with("client_secret", properties.getClientSecret())
                        .with("redirect_uri", properties.getRedirectUri())
                        .with("code", code))
                .retrieve()
                .bodyToMono(RespostaTokenOAuthDTO.class)
                .block();
    }
}
