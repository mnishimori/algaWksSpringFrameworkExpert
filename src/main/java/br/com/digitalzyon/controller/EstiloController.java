package br.com.digitalzyon.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.digitalzyon.controller.page.PageWrapper;
import br.com.digitalzyon.model.Estilo;
import br.com.digitalzyon.repository.EstiloRepository;
import br.com.digitalzyon.service.EstiloService;
import br.com.digitalzyon.service.exception.ValidacaoException;

@Controller
@RequestMapping("/estilo")
public class EstiloController {
	
	private static final Logger logger = LoggerFactory.getLogger(EstiloController.class);
	
	@Autowired
	private EstiloRepository estiloRepository;
	
	@Autowired
	private EstiloService estiloService;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Estilo estilo) {
		if (logger.isDebugEnabled()) {
			logger.error("Log de info do objeto " + estilo.toString());
		}
		ModelAndView modelAndView = new ModelAndView("estilo/CadastroEstilo");
		return modelAndView;
	}
	
	@RequestMapping(value = { "/novo", "{\\d+}" }, method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Estilo estilo, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return this.novo(estilo);
		}
		
		try {
			estiloService.salvar(estilo);
		} catch (ValidacaoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return this.novo(estilo);
		}
		
		redirectAttributes.addFlashAttribute("mensagem", "Estilo salvo com sucesso!");
		return new ModelAndView("redirect:/estilo/novo");
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<?> salvar(@RequestBody @Valid Estilo estilo, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(result.getFieldError("nome").getDefaultMessage());
		}
		estilo = estiloService.salvar(estilo);
		return ResponseEntity.ok(estilo);
	}
	
	@GetMapping
	public ModelAndView pesquisar(Estilo estilo, BindingResult result, @PageableDefault(size = 2) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView modelAndView = new ModelAndView("estilo/pesquisaEstilos");
		
		PageWrapper<Estilo> pageWrapper = new PageWrapper<>(estiloService.buscarEstilos(estilo, pageable),
				httpServletRequest);
		modelAndView.addObject("pagina", pageWrapper);
		
		return modelAndView;
	}
	
	@GetMapping("/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Estilo estilo = estiloRepository.findById(id).get(); // estiloRepository.findOne(id);
		ModelAndView mv = this.novo(estilo);
		mv.addObject(estilo);
		return mv;
	}
	
	@DeleteMapping("/{id}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("id") Estilo estilo){
		try {
			estiloService.excluir(estilo);
		} catch (ValidacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}

}
