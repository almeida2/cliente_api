package com.fatec.cliente_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fatec.cliente_api.model.Cliente;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpf(String cpf);

    /**
     * @Modifying avisa ao EntityManager que a operação não é um SELECT, mas sim
     *            um INSERT, UPDATE ou DELETE
     * @Query é utilizada para definir manualmente o comando JPQL ou SQL
     * 
     * @param cpf
     */
    @Modifying
    @Query("delete from Cliente c where c.cpf = :cpf")
    void deleteByCpf(String cpf);

}