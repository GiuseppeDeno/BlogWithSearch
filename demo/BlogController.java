package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class BlogController {

    private BlogService b1;

    @Autowired
    public BlogController(BlogService b1) {
        this.b1 = b1;
    }

  
    
    // IMPORTANTE  https://stackoverflow.com/questions/64715520/trying-to-make-a-request-with-or-without-parameters
    //in  requestParam bisogna mettere    value = "categoria required = false   altrimenti richiederebbe la categoria
    //nella index prima di partire  e non partirebbe
    //Infatti  getArticoliPerCategoria richiede categoria come paramentro ed è usato come metodo all'interno di homePage
    
    
    @GetMapping("/")
    public String homePage(@RequestParam(value = "categoria" , required = false) String categoria, Model model) {
        ArrayList<String> categorie = b1.getCategorieDistinte(); //creo un arraylist di categorie da stampare nella sidebar
        model.addAttribute("categorie", categorie);

        ArrayList<Articolo> listaArticoli;
        //se non è null ovvero è stata creata e non  è vuota 
        if (categoria != null && !categoria.isEmpty()) {
        	//su b1 chiamo il metodo per filtrare le categorie
            listaArticoli = b1.getArticoliPerCategoria(categoria);
        } else {
        	//se non ci sono categorie restuisco tutti gli articoli 
            listaArticoli = b1.getAllArticoli();
        }

        model.addAttribute("listaArticoli", listaArticoli);
        return "index";
    }
    ///funzione per visualizzare un articolo cercando tramite titolo
    
    
    //ricerca articoli tramite parola chiave su titilo e su contenuto
    @GetMapping("/articoli")
    public String visualizzaArticoli(@RequestParam(value = "categoria" , required = false) String categoria,
    		@RequestParam(value = "parolaChiave" , required = false) String parolaChiave,
    		    Model model) {
        ArrayList<String> categorie = b1.getCategorieDistinte(); //creo un arraylist di categorie da stampare nella sidebar
        model.addAttribute("categorie", categorie);

        ArrayList<Articolo> listaArticoli;
        //se non è null ovvero è stata creata e non  è vuota 
        if (parolaChiave != null && !parolaChiave.isEmpty()) {
        	//su b1 chiamo il metodo per filtrare le categorie
            listaArticoli = b1.getArticoliPerParolaChiave(parolaChiave);
        } else {
        	//se non ci sono categorie restuisco tutti gli articoli 
            listaArticoli = b1.getAllArticoli();
        }

        model.addAttribute("listaArticoli", listaArticoli);
        return "index";
    }
    
    
//mi carica tutto il gestore backend
    @GetMapping("/gestoreArticoli")
    public String gestioneArticoli(Model model) {
        ArrayList<Articolo> listaArticoli = b1.getAllArticoli();
        model.addAttribute("lista", listaArticoli); // Cambiato per corrispondere al nome utilizzato nella view
        return "gestoreBlog"; 
    }

    @PostMapping("/aggiungiArticolo")
    public String addArticolo(@RequestParam("titolo") String titolo,
                              @RequestParam("contenuto") String contenuto,
                              @RequestParam("categoria") String categoria,
                              @RequestParam("url") String url) {
        b1.createArticle(titolo, contenuto, categoria, url);
        return "redirect:/gestoreArticoli"; // Cambiato per reindirizzare correttamente
    }

    @PostMapping("/rimuoviArticolo") // Cambiato da rimuoviDalBlog a rimuoviArticolo
    public String removeArticolo(@RequestParam("titolo") String titolo) {
        b1.deleteArticolo(titolo);
        return "redirect:/gestoreArticoli"; 
    }

    @PostMapping("/cambiaCategoria")
    public String cambiaGenere(@RequestParam("titolo") String titolo, @RequestParam("categoria") String nuovaCategoria, Model model) {
        boolean articoloAggiornato = b1.updateCategoria(titolo, nuovaCategoria);

        if (!articoloAggiornato) {
            model.addAttribute("messaggio", "Categoria non presente");
        }

        model.addAttribute("lista", b1.getAllArticoli());
        model.addAttribute("messaggio", "Categoria aggiornata con successo");
        return "gestoreBlog"; 
    }
    
    

    @GetMapping("/articoliPerCategoria")
    public String articoliPerCategoria(@RequestParam("categoria") String categoria, Model model) {
        ArrayList<Articolo> articoliFiltrati = b1.getArticoliPerCategoria(categoria);
        model.addAttribute("lista", articoliFiltrati);
        return "index";
    }
}
