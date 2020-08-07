package br.com.digitalzyon.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.digitalzyon.dto.VendaMesDTO;
import br.com.digitalzyon.dto.VendaOrigemDTO;
import br.com.digitalzyon.model.Venda;
import br.com.digitalzyon.repository.filter.VendaFilter;

public interface VendaRepositoryCustom {
	
	Page<Venda> filtrar(VendaFilter vendaFilter, Pageable pageable);
	
	Venda buscarComItens(Long codigo);
	
	BigDecimal valorTotalNoAno();
	
	BigDecimal valorTotalNoMes();
	
	BigDecimal valorTicketMedioNoAno();
	
	List<VendaMesDTO> totalPorMes();
	
	List<VendaOrigemDTO> totalPorOrigem();

}
