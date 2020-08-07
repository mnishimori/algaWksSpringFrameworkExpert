package br.com.digitalzyon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.digitalzyon.model.Estilo;

public interface EstiloRepositoryCustom {
	
	Page<Estilo> findByEstilo(Estilo estilo, Pageable pageable);

}
