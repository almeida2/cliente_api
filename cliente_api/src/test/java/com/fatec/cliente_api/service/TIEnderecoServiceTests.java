package com.fatec.cliente_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.cliente_api.model.Endereco;

@SpringBootTest
public class TIEnderecoServiceTests {

    @Autowired
    private EnderecoService enderecoService;

    @Test
    void ct01_obtemLogradouroPorCep_com_sucesso() {
        // Dado
        String cep = "01001000";

        // Quando
        Optional<Endereco> resultado = enderecoService.obtemLogradouroPorCep(cep);

        // Entao
        assertTrue(resultado.isPresent(), "O endereço deve ser retornado pela API do ViaCEP");
        assertEquals("Praça da Sé", resultado.get().getLogradouro());
        assertEquals("São Paulo", resultado.get().getLocalidade());
        assertEquals("SP", resultado.get().getUf());
    }

    @Test
    void ct02_obtemLogradouroPorCep_invalido() {
        // Arrange
        String cep = "00000000"; // CEP invalido inexistente

        // Act
        Optional<Endereco> resultado = enderecoService.obtemLogradouroPorCep(cep);

        // Assert
        assertTrue(resultado.isEmpty(), "A API deve retornar vazio para um CEP inexistente");
    }
}
