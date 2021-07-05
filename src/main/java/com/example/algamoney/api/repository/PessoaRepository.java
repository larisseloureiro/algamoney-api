package com.example.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.PessoaModel;

public interface PessoaRepository extends JpaRepository<PessoaModel, Long>{

}
