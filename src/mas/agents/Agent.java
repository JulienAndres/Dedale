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

/**
 * This method is automatically called when "agent".start() is executed.
 * Consider that Agent is launched for the first time. 
 * 			1) set the agent attributes 
 *	 		2) add the behaviours
 *          
 */

public class Agent extends abstractAgent{

	private static final long serialVersionUID = -1784844593772918359L;
	
//	private Map<String, Couple<Couple<String,Boolean>,Couple<List<Attribute>, List<String>>>> map;
	protected Graphe map;
	protected List<String> path;
	protected int cptBlock;

	
	
	protected void setup(){

		super.setup();
		cptBlock=0;
		
		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		this.map=new Graphe(this);
		if(args!=null && args[0]!=null && args[1]!=null){
			deployAgent((Environment) args[0],(EntityType)args[1]);
		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}

	}
	public Graphe getGraphe() {
		return map;
	}
	public List<String> getPath() {
		return this.path;
	}
	
	public void doPath(ArrayList<String> path) {
		this.path=path;
	}
	public String getNextMove() {
		if (this.path.isEmpty()){
			return "";
		}
		String toRet=path.get(0);
		return toRet;
	}
	public void hasMove(boolean sucess){
		if(sucess) {
			this.path.remove(0);
		}

	}
	//Return AID of all except his own
	public ArrayList<AID>  getAllAgent() {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("EXPLORE");
		dfd.addServices(sd);
		ArrayList<AID> agents = new ArrayList<AID>();
		try {
			DFAgentDescription[] result = DFService.search(this, dfd);
			for(DFAgentDescription agent:result){
				if(!this.getAID().toString().equals(agent.getName().toString())){
					agents.add(agent.getName());

				}
			}
			
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		return agents;
	}
	
	public ArrayList<AID>  getTankerAgent() {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("TANKER");
		dfd.addServices(sd);
		ArrayList<AID> agents = new ArrayList<AID>();
		try {
			DFAgentDescription[] result = DFService.search(this, dfd);
			for(DFAgentDescription agent:result){
				if(!this.getAID().toString().equals(agent.getName().toString())){
					agents.add(agent.getName());

				}
			}
			
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		return agents;
	}
	
	
	
public void resetBlock() {
	this.cptBlock=0;
}
public void incBlock() {
	this.cptBlock+=1;
}
public boolean isBlocked() { //return true si bloqué
	return this.cptBlock>=5;
}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){
	}


}