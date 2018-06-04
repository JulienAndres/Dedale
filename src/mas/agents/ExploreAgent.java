package mas.agents;

import utilities.*;
import jade.core.AID;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;


import utilities.Graphe;
import env.Attribute;
import env.Couple;
import env.EntityType;
import env.Environment;
import mas.abstractAgent;
import mas.behaviours.*;

/**
 * This method is automatically called when "agent".start() is executed.
 * Consider that Agent is launched for the first time. 
 * 			1) set the agent attributes 
 *	 		2) add the behaviours
 *          
 */

public class ExploreAgent extends Agent{

	private static final long serialVersionUID = -1784844593772918359L;
	private int nbAlea;
	private boolean searchTreasure;
	
	protected void setup(){

		super.setup();
		
		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		if(args!=null && args[0]!=null && args[1]!=null){
			deployAgent((Environment) args[0],(EntityType)args[1]);
		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}
		doWait(4000);

		this.searchTreasure=false;
		this.nbAlea=0;
		this.map=new Graphe(this);
		cptBlock=0;
		path=new ArrayList<String>();
		FSMBehaviour f= new FSMBehaviour();
		f.registerFirstState(new ObserveBehaviour(this), "observe");
		f.registerState(new MailCheckBehaviour(this), "mailcheck");
		f.registerState(new MoveBehaviour(this), "Deplacement");
		f.registerState(new ShareMapBehaviour(this), "sharemap");
		f.registerState(new RandomMoveBehaviour(this), "randomMove");
		f.registerState(new RandomWalkBehaviour(this), "randomWalk");
		f.registerState(new SendInterblocageBehaviour(this), "sendblock");
		f.registerState(new InterblocageBehaviour(this),"receiveblock");
		
		f.registerDefaultTransition("sendblock", "receiveblock");
		f.registerDefaultTransition("receiveblock", "observe");
		
		f.registerTransition("observe", "sendblock", 2);
		
		f.registerDefaultTransition("observe", "mailcheck");
		f.registerTransition("Deplacement", "randomMove", 5);
		f.registerDefaultTransition("randomMove", "sharemap");
		f.registerDefaultTransition("mailcheck", "Deplacement");
		f.registerTransition("mailcheck", "randomWalk", 5);
		f.registerDefaultTransition("randomWalk","sharemap");
		
		f.registerDefaultTransition("Deplacement", "sharemap");
		f.registerDefaultTransition("sharemap", "observe");
		f.registerTransition("observe", "randomMove", 20);

		addBehaviour(f);

		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("EXPLORE");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		System.out.println("the agent "+this.getLocalName()+ " is started");
//		try {
//		System.out.println("Press Enter in the console to allow the agent "+this.getLocalName() +" to continue");
//		System.in.read();
//	} catch (IOException e) {
//		e.printStackTrace();
//	}

	}
	public int getAlea() {
		return this.nbAlea;
	}
	public void decAlea() {
		this.nbAlea-=1;
	}
	public void setAlea(int nb) {
		this.nbAlea=nb;
	}
	public void setSearchTreasure(boolean b) {
		this.searchTreasure=b;
	}
	public boolean getSearchTreasure() {
		return this.searchTreasure;
	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){
	}


}