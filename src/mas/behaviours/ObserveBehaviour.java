package mas.behaviours;

import java.util.List;

import env.Attribute;
import env.Couple;
import mas.agents.ExploreAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;

public class ObserveBehaviour extends SimpleBehaviour {
	private ExploreAgent myAgent;
	
	public ObserveBehaviour(ExploreAgent myAgent) {
		super(myAgent);
		this.myAgent = myAgent;
	}
	
	@Override
	public void action() {
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		if (myPosition!=""){
			List<Couple<String,List<Attribute>>> lobs = ((mas.abstractAgent)this.myAgent).observe();
			
			//IF BLOCKED i.e. NOT MOVED
			//incrementer compteur de blocage
			//ELSE
			//actualiser la map, reinitialiser le compteur de blocage
			//myAgent.getWorld().
			
			
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int onEnd(){
		//IF OK POUR SHARE (toujours!)
		return 1;
		//IF BLOQUE
		//return 2
		//SINON GO CHECK MESSAGES
		//return 3
	}

}
