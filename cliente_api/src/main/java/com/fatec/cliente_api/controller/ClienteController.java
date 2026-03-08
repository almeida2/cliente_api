package com.fatec.cliente_api.controller;

import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.cliente_api.model.Cliente;
import com.fatec.cliente_api.model.ClienteDTO;
import com.fatec.cliente_api.model.Endereco;
import com.fatec.cliente_api.service.IClienteService;
import com.fatec.cliente_api.service.IEnderecoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Tag(name = "ClienteController", description = "Endpoints para gerenciamento de clientes")
@CrossOrigin("*") // desabilita o cors do spring security
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {
    Logger logger = LogManager.getLogger(this.getClass());

    IClienteService clienteService;
    IEnderecoService enderecoService;

    // injecao da dependencia pelo metodo construtor
    public ClienteController(IClienteService clienteService, IEnderecoService enderecoService) {
        this.clienteService = clienteService;
        this.enderecoService = enderecoService;
    }
    /*
     * As informacoes do cliente sao recebidas em um arquivo DTO entidades podem
     * conter informacoes sensiveis que não devem ser expostas diretamente para o
     * app cliente
     * As informacoes de endereco sao fornecidas automaticamente na interface
     * alteracoes do usuario na interface serão cadastradas.
     */

    @Operation(summary = "Cadastra um novo cliente", description = "Recebe um DTO de cliente e persiste no banco de dados após validações")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex: CPF ou CEP inválido)"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<ResponseApi<Cliente>> saveCliente(@RequestBody ClienteDTO clienteDTO) {
        logger.info(">>>>>> apicontroller cadastro de cliente iniciado...");

        try {
            Cliente novoCliente = clienteService.cadastrar(clienteDTO);
            ResponseApi<Cliente> response = new ResponseApi<>(novoCliente, "Cliente cadastrado com sucesso.");
            logger.info(">>>>>> apicontroller cliente cadastrado");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            // Captura exceção de CEP inválido
            ResponseApi<Cliente> response = new ResponseApi<>(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            // Captura qualquer outro erro inesperado
            ResponseApi<Cliente> response = new ResponseApi<>("Erro inesperado ao cadastrar cliente.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{cep}")
    public ResponseEntity<ResponseApi<Endereco>> consultarCep(@PathVariable String cep) {
        Optional<Endereco> e = enderecoService.obtemLogradouroPorCep(cep);
        if (e.isPresent()) {
            ResponseApi<Endereco> response = new ResponseApi<>(e.get(), "Endereco obtido com sucesso.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            ResponseApi<Endereco> response = new ResponseApi<>("CEP não encontrado.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
