package com.fatec.cliente_api.model;

public record ClienteDTO(String cpf, String nome, String cep, String endereco, String bairro, String cidade,
        String complemento, String email) {

}