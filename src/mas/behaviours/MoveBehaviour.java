package mas.behaviours;

import java.io.IOException;
import java.util.List;

import jade.core.behaviours.OneShotBehaviour;
import utilities.Graphe;
import mas.abstractAgent;
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
		if(this.myAgent instanceof CollectorAgent) {
			((abstractAgent)this.myAgent).emptyMyBackPack(((CollectorAgent) this.myAgent).getTankerName());
		}
		System.out.println("process movement");
		onEnd=0;
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		if (myPosition!=""){
			
			if (!myAgent.getGraphe().isFullyExplored()) {
				if (this.myAgent instanceof TankerAgent){
					System.out.println("tanker explore");
				}
//				System.out.println("exploration");
				this.exploration(myPosition);
				return;
			}
			
			if(myAgent instanceof ExploreAgent && myAgent.getGraphe().isFullyExplored()){
				System.out.println("entre ici");
				if(myAgent.getGraphe().isUpdater()) {
//					System.out.print("isUpdater  "+myAgent.getName());
					if(((ExploreAgent) myAgent).getAlea()>0) {
//						System.out.println("IMPOSSIBLE");
//						System.out.println("alea "+((ExploreAgent) myAgent).getAlea());
						onEnd=5;
						((ExploreAgent) myAgent).decAlea();
						((ExploreAgent) myAgent).setSearchTreasure(false);

						return;
					}
					String nextId=myAgent.getNextMove();
					
					//go next move
//					System.out.print("nextmove"+nextId);
					if(nextId!="") {
//						System.out.println("    go next");
						boolean sucess=((mas.abstractAgent)this.myAgent).moveTo(nextId);
						myAgent.hasMove(sucess);
						return;
					}
					
					//compute next treasure
					if((nextId=="" && !((ExploreAgent) myAgent).getSearchTreasure()) || (nextId=="" && ((ExploreAgent)myAgent).getSearchTreasure() && myAgent.getGraphe().getNode(myPosition).hasTreasure())) {
//						System.out.print("compute next treasure   hastreasure :  ");
//						System.out.print(myAgent.getGraphe().hasTreasure() + "pathsize  ");
						String target=myAgent.getGraphe().getOldestTreasure();
						myAgent.doPath(myAgent.getGraphe().getPath(myPosition, target));
//						System.out.println(myAgent.getGraphe().getPath(myPosition, target).size()+"   ");
						nextId=myAgent.getNextMove();
						boolean sucess=((mas.abstractAgent)this.myAgent).moveTo(nextId);
						myAgent.hasMove(sucess);
						((ExploreAgent) myAgent).setSearchTreasure(true);
						return;
					}
					//compute next random
					if(nextId=="" && ((ExploreAgent)myAgent).getSearchTreasure() ) {
//						System.out.println("treasure find go random ");
						((ExploreAgent) myAgent).setSearchTreasure(false);
						((ExploreAgent) myAgent).setAlea(7);
						onEnd=5;
						return;

					}
					//
					
				}else {
					System.out.println("AGENT RESET GRAPHE"+myAgent.getName());
					myAgent.getGraphe().resetGraphe();
				}
				return;

			}
			if(myAgent instanceof CollectorAgent) {
				String nextId=myAgent.getNextMove();
				if (nextId=="") {
					System.out.println("nextID not know");
					if (myAgent.getGraphe().isFullyExplored() && ((CollectorAgent)myAgent).getBackPackFreeSpace()<((CollectorAgent)myAgent).getCapacity()) {
						System.out.println("full backpack go tanker");
						String target=myAgent.getGraphe().getTankerNode();//Aller au silo.
						myAgent.doPath(myAgent.getGraphe().getPath(myPosition, target));
						boolean sucess=((mas.abstractAgent)this.myAgent).moveTo(nextId);
						myAgent.hasMove(sucess);
						return;
						
					}else if(((CollectorAgent)myAgent).getBackPackFreeSpace()==((CollectorAgent)myAgent).getCapacity() && myAgent.getGraphe().hasTreasure()) {
						System.out.println("backpack vide go tresor");
//						String target=myAgent.getGraphe().getClosestTreasure(myPosition);
						String target=myAgent.getGraphe().getBestTreasure();
						if(target=="") {
							target=myAgent.getGraphe().getClosestTreasure(myPosition);
						}
						System.out.println(((CollectorAgent) myAgent).getType());
						System.out.println("besttresor :  " +target);
						System.out.println(myAgent.getGraphe().getNode(target).getDiamonds());
						System.out.println(myAgent.getGraphe().getNode(target).getTreasure());
						System.out.println(((CollectorAgent) myAgent).getType()+" diamons  "+myAgent.getGraphe().getNode(target).getDiamonds()+" tresor "+myAgent.getGraphe().getNode(target).getTreasure());
						myAgent.doPath(myAgent.getGraphe().getPath(myPosition, target));
						boolean sucess=((mas.abstractAgent)this.myAgent).moveTo(nextId);
						myAgent.hasMove(sucess);
						return;
					}else if(((CollectorAgent)myAgent).getBackPackFreeSpace()>((CollectorAgent)myAgent).getCapacity()/2) {
						System.out.println("full backpack go tanker"+myAgent.getName());
						String target=myAgent.getGraphe().getTankerNode();//Aller au silo.
						myAgent.doPath(myAgent.getGraphe().getPath(myPosition, target));
						boolean sucess=((mas.abstractAgent)this.myAgent).moveTo(nextId);
						myAgent.hasMove(sucess);
						return;
					}
				
				
				} else {
					System.out.println("nextid know");
					boolean sucess=((mas.abstractAgent)this.myAgent).moveTo(nextId);
					myAgent.hasMove(sucess);
					return;
				}
				

			}
			if (myAgent instanceof TankerAgent) {
				if(myAgent.getGraphe().isFullyExplored()) {
					if(!((TankerAgent)myAgent).isTargetFound()) {
						String target=myAgent.getGraphe().getTankerNode();
//						if(myPosition.equals(target)) {
//							((TankerAgent) myAgent).setTargetFound(true);
//						}
//						System.out.println(target);
						myAgent.doPath(myAgent.getGraphe().getPath(myPosition, target));
						String nextId=myAgent.getNextMove();
//						System.out.println(nextId);
						((mas.abstractAgent)this.myAgent).moveTo(nextId);
						return;
					}else {
						onEnd=2;
						((TankerAgent) myAgent).setTargetFound(false);
						return;
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
		//2 : tanker
		//5 : ExploreAgent -> moveAlea
		return onEnd;
	}
}