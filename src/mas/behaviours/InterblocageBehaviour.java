package mas.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import env.Attribute;
import env.Couple;
import mas.agents.Agent;
import mas.agents.ExploreAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.core.behaviours.OneShotBehaviour;;


public class InterblocageBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -1935358198561959915L;
	private Agent myAgent;
	private int onEnd;
	
	public InterblocageBehaviour(Agent myAgent) {
		super(myAgent);
		this.myAgent = myAgent;
	}
	
	@Override
	public void action() {
		System.out.println(myAgent.getPath());
		onEnd=0;
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		if (myPosition!=""){
	        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.FAILURE);
	        
	        ACLMessage msg = this.myAgent.blockingReceive(mt,100);
	        
	        if(msg!=null) {
	        	System.out.println("msg recu-> process");
	        	try {
	        		DataStore DS=(DataStore) msg.getContentObject();
        		
	        		 String msgGoal= (String)DS.get("goal");
	        		 String msgPosition=(String)DS.get("position");
	        		 int msgDist=(int)DS.get("dist");
	        		
	        		
	        		 int myDist=myAgent.getGraphe().distCouloir(myPosition);
	        		 AID msgSender=msg.getSender();	        		
	        		 if(msgDist==myDist) {
	        			 if(myAgent.getLocalName().compareTo(msgSender.getLocalName())>0) {
	        				 onEnd=1;
	        			 }else {
	        				 onEnd=2;
	        			 }
	        		 }else {
	        			 if(msgDist==-1 || myDist==-1) {
	        				 //AFAIRE
	        				 System.out.println("pouet");
	        				 if (msgDist==-1){
	        					 onEnd=1;	 
	        				 }else {
	        					 onEnd=2;
	        				 }
	        			 }
	        			 if (msgDist< myDist) {
	        				 onEnd=2;
	        			 }else {
	        				 onEnd=1;
	        			 }
	        		 }
	        		 move(onEnd,msgPosition,msgGoal);

	        }catch(UnreadableException e) {
	        	e.printStackTrace();
	        }
	        }	
	      }
		}
	
	public void move(int res,String msgPosition,String msgGoal) {
		System.out.println("ici?");
		if (onEnd==2) {
			boolean sucess=myAgent.moveTo(myAgent.getNextMove());
			System.out.println(myAgent.getLocalName()+"avance "+myAgent.getNextMove());
			myAgent.hasMove(sucess);
		}else {
			System.out.println(myAgent.getLocalName()+"recule");

			String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
			HashSet<String> toMove=myAgent.getGraphe().getNode(myPosition).getVoisins();
			toMove.remove(msgPosition);
			toMove.remove(msgGoal);
			for (String i:toMove) {
				boolean b=myAgent.moveTo(i);
				if(b) {
					break;
				}
			}
			
		}
	}
	
	
	
	
	//onEnd : recule =1 avance =2 rien =0
	
	public int onEnd(){
		System.out.println(myAgent.getLocalName()+"  alo?");
		return 0;

	}

}
