package utilities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import env.Attribute;

import java.util.Date;





public class Node implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -189730942693487917L;
	private String id;
	private HashSet<String> voisins;
	private int diamonds;
	private int treasure;
	private int valueTreasure;
	private boolean visited=false;
	private Date date;
	
	public Node(String id, HashSet<String> voisins, boolean visited, int timestamp) {
		this.id=id;
		this.voisins=voisins;
		this.visited=visited;
		this.date=new Date();
	}
	
	public Node(String id) {
		this(id, new HashSet<String>(), false, 0);
		this.date=new Date();
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

	public int getTreasure() {
		return this.treasure;
	}
	public int getDiamonds() {
		return this.diamonds;
	}
	public String typeTreasure() {
		if(this.treasure>0 && this.diamonds==0) {
			return "treasure";
		}else if(this.treasure==0 && this.diamonds>0) {
			return "diamonds";
		}else {
			return "both";
		}
	}
	public boolean hasTreasure() {
		return (this.treasure>0 || this.diamonds>0);
	}

	public void setValueTreasure(int value) {
		this.valueTreasure=value;
	}
	public int getValueTreasure() {
		return this.valueTreasure;
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
		if(this.date.before(node.getDate())) {
			this.treasure=node.getTreasure();
			this.diamonds=node.getDiamonds();
		}
		
	}
	
	public void setContent(List<Attribute> content) {
		boolean treasure = false;
		boolean diamand = false;
		for(Attribute attr:content){
			if((attr.getName()=="Treasure")){
				this.treasure=(int) attr.getValue();
				treasure=true;
			}
			if(attr.getName()=="Diamonds") {
				this.diamonds=(int) attr.getValue();
				diamand=true;

			}
		}
		
		if(treasure == false){
			this.treasure=0;
			
		}
		if (diamand==false) {
			this.diamonds=0;
		}
		this.date=new Date();
	}
	public Date getDate() {
		return this.date;
	}
	
}
