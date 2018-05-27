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
import java.util.ArrayList;


import utilities.Graphe;
import env.Attribute;
import env.Couple;
import env.EntityType;
import env.Environment;
import mas.abstractAgent;
import mas.behaviours.*;


public class TankerAgent extends Agent{
	private boolean targetfound;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1784844593772918359L;

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
		
		cptBlock=0;
		this.map=new Graphe(this);
		path=new ArrayList<String>();
		FSMBehaviour f= new FSMBehaviour();
		f.registerFirstState(new ObserveBehaviour(this), "observe");
		f.registerState(new MailCheckBehaviour(this), "mailcheck");
		f.registerState(new MoveBehaviour(this), "Deplacement");
		f.registerState(new ShareMapBehaviour(this), "sharemap");
		f.registerState(new RandomMoveBehaviour(this), "randomMove");
		
		f.registerDefaultTransition("observe", "mailcheck");		
		f.registerDefaultTransition("mailcheck", "Deplacement");
		f.registerDefaultTransition("Deplacement", "sharemap");
		f.registerDefaultTransition("sharemap", "observe");
		f.registerTransition("Deplacement", "randomMove", 2);
		f.registerDefaultTransition("randomMove", "sharemap");

		addBehaviour(f);

		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("TANKER");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		doWait(2000);

		System.out.println("the agent "+this.getLocalName()+ " is started");

	}
	public boolean isTargetFound() {
		return this.targetfound;
	}
	public void setTargetFound(boolean a) {
		this.targetfound=a;
	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}
}
