package br.com.digitalzyon.repository.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import br.com.digitalzyon.dto.CervejaDTO;
import br.com.digitalzyon.dto.ValorItensEstoqueDTO;
import br.com.digitalzyon.model.Cerveja;
import br.com.digitalzyon.repository.CervejaRepositoryCustom;
import br.com.digitalzyon.repository.filter.CervejaFilter;
import br.com.digitalzyon.repository.paginacao.JpaUtil;
import br.com.digitalzyon.storage.FotoStorage;

public class CervejaRepositoryImpl implements CervejaRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private JpaUtil<Cerveja> jpaUtil;
	
	@Autowired
	private FotoStorage fotoStorage;

	@Override
	public Page<Cerveja> buscarPorCervejaFilter(CervejaFilter cervejaFilter, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Cerveja> criteriaQuery = criteriaBuilder.createQuery(Cerveja.class);
		Root<Cerveja> rootCerveja = criteriaQuery.from(Cerveja.class);
		rootCerveja.fetch("estilo");
		
		List<Predicate> predicates = new ArrayList<>();
		this.gerarParametrosPesquisa(cervejaFilter, criteriaBuilder, rootCerveja, predicates);
		
		criteriaQuery.select(rootCerveja).where(predicates.toArray(new Predicate[] {}));
		jpaUtil.configurarOrdenacao(criteriaBuilder, criteriaQuery, rootCerveja, pageable);
		
		TypedQuery<Cerveja> query = entityManager.createQuery(criteriaQuery);
		jpaUtil.configurarPaginacao(query, pageable);
		
		return new PageImpl<Cerveja>(query.getResultList(), pageable,
				this.quantidadeTotalCervejas(cervejaFilter));
	}

	private Integer quantidadeTotalCervejas(CervejaFilter cervejaFilter) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Cerveja> criteriaQuery = criteriaBuilder.createQuery(Cerveja.class);
		Root<Cerveja> rootCerveja = criteriaQuery.from(Cerveja.class);
		rootCerveja.fetch("estilo");
		
		List<Predicate> predicates = new ArrayList<>();
		this.gerarParametrosPesquisa(cervejaFilter, criteriaBuilder, rootCerveja, predicates);
		
		criteriaQuery.select(rootCerveja).where(predicates.toArray(new Predicate[] {}));
		
		TypedQuery<Cerveja> query = entityManager.createQuery(criteriaQuery);
		return query.getResultList().size();
	}

	private void gerarParametrosPesquisa(CervejaFilter cervejaFilter, CriteriaBuilder criteriaBuilder,
			Root<Cerveja> rootCerveja, List<Predicate> predicates) {
		if (cervejaFilter != null) {
			if (!StringUtils.isEmpty(cervejaFilter.getSku()) && !cervejaFilter.getSku().trim().isEmpty()) {
				predicates.add(criteriaBuilder.equal(rootCerveja.get("sku"), cervejaFilter.getSku().trim()));
			}
			if (!StringUtils.isEmpty(cervejaFilter.getNome()) && !cervejaFilter.getNome().trim().isEmpty()) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.upper(rootCerveja.get("nome")), "%" + cervejaFilter.getNome().trim().toUpperCase() + "%"));
			}
			if (cervejaFilter.getEstilo() != null && cervejaFilter.getEstilo().getId() != null && cervejaFilter.getEstilo().getId() > 0) {
				predicates.add(criteriaBuilder.equal(rootCerveja.get("estilo"), cervejaFilter.getEstilo()));
			}
			if (cervejaFilter.getSabor() != null) {
				predicates.add(criteriaBuilder.equal(rootCerveja.get("sabor"), cervejaFilter.getSabor()));
			}
			if (cervejaFilter.getOrigem() != null) {
				predicates.add(criteriaBuilder.equal(rootCerveja.get("origem"), cervejaFilter.getOrigem()));
			}
			if (cervejaFilter.getValorDe() != null) {
				predicates.add(criteriaBuilder.ge(rootCerveja.get("valor"), cervejaFilter.getValorDe()));
			}
			if (cervejaFilter.getValorAte() != null) {
				predicates.add(criteriaBuilder.le(rootCerveja.get("valor"), cervejaFilter.getValorAte()));
			}
		}
	}

	@Override
	public List<CervejaDTO> porSkuOuNome(String skuOuNome) {
		String jpql = "select new br.com.digitalzyon.dto.CervejaDTO(id, sku, nome, origem, valor, foto) "
				+ "from Cerveja where lower(sku) like lower(:skuOuNome) or lower(nome) like lower(:skuOuNome)";
		List<CervejaDTO> cervejasFiltradas = entityManager.createQuery(jpql, CervejaDTO.class)
					.setParameter("skuOuNome", skuOuNome + "%")
					.getResultList();
		cervejasFiltradas
			.forEach(c -> c.setUrlThumbnailFoto(fotoStorage.getUrl(FotoStorage.THUMBNAIL_PREFIX + c.getFoto())));
		return cervejasFiltradas;
	}

	@Override
	public ValorItensEstoqueDTO valorItensEstoque() {
		String query = "select new br.com.digitalzyon.dto.ValorItensEstoqueDTO(sum(valor * quantidadeEstoque), sum(quantidadeEstoque)) from Cerveja";
		return entityManager.createQuery(query, ValorItensEstoqueDTO.class).getSingleResult();
	}
	
	/* 
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Cerveja> filtrar(CervejaFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cerveja.class);
		
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getSku())) {
				criteria.add(Restrictions.eq("sku", filtro.getSku()));
			}
			
			if (!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}

			if (isEstiloPresente(filtro)) {
				criteria.add(Restrictions.eq("estilo", filtro.getEstilo()));
			}

			if (filtro.getSabor() != null) {
				criteria.add(Restrictions.eq("sabor", filtro.getSabor()));
			}

			if (filtro.getOrigem() != null) {
				criteria.add(Restrictions.eq("origem", filtro.getOrigem()));
			}

			if (filtro.getValorDe() != null) {
				criteria.add(Restrictions.ge("valor", filtro.getValorDe()));
			}

			if (filtro.getValorAte() != null) {
				criteria.add(Restrictions.le("valor", filtro.getValorAte()));
			}
		}
		
		return criteria.list();
	}
	
	private boolean isEstiloPresente(CervejaFilter filtro) {
		return filtro.getEstilo() != null && filtro.getEstilo().getCodigo() != null;
	}	
	 */
}
