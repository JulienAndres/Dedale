package mas.behaviours;

import java.io.IOException;
import java.util.List;

import jade.core.behaviours.OneShotBehaviour;
import utilities.Graphe;
import mas.agents.*;


/**************************************
 *
 *
 * 				BEHAVIOUR
 *
 *
 **************************************/


public class MoveBehaviour extends OneShotBehaviour{

	private static final long serialVersionUID = -1784844593772918359L;
	private ExploreAgent myAgent;
	
	public MoveBehaviour (ExploreAgent myAgent) {
		super(myAgent);
		this.myAgent=myAgent;
	}

	@Override
	public void action() {
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		//System.out.println(myPosition);

		if (myPosition!=""){

			if (!myAgent.getGraphe().isFullyExplored()) {
				this.exploration(myPosition);
			}else {
				System.out.println("EXPLORATION FINI"+myAgent.getLocalName());
				System.out.println(myAgent.getGraphe().nombreNoeud());
				try {
				System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to continue");
				System.in.read();

			} catch (IOException e) {
				e.printStackTrace();
			}
				myAgent.getGraphe().afficher();
			}
		}
//		try {
//			System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to continue");
//			System.in.read();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public void exploration(String myPosition) {
		String nextId=myAgent.getNextMove();
		if (nextId=="") {
			String target=myAgent.getGraphe().getClosestNonExplore(myPosition);
			myAgent.doPath(myAgent.getGraphe().getPath(myPosition, target));
			nextId=myAgent.getNextMove();
		}
		((mas.abstractAgent)this.myAgent).moveTo(nextId);

		
		

	}
}