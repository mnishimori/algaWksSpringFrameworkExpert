package br.com.digitalzyon.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.digitalzyon.dto.CervejaDTO;
import br.com.digitalzyon.model.Cerveja;
import br.com.digitalzyon.model.Estilo;
import br.com.digitalzyon.repository.CervejaRepository;
import br.com.digitalzyon.repository.filter.CervejaFilter;
import br.com.digitalzyon.service.event.cerveja.CervejaSalvaEvent;
import br.com.digitalzyon.service.exception.ValidacaoException;
import br.com.digitalzyon.storage.FotoStorage;

@Service
public class CervejaService {
	
	@Autowired
	private CervejaRepository cervejaRepository;

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private FotoStorage fotoStorage;

	@Transactional
	public void salvar(Cerveja cerveja) {
		cervejaRepository.save(cerveja);

		publisher.publishEvent(new CervejaSalvaEvent(cerveja));
	}
	
	@Transactional
	public void excluir(Cerveja cerveja) {
		try {
			String foto = cerveja.getFoto();
			cervejaRepository.delete(cerveja);
			cervejaRepository.flush();
			fotoStorage.excluir(foto);
		} catch (Exception e) {
			throw new ValidacaoException("Impossível apagar cerveja. Já foi usada em alguma venda.");
		}
	}

	public Page<Cerveja> buscarPorCervejaFilter(CervejaFilter cervejaFilter, Pageable pageable) {
		return cervejaRepository.buscarPorCervejaFilter(cervejaFilter, pageable);
	}

	public List<CervejaDTO> porSkuOuNome(String skuOuNome) {
		return cervejaRepository.porSkuOuNome(skuOuNome);
	}
	
	public List<Cerveja> buscarCervejaPorEstilo(Estilo estilo) {
		return cervejaRepository.findByEstilo(estilo);
	}
}
