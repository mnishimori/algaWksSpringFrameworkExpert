package br.com.digitalzyon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.digitalzyon.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioRepositoryCustom {
	
	Optional<Usuario> findByEmail(String email);
	
	List<Usuario> findByCodigoIn(Long[] codigos);

}
