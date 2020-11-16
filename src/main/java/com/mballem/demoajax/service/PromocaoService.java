package com.mballem.demoajax.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mballem.demoajax.domain.Promocao;

public interface PromocaoService {

	public void salvar(Promocao promocao);
	public Page<Promocao> buscarTodos(Pageable pageable);
	public void atualizarSomaDeLikes(Long id);
	public int buscarLikesPorId(Long id);
	List<String> buscarSitesPorTermo(String site);
	Page<Promocao> buscarPorSite(String site, Pageable pageable);
}
