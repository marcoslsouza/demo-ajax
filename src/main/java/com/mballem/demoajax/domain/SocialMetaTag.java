package com.mballem.demoajax.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SocialMetaTag implements Serializable {

	// Armazena o nome do site capturado
	private String site;
	// Titulo do produto capturado
	private String title;
	// Armazena a url do site capturado
	private String url;
	// Armazena a url da imagem que o site fornece
	private String image;
	
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	@Override
	public String toString() {
		return "SocialMetaTag [site=" + site + ", title=" + title + ", url=" + url + ", image=" + image + "]";
	}
}
