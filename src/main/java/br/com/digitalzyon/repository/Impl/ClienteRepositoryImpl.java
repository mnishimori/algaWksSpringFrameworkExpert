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

import br.com.digitalzyon.model.Cliente;
import br.com.digitalzyon.repository.ClienteRepositoryCustom;
import br.com.digitalzyon.repository.filter.ClienteFilter;
import br.com.digitalzyon.repository.paginacao.PaginacaoUtil;

public class ClienteRepositoryImpl implements ClienteRepositoryCustom{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> buscarClientePorClienteFilter(ClienteFilter clienteFilter, Pageable pageable) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Cliente.class);
		
		paginacaoUtil.preparar(criteria, pageable);
		this.adicionarFiltro(clienteFilter, criteria);
		criteria.createAlias("endereco.cidade", "c", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("c.estado", "e", JoinType.LEFT_OUTER_JOIN);
		
		return new PageImpl<>(criteria.list(), pageable, total(clienteFilter));
	}
	
	private Long total(ClienteFilter filtro) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Cliente.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(ClienteFilter clienteFilter, Criteria criteria) {
		if (clienteFilter != null) {
			if (!StringUtils.isEmpty(clienteFilter.getNome())) {
				criteria.add(Restrictions.ilike("nome", clienteFilter.getNome(), MatchMode.ANYWHERE));
			}

			if (!StringUtils.isEmpty(clienteFilter.getCpfCnpj())) {
				criteria.add(Restrictions.eq("cpfCnpj", clienteFilter.getCpfCnpjSemFormatacao()));
			}
		}
	}

}
