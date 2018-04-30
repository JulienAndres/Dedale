package mas.behaviours;

import java.io.IOException;
import java.util.List;

import env.Attribute;
import env.Couple;
import mas.agents.ExploreAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.OneShotBehaviour;;


public class ObserveBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -1935358198561959915L;
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
			myAgent.getGraphe().updateFomObserve(lobs);
			
			//IF BLOCKED i.e. NOT MOVED
			//incrementer compteur de blocage
			//ELSE
			//actualiser la map, reinitialiser le compteur de blocage
			//myAgent.getWorld().
			
			
		}
//		try {
//			System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to continue");
//			System.in.read();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
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
