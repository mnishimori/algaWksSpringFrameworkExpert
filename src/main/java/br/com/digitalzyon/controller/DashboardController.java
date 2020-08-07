package br.com.digitalzyon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.digitalzyon.repository.CervejaRepository;
import br.com.digitalzyon.repository.ClienteRepository;
import br.com.digitalzyon.repository.VendaRepository;

@Controller
public class DashboardController {
	
	@Autowired
	private VendaRepository vendaRepository;
	@Autowired
	private CervejaRepository cervejaRepository;
	@Autowired
	private ClienteRepository clienteRepository;

	@GetMapping("/")
	public ModelAndView dashboard() {
		ModelAndView mv = new ModelAndView("Dashboard");
		
		mv.addObject("vendasNoAno", vendaRepository.valorTotalNoAno());
		mv.addObject("vendasNoMes", vendaRepository.valorTotalNoMes());
		mv.addObject("ticketMedio", vendaRepository.valorTicketMedioNoAno());
		
		mv.addObject("valorItensEstoque", cervejaRepository.valorItensEstoque());
		mv.addObject("totalClientes", clienteRepository.count());
		
		return mv;
	}
	
}
