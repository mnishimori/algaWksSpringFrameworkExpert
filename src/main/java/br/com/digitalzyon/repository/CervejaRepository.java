package br.com.digitalzyon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import br.com.digitalzyon.model.Cerveja;
import br.com.digitalzyon.model.Estilo;

@Repository
public interface CervejaRepository extends JpaRepository<Cerveja, Long>, QueryByExampleExecutor<Cerveja>, CervejaRepositoryCustom {
	
	Optional<Cerveja> findByCodigo(Long codigo);
	
	Optional<Cerveja> findBySku(String sku);
	
	List<Cerveja> findByEstilo(Estilo estilo);
	
}
