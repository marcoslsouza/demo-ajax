package com.mballem.demoajax.web.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mballem.demoajax.domain.Categoria;
import com.mballem.demoajax.domain.Promocao;
import com.mballem.demoajax.dto.PromocaoDTO;
import com.mballem.demoajax.repository.PromocaoRepository;
import com.mballem.demoajax.service.CategoriaService;
import com.mballem.demoajax.service.PromocaoDataTablesService;
import com.mballem.demoajax.service.PromocaoService;

@Controller
@RequestMapping("/promocao")
public class PromocaoController {
	
	private static Logger log = LoggerFactory.getLogger(PromocaoController.class);
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private PromocaoService promocaoService;
	
	@Autowired
	private PromocaoRepository promocaoRepository;
	
	@GetMapping("/add")
	public String abrirCadastro() {
		return "promo-add";
	}
	
	@ModelAttribute("categorias")
	public List<Categoria> getCategoria(ModelMap modelMap) {
		return categoriaService.buscarTodasCategorias();
	}
	
	@PostMapping("/save") // "?" e não "Promocao", porque temos "return ResponseEntity.unprocessableEntity().body(errors);", este é um retorno diferente de "Promocao", por isso usamos um generico "?"
	public ResponseEntity<?> salvarPromocao(@Valid Promocao promocao, BindingResult result) {
		log.info("Promocao {}", promocao.toString());
		
		if(result.hasErrors()) {
			Map<String, String> errors = new HashMap<>(); // Campo, mensagem
			for(FieldError error : result.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.unprocessableEntity().body(errors); // Retorna error status 422 (Sempre que for fazer validação usar "ResponseEntity.unprocessableEntity().body(errors)")
		}
			
		// Seta a data de cadastro
		promocao.setDtCadastro(LocalDate.now());
		promocaoService.salvar(promocao);
		return ResponseEntity.ok().build(); // Envia a mensagem de ok pelo status 200
	}
	
	@GetMapping("/list")
	public String listarOfertas(ModelMap model) {
		PageRequest pageRequest = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "dtCadastro"));
		model.addAttribute("promocoes", promocaoService.buscarTodos(pageRequest));
		return "promo-list";
	}
	
	@GetMapping("/list/ajax")
	public String listarCards(@RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber, ModelMap model, 
			@RequestParam(name = "site", defaultValue = "") String site) {
		PageRequest pageRequest = PageRequest.of(pageNumber, 4, Sort.by(Sort.Direction.DESC, "dtCadastro"));
		if(site.isEmpty())
			model.addAttribute("promocoes", promocaoService.buscarTodos(pageRequest));
		else
			model.addAttribute("promocoes", promocaoService.buscarPorSite(site, pageRequest));
		return "promo-card"; // Como é o javascript que está requisitando então é enviado o código html da página promo-card
	}
	
	// Add Likes
	@PostMapping("/like/{id}")
	public ResponseEntity<?> adicionarLikes(@PathVariable("id") Long id) {
		promocaoService.atualizarSomaDeLikes(id);
		int likes = promocaoService.buscarLikesPorId(id);
		return ResponseEntity.ok(likes);
	}
	
	// ===============================================DATATABLE========================================================================
	
	@GetMapping("/tabela")
	public String showTabela() {
		return "promo-datatables";
	}
	
	@GetMapping("/datatables/server")
	public ResponseEntity<?> datatables(HttpServletRequest request) {
		Map<String, Object> data = new PromocaoDataTablesService().execute(promocaoRepository, request);
		return ResponseEntity.ok(data);
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> excluirPromocao(@PathVariable("id") Long id) {
		promocaoRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/edit/{id}")
	public ResponseEntity<?> preEditarPromocao(@PathVariable("id") Long id) {
		Promocao promo = promocaoRepository.findById(id).get();
		return ResponseEntity.ok(promo);
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editarPromocao(@Valid PromocaoDTO dto, BindingResult result) {
		log.info(dto.toString());
		if(result.hasErrors()) {
			Map<String, String> errors = new HashMap<>(); // Campo, mensagem
			for(FieldError error : result.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.unprocessableEntity().body(errors); // Retorna error status 422 (Sempre que for fazer validação usar "ResponseEntity.unprocessableEntity().body(errors)")
		}
		
		Promocao promo = promocaoRepository.findById(dto.getId()).get();
		promo.setCategoria(dto.getCategoria());
		promo.setDescricao(dto.getDescricao());
		promo.setLinkImagem(dto.getLinkImagem());
		promo.setPreco(dto.getPreco());
		promo.setTitulo(dto.getTitulo());
		
		promocaoRepository.save(promo);
		
		return ResponseEntity.ok().build();
	}
	
	// ===============================================AUTOCOMPLETE=====================================================================
	
	@GetMapping("/site")
	public ResponseEntity<?> autocompleteByTermo(@RequestParam("termo") String termo) {
		List<String> sites = promocaoService.buscarSitesPorTermo(termo);
		return ResponseEntity.ok(sites);
	}
	
	@GetMapping("/site/list")
	public String listarPorSite(@RequestParam("site") String site, ModelMap model) {
		PageRequest pageRequest = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "dtCadastro"));
		if(site.isEmpty())
			model.addAttribute("promocoes", promocaoService.buscarTodos(pageRequest));
		else
			model.addAttribute("promocoes", promocaoService.buscarPorSite(site, pageRequest));
		return "promo-card";
	}
}
