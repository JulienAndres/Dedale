package mas.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import env.Attribute;
import env.Couple;
import mas.agents.ExploreAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.OneShotBehaviour;;


public class InterblocageBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -1935358198561959915L;
	private ExploreAgent myAgent;
	
	public InterblocageBehaviour(ExploreAgent myAgent) {
		super(myAgent);
		this.myAgent = myAgent;
	}
	
	@Override
	public void action() {
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		if (myPosition!=""){
			System.out.println("interblocage");
			ACLMessage msg=new ACLMessage(ACLMessage.FAILURE);
	        msg.setSender(this.myAgent.getAID());
	        msg.setConversationId("interblocage");
	        String pos = myAgent.getPath().get(0);//pos to go
//	        int dist = dfs.distIntersection(mypos);
//	        msg.setContent(dist + "");
	        msg.setContent(pos + "");
	        ArrayList<AID>  receivers=myAgent.getAllAgent();
	        for(AID aid: receivers){
	            msg.addReceiver(aid);
	        }


			
		}

	}


}
