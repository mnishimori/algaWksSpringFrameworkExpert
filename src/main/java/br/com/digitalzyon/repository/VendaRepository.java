package br.com.digitalzyon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import br.com.digitalzyon.model.Usuario;
import br.com.digitalzyon.model.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long>, QueryByExampleExecutor<Venda>, VendaRepositoryCustom {
	
	Optional<Venda> findByCodigo(Long codigo);
	
	Optional<Venda> findByUsuario(Usuario usuario);

}
