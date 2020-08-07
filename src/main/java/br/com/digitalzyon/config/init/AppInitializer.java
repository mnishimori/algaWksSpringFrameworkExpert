package br.com.digitalzyon.config.init;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import br.com.digitalzyon.config.JPAConfig;
import br.com.digitalzyon.config.SecurityConfig;
import br.com.digitalzyon.config.ServiceConfig;
import br.com.digitalzyon.config.WebConfig;

/**
 * Configuração inicial da aplicação do Spring DispatcherServlet (front controller do spring), 
 * para receber a requisição e identificar o seu destino.
 * @author akio
 *
 */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/**
	 * Recupera configurações para o backend da aplicação. 
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { JPAConfig.class, ServiceConfig.class, SecurityConfig.class };
	}

	/**
	 * Recupera configurações para o frontend da aplicação. 
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}

	/**
	 * Padrão da URL que será delegado ao DispatcherServlet. 
	 * Qualquer nome após o barra da URL será delegado ao DispatcherServlet.
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	
	/**
	 * Filtro para forçar os caracteres do encoding UTF-8.
	 */
	protected Filter[] getServletFilters() {
		HttpPutFormContentFilter httpPutFormContentFilter = new HttpPutFormContentFilter();
        return new Filter[] { httpPutFormContentFilter };
	}
	
	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setMultipartConfig(new MultipartConfigElement(""));
	}
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		servletContext.setInitParameter("spring.profiles.default", "local");
	}	

}
