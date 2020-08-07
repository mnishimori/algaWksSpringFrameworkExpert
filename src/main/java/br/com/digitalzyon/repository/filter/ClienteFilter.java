package br.com.digitalzyon.repository.filter;

import br.com.digitalzyon.model.TipoPessoa;

public class ClienteFilter {
	
	private String nome;
	private String cpfCnpj;
	
	public String getCpfCnpj() {
		return cpfCnpj;
	}
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Object getCpfCnpjSemFormatacao() {
		return TipoPessoa.removerFormatacao(this.cpfCnpj);
	}

}
