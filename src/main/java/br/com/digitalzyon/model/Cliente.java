package br.com.digitalzyon.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.digitalzyon.model.validation.ClienteGroupSequenceProvider;
import br.com.digitalzyon.model.validation.group.CnpjGroup;
import br.com.digitalzyon.model.validation.group.CpfGroup;

@Entity
@Table(name = "cliente")
@GroupSequenceProvider(ClienteGroupSequenceProvider.class)
public class Cliente implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5596423902767202261L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codigo;
	
	@NotBlank(message = "Nome é obrigatório")
	@Size(min = 1, max = 50, message = "O tamanho do nome deve estar entre 1 e 50")
	private String nome;
	
	@NotNull(message = "O Tipo pessoa é obrigatório")
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_pessoa")
	private TipoPessoa tipoPessoa;
	
	@NotBlank(message = "CPF/CNPJ é obrigatório")
	@CPF(groups = CpfGroup.class)
	@CNPJ(groups = CnpjGroup.class)
	@Column(name = "cpf_cnpj")
	private String cpfCnpj;
	
	private String telefone;
	
	@Email(message = "e-mail inválido")
	private String email;
	
	@JsonIgnore
	@Embedded
	private Endereco endereco;
	
	
	@PrePersist @PreUpdate
	public void prePersistPreUpdate() {
		this.cpfCnpj = TipoPessoa.removerFormatacao(this.cpfCnpj);
	}
	
	@PostLoad
	private void postLoad() {
		this.cpfCnpj = this.tipoPessoa.formatar(this.cpfCnpj); 
	}
	
	public String getCpfCnpjSemFormatacao() {
		return TipoPessoa.removerFormatacao(this.cpfCnpj);
	}
	
	public boolean isNovo() {
		return this.codigo == null;
	}
	
	
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpfCnpj() {
		return cpfCnpj;
	}
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}
	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

}
