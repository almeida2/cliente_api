package com.fatec.cliente_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fatec.cliente_api.model.Endereco;

public class TUEnderecoServiceTests {

    private EnderecoService enderecoService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        enderecoService = new EnderecoService(restTemplate);
    }

    @Test
    void ct01_obtemLogradouroPorCep_com_sucesso() {
        // Arrange
        String cep = "01001000";
        Endereco mockEndereco = new Endereco();
        mockEndereco.setCep(cep);
        mockEndereco.setLogradouro("Praça da Sé");
        mockEndereco.setBairro("Sé");
        mockEndereco.setLocalidade("São Paulo");
        mockEndereco.setUf("SP");

        ResponseEntity<Endereco> responseEntity = new ResponseEntity<>(mockEndereco, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://viacep.com.br/ws/{cep}/json/"),
                eq(HttpMethod.GET),
                any(),
                eq(Endereco.class),
                eq(cep)))
                .thenReturn(responseEntity);

        // Act
        Optional<Endereco> resultado = enderecoService.obtemLogradouroPorCep(cep);

        // Assert
        assertTrue(resultado.isPresent(), "O endereço deveria estar presente");
        assertEquals("Praça da Sé", resultado.get().getLogradouro());
    }

    @Test
    void ct02_obtemLogradouroPorCep_quando_nao_encontrado() {
        // Arrange
        String cep = "00000000";
        Endereco mockEndereco = new Endereco(); // Endereco fields are null by default

        ResponseEntity<Endereco> responseEntity = new ResponseEntity<>(mockEndereco, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://viacep.com.br/ws/{cep}/json/"),
                eq(HttpMethod.GET),
                any(),
                eq(Endereco.class),
                eq(cep)))
                .thenReturn(responseEntity);

        // Act
        Optional<Endereco> resultado = enderecoService.obtemLogradouroPorCep(cep);

        // Assert
        assertTrue(resultado.isEmpty(), "O endereço não deveria estar presente para um CEP inválido/vazio");
    }
}
