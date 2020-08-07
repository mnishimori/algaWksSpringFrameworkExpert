package br.com.digitalzyon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.digitalzyon.model.Cliente;
import br.com.digitalzyon.repository.filter.ClienteFilter;

public interface ClienteRepositoryCustom {
	
	Page<Cliente> buscarClientePorClienteFilter(ClienteFilter clienteFilter, Pageable pageable);

}
