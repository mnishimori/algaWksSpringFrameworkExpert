package br.com.digitalzyon.repository.paginacao;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class JpaRepositoryUtil<T> {
	
	public void configurarPaginacao(TypedQuery<T> query, Pageable pageable) {
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
	}
	
	public void configurarOrdenacao(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery, Root<T> root,
			Pageable pageable) {
		Sort sort = pageable.getSort();
		if (sort != null) {
			Sort.Order order = sort.iterator().next();
			String property = order.getProperty();
			criteriaQuery.orderBy(order.isAscending() ? criteriaBuilder.asc(root.get(property))
					: criteriaBuilder.desc(root.get(property)));
		}
	}
	
}
