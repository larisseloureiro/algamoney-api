package com.example.algamoney.api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.LancamentoModel;
import com.example.algamoney.api.repository.filter.LancamentoFilter;
import com.example.algamoney.api.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<LancamentoModel, Long>, LancamentoRepositoryQuery{
	public Optional<LancamentoModel> findByPessoa_Codigo(Long Codigo);

	public Page<LancamentoModel> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
}
