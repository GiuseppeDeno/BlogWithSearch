package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@Service
public class BlogService {
	// Classe di servizio per la gestione delle operazioni CRUD sugli oggetti User

		private EntityManagerFactory entityManagerFactory;

		public BlogService() {
			// Crea un'EntityManagerFactory utilizzando il nome dell'unità di persistenza
			// "example-unit"
			entityManagerFactory = Persistence.createEntityManagerFactory("example-unit");

		}

		// Iniezione dell'EntityManager
		@PersistenceContext
		private EntityManager entityManager;

		// Metodo per creare un nuovo prodotto e e salvarlo nel database
		@Transactional
		public void createArticle(String titolo, String contenuto, String categoria, String url) {
			// Crea un nuovo oggetto Dip con i parametri forniti
			Articolo A1 = new Articolo();
			A1.setTitolo(titolo);
			A1.setContenuto(contenuto);
			A1.setCategoria(categoria);
			A1.setUrl(url);

			// Salva l'oggetto prodotto nel contesto di persistenza (lo rende gestito)
			entityManager.persist(A1);
		}

		// Metodo per ottenere tutti i prodotti usando una LIST
		/*
		 * @Transactional public List<Prodotto_nov> getAllProd() {
		 * 
		 * //posso fare cast della List ad ArrayList
		 * 
		 * //ArrayList<Prodotto_nov> listaP = (ArrayList<Prodotto_nov) lista; // Esegue
		 * una query per selezionare tutti gli oggetti di tipo Prodotto_nov return
		 * entityManager.createQuery("SELECT p FROM Prodotto_nov p",
		 * Prodotto_nov.class).getResultList();
		 * 
		 * }
		 */
		
		@Transactional  ///Nota 1
		public ArrayList<Articolo> getAllArticoli() {

			/// >>>>>>>>>>>>>NOTA IMPORTANTE
			// Select l From Libri l" Libri è l'entita che crea la tabella. .
			/// con jdbc template
			// mettavamo il nome della tabella ma qui l'entita crea la tabella , e bisogna
			/// mettere l'entita!!!!!!!!!!!!!

			List<Articolo> lista = entityManager.createQuery("SELECT a FROM Articolo a", Articolo.class).getResultList();

			// cast da list a ArrayList
			ArrayList<Articolo> listaArticoli = (ArrayList<Articolo>) lista;

			return listaArticoli;
		}

		@Transactional // metodo boolean per avere un true o false in caso di possibile aggirnamento
		public boolean updateCategoria(String titolo, String categoria) {

			List<Articolo> lista = entityManager.createQuery("SELECT a FROM Articolo a", Articolo.class).getResultList();

			// cast da lista a arraylist
			ArrayList<Articolo> listaArticoli = (ArrayList<Articolo>) lista;

			for (Articolo a1 : listaArticoli) {

				if (a1.getTitolo().equalsIgnoreCase(titolo)) {

					a1.setCategoria(categoria);
					entityManager.persist(a1);
					return true; // Libro trovato e aggiornato - mi serve nel controller per far apparire
									// messaggio di aggiornamento ok o non ok
				} else {
					//System.out.println("libro non presente");
				}
			}
			return false; // Libro non trovato
		}

		@Transactional
		public void deleteArticolo(String titolo) {

			// Uso del parametro posizionale ?1 per impostare il valore del nome
			entityManager.createQuery("DELETE FROM Articolo a  WHERE a.titolo = ?1").setParameter(1, titolo) // Setta il
																											// parametro
																											// posizionale
																											// ?1
					.executeUpdate();

		}

		  // Metodo per ottenere tutte le categorie distinte- mi servirà per far apparire gli articoli in box distinti o cmq per selezionare
		// con cast ad ArrayList 
		
		// nota bene SELECT DISTINCT 
		
	    public ArrayList<String> getCategorieDistinte() {
	        List<String> listaCategorie = entityManager.createQuery(
	                "SELECT DISTINCT a.categoria FROM Articolo a", String.class)
	                .getResultList();
	        return new ArrayList<>(listaCategorie);
	    }
		
	    
	    
		//ricerca articolo per categoria 
		public ArrayList<Articolo> getArticoliPerCategoria(String categoria) {
	        List<Articolo> lista = entityManager.createQuery(
	                "SELECT a FROM Articolo a WHERE a.categoria = :categoria", Articolo.class)
	                .setParameter("categoria", categoria)
	                .getResultList();

	        return new ArrayList<>(lista); // cast da List a ArrayList
		}
	        
		//ricerca articolo per parolachiave Nei titoli e nel contenuto 
		//https://stackoverflow.com/questions/10287971/spring-jpa-data-or-query
		//https://www.baeldung.com/spring-jpa-like-queries
		//https://www.w3schools.com/sql/sql_like.asp
		
		
		
		public ArrayList<Articolo> getArticoliPerParolaChiave(String parolaChiave) {
		    List<Articolo> lista = entityManager.createQuery(
		            "SELECT a FROM Articolo a WHERE a.titolo LIKE :parolaChiave OR a.contenuto LIKE :parolaChiave", Articolo.class)
		            .setParameter("parolaChiave", "%" + parolaChiave + "%") // '%' per la ricerca parziale vedi w3schools.com
		            .getResultList();

		    return new ArrayList<>(lista); // cast da List a ArrayList
		}
		
		
		
		
		
		
		
		/*
		public ArrayList<Articolo> getArticoliPerParolaChiave(String parolaChiave) {
	        List<Articolo> lista = entityManager.createQuery(
	                "SELECT a FROM Articolo a WHERE a.titolo = :parolaChiave", Articolo.class)
	                .setParameter("parolaChiave", parolaChiave)
	                .getResultList();

	        return new ArrayList<>(lista); // cast da List a ArrayList
		}
	        */
	        
	        
	        
	        
	        
		
		/*  NON FUNZIONA 
		@Transactional  ///Nota 1
		public ArrayList<Articolo> getArtByC() {

			/// >>>>>>>>>>>>>NOTA IMPORTANTE
			// Select l From Libri l" Libri è l'entita che crea la tabella. .
			/// con jdbc template
			// mettavamo il nome della tabella ma qui l'entita crea la tabella , e bisogna
			/// mettere l'entita!!!!!!!!!!!!!

			List<Articolo> lista = entityManager.createQuery("SELECT a FROM Articolo a WHERE a.categoria = ?1", Articolo.class).getResultList();

			// cast da list a ArrayList
			ArrayList<Articolo> listaArticoliC = (ArrayList<Articolo>) lista;

			return listaArticoliC;
		}
		*/
		

}

/* public ArrayList<Articolo> getArtByC(String categoria) {
        List<Articolo> lista = entityManager.createQuery(
                "SELECT a FROM Articolo a WHERE a.categoria = :categoria", Articolo.class)
                .setParameter("categoria", categoria)
                .getResultList();

        return new ArrayList<>(lista); // cast da List a ArrayList
    }*/
