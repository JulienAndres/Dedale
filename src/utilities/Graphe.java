package utilities;

import java.io.Serializable;
import mas.agents.ExploreAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import env.Attribute;
import env.Couple;

public class Graphe implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3325106123595323118L;
	private boolean fullyExplored;
	private HashMap<String,Node> noeudConnu;
	private ExploreAgent myAgent;
	
	public  Graphe(ExploreAgent myAgent,HashMap<String,Node> noeudConnu,boolean fullyExplored) {
		this.fullyExplored=fullyExplored;
		this.noeudConnu=noeudConnu;
		this.myAgent=myAgent;
	}
	public Graphe(ExploreAgent myAgent) {
		this.myAgent=myAgent;
		this.fullyExplored=false;
		this.noeudConnu=new HashMap<String,Node>();
	}
	
	public boolean isFullyExplored() {
		if (fullyExplored) {
			return true;
		}else {
			Set<String> allid=noeudConnu.keySet();
			for (String id:allid) {
				if(!noeudConnu.get(id).isVisited()) {
					return false;
				}
			}
		}
		fullyExplored=true;
		return true;
	}
	
	public void updateFomObserve(List<Couple<String,List<Attribute>>> lobs) {
		String current=lobs.get(0).getLeft();
		//List<Attribute> currentContent= sensorInformation.get(0).getRight();
		Node currentNode;
		//si le noeud actuel est déjà dans le graphe
			//maj horloge TODO
			//maj contenu du noeud TODO
			//passer ce noeud en visité
		if(this.contains(current)) {
			currentNode=this.getNode(current);
			currentNode.setVisited(true);
	
		}else {
			currentNode=new Node(current);
			currentNode.setVisited(true);
			this.addNode(currentNode);
		}


		
		//pour tous les voisins du noeud courant
		for(int i=1; i<lobs.size();i++) {
			Node voisinNoeud;
			String idvoisin = lobs.get(i).getLeft();
			
			//si le graphe contient le voisin
			if(this.contains(idvoisin)) {
				voisinNoeud=this.getNode(idvoisin);
				voisinNoeud.addVoisin(currentNode);
				
			}else {
				//sinon
				HashSet<String> voisins = new HashSet<String>();
				voisins.add(currentNode.getId());
				voisinNoeud = new Node(idvoisin, voisins, false, 0);
				this.addNode(voisinNoeud);
			}
			currentNode.addVoisin(voisinNoeud);
			

		}
		this.isFullyExplored();
		
	}
	public void getCloserNode() {
		if (this.isFullyExplored()){
			return;
		}else {
			
		}
	}
	public String getClosestNonExplore(String start){
		boolean found = false;
		List<String> fifo = new ArrayList<String>();
		List<String> listNodesVisited = new ArrayList<String>();
		listNodesVisited.add(start);
		fifo.add(start);
		String currentNode = start;

		while(!found && !fifo.isEmpty()){ // tant qu'on a pas trouvé de noeud but ou que il reste des noeuds à parcourir

			currentNode = fifo.get(0);
			fifo.remove(0);

			HashSet<String> allFils = this.getNode(currentNode).getVoisins(); // récupération de la liste des fils
			for(String fils : allFils){
				if(!listNodesVisited.contains(fils)){ // si fils jamais visité dans le parcours
					fifo.add(fils); // ajout du fils à la liste des noeuds à visiter
					listNodesVisited.add(fils); // marquage du fils
				}
			}
			found=!this.getNode(currentNode).isVisited();			 // vérification si noeud courant est un noeud but
		}
		if(found && currentNode != null){ 
			return currentNode;
		}
		else{ 
			return "";
		}
	}
	
	//parcours en largeur pour plus court chemin entre 2 nodes (identifie par leurs id)
	//TODO Chemin aleatoire ?
	
	public ArrayList<String> getPath(String start, String end){


		boolean found = false;
		List<String> fifo = new ArrayList<String>();
		HashMap<String, String> listNodesVisited = new HashMap<String, String>(); // dictionnaire de la forme -> (fils, pere)
		listNodesVisited.put(start, start);
		fifo.add(start);
		String currentNode = start;

		while(!found && !fifo.isEmpty()){ // tant qu'on a pas trouvé de noeud but ou que il reste des noeuds à parcourir

			currentNode = fifo.get(0);
			fifo.remove(0);

			HashSet<String> allFils = this.getNode(currentNode).getVoisins(); // récupération de la liste des fils
			for(String fils : allFils){
				if(!listNodesVisited.containsKey(fils)){ // si fils jamais visité dans le parcours
					fifo.add(fils); // ajout du fils à la liste des noeuds à visiter
					listNodesVisited.put(fils, currentNode); // ajout du fils dans le dictionnaire
				}
			}
			found=end.equals(currentNode);			 // vérification si noeud courant est un noeud but
		}
		if(found && currentNode != null){ // si on a trouvé un noeud but, retourne le chemin entre start et ce noeud
			return reconstruction(start, currentNode, listNodesVisited);
		}
		else{ // si on a trouvé aucun noeud but, retourne une liste vide
			return new ArrayList<String>();
		}
	}
	
	public ArrayList<String> reconstruction(String start, String end, HashMap<String, String> dico){
        ArrayList<String> path = new ArrayList<String>();
        String currentNode = end;
        path.add(end);
        while(!currentNode.equals(start)){
            currentNode = dico.get(currentNode);
            if(!currentNode.equals(start)){
                path.add(0, currentNode);
            }
        }
        return path;
    }
	
	public Node getNode(String id) {
		return this.noeudConnu.get(id);
	}
	
	public void addNode(Node node) {
		this.noeudConnu.put(node.getId(), node);
	}
	
	public boolean contains(String id){
		return this.noeudConnu.containsKey(id);
	}
	
	public HashMap<String,Node> export() {
		return noeudConnu;
	}
	
}
