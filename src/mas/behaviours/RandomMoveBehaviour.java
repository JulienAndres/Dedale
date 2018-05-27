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

	private static final long serialVersionUID = 9088209402507795289L;
	public RandomMoveBehaviour (Agent myagent) {
		super(myagent);
	}


	@Override
	public void action() {
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		if (myPosition.equals("")) return;
		List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();
		Random r= new Random();
		int moveId=r.nextInt(lobs.size());
		((mas.abstractAgent)this.myAgent).moveTo(lobs.get(moveId).getLeft());

	}

}