package br.com.digitalzyon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.digitalzyon.model.Cidade;
import br.com.digitalzyon.model.Estado;

public interface CidadeRepository extends JpaRepository<Cidade, Long>, CidadeRepositoryCustom {
	
	List<Cidade> findByEstadoCodigo(Long codigo);

	Optional<Cidade> findByNomeAndEstado(String nome, Estado estado);
	
	@Query("select c from Cidade c join fetch c.estado where c.codigo = :codigo")
	public Cidade findByCodigoFetchingEstado(@Param("codigo") Long codigo);	

}
