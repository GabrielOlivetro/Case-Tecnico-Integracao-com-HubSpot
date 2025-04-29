package com.meetime.hubspot.service;

import com.meetime.hubspot.config.HubspotPropertiesConfig;
import com.meetime.hubspot.dto.ContatoRequestDTO;
import com.meetime.hubspot.dto.TokenStorageDTO;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

import static io.github.resilience4j.ratelimiter.RateLimiter.decorateRunnable;

@Service
public class HubspotIntegrationService {
    @Autowired
    private WebClient clientHubspot;
    private final TokenStorageDTO tokenStorage;
    private final RateLimiter limitadorRequisicoes;

    public HubspotIntegrationService(WebClient.Builder construtorClient,
                                     TokenStorageDTO gerenciadorTokens,
                                     RateLimiterRegistry registroLimitadores,
                                     HubspotPropertiesConfig propriedadesHubspot) {
        this.clientHubspot = construtorClient.baseUrl(propriedadesHubspot.getApiUrl()).build();
        this.tokenStorage = gerenciadorTokens;
        this.limitadorRequisicoes = registroLimitadores.rateLimiter("limiteContatosHubspot");
    }

    public boolean criarContato(ContatoRequestDTO requisicaoContato) {
        String tokenAcesso = tokenStorage.getAccessToken();

        Map<String, Object> dadosContato = Map.of(
                "properties", Map.of(
                        "email", requisicaoContato.getEmail(),
                        "nome", requisicaoContato.getNome(),
                        "sobrenome", requisicaoContato.getSobrenome(),
                        "telefone", requisicaoContato.getTelefone()
                )
        );

        Runnable chamadaCriacaoContato = () -> {
            try {
                clientHubspot.post()
                        .uri("/crm/v3/objects/contacts")
                        .header("Authorization", "Bearer " + tokenAcesso)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dadosContato)
                        .retrieve()
                        .toBodilessEntity()
                        .block();
            } catch (WebClientResponseException ex) {
                System.out.println("Erro ao criar contato: " + ex.getResponseBodyAsString());
                throw ex;
            }
        };

        try {
            decorateRunnable(limitadorRequisicoes, chamadaCriacaoContato).run();
            return true;
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
            return false;
        }
    }
}

