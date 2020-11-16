package com.mballem.demoajax.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.demoajax.domain.Promocao;
import com.mballem.demoajax.repository.PromocaoRepository;

@Service
@Transactional
public class PromocaoServiceImpl implements PromocaoService {

	@Autowired
	private PromocaoRepository promocaoRepository;

	@Override
	public void salvar(Promocao promocao) {
		promocaoRepository.save(promocao);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Promocao> buscarTodos(Pageable pageable) {
		return promocaoRepository.findAll(pageable);
	}

	@Override
	public void atualizarSomaDeLikes(Long id) {
		promocaoRepository.updateSomarLikes(id);
	}

	@Override
	public int buscarLikesPorId(Long id) {
		return promocaoRepository.findLikesById(id);
	}

	@Override
	public List<String> buscarSitesPorTermo(String termo) {
		return promocaoRepository.findSitesByTermo(termo);
	}

	@Override
	public Page<Promocao> buscarPorSite(String site, Pageable pageable) {
		return promocaoRepository.findBySite(site, pageable);
	}
}
