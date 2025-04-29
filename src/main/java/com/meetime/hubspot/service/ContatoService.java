package com.meetime.hubspot.service;


import com.meetime.hubspot.dto.ContatoRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ContatoService {

    @Value("${hubspot.apiUrl}")
    private String apiUrlHubspot;

    private final RestTemplate clienteRest = new RestTemplate();

    public ResponseEntity<?> registrarContato(ContatoRequestDTO dadosContato, String tokenAcesso) {
        String url = apiUrlHubspot + "/crm/v3/objects/contacts";

        HttpHeaders cabecalhos = new HttpHeaders();
        cabecalhos.setContentType(MediaType.APPLICATION_JSON);
        cabecalhos.setBearerAuth(tokenAcesso);

        // Construção do payload no formato esperado pela API do HubSpot
        var propriedades = new HashMap<String, Object>();
        propriedades.put("email", dadosContato.getEmail());
        propriedades.put("firstname", dadosContato.getNome());
        propriedades.put("lastname", dadosContato.getSobrenome());
        propriedades.put("phone", dadosContato.getTelefone());

        var corpoRequisicao = new HashMap<String, Object>();
        corpoRequisicao.put("properties", propriedades);

        HttpEntity<Map<String, Object>> requisicao = new HttpEntity<>(corpoRequisicao, cabecalhos);

        return clienteRest.postForEntity(url, requisicao, String.class);
    }
}

