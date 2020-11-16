package com.mballem.demoajax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.demoajax.domain.Promocao;

public interface PromocaoRepository extends JpaRepository<Promocao, Long> {
	
	// Método de atualização de likes
	@Transactional(readOnly = false) // O padrão do springDataJPA é somente consulta/leitua. Para termos um update ou insert usa-se readOnly = false
	@Modifying // Indica que a anotação query será também de escrita e não somente de leitura
	@Query("UPDATE Promocao p SET p.likes = p.likes + 1 WHERE p.id = :id")
	void updateSomarLikes(@Param("id") Long id);

	// Método de consulta de likes
	@Query("SELECT p.likes FROM Promocao p WHERE p.id = :id") // :id representa o id que temos na anotação @Param
	int findLikesById(@Param("id") Long id);
	
	// Consulta autocomplete
	@Query("SELECT DISTINCT p.site FROM Promocao p WHERE p.site LIKE %:site%")
	List<String> findSitesByTermo(@Param("site") String site);
	
	// Consulta promoções a partir do termo digitado no campo de consulta
	@Query("SELECT p FROM Promocao p WHERE p.site LIKE :site")
	Page<Promocao> findBySite(@Param("site") String site, Pageable pageable);
	
	// Consulta no input da datatable (titulo, site, categoria)
	@Query("SELECT p FROM Promocao p WHERE p.titulo LIKE %:search% OR p.site LIKE %:search% OR p.categoria.titulo LIKE %:search%")
	Page<Promocao> findByTituloOrSiteOrCategoria(@Param("search") String search, Pageable pageable);
	
	// Consulta no input daz datatable (preço)
	@Query("SELECT p FROM Promocao p WHERE p.preco = :preco")
	Page<Promocao> findByPreco(@Param("preco") BigDecimal preco, Pageable pageable);
}
