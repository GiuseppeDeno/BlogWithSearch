package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Articolo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //serve a dire che id  è autoincrement 
	private long id;

	private String titolo;
	
	@Column(length = 5000) //per piu caratteri usare BLOB o TEXT invece che string??
	private String contenuto;
	private String categoria;
	private String url;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getContenuto() {
		return contenuto;
	}
	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return "Articolo [id=" + id + ", titolo=" + titolo + ", contenuto=" + contenuto + ", categoria=" + categoria
				+ ", url=" + url + "]";
	}
	
	

}
