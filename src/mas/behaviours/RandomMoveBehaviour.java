package mas.behaviours;

import java.util.List;
import java.util.Random;

import env.Attribute;

import env.Couple;
import jade.core.behaviours.OneShotBehaviour;
import mas.agents.Agent;
import mas.agents.ExploreAgent;

/**************************************
 * 
 * 
 * 				BEHAVIOUR
 * 
 * 
 **************************************/


public class RandomMoveBehaviour extends OneShotBehaviour {
	Agent myAgent;
	private static final long serialVersionUID = 9088209402507795289L;
	public RandomMoveBehaviour (Agent myagent) {
		super(myagent);
		this.myAgent=myagent;
	}


	@Override
	public void action() {
//		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
//		if (myPosition.equals("")) return;
//		List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();
//		Random r= new Random();
//		int moveId=r.nextInt(lobs.size());
//		boolean sucess=((mas.abstractAgent)this.myAgent).moveTo(lobs.get(moveId).getLeft());
//		if(myAgent.getPath().size()>0) {
//			String target=myAgent.getGoal();
//			if(sucess) {
//				myAgent.doPath(myAgent.getGraphe().getPath(myPosition, myPosition));
//			}else {
//				myAgent.doPath(myAgent.getGraphe().getPath(myPosition, target));
//
//			}
//
//		}
	
	
	String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
	if (myPosition=="") return;

	String target=myAgent.getGraphe().getRandom();
	System.out.println(target +"  tagret");
	myAgent.doPath(myAgent.getGraphe().getPath(myPosition, target));
	String nextId=myAgent.getNextMove();
	boolean sucess=((mas.abstractAgent)this.myAgent).moveTo(nextId);
	System.out.println(sucess);
	myAgent.hasMove(sucess);
	return;

	}

}