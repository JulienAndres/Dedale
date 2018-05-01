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
			if (myAgent.getGraphe().getPosition().equals(myPosition)) {
				myAgent.incBlock();
			}
			myAgent.getGraphe().setPosition(myPosition);
			myAgent.getGraphe().updateFomObserve(lobs);


			
		}
//		try {
//			System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to continue");
//			System.in.read();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}


	
	public int onEnd(){
		if (myAgent.isBlocked()){
			return 2;
		}
		return 1;
		//IF BLOQUE
		//return 2
		//SINON GO CHECK MESSAGES
		//return 3
	}

}
