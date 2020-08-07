package br.com.digitalzyon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.com.digitalzyon.model.Cidade;
import br.com.digitalzyon.model.Cliente;
import br.com.digitalzyon.repository.CidadeRepository;
import br.com.digitalzyon.repository.ClienteRepository;
import br.com.digitalzyon.repository.filter.ClienteFilter;
import br.com.digitalzyon.service.exception.ValidacaoException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Transactional
	public void salvar(Cliente cliente) throws ValidacaoException {
		if (cliente.isNovo()) {
			this.clienteRepository.findByCpfCnpj(cliente.getCpfCnpjSemFormatacao())
						 .ifPresent(c -> {
							 throw new ValidacaoException("CPF/CNPJ já cadastrado"); 
						 });
		}
		
		clienteRepository.save(cliente);
	}
	
	public Page<Cliente> buscarClientePorClienteFilter(ClienteFilter clienteFilter, Pageable pageable) {
		return clienteRepository.buscarClientePorClienteFilter(clienteFilter, pageable);
	}
	
	public List<Cliente> findByNomeStartingWithIgnoreCase(String nome) throws IllegalArgumentException {
		this.validarTamanhoNome(nome);
		return clienteRepository.findByNomeStartingWithIgnoreCase(nome);
	}

	private void validarTamanhoNome(String nome) {
		if (StringUtils.isEmpty(nome) || nome.trim().length() < 3) {
			throw new IllegalArgumentException();
		}
	}
	
	@Transactional(readOnly = true)
	public void comporDadosEndereco(Cliente cliente) {
		if (cliente.getEndereco() == null 
				|| cliente.getEndereco().getCidade() == null
				|| cliente.getEndereco().getCidade().getCodigo() == null) {
			return;
		}

		Cidade cidade = this.cidadeRepository.findByCodigoFetchingEstado(cliente.getEndereco().getCidade().getCodigo());

		cliente.getEndereco().setCidade(cidade);
		cliente.getEndereco().setEstado(cidade.getEstado());
	}	

	@Transactional
	public void excluir(Cliente cliente) {
		try {
			clienteRepository.delete(cliente);
			clienteRepository.flush();
		} catch (ValidacaoException e) {
			throw new ValidacaoException("O cliente não pode ser excluído pois já possui venda cadastrada!");
		}
	}

}
