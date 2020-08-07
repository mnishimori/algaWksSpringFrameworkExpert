package br.com.digitalzyon.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.digitalzyon.model.Cerveja;
import br.com.digitalzyon.model.Estilo;
import br.com.digitalzyon.repository.EstiloRepository;
import br.com.digitalzyon.service.exception.ValidacaoException;

@Service
public class EstiloService {
	
	@Autowired
	private EstiloRepository estiloRepository;
	
	@Autowired
	private CervejaService cervejaService;
	
	
	@Transactional
	public Estilo salvar(Estilo estilo) {
		
		Optional<Estilo> estiloOptional = estiloRepository.findByNomeIgnoreCase(estilo.getNome());
		if (estiloOptional.isPresent()) {
			throw new ValidacaoException("Nome do estilo já cadastrado");
		}
		
		return estiloRepository.saveAndFlush(estilo);
	}
	
	public Page<Estilo> buscarEstilos(Estilo estilo, Pageable pageable){
		
		return estiloRepository.findByEstilo(estilo, pageable);
		
	}

	@Transactional
	public void excluir(Estilo estilo) throws ValidacaoException {
		this.verificarEstiloEmCerveja(estilo);

		estiloRepository.delete(estilo);
	}
	
	private void verificarEstiloEmCerveja(Estilo estilo) throws ValidacaoException {
		String cervejas = cervejaService.buscarCervejaPorEstilo(estilo)
				.stream().map(Cerveja::getNome).collect(Collectors.joining(", "));

		if (cervejas != null && !cervejas.isEmpty()) {
			throw new ValidacaoException(
					"Este estilo não pode ser excluído pois está cadastrado em " + cervejas);
		}
	}
	

}
