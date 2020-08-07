package br.com.digitalzyon.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.digitalzyon.dto.CervejaDTO;
import br.com.digitalzyon.dto.ValorItensEstoqueDTO;
import br.com.digitalzyon.model.Cerveja;
import br.com.digitalzyon.repository.filter.CervejaFilter;

public interface CervejaRepositoryCustom {
	
	Page<Cerveja> buscarPorCervejaFilter(CervejaFilter cervejaFilter, Pageable pageable);
	
	List<CervejaDTO> porSkuOuNome(String skuOuNome);
	
	public ValorItensEstoqueDTO valorItensEstoque();

}
