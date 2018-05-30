package mas.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import env.Attribute;
import env.Couple;
import mas.agents.Agent;
import mas.agents.ExploreAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.core.behaviours.OneShotBehaviour;;


public class SendInterblocageBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -1935358198561959915L;
	private Agent myAgent;
	private int rnd;
	
	public SendInterblocageBehaviour(Agent myAgent) {
		super(myAgent);
		this.myAgent = myAgent;
		
	}
	
	@Override
	public void action() {
		if(0==0) {
			return;
		}
		
		
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		if (myPosition!=""){
	        ACLMessage msg = new ACLMessage(ACLMessage.FAILURE);
	        msg.setSender(this.myAgent.getAID());
	        ArrayList<AID>  receivers=myAgent.getAllAgent();
	        for(AID aid: receivers){
	            msg.addReceiver(aid);
	        }
	        //String msgGoal=myAgent.getGoal();
	        
	        	DataStore DS = new DataStore();
	        	DS.put("goal", myAgent.getNextMove());
	        	DS.put("position", myPosition);
	        	DS.put("cptblock", myAgent.getcptblock());
	        	DS.put("path", (ArrayList<String>)myAgent.getPath());
	        	DS.put("dist", myAgent.getGraphe().distCouloir(myPosition));
//	        	
	        	
	        	try {
					msg.setContentObject(DS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	     
	        myAgent.sendMessage(msg);
	        
		
			
			
//			System.out.println("interblocage");
//			ACLMessage msg=new ACLMessage(ACLMessage.FAILURE);
//	        msg.setSender(this.myAgent.getAID());
//	        msg.setConversationId("interblocage");
//	        String pos = myAgent.getPath().get(0);//pos to go
////	        int dist = dfs.distIntersection(mypos);
////	        msg.setCoééntent(dist + "");
//	        msg.setContent(pos + "");
//	        ArrayList<AID>  receivers=myAgent.getAllAgent();
//	        for(AID aid: receivers){
//	            msg.addReceiver(aid);
//	        }
			
		}

	}


}
