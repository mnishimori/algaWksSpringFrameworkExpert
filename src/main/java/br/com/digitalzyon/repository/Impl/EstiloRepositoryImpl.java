package br.com.digitalzyon.repository.Impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.com.digitalzyon.model.Estilo;
import br.com.digitalzyon.repository.EstiloRepositoryCustom;

public class EstiloRepositoryImpl implements EstiloRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Estilo> findByEstilo(Estilo estilo, Pageable pageable) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Estilo.class);
		
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
		
		criteria.setFirstResult(primeiroRegistro);
		criteria.setMaxResults(totalRegistrosPorPagina);
		
		Sort sort = pageable.getSort();
		if (sort != null) {
			Sort.Order order = sort.iterator().next();
			String property = order.getProperty();
			criteria.addOrder(order.isAscending() ? Order.asc(property) : Order.desc(property));
		}
		
		this.adicionarFiltro(estilo, criteria);
		
		return new PageImpl<>(criteria.list(), pageable, this.total(estilo));
	}
	
	private Long total(Estilo filtro) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Estilo.class);
		this.adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(Estilo estilo, Criteria criteria) {
		if (estilo != null) {
			if (!StringUtils.isEmpty(estilo.getNome())) {
				criteria.add(Restrictions.ilike("nome", estilo.getNome(), MatchMode.ANYWHERE));
			}
		}
	}
}
