package br.com.digitalzyon.repository.Impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.com.digitalzyon.model.Cidade;
import br.com.digitalzyon.repository.CidadeRepositoryCustom;
import br.com.digitalzyon.repository.paginacao.PaginacaoUtil;

public class CidadeRepositoryImpl implements CidadeRepositoryCustom{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Cidade> buscarCidades(Cidade cidade, Pageable pageable) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Cidade.class);
		
		paginacaoUtil.preparar(criteria, pageable);
		this.adicionarFiltro(cidade, criteria);
		criteria.createAlias("estado", "e", JoinType.LEFT_OUTER_JOIN);
		
		return new PageImpl<>(criteria.list(), pageable, total(cidade));
	}

	private Long total(Cidade filtro) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Cidade.class);
		this.adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(Cidade filtro, Criteria criteria) {
		if (filtro != null) {
			if (filtro.getEstado() != null) {
				criteria.add(Restrictions.eq("estado", filtro.getEstado()));
			}
			
			if (!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}
		}
	}
}
