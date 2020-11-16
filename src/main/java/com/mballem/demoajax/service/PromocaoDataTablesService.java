package com.mballem.demoajax.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.mballem.demoajax.domain.Promocao;
import com.mballem.demoajax.repository.PromocaoRepository;

public class PromocaoDataTablesService {
	
	private String[] cols = {"id", "titulo", "site", "linkPromocao", "descricao", "linkImagem", "preco", "likes", "dtCadastro", "categoria"};
	
	public Map<String, Object> execute(PromocaoRepository repository, HttpServletRequest request) {
		
		int start = Integer.parseInt(request.getParameter("start")); // Informação do número da página
		int lenght = Integer.parseInt(request.getParameter("length")); // Informação da quantidade de itens
		int draw = Integer.parseInt(request.getParameter("draw")); // Incrementa a cada paginação. Valor inicial 0
		
		int current = currentPage(start, lenght);
		String column = columnName(request);
		Sort.Direction direction = orderBy(request);
		String search = searchBy(request); 
		
		Pageable pageable = PageRequest.of(current, lenght, direction, column);
		
		Page<Promocao> page = queryBy(search, repository, pageable);
		
		Map<String, Object> json = new LinkedHashMap<>();
		json.put("draw", draw);
		json.put("recordsTotal", page.getTotalElements());
		json.put("recordsFiltered", page.getTotalElements());
		json.put("data", page.getContent()); // Lista de promoções
		
		return json;
	}
	
	private Page<Promocao> queryBy(String search, PromocaoRepository repository, Pageable pageable) {
		
		if(search.isEmpty()) 
			return repository.findAll(pageable);
		
		// Testar se é um valor monentario, para pesquisar em preco
		if(search.matches("^[0-9]+([.,][0-9]{2})?$")) {
			search = search.replace(",", "."); // Como o valor é bigdecimal, se for enviar os centavos separados por virgula teremos uma exceção.
			return repository.findByPreco(new BigDecimal(search), pageable); // O search é uma String então convertemos para bigdecimal.
		}
		
		return repository.findByTituloOrSiteOrCategoria(search, pageable);
	}

	private String searchBy(HttpServletRequest request) {
		
		return request.getParameter("search[value]").isEmpty() ? "" : request.getParameter("search[value]");
	}
	
	private Direction orderBy(HttpServletRequest request) {
		String order = request.getParameter("order[0][dir]");
		Sort.Direction sort = Sort.Direction.ASC;
		if(order.equalsIgnoreCase("desc"))
			sort = Sort.Direction.DESC;
		return sort;
	}

	private String columnName(HttpServletRequest request) {
		int iCol = Integer.parseInt(request.getParameter("order[0][column]"));
		return cols[iCol];
	}

	private int currentPage(int start, int lenght) {
		return start / lenght;
	}
}
