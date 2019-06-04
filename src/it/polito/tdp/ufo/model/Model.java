package it.polito.tdp.ufo.model;

import java.time.Year;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	// PER LA RICORSIONE
	// 1 struttura dati finale
	private List<String> ottima; // è una lista di stati(String) in cui c'è uno stato di partenza
								// e un insieme di altri stati(non ripetuti)
	// 2 struttura dati parziale
	// una lista definita nel metodo ricorsivo
	
	// 3 condizione di terminazione
	// dopo un determinato nodo, non ci sono più succesori che non ho considerato
	
	// 4 generare una nuova soluzione a partire da una soluzione parziale
	// dato l'ultimo nodo inserito nella soluzione parziale, considero tutti i successori di quel nodo
	// che non ho ancora considerato
	
	// 5 filtro
	// alla fine ritornerò una sola soluzione --> quella per cui la size() è massima
	
	// 6 livello ricorsione
	// lunghezza del percorso parziale
	
	// 7 il caso inizale
	// parziale contenente il mio stato di partenza
	
	private SightingsDAO dao;
	private List<String> stati;
	private Graph<String, DefaultEdge> grafo;
	
	public Model() {
		this.dao = new SightingsDAO();
	}

	public List<AnnoCount> getAnni(){
		return this.dao.getAnni();
	}
	public void creaGrafo(Year year) {
		this.stati = this.dao.getStati(year);
		this.grafo = new SimpleDirectedGraph<String,DefaultEdge>(DefaultEdge.class);
		Graphs.addAllVertices(this.grafo, stati);
		
		// soluzione semplice --> doppio ciclo, controllo esistenza arco
		for(String s1: this.grafo.vertexSet()) {
			for(String s2: this.grafo.vertexSet()) {
				if(!s1.equals(s2)) {
					if(this.dao.esisteArco(s1,s2, year))
						this.grafo.addEdge(s1, s2);
				}
			}
		}
		System.out.println("grafo creato\nVertici: "+this.grafo.vertexSet().size()+"\nArchi: "+this.grafo.edgeSet().size());
	}

	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}

	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}

	public List<String> getStati() {
		return this.stati;
	}
	public List<String> getSuccessori(String stato){
		return Graphs.successorListOf(grafo, stato);
	}
	public List<String> getPredecessori(String stato){
		return Graphs.predecessorListOf(grafo, stato);
	}
	public List<String> getRaggiungibili(String stato){
		List<String> raggiungibili = new LinkedList<>();
		DepthFirstIterator<String, DefaultEdge> dp = new DepthFirstIterator<String, DefaultEdge>(this.grafo, stato);
		dp.next();
		while(dp.hasNext()) {
			raggiungibili.add(dp.next());
		}
		return raggiungibili;
	}
	public List<String> getPercorsoMassimo(String partenza){
		this.ottima = new LinkedList<String>();
		List<String> parziale = new LinkedList<String>();
		parziale.add(partenza);
		cercaPercorso(parziale);
		return this.ottima;
	}

	private void cercaPercorso(List<String> parziale) {
		// vedere se la soluzione corrente è la migliore della ottima corrente
		if(parziale.size()>ottima.size())
			this.ottima = new LinkedList(parziale);
		
		List<String> candidati = this.getSuccessori(parziale.get(parziale.size()-1));
		for(String candidato: candidati) {
			if(!parziale.contains(candidato)) {
				parziale.add(candidato);
				this.cercaPercorso(parziale);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
}
