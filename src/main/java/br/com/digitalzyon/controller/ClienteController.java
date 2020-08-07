package br.com.digitalzyon.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.digitalzyon.controller.page.PageWrapper;
import br.com.digitalzyon.model.Cliente;
import br.com.digitalzyon.model.TipoPessoa;
import br.com.digitalzyon.repository.ClienteRepository;
import br.com.digitalzyon.repository.EstadoRepository;
import br.com.digitalzyon.repository.filter.ClienteFilter;
import br.com.digitalzyon.service.ClienteService;
import br.com.digitalzyon.service.exception.ValidacaoException;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Cliente cliente) {
		ModelAndView mv = new ModelAndView("cliente/CadastroCliente");
		mv.addObject("tiposPessoa", TipoPessoa.values());
		mv.addObject("estados", estadoRepository.findAll());
		return mv;
	}
	
	@PostMapping({"/novo", "{\\d+}"})
	public ModelAndView salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(cliente);
		}
		
		try {
			clienteService.salvar(cliente);
		} catch (ValidacaoException e) {
			result.rejectValue("cpfCnpj", e.getMessage(), e.getMessage());
			return novo(cliente);
		}
		
		attributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
		return new ModelAndView("redirect:/clientes/novo"); 
	}
	
	@GetMapping
	public ModelAndView pesquisar(ClienteFilter clienteFilter, BindingResult result,
			@PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("cliente/pesquisaClientes");
		mv.addObject("tiposPessoa", TipoPessoa.values());
		
		PageWrapper<Cliente> paginaWrapper = new PageWrapper<>(
				clienteService.buscarClientePorClienteFilter(clienteFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<Cliente> pesquisar(String nome) {
		return clienteService.findByNomeStartingWithIgnoreCase(nome);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Void> tratarIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().build();
	}
	
	@RequestMapping(value = {"/{codigo}"}, method = RequestMethod.GET)
	public ModelAndView editar(@PathVariable("codigo") Cliente cliente) {
		ModelAndView mv = this.novo(cliente);
		this.clienteService.comporDadosEndereco(cliente);
		mv.addObject(cliente);
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Cliente cliente) {
		try {
			clienteService.excluir(cliente);
		} catch (ValidacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping("/quantidade")
	public Long calcularQuantidadeClientes() {
		return clienteRepository.count();
	}

}
