package br.com.digitalzyon.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.digitalzyon.controller.page.PageWrapper;
import br.com.digitalzyon.model.Cidade;
import br.com.digitalzyon.repository.CidadeRepository;
import br.com.digitalzyon.repository.EstadoRepository;
import br.com.digitalzyon.service.CidadeService;
import br.com.digitalzyon.service.exception.ValidacaoException;

@Controller
@RequestMapping("/cidades")
public class CidadeController {
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;

	@RequestMapping("/nova")
	public ModelAndView nova(Cidade cidade) {
		ModelAndView mv = new ModelAndView("cidade/CadastroCidade");
		mv.addObject("estados", estadoRepository.findAll());
		return mv;
	}
	
	// @Cacheable(value = "cidades", key = "#codigo")
	@Cacheable(value = "cidades")
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Cidade> pesquisarCidadesPorCodigoEstado(
			@RequestParam(name = "estado", defaultValue = "-1") Long codigo) {
		List<Cidade> cidades = new ArrayList<>();
		if (codigo != null && codigo > 0) {
			cidades = cidadeRepository.findByEstadoCodigo(codigo);
		}
		return cidades;
	}
	
	//@CacheEvict(value = "cidades", key = "#cidade.estado.codigo", condition = "#cidade.temEstado()") // allEntries = true
	@CacheEvict(value = "cidades", allEntries = true) // 
	@PostMapping({"/nova", "/nova/{codigo}"})
	public ModelAndView salvar(@Valid Cidade cidade, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return nova(cidade);
		}
		try {
			cidadeService.salvar(cidade);
		} catch (ValidacaoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return nova(cidade);
		}
		
		attributes.addFlashAttribute("mensagem", "Cidade salva com sucesso!");
		return new ModelAndView("redirect:/cidades/nova");
	}

	@GetMapping
	public ModelAndView pesquisar(Cidade cidadeFilter, BindingResult result, @PageableDefault Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("cidade/pesquisaCidades");
		mv.addObject("estados", estadoRepository.findAll());
		
		PageWrapper<Cidade> pageWrapper = new PageWrapper<Cidade>(cidadeService.buscarCidades(cidadeFilter, pageable),
				httpServletRequest);
		mv.addObject("pagina", pageWrapper);
		
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public ResponseEntity<?> excluir(@PathVariable("codigo") Cidade cidade) {
		try {
			this.cidadeService.excluir(cidade);
		} catch (ValidacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView atualizar(@PathVariable("codigo") Cidade cidade) {
		ModelAndView mv = this.nova(cidade);
		mv.addObject(this.cidadeRepository.findByCodigoFetchingEstado(cidade.getCodigo()));
		return mv;
	}	
	
}
