package com.mballem.demoajax.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mballem.demoajax.domain.Categoria;
import com.mballem.demoajax.repository.CategoriaRepository;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {
	
	private static Logger log = LoggerFactory.getLogger(CategoriaController.class);
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping("/add")
	public String abrirCadastro() {
		return "categoria-add";
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> salvarCategoria(@Valid Categoria categoria, BindingResult result) {
		
		log.info("Categoria {}", categoria.toString());
		
		if(result.hasErrors()) {
			Map<String, String> errors = new HashMap<>(); // Campo, mensagem
			for(FieldError error : result.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.unprocessableEntity().body(errors);
		}
	
		categoriaRepository.save(categoria);
		return ResponseEntity.ok().build();
	}
}

