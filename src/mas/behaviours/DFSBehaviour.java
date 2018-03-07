package mas.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;	
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import javax.xml.ws.handler.LogicalHandler;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;

import utilities.Graphe;
import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;

/**************************************
 * 
 * 
 * 				BEHAVIOUR
 * 
 * 
 **************************************/


public class DFSBehaviour extends SimpleBehaviour{
	/**
	 * When an agent choose to move
	 *  
	 */
	private static final long serialVersionUID = 9088209402507795289L;
	private boolean finished = false;
	private Map<String, Couple<Couple<String,Boolean>,Couple<List<Attribute>, List<String>>>>  map; //key, value ((père,visité), (liste_attributs, liste_fils))
	
	public DFSBehaviour (final mas.abstractAgent myagent, Map<String, Couple<Couple<String,Boolean>,Couple<List<Attribute>, List<String>>>>  map) {
		super(myagent);
		this.map = map;
	}

	@Override
	public void action() {
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		Couple<String, List<Attribute>> curr=null;
		
		if (myPosition!=""){
			List<Couple<String,List<Attribute>>> lobs = ((mas.abstractAgent)this.myAgent).observe();
			
			for (Couple<String, List<Attribute>> c : lobs){
				if (c.getLeft()==myPosition){
					curr=c;
					break;
				}
			}
			lobs.remove(curr);
			List<String> fils = new ArrayList<String>();
			for (Couple<String, List<Attribute>> c : lobs) {
				fils.add(c.getLeft());
			}
			System.out.println(myPosition);
			if(!map.containsKey(myPosition)) {
				map.put(myPosition, new Couple<>(new Couple<>("",true), new Couple<>(curr.getRight(), fils)));
			}else {
				map.put(myPosition, new Couple<>(new Couple<>(map.get(myPosition).getLeft().getLeft(),true), new Couple<>(curr.getRight(), fils)));//mettre le noeud à true
			}
			
			
			for (Couple<String, List<Attribute>> c : lobs){
				if(!map.containsKey(c.getLeft())) {
					map.put(c.getLeft(), new Couple<>(new Couple<>(myPosition, false), new Couple<>(c.getRight(), null)));
				}
			}
			boolean willmove=false;
			String moveId=myPosition;
			for (Couple<String, List<Attribute>> c : lobs){
				boolean visited = map.get(c.getLeft()).getLeft().getRight();
				
				if(!visited) {
					moveId = c.getLeft();
					willmove=true;
					System.out.println(moveId);
					break;
				}
			}
			

			//System.out.println(map);
			if (willmove) {
				map.put(moveId, new Couple<>(new Couple<>(myPosition,false),map.get(moveId).getRight()));
				((mas.abstractAgent)this.myAgent).moveTo(moveId);
			} else {
				String pere=map.get(myPosition).getLeft().getLeft();
				System.out.println("pere"+pere);

				if (pere != "") {
				((mas.abstractAgent)this.myAgent).moveTo(pere);
				}else {
					System.out.println("exploration finie");
					//this.finished=true;
				}
			}
//			//Little pause to allow you to follow what is going on
			try {
				System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Graph g = Graphe.generateGraphFromMap(map);
			Viewer viewer = g.display();


		}

	}
	
	@Override
	public boolean done() {
		Set<String> keySet = map.keySet();
		for (String s : keySet){
			boolean visited = map.get(s).getLeft().getRight();
			if(!visited){
				return false;
			}
		}
		return true;
	}
	

}