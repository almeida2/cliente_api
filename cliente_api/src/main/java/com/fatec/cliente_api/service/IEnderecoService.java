package com.fatec.cliente_api.service;

import java.util.Optional;

import com.fatec.cliente_api.model.Endereco;

/**
 * Interface para obter o endereco da api ViaCEP
 * 
 * @param cep
 * @return o json da api ViaCEP
 */
public interface IEnderecoService {
    public Optional<Endereco> obtemLogradouroPorCep(String cep);

}