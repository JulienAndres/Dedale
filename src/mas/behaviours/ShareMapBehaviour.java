package mas.behaviours;

import java.io.IOException;
import java.util.List;

import env.Attribute;
import env.Couple;
import mas.agents.Agent;
import mas.agents.ExploreAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class ShareMapBehaviour extends OneShotBehaviour{

	private static final long serialVersionUID = -2379766870233405738L;
	private Agent myAgent;
	
	public ShareMapBehaviour(Agent myAgent) {
		super(myAgent);
		this.myAgent=myAgent;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

		ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
		msg.setSender(this.myAgent.getAID());
		msg.setConversationId("map");
		if (myPosition.equals("")) return;
//		if (myAgent.getGraphe().getOtherAgent().isEmpty()){
//			myAgent.getGraphe().initOtherCollect();
//		}
		try {
			msg.setContentObject(myAgent.getGraphe());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(AID i:myAgent.getAllAgent()) {
			msg.addReceiver(i);
			
		}
		((mas.abstractAgent)this.myAgent).sendMessage(msg);

		
	}


}
