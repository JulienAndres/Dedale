package utilities;

import java.io.Serializable;

import mas.agents.Agent;
import mas.agents.CollectorAgent;
import mas.agents.ExploreAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Date;

import env.Attribute;
import env.Couple;
import jade.core.AID;

public class Graphe implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3325106123595323118L;
	private boolean fullyExplored;
	private HashMap<String,Node> noeudConnu;
	private HashMap<String,Couple<String,Integer>> otherAgent;
	private transient Agent myAgent;
	private String myPosition;
	
	private boolean isUpdater;
	private String Updater;
	private Date UpdaterStart;
	
	
	
	public  Graphe(ExploreAgent myAgent,HashMap<String,Node> noeudConnu,boolean fullyExplored) {
		this.fullyExplored=fullyExplored;
		this.noeudConnu=noeudConnu;
		this.myAgent=myAgent;
		this.myPosition="";
		this.Updater=myAgent.getName();
		this.UpdaterStart=new Date();
		this.isUpdater=true;
		initOtherCollect();
	}
	public Graphe(Agent myAgent) {
		this.myAgent=myAgent;
		this.fullyExplored=false;
		this.noeudConnu=new HashMap<String,Node>();
		this.myPosition="";
		this.Updater=myAgent.getName();
		this.UpdaterStart=new Date();
		this.isUpdater=true;
		initOtherCollect();
		if(myAgent instanceof ExploreAgent) {
			Random r = new Random();
			this.UpdaterStart=new Date(r.nextInt(Integer.MAX_VALUE));

		}else {
			this.UpdaterStart=new Date(Long.MAX_VALUE);

		}

	}
	public void initOtherCollect() {
		ArrayList<AID> list = myAgent.getCollectAgent();
		System.out.println(myAgent.getLocalName() + " collecteur" + list);
		otherAgent= new HashMap<String,Couple<String,Integer>>();
		for (AID a:list) {
			
			if (a.getLocalName().equals(myAgent.getLocalName())){
				Couple <String,Integer>tmp=new Couple<>(((CollectorAgent) myAgent).getType(),((CollectorAgent) myAgent).getCapacity());
				otherAgent.put(a.getLocalName(), tmp);
			}else {
				Couple <String,Integer>tmp=new Couple<>("NONE",-1);
				otherAgent.put(a.getLocalName(), tmp);
			}
		}
		return;
	}
	public HashMap<String,Couple<String,Integer>> getOtherAgent() {
		return this.otherAgent;
	}
	public boolean isUpdater() {
		return this.isUpdater;
	}
	public String getUpdater() {
		return this.Updater;
	}

	public Date getUpdaterDate() {
		return this.UpdaterStart;
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
	public boolean containTreasure() {
		if(!this.isFullyExplored()) {
			return true;
		}
		for(String n:noeudConnu.keySet()) {
			if (this.getNode(n).getTreasure()>0){
				return true;
			}
		}
		
		
		return false;
	}
	public boolean containDiamonds() {
		if(!this.isFullyExplored()) {
			return true;
		}
		for(String n:noeudConnu.keySet()) {
			if (this.getNode(n).getDiamonds()>0){
				return true;
			}
		}
		
		
		return false;
		
	}
	public String getOldestTreasure() {
		Date oldest=new Date(Long.MAX_VALUE);
		String old="";
		for(String n:noeudConnu.keySet()) {
			if(noeudConnu.get(n).hasTreasure() && noeudConnu.get(n).getDate().before(oldest)) {
				oldest=noeudConnu.get(n).getDate();
				old=n;
//				System.out.print("TREASURE FOUND");
			}
		}
//		System.out.println(old);
		return old;
		
	}
	
	public void updateFomObserve(List<Couple<String,List<Attribute>>> lobs) {
		String current=lobs.get(0).getLeft();
		List<Attribute> currentContent= lobs.get(0).getRight();
		Node currentNode;
		//si le noeud actuel est déjà dans le graphe
			//maj horloge TODO
			//maj contenu du noeud TODO
			//passer ce noeud en visité
		if(this.contains(current)) {
			currentNode=this.getNode(current);
			currentNode.setVisited(true);
			currentNode.setContent(currentContent);
	
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
		currentNode.setContent(currentContent);
		this.isFullyExplored();
		
	}
	public void getCloserNode() {
		if (this.isFullyExplored()){
			return;
		}else {
			
		}
	}
	public String getBestTreasure() {
		if (!this.hasTreasure()) {
			System.out.println("PLUS DE TRESOR");
		}
		float ratio=0;
		String best="";
		float ratio_bad=Float.MAX_VALUE;
		String best_bad="";
		for(String n:noeudConnu.keySet()) {
			if(this.getNode(n).hasTreasure() ) {
				if(((CollectorAgent) this.myAgent).getType()=="Diamonds") {
					float ratiotmp=this.getNode(n).getDiamonds()/(float)((CollectorAgent) this.myAgent).getCapacity();
					if (ratiotmp>1) {
						if (ratiotmp<ratio_bad) {
							ratio_bad=ratiotmp;
							best_bad=n;
						}
					}else {
						if (ratiotmp > ratio){
							ratio=ratiotmp;
							best=n;
						}
					}
				}else {
					float ratiotmp=this.getNode(n).getTreasure()/(float)((CollectorAgent) this.myAgent).getCapacity();
					if (ratiotmp>1) {
						if (ratiotmp<ratio_bad) {
							ratio_bad=ratiotmp;
							best_bad=n;
						}
					}else {
						if (ratiotmp > ratio){
							ratio=ratiotmp;
							best=n;
						}
					}
					
				}
			}
		}
		if (best=="") {
			return  best_bad;
		}else {
			return best;
		}
		
	}


	public String getClosestTreasure(String start) {
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
			found=this.getNode(currentNode).hasTreasure();			 // vérification si noeud courant est un noeud but
			if(found) {
				if(((CollectorAgent)this.myAgent).getType()=="NONE") {
					found=true;
				}else if(((CollectorAgent)this.myAgent).getType()==this.getNode(currentNode).typeTreasure()) {
					found=true;
				}else {
					found=false;
				}
			}

		}
		if(found && currentNode != null){ 
			return currentNode;
		}
		else{ 
			return "";
		}
	}
	public boolean hasTreasure() {
		for(String n:noeudConnu.keySet()) {
			if(this.getNode(n).hasTreasure()) {
				return true;
			}
		}
		return false;
	}
	
	public String getRandomNonExplore() {
		Random rand=new Random();
		ArrayList <String> tmpRd=new ArrayList <String>();
		for(String n:noeudConnu.keySet()) {
			if (!this.getNode(n).isVisited()) {
				tmpRd.add(n);
			}
		}
		int i=rand.nextInt(tmpRd.size());
		return tmpRd.get(i);
	}
	
	public String getRandom() {
		Random rand=new Random();
		ArrayList <String> tmpRd=new ArrayList <String>();
		for(String n:noeudConnu.keySet()) {
				tmpRd.add(n);
			
		}
		int i=rand.nextInt(tmpRd.size());
		return tmpRd.get(i);
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
	/**
	 * 
	 * @param pos 
	 * @return -1 si cul de sac
	 * @return -2 si autre chemin possible
	 * @return toRet : nombre de noeud du couloir à reculer
	 */
	public int distCouloir(String pos) {//distance  du couloir à reculer --pour les interblocages 
		if(myAgent.getPath().size()==0) return 0;
		HashSet<String> voisins = noeudConnu.get(pos).getVoisins();
		String intersection=myAgent.getPath().get(0);
		int toRet=0;
		String tmp;
		String current=pos;
		if (voisins.size()==1) {//cul de sac
			return -1;
		}
		while(voisins.size()==2) {
			tmp=current;
			toRet++;
			for(String i:voisins) {
				if(!i.equals(intersection)) {
					current=i;
				}
			}
			intersection=tmp;
			voisins=noeudConnu.get(current).getVoisins();
		}
		if (voisins.size()==1) {//cul de sac
			return -1;
		}
		return toRet;
	}
	
	public void sync(Graphe graphe) {
		for (String id:graphe.getIds()) {
			if(this.contains(id)) {//maj du node s il existe
				this.getNode(id).sync(graphe.getNode(id));
			}else {//sinon ajout du node
				this.addNode(graphe.getNode(id));
				
			}
		}
		this.isFullyExplored();
		return;
	}
	public void syncUpdater(Graphe graphe) {
		if(this.UpdaterStart.after(graphe.getUpdaterDate())) {
			this.UpdaterStart=graphe.getUpdaterDate();
			this.Updater=graphe.getUpdater();
		}
		if(this.Updater.equals(myAgent.getName())){
			this.isUpdater=true;
		}else {
			this.isUpdater=false;
		}
		if (!(myAgent instanceof  ExploreAgent)) {
			syncCollect(graphe);
		}
	}
	
	public void syncCollect(Graphe graphe) {
		for (String tmp1:this.otherAgent.keySet()) {
			if(this.otherAgent.get(tmp1).getLeft()=="NONE") {
				System.out.println(graphe.getOtherAgent());
				if(graphe.getOtherAgent().get(tmp1).getLeft()!="NONE") {
					this.otherAgent.remove(tmp1);
					Couple<String,Integer>tmp=new Couple<>(graphe.getOtherAgent().get(tmp1).getLeft(),graphe.getOtherAgent().get(tmp1).getRight());
					this.otherAgent.put(tmp1,tmp);
				}

			}
		}
	}
	public void resetGraphe() {
		fullyExplored=false;
		this.noeudConnu=new HashMap<String,Node>();
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
	
	public Set<String> getIds(){
		return this.noeudConnu.keySet();
	}
	public int nombreNoeud(){
		
		return noeudConnu.keySet().size();
	}
	public void afficher() {
		for (String i:noeudConnu.keySet()) {
			System.out.println(i+" "+noeudConnu.get(i).isVisited());
		}
	}
	public void setPosition(String pos) {
		this.myPosition=pos;
	}
	public String getPosition() {
		return myPosition;
	}
	
	public String getTankerNode() { //premier noeud le plus connexe
		int max=0;
		String toRet="";
		for(String n:this.noeudConnu.keySet()) {
			if (max<this.getNode(n).getVoisins().size()) {
				max=this.getNode(n).getVoisins().size();
				toRet=n;
			}
		}
		
		return toRet;
	}
	
}
