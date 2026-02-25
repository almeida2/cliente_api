package com.fatec.cliente_api.service;

import java.util.Optional;

import com.fatec.cliente_api.model.Endereco;

public interface IEnderecoService {
    public Optional<Endereco> obtemLogradouroPorCep(String cep);

}