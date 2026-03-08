package com.fatec.cliente_api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.fatec.cliente_api.model.Cliente;

@DataJpaTest
@ActiveProfiles("test")
public class TIReq09CadastrarClienteTests {

    @Autowired
    private IClienteRepository repository;

    @BeforeEach
    void cleanUp() {
        // Limpa o banco de dados antes de cada teste para garantir isolamento
        repository.deleteAll();

    }

    public String obtemDataAtual() {
        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String data = dataAtual.format(pattern);
        return data;
    }

    /**
     * Validar o cadastro de um cliente com dados validos
     */
    @Test
    void ct01_cadastrar_cliente_com_sucesso() {
        Cliente cliente = new Cliente();
        cliente.setNome("Jose");
        cliente.setCpf("34525227095");
        cliente.setEmail("jose@gmail.com");
        cliente.setCep("01001000");
        cliente.setEndereco("Praça da Sé");
        cliente.setBairro("Sé");
        cliente.setCidade("São Paulo");
        cliente.setComplemento("1");
        cliente.setDataCadastro();
        Cliente novoCliente = repository.save(cliente);
        // 1. Confirma que um id foi gerado no db
        assertNotNull(novoCliente.getId());
        assertEquals("34525227095", novoCliente.getCpf());
        assertEquals(obtemDataAtual(), novoCliente.getDataCadastro());

    }

}