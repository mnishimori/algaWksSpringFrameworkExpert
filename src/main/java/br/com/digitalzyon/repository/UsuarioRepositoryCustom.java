package br.com.digitalzyon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.digitalzyon.model.Usuario;
import br.com.digitalzyon.repository.filter.UsuarioFilter;

public interface UsuarioRepositoryCustom {
	
	public Optional<Usuario> porEmailEAtivo(String email);
	
	List<String> permissoes(Usuario usuario);
	
	Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable);
	
	Usuario buscarComGrupos(Long codigo);

}
