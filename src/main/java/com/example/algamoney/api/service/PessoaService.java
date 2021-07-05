package com.example.algamoney.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.PessoaModel;
import com.example.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	PessoaRepository pessoaRepository;

	public PessoaModel atualizar(Long codigo, PessoaModel pessoa) {
		Optional<PessoaModel> pessoaSalva = buscarPessoaPeloCodigo(codigo);

		// ta copiando de pessoaModel para pessoaSalva ignorando o codigo
		BeanUtils.copyProperties(pessoa, pessoaSalva.get(), "codigo");
		return pessoaRepository.save(pessoaSalva.get());
	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Optional<PessoaModel> pessoaSalva = buscarPessoaPeloCodigo(codigo);
		pessoaSalva.get().setAtivo(ativo);
		pessoaRepository.save(pessoaSalva.get());
	}
	
	//codigo utilizado para atualizar e atualizarPropriedade sem duplicar.
	public Optional<PessoaModel> buscarPessoaPeloCodigo(Long codigo) {
		Optional<PessoaModel> pessoaSalva = pessoaRepository.findById(codigo);

		if (pessoaSalva.isEmpty()) {
			throw new EmptyResultDataAccessException(1);
		}
		return pessoaSalva;
	}
}
