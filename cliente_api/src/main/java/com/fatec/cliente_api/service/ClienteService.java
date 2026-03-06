package com.fatec.cliente_api.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fatec.cliente_api.model.Cliente;
import com.fatec.cliente_api.model.ClienteDTO;
import com.fatec.cliente_api.model.Endereco;
import com.fatec.cliente_api.repository.IClienteRepository;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ClienteService implements IClienteService {
    Logger logger = LogManager.getLogger(this.getClass());
    private IClienteRepository repository;
    private IEnderecoService enderecoService;

    // Injeção de dependências pelo construtor
    public ClienteService(IClienteRepository clienteRepository, IEnderecoService enderecoService) {
        this.repository = clienteRepository;
        this.enderecoService = enderecoService;
    }

    @Override
    public List<Cliente> consultaTodos() {
        return null;
    }

    @Transactional
    public Cliente cadastrar(ClienteDTO clienteDTO) {
        // 1. Verifica se o cliente já existe com base no CPF
        if (repository.findByCpf(clienteDTO.cpf()).isPresent()) {
            logger.info(">>>>>> clienteservico - cliente já cadastrado: " + clienteDTO.cpf());
            // Lança uma exceção personalizada para CPF duplicado
            throw new IllegalArgumentException("Cliente com este CPF já cadastrado.");
        }

        // 2. Busca o endereço pelo CEP. Se não encontrar, lança exceção.
        Optional<Endereco> endereco = enderecoService.obtemLogradouroPorCep(clienteDTO.cep());
        if (endereco.isEmpty()) {
            logger.info(">>>>>> Endereço não encontrado para o CEP: " + clienteDTO.cep());
            throw new IllegalArgumentException("Endereço não encontrado para o CEP informado.");
        }

        // 3. Converte DTO para entidade e persiste
        // as informacoes de endereco sao fornecidas automaticamente diretamente na
        // interface o endereco, bairro, cidade sao obtidos novamente do viacep
        Cliente novoCliente = new Cliente();
        novoCliente.setCpf(clienteDTO.cpf());
        novoCliente.setNome(clienteDTO.nome());
        novoCliente.setCep(clienteDTO.cep());
        novoCliente.setEndereco(endereco.get().getLogradouro());
        novoCliente.setBairro(endereco.get().getBairro());
        novoCliente.setCidade(endereco.get().getLocalidade());
        novoCliente.setComplemento(clienteDTO.complemento());
        novoCliente.setEmail(clienteDTO.email());
        novoCliente.setDataCadastro();

        logger.info(">>>>>> clienteservico - tentativa de executar o metodo save no repositório.");
        return repository.save(novoCliente);
    }

    @Override
    public Optional<Cliente> consultarPorCpf(String cpf) {
        return Optional.empty();
    }

    @Override
    public Optional<Cliente> atualizar(ClienteDTO cliente) {
        return Optional.empty();
    }

    @Override
    public boolean excluir(String cpf) {
        return false;
    }
}
