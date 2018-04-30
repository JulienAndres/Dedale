package utilities;

import java.io.Serializable;
import java.util.HashSet;



public class Node implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -189730942693487917L;
	private String id;
	private HashSet<String> voisins;
	private String treasure=null;
	private boolean visited=false;
	
	public Node(String id, HashSet<String> voisins, boolean visited, int timestamp) {
		this.id=id;
		this.voisins=voisins;
		this.visited=visited;
	}
	
	public Node(String id) {
		this(id, new HashSet<String>(), false, 0);
	}

	public String getId() {
		return id;
	}

	public HashSet<String> getVoisins() {
		return voisins;
	}

	public void setVoisins(HashSet<String> voisins) {
		this.voisins = voisins;
	}

	public String getTreasure() {
		return treasure;
	}

	public void setTreasure(String treasure) {
		this.treasure = treasure;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public boolean addVoisin(Node voisin) {
		return this.voisins.add(voisin.getId());
	}
	
	public void sync(Node node) {
		if (!node.getId().equals(this.id)) {
			System.out.println("syncronisation sur 2 noeuds différents");
			return;
		}
		if(node.isVisited()) {
			this.visited=true;
		}
		for(String voisin:node.getVoisins()) {
			this.voisins.add(voisin);//return true si le voisin n était pas dedans et que l ajout se fait.
		}
	}
	
}
