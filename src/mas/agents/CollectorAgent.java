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


public class CollectorAgent extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1784844593772918359L;

	private int capacity;
	private String type;
	private String tankerName="";
	
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
		
        capacity = this.getBackPackFreeSpace();
        type="NONE";
		cptBlock=0;
		this.map=new Graphe(this);
		path=new ArrayList<String>();
		FSMBehaviour f= new FSMBehaviour();
		f.registerFirstState(new ObserveBehaviour(this), "observe");
		f.registerState(new MailCheckBehaviour(this), "mailcheck");
		f.registerState(new MoveBehaviour(this), "Deplacement");
		f.registerState(new ShareMapBehaviour(this), "sharemap");
		f.registerState(new PickBehaviour(this), "pick");
		
		f.registerDefaultTransition("observe", "mailcheck");
		f.registerTransition("observe", "pick", 5);
		f.registerDefaultTransition("pick", "observe");
		f.registerDefaultTransition("mailcheck", "Deplacement");
		f.registerDefaultTransition("Deplacement", "sharemap");
		f.registerDefaultTransition("sharemap", "observe");

		addBehaviour(f);

		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("COLLECT");
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
	public void setTreasure() {
		this.type="treasure";
	}
	public void setDiamonds() {
		this.type="diamonds";
	}
	public String getType() {
		return this.type;
	}
	public boolean canPick(Node n) {
		if (this.getBackPackFreeSpace()==0) {
			return false;
		}
		if (this.type=="NONE") {
			return true;
		}
		if (this.type=="diamonds"){
			return n.getDiamonds()>0;
		}
		if (this.type=="treasure") {
			return n.getTreasure()>0;
		}
		return false;
	}
	public void hasMove(boolean sucess){
		if(sucess) {
			this.path.remove(0);
		}
		this.emptyMyBackPack(tankerName);

	}
	public int getCapacity() {
		return capacity;
	}
	public void setTankerName() {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("TANKER");
        dfd.addServices(sd);
        DFAgentDescription[] results;

        while(this.tankerName == "") {
            try {
                results = DFService.search(this, dfd);
                if (results.length > 0) {
                    for (DFAgentDescription d : results) {
                        tankerName = d.getName().getLocalName();
                    }
                }
            } catch (FIPAException e) {
                e.printStackTrace();
                return;
            }
        }
	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}
}
