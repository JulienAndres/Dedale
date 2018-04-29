package mas.behaviours;

import java.io.IOException;
import java.util.List;

import env.Attribute;
import env.Couple;
import mas.agents.ExploreAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class ShareMapBehaviour extends SimpleBehaviour{

	private static final long serialVersionUID = -2379766870233405738L;
	private ExploreAgent myAgent;
	
	public ShareMapBehaviour(ExploreAgent myAgent) {
		super(myAgent);
		this.myAgent=myAgent;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		System.out.println(myAgent.getLocalName() + ": Send graph");
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

		ACLMessage msg=new ACLMessage(ACLMessage.INFORM);
		msg.setSender(this.myAgent.getAID());
		msg.setConversationId("map");
		if (myPosition.equals("")) return;
		
		try {
			msg.setContentObject("test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(AID i:myAgent.getAllAgent()) {
			msg.addReceiver(i);
			
		}
		System.out.println("avantenvoie");
		((mas.abstractAgent)this.myAgent).sendMessage(msg);
		System.out.println("apres");

		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return true;
	}

}
