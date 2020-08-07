package br.com.digitalzyon.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.com.digitalzyon.model.Usuario;
import br.com.digitalzyon.model.Venda;
import br.com.digitalzyon.repository.UsuarioRepository;
import br.com.digitalzyon.repository.VendaRepository;
import br.com.digitalzyon.service.exception.ValidacaoException;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private VendaRepository vendaRepository;
	
	@Transactional
	public void salvar(Usuario usuario) throws ValidacaoException {
		Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
		if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
			throw new ValidacaoException("E-mail já cadastrado");
		}
		
		if (usuario.isNovo() && StringUtils.isEmpty(usuario.getSenha())) {
			throw new ValidacaoException("Senha é obrigatória para novo usuário");
		}
		
		if (usuario.isNovo() || !StringUtils.isEmpty(usuario.getSenha())) {
			usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
		} else if (StringUtils.isEmpty(usuario.getSenha())) {
			usuario.setSenha(usuarioExistente.get().getSenha());
		}
		usuario.setConfirmacaoSenha(usuario.getSenha());
		
		if (!usuario.isNovo() && usuario.getAtivo() == null) {
			usuario.setAtivo(usuarioExistente.get().getAtivo());
		}
		
		usuarioRepository.save(usuario);
	}

	@Transactional
	public void alterarStatus(Long[] codigos, StatusUsuario statusUsuario) {
		statusUsuario.executar(codigos, usuarioRepository);
	}

	@Transactional
	public void excluir(Usuario usuario) throws ValidacaoException {
		this.validarExclusaoUsuario(usuario);
		
		usuarioRepository.delete(usuario);
		
	}

	private void validarExclusaoUsuario(Usuario usuario) throws ValidacaoException {
		Optional<Venda> vendaOptional = vendaRepository.findByUsuario(usuario);
		if (vendaOptional.isPresent()) {
			throw new ValidacaoException("O usuário não pode ser excluído pois já cadastrou vendas.");
		}
	}

}
