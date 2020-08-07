package br.com.digitalzyon.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import br.com.digitalzyon.service.CervejaService;
import br.com.digitalzyon.storage.FotoStorage;

@Configuration
@ComponentScan(basePackageClasses = {CervejaService.class, FotoStorage.class})
public class ServiceConfig {
	
	/*
	 * @Bean public FotoStorage fotoStorageLocal() { return new FotoStorageLocal();
	 * }
	 */

}
