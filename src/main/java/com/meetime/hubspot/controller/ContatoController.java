package com.meetime.hubspot.controller;

import com.meetime.hubspot.dto.ContatoRequestDTO;
import com.meetime.hubspot.dto.ContatosRequestDTO;
import com.meetime.hubspot.service.ContatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(ContatoController.class);

    @Autowired
    private ContatoService contatoService;

    @Operation(
            summary = "Registra um contato único no HubSpot",
            description = "Necessário fornecer um Bearer Token OAuth2 para criar um contato no CRM do HubSpot. Exemplo do corpo esperado:\n{\n  \"email\": \"exemplo@email.com\",\n  \"nome\": \"João\",\n  \"sobrenome\": \"Silva\",\n  \"telefone\": \"11999999999\"\n}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contato registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição malformada"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou ausente"),
            @ApiResponse(responseCode = "500", description = "Erro interno do sistema")
    })
    @PostMapping("/contato")
    public ResponseEntity<?> adicionarContato(
            @Valid @RequestBody ContatoRequestDTO dto,
            @Parameter(hidden = true) @RequestHeader("Authorization") String cabecalhoAutorizacao) {
        logger.info("Recebido payload para criação de contato único: {}");

        if (cabecalhoAutorizacao == null || !cabecalhoAutorizacao.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("O cabeçalho de autorização deve estar no formato: Bearer {token_de_acesso}");
        }

        String tokenBearer = cabecalhoAutorizacao.replace("Bearer ", "").trim();
        return contatoService.registrarContato(dto, tokenBearer);
    }

    @Operation(
            summary = "Registra múltiplos contatos no HubSpot",
            description = "Necessário fornecer um Bearer Token OAuth2 para criar vários contatos no CRM do HubSpot. Exemplo do corpo esperado:\n{\n  \"contatos\": [\n    {\n      \"email\": \"exemplo1@email.com\",\n      \"nome\": \"João\",\n      \"sobrenome\": \"Silva\",\n      \"telefone\": \"11999999999\"\n    },\n    {\n      \"email\": \"exemplo2@email.com\",\n      \"nome\": \"Maria\",\n      \"sobrenome\": \"Oliveira\",\n      \"telefone\": \"11888888888\"\n    }\n  ]\n}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contatos registrados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição malformada"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou ausente"),
            @ApiResponse(responseCode = "500", description = "Erro interno do sistema")
    })
    @PostMapping
    public ResponseEntity<?> adicionarVariosContatos(
            @Valid @RequestBody ContatosRequestDTO dto,
            @Parameter(hidden = true) @RequestHeader("Authorization") String cabecalhoAutorizacao) {
        logger.info("Recebido payload para criação de múltiplos contatos: {}");

        if (cabecalhoAutorizacao == null || !cabecalhoAutorizacao.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("O cabeçalho de autorização deve estar no formato: Bearer {token_de_acesso}");
        }

        if (dto.getContatos() == null || dto.getContatos().isEmpty()) {
            return ResponseEntity.badRequest().body("A lista de contatos não pode estar vazia ou nula.");
        }

        String tokenBearer = cabecalhoAutorizacao.replace("Bearer ", "").trim();
        List<Object> resultados = new ArrayList<>();
        for (ContatoRequestDTO contatoDTO : dto.getContatos()) {
            logger.info("Processando contato individual: {}");
            ResponseEntity<?> resposta = contatoService.registrarContato(contatoDTO, tokenBearer);
            resultados.add(resposta.getBody());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(resultados);
    }
}

