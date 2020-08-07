package br.com.digitalzyon.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import br.com.digitalzyon.model.Cidade;

public class CidadeConverter implements Converter<String, Cidade> {

	@Override
	public Cidade convert(String id) {
		if (!StringUtils.isEmpty(id)) {
			return new Cidade(Long.parseLong(id));
		}
		return null;
	}
}
