package mas.behaviours;

import java.io.IOException;
import java.util.List;

import jade.core.behaviours.OneShotBehaviour;
import utilities.Graphe;
import mas.agents.*;



public class MoveBehaviour extends OneShotBehaviour{

	private static final long serialVersionUID = -1784844593772918359L;
	private Agent myAgent;
	private int onEnd=0;
	public MoveBehaviour (Agent myAgent) {
		super(myAgent);
		this.myAgent=myAgent;
	}

	@Override
	public void action() {
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

		if (myPosition!=""){
			
			if (!myAgent.getGraphe().isFullyExplored()) {
				this.exploration(myPosition);
			}else if(myAgent instanceof ExploreAgent){
				System.out.println("EXPLORATION FINI"+myAgent.getLocalName());
				myAgent.getGraphe().resetGraphe();

			}
			
			if(myAgent instanceof CollectorAgent) {
				String nextId=myAgent.getNextMove();
				if (nextId=="") {
					if (myAgent.getGraphe().isFullyExplored() && ((CollectorAgent)myAgent).getBackPackFreeSpace()==0) {
						String target=myAgent.getGraphe().getTankerNode();//Aller au silo.
						myAgent.doPath(myAgent.getGraphe().getPath(myPosition, target));
						boolean sucess=((mas.abstractAgent)this.myAgent).moveTo(nextId);
						myAgent.hasMove(sucess);
						return;
						
					}else if(((CollectorAgent)myAgent).getBackPackFreeSpace()==((CollectorAgent)myAgent).getCapacity()) {
						String target=myAgent.getGraphe().getClosestTreasure(myPosition);
						myAgent.doPath(myAgent.getGraphe().getPath(myPosition, target));
						boolean sucess=((mas.abstractAgent)this.myAgent).moveTo(nextId);
						myAgent.hasMove(sucess);
						return;
					}
				
				
				
				
				
				} else {
					boolean sucess=((mas.abstractAgent)this.myAgent).moveTo(nextId);
					myAgent.hasMove(sucess);
					return;
				}
				

			}
			if (myAgent instanceof TankerAgent) {
				if(myAgent.getGraphe().isFullyExplored()) {
					if(!((TankerAgent)myAgent).isTargetFound()) {
						String target=myAgent.getGraphe().getTankerNode();
						if(myPosition.equals(target)) {
							((TankerAgent) myAgent).setTargetFound(true);
						}
						System.out.println(target);
						myAgent.doPath(myAgent.getGraphe().getPath(myPosition, target));
						String nextId=myAgent.getNextMove();
						System.out.println(nextId);
						((mas.abstractAgent)this.myAgent).moveTo(nextId);
					}else {
						onEnd=2;
						((TankerAgent) myAgent).setTargetFound(false);
					}

				}
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

		boolean sucess=((mas.abstractAgent)this.myAgent).moveTo(nextId);
		myAgent.hasMove(sucess);

		
		

	}
	
	public int onEnd(){
		return onEnd;
	}
}