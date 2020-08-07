package br.com.digitalzyon.service.event.venda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.digitalzyon.model.Cerveja;
import br.com.digitalzyon.model.ItemVenda;
import br.com.digitalzyon.repository.CervejaRepository;

@Component
public class VendaListener {
	
	@Autowired
	private CervejaRepository cervejaRepository;
	
	@EventListener
	public void vendaEmitida(VendaEvent vendaEvent) {
		for (ItemVenda item : vendaEvent.getVenda().getItens()) {
			Cerveja cerveja = cervejaRepository.findByCodigo(item.getCerveja().getCodigo()).get();// findOne(item.getCerveja().getCodigo());
			cerveja.setQuantidadeEstoque(cerveja.getQuantidadeEstoque() - item.getQuantidade());
			cervejaRepository.save(cerveja);
		}
	}

}
