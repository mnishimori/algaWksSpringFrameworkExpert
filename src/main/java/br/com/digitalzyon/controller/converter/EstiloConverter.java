package br.com.digitalzyon.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import br.com.digitalzyon.model.Estilo;

public class EstiloConverter implements Converter<String, Estilo> {

	@Override
	public Estilo convert(String id) {
		if (!StringUtils.isEmpty(id)) {
			return new Estilo(Long.parseLong(id));
		}
		return null;
	}

}
