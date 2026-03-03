package com.fatec.cliente_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fatec.cliente_api.model.Cliente;
import com.fatec.cliente_api.model.ClienteDTO;
import com.fatec.cliente_api.model.Endereco;
import com.fatec.cliente_api.repository.IClienteRepository;

public class TUReq09CadastrarClienteTests {

    @Mock
    private IClienteRepository repository;

    @Mock
    private IEnderecoService enderecoService;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteDTO clienteDTO;
    private Endereco enderecoMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        clienteDTO = new ClienteDTO(
                "97521069005",
                "Joao",
                "01001000",
                "", // Endereço vazio, será preenchido pelo serviço
                "Sé",
                "São Paulo",
                "1",
                "joao@gmail.com");

        enderecoMock = new Endereco();
        enderecoMock.setCep("01001000");
        enderecoMock.setLogradouro("Praça da Sé");
        enderecoMock.setBairro("Sé");
        enderecoMock.setLocalidade("São Paulo");
        enderecoMock.setUf("SP");
    }

    @Test
    void ct01_cadastrar_cliente_com_sucesso() {
        // Arrange
        when(repository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(enderecoService.obtemLogradouroPorCep(anyString())).thenReturn(Optional.of(enderecoMock));
        when(repository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Cliente clienteSalvo = clienteService.cadastrar(clienteDTO);

        // Assert
        assertNotNull(clienteSalvo);
        assertEquals(clienteDTO.cpf(), clienteSalvo.getCpf());
        assertEquals("Praça da Sé", clienteSalvo.getEndereco()); // Validando que o endereço foi preenchido corretamente
        verify(repository).save(any(Cliente.class));
    }

    @Test
    void ct02_cadastrar_cliente_cpf_duplicado_lanca_excecao() {
        // Arrange
        when(repository.findByCpf(clienteDTO.cpf())).thenReturn(Optional.of(new Cliente()));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.cadastrar(clienteDTO);
        });

        assertEquals("Cliente com este CPF já cadastrado.", exception.getMessage());
        verify(repository, never()).save(any(Cliente.class));
    }

    @Test
    void ct03_cadastrar_cliente_cep_invalido_lanca_excecao() {
        // Arrange
        when(repository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(enderecoService.obtemLogradouroPorCep(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.cadastrar(clienteDTO);
        });

        assertEquals("Endereço não encontrado para o CEP informado.", exception.getMessage());
        verify(repository, never()).save(any(Cliente.class));
    }
}
