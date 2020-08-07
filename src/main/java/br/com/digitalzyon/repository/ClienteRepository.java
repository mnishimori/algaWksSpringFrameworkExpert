package br.com.digitalzyon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.digitalzyon.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>, ClienteRepositoryCustom {
	
	Optional<Cliente> findByNomeIgnoreCase(String nome);
	
	Optional<Cliente> findByNomeContaining(String nome);

	Optional<Cliente> findByCpfCnpj(String cpfCnpj);

	List<Cliente> findByNomeStartingWithIgnoreCase(String nome);

}
