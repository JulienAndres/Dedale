package mas.behaviours;

import java.io.IOException;
import java.util.List;

import env.Attribute;
import env.Couple;
import mas.agents.Agent;
import mas.agents.CollectorAgent;
import mas.agents.ExploreAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.OneShotBehaviour;;


public class PickBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -1935358198561959915L;
	private CollectorAgent myAgent;
	
	public PickBehaviour(CollectorAgent myAgent) {
		super(myAgent);
		this.myAgent = myAgent;
	}
	
	@Override
	public void action() {
		System.out.println("PICKKKKKKKKKKKK");
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		int freeSpace = ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace();
		int diamand=myAgent.getGraphe().getNode(myPosition).getDiamonds();
		int treasure=myAgent.getGraphe().getNode(myPosition).getTreasure();
		System.out.println(freeSpace);
		System.out.println(((mas.abstractAgent)this.myAgent).pick());
		

		
//		try {
//			System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to continue");
//			System.in.read();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}


	
	public int onEnd(){
		//return en observe
		return 0;
	}

}
