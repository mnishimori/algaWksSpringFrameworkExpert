package br.com.digitalzyon.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "estilo")
public class Estilo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6856257665682226684L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Informe a descrição")
	@Size(min = 1, max = 50, message = "A descrição deve possuir entre 1 e 50 caracteres")
	private String nome;
	
	@OneToMany(mappedBy = "estilo")
	private List<Cerveja> cervejas;
	
	public Estilo() {
	}
	
	public Estilo(Long id) {
		this.setId(id);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estilo other = (Estilo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
	
	public boolean isNovo() {
		return id == null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Cerveja> getCervejas() {
		return cervejas;
	}

	public void setCervejas(List<Cerveja> cervejas) {
		this.cervejas = cervejas;
	}

}
