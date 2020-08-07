package br.com.digitalzyon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import br.com.digitalzyon.model.Estilo;

@Repository
public interface EstiloRepository extends JpaRepository<Estilo, Long>, QueryByExampleExecutor<Estilo>, EstiloRepositoryCustom{
	
	Optional<Estilo> findById(Long id);
	
	Optional<Estilo> findByNome(String nome);
	
	Optional<Estilo> findByNomeIgnoreCase(String nome);
	
	Optional<Estilo> findByNomeContaining(String nome);
	
}
