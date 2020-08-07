package br.com.digitalzyon.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.digitalzyon.controller.page.PageWrapper;
import br.com.digitalzyon.dto.CervejaDTO;
import br.com.digitalzyon.model.Cerveja;
import br.com.digitalzyon.model.Origem;
import br.com.digitalzyon.model.Sabor;
import br.com.digitalzyon.repository.EstiloRepository;
import br.com.digitalzyon.repository.filter.CervejaFilter;
import br.com.digitalzyon.service.CervejaService;
import br.com.digitalzyon.service.exception.ValidacaoException;

/**
 * Controller do cadastro de cervejas.
 * @author akio
 *
 */
@Controller
@RequestMapping("/cervejas")
public class CervejaController {
	
	private static final Logger logger = LoggerFactory.getLogger(CervejaController.class);

	@Autowired
	private CervejaService cervejaService;

	@Autowired
	private EstiloRepository estiloRepository;
	
	@RequestMapping("/nova")
	public ModelAndView nova(Cerveja cerveja) {
		if (logger.isDebugEnabled()) {
			logger.error("Log de info do objeto " + cerveja.toString());
		}
		ModelAndView modelAndView = new ModelAndView("cerveja/CadastroCerveja");
		modelAndView.addObject("sabores", Sabor.values());
		modelAndView.addObject("estilos", estiloRepository.findAll());
		modelAndView.addObject("origens", Origem.values());
		return modelAndView;
	}
	
	@RequestMapping(value = { "/nova", "{\\d+}" }, method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Cerveja cerveja, BindingResult result, Model model, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return nova(cerveja);
		}
		
		cervejaService.salvar(cerveja);
		attributes.addFlashAttribute("mensagem", "Cerveja salva com sucesso!");
		return new ModelAndView("redirect:/cervejas/nova");
	}
	
	@RequestMapping("/cadastro")
	public String cadastro() {
		return "cerveja/cadastro-produto";
	}
	
	@GetMapping
	public ModelAndView pesquisar(CervejaFilter cervejaFilter, BindingResult result,
			@PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView modelAndView = new ModelAndView("cerveja/pesquisaCervejas");
		modelAndView.addObject("estilos", estiloRepository.findAll());
		modelAndView.addObject("sabores", Sabor.values());
		modelAndView.addObject("origens", Origem.values());
		
		PageWrapper<Cerveja> pagina = new PageWrapper<>(cervejaService.buscarPorCervejaFilter(cervejaFilter, pageable),
				httpServletRequest); 
		modelAndView.addObject("pagina", pagina);
		return modelAndView;
	}
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<CervejaDTO> pesquisar(String skuOuNome) {
		return cervejaService.porSkuOuNome(skuOuNome);
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Cerveja cerveja){
		try {
			cervejaService.excluir(cerveja);
		} catch (ValidacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Cerveja cerveja) {
		ModelAndView mv = this.nova(cerveja);
		mv.addObject(cerveja);
		return mv;
	}
}
