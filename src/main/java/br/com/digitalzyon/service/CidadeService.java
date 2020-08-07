package br.com.digitalzyon.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.digitalzyon.model.Cidade;
import br.com.digitalzyon.repository.CidadeRepository;
import br.com.digitalzyon.service.exception.ValidacaoException;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Transactional
	public void salvar(Cidade cidade) {
		Optional<Cidade> optCidade = cidadeRepository.findByNomeAndEstado(cidade.getNome(), cidade.getEstado());
		if (optCidade.isPresent()) {
			throw new ValidacaoException("Nome de cidade já cadastrado");
		}
		cidadeRepository.save(cidade);
	}
	
	public Page<Cidade> buscarCidades(Cidade cidade, Pageable pageable){
		return cidadeRepository.buscarCidades(cidade, pageable);
	}

	@Transactional
	public void excluir(Cidade cidade) {
		try {
			this.cidadeRepository.delete(cidade);
			this.cidadeRepository.flush();
		} catch (PersistenceException e) {
			throw new ValidacaoException("Impossível apagar cidade. O registro está sendo usado.");
		}
	}

}
