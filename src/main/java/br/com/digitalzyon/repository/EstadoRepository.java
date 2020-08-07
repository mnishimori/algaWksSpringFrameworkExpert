package br.com.digitalzyon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.digitalzyon.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long>{
	
	Optional<Estado> findByNome(String nome);
	
	Optional<Estado> findByNomeIgnoreCase(String nome);
	
	Optional<Estado> findByNomeContaining(String nome);
	
	Optional<Estado> findBySigla(String sigla);
	
	Optional<Estado> findBySiglaIgnoreCase(String sigla);

}
