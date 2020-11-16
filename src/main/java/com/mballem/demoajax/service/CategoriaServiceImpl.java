package com.mballem.demoajax.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.demoajax.domain.Categoria;
import com.mballem.demoajax.repository.CategoriaRepository;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Categoria> buscarTodasCategorias() {
		return categoriaRepository.findAll();
	}
}
