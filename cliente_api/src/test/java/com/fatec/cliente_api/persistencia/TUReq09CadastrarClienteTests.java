package com.fatec.cliente_api.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.cliente_api.model.Cliente;

@SpringBootTest
public class TUReq09CadastrarClienteTests {
    // entidade
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
    }

    @Test
    void ct01_dadosValidos_cadastro_realizado_com_sucesso() {
        cliente.setNome("Joao");
        cliente.setCpf("97521069005");
        cliente.setEmail("joao@gmail.com");
        cliente.setCep("01001000");
        cliente.setEndereco("Praça da Sé");
        cliente.setBairro("Sé");
        cliente.setCidade("São Paulo");
        cliente.setComplemento("1");
        cliente.setDataCadastro();
        assertNotNull(cliente);
    }

    @Test
    void ct02_nomeInvalido_mensagem_de_erro_retornada() {
        try {
            cliente.setNome("");
        } catch (IllegalArgumentException e) {
            assertEquals("O nome não deve estar em branco", e.getMessage());
        }

    }

    @Test
    void ct03_nomeInvalido_mensagem_de_erro_retornada() {
        try {
            cliente.setNome(null);
        } catch (IllegalArgumentException e) {
            assertEquals("O nome não deve estar em branco", e.getMessage());
        }

    }

}
