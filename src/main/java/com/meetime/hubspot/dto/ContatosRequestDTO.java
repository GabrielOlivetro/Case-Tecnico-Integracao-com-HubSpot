package com.meetime.hubspot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ContatosRequestDTO {
    private List<ContatoRequestDTO> contatos = new ArrayList<>();

    public List<ContatoRequestDTO> getContatos() {
        return contatos;
    }

    public void setContatos(List<ContatoRequestDTO> contatos) {
        this.contatos = (contatos != null) ? contatos : new ArrayList<>();
    }
}
