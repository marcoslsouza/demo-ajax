package com.mballem.demoajax.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "categorias")
public class Categoria implements Serializable {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "{NotBlank.link_categoria}")
	@Column(nullable = false, unique = true)
	private String titulo;
	
	@JsonIgnore // Ignora esta lista de promoções quando for montar o json, para evitar recursividade, pois ao chegar aqui voltara para Promocao, e depois voltara para categoria.
	@OneToMany(mappedBy = "categoria") // categoria, faz referencia ao atributo categoria na classe Promocao
	private List<Promocao> promocoes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<Promocao> getPromocoes() {
		return promocoes;
	}

	public void setPromocoes(List<Promocao> promocoes) {
		this.promocoes = promocoes;
	}

	@Override
	public String toString() {
		return "Categoria [id=" + id + ", titulo=" + titulo + "]";
	}
}
