package com.example.algamoney.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import com.example.algamoney.api.model.LancamentoModel;
import com.example.algamoney.api.model.LancamentoModel_;
import com.example.algamoney.api.repository.filter.LancamentoFilter;


public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<LancamentoModel> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<LancamentoModel> criteria = builder.createQuery(LancamentoModel.class);
		Root<LancamentoModel> root = criteria.from(LancamentoModel.class);

		// criar restrições
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		TypedQuery<LancamentoModel> query = manager.createQuery(criteria);
		
		//paginação
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	
	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<LancamentoModel> root) {
		List<Predicate> predicates = new ArrayList<>();
		
		if (!ObjectUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(
					builder.lower(root.get(LancamentoModel_.descricao)), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}
		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(
					builder.greaterThanOrEqualTo(root.get(LancamentoModel_.dataVencimento), lancamentoFilter.getDataVencimentoDe()));
		}
		if (lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(
					builder.lessThanOrEqualTo(root.get(LancamentoModel_.dataVencimento), lancamentoFilter.getDataVencimentoAte()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	//paginação
		private Long total(LancamentoFilter lancamentoFilter) {
			CriteriaBuilder builder = manager.getCriteriaBuilder();
			CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
			Root<LancamentoModel> root = criteria.from(LancamentoModel.class);
			
			Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
			criteria.where(predicates);
			
			criteria.select(builder.count(root));
			return manager.createQuery(criteria).getSingleResult();
		}


		private void adicionarRestricoesDePaginacao(TypedQuery<LancamentoModel> query, Pageable pageable) {
			int paginaAtual = pageable.getPageNumber();
			int totalRegistrosPorPagina = pageable.getPageSize();
			int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
			
			query.setFirstResult(primeiroRegistroDaPagina);
			query.setMaxResults(totalRegistrosPorPagina);
			
		}

}
