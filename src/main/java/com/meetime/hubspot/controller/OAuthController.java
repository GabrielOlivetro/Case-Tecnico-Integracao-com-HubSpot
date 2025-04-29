package com.meetime.hubspot.controller;

import com.meetime.hubspot.dto.RespostaTokenOAuthDTO;
import com.meetime.hubspot.dto.TokenStorageDTO;
import com.meetime.hubspot.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/autenticacao")
public class OAuthController {

    @Autowired
    private OAuthService servicoAutenticacao;

    @Autowired
    private TokenStorageDTO tokenStorage;

    @Operation(summary = "Obtém a URL HubSpot OAuth2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL gerada com sucesso.")
    })
    @GetMapping("/url-autorizacao")
    public ResponseEntity<Map<String, String>> obterUrlAutorizacao() {
        String urlAutenticacao = servicoAutenticacao.gerarUrlAutenticacao();
        return ResponseEntity.ok(Map.of("url", urlAutenticacao));
    }

    @Operation(summary = "Realiza o callback OAuth2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token obtido com sucesso."),
            @ApiResponse(responseCode = "400", description = "Código inválido ou expirado."),
            @ApiResponse(responseCode = "500", description = "Erro interno.")
    })
    @GetMapping("/callback")
    public ResponseEntity<RespostaTokenOAuthDTO> processarCallbackAutenticacao(@RequestParam String codigoAutorizacao) {
        RespostaTokenOAuthDTO respostaToken = servicoAutenticacao.trocarCodigoPorToken(codigoAutorizacao);
        tokenStorage.setAccessToken(respostaToken.getAccessToken());
        return ResponseEntity.ok(respostaToken);
    }
}

