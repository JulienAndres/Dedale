package mas.behaviours;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import env.Attribute;
import env.Couple;
import mas.agents.ExploreAgent;
import utilities.Graphe;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.core.behaviours.OneShotBehaviour;;


public class MailCheckBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -1935358198561959915L;
	private ExploreAgent myAgent;
	
	public MailCheckBehaviour(ExploreAgent myAgent) {
		super(myAgent);
		this.myAgent = myAgent;
	}
	
	@Override
	public void action() {
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		if (myPosition=="") return;

		
		MessageTemplate msgTemplate;
		ACLMessage msg = null;
		Serializable newMap = null;			
		Graphe graphe=null;
		msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		//Nombre total message dans boite aux lettres
		int nbmessage = this.myAgent.getCurQueueSize();
		//System.out.println(myAgent.getLocalName()+" nb message dans boite "+nbmessage);
		
		msg = ((mas.abstractAgent) this.myAgent).receive(msgTemplate);
		if (msg!=null) {
			try {
				newMap=msg.getContentObject();
				graphe=(Graphe) newMap;

				if (newMap!=null) {
					myAgent.getGraphe().sync(graphe);
					System.out.println("syncronisation "+myAgent.getLocalName());
				}
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			//System.out.println("pas de msg");
		}

	}


	
	public int onEnd(){
		return 1;

	}

}
