package com.fatec.cliente_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.cliente_api.model.Cliente;
import com.fatec.cliente_api.model.ClienteDTO;
import com.fatec.cliente_api.repository.IClienteRepository;

@SpringBootTest
public class TIReq09CadastrarClienteTests {

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IClienteRepository repository;

    @BeforeEach
    void cleanUp() {
        // Limpa o banco de dados antes de cada teste para garantir isolamento
        repository.deleteAll();
    }

    @Test
    void ct01_cadastrar_cliente_com_sucesso() {

        ClienteDTO clienteDTO = new ClienteDTO(
                "26501199000",
                "Maria Oliveira",
                "04280130",
                "Rua Frei João",
                "Vila Nair",
                "São Paulo",
                "100",
                "maria@test.com");

        // Act
        Cliente novoCliente = clienteService.cadastrar(clienteDTO);

        // 1. Confirma que um id foi gerado no db
        assertNotNull(novoCliente.getId());
        assertEquals("26501199000", novoCliente.getCpf());

        // 2. Confirma que a integração com ViaCEP preencheu o logradouro
        // O CEP 04280130 corresponde à "Rua Frei João"
        assertEquals("Rua Frei João", novoCliente.getEndereco());

        // 3. Confirma que o registro existe no banco de dados real (H2)
        Optional<Cliente> c = repository.findByCpf("26501199000");
        assertTrue(c.isPresent());
    }

    @Test
    void ct02_cadastrar_cliente_cep_inexistente_integracao_real() {
        // Arrange
        ClienteDTO clienteDTO = new ClienteDTO(
                "12345678901",
                "Jose Silva",
                "00000000", // CEP que não existe
                "",
                "Bairro",
                "Cidade",
                "1",
                "jose@test.com");

        // Act & Assert
        try {
            clienteService.cadastrar(clienteDTO);
        } catch (IllegalArgumentException e) {
            assertEquals("Endereço não encontrado para o CEP informado.", e.getMessage());
        }

        // Garante que nada foi salvo no banco
        assertEquals(0, repository.count());
    }
}
