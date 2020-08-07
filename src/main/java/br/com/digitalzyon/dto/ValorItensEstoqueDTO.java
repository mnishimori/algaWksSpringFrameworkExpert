package br.com.digitalzyon.dto;

import java.math.BigDecimal;

public class ValorItensEstoqueDTO {
	
	private BigDecimal valor;
	private Long totalItens;
	
	public ValorItensEstoqueDTO() {
	}
	
	public ValorItensEstoqueDTO(BigDecimal valor, Long totalItens) {
		this.valor = valor;
		this.totalItens = totalItens;
	}
	
	public BigDecimal getValor() {
		return valor != null ? valor : BigDecimal.ZERO;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Long getTotalItens() {
		return totalItens != null ? totalItens : 0L;
	}

	public void setTotalItens(Long totalItens) {
		this.totalItens = totalItens;
	}
}
