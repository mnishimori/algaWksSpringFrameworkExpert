package br.com.digitalzyon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.digitalzyon.model.Cidade;

public interface CidadeRepositoryCustom {
	
	Page<Cidade> buscarCidades(Cidade cidade, Pageable pageable);

}
