package br.com.digitalzyon.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import br.com.digitalzyon.model.Estado;

public class EstadoConverter implements Converter<String, Estado>  {

	@Override
	public Estado convert(String id) {
		if (!StringUtils.isEmpty(id)) {
			return new Estado(Long.parseLong(id));
		}
		return null;
	}

}
