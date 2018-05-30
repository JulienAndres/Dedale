package mas.behaviours;

import java.io.IOException;
import java.util.List;

import env.Attribute;
import env.Couple;
import mas.agents.Agent;
import mas.agents.CollectorAgent;
import mas.agents.ExploreAgent;
import mas.agents.TankerAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.OneShotBehaviour;;


public class ObserveBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -1935358198561959915L;
	private Agent myAgent;
	
	public ObserveBehaviour(Agent myAgent) {
		super(myAgent);
		this.myAgent = myAgent;
	}
	
	@Override
	public void action() {
		if (myAgent instanceof CollectorAgent){
			System.out.println("still treasure?"+((CollectorAgent) myAgent).isUsefull());
			if (!((CollectorAgent) myAgent).isUsefull()){
				
			}

		}
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

		if (myPosition!=""){
			List<Couple<String,List<Attribute>>> lobs = ((mas.abstractAgent)this.myAgent).observe();
			if (myAgent.getGraphe().getPosition().equals(myPosition)) {
				myAgent.incBlock();
			}else {
				
//				myAgent.decBlock();
				myAgent.resetBlock();
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
		
//		System.out.println("free space "+myAgent.getBackPackFreeSpace()+"capacite  "+((CollectorAgent) myAgent).getCapacity()+"is explore "+myAgent.getGraphe().isFullyExplored());

		if(myAgent.blockRandom()) {
			System.out.println("BLOCAGE");
			System.out.println(20);
			return 20;
		}
		if (myAgent instanceof CollectorAgent) {
			if(myAgent.getGraphe().getNode(myAgent.getGraphe().getPosition()).hasTreasure()) {
//				System.out.println("has trésor");
//				try {
//					System.out.println("type agen :"+ ((CollectorAgent) myAgent).getType());
//					System.out.println(myAgent.getGraphe().getNode(myAgent.getGraphe().getPosition()).getValueTreasure());
//					System.out.println("diamand : "+myAgent.getGraphe().getNode(myAgent.getGraphe().getPosition()).getDiamonds());
//					System.out.println("trésor : "+myAgent.getGraphe().getNode(myAgent.getGraphe().getPosition()).getTreasure());
//
//					System.out.println(((CollectorAgent) myAgent).canPick(myAgent.getGraphe().getNode(myAgent.getGraphe().getPosition())));
//				System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to continue");
//				System.in.read();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
				if(((CollectorAgent) myAgent).canPick(myAgent.getGraphe().getNode(myAgent.getGraphe().getPosition()))) {
					System.out.println("can pick");
					System.out.println(5);
					return 5;
				}
			}
		}
//		if (myAgent.isBlocked()){
////			System.out.println(myAgent.getcptblock());
//			return 2;
//		}
		if(myAgent instanceof TankerAgent && myAgent.getGraphe().isFullyExplored()) {
			System.out.println("tanker full explore" );
			return 10;
		}
		
		return 1;

	}

}
