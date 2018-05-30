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
	private boolean search;
	
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
		search=true;
		this.map=new Graphe(this);
		path=new ArrayList<String>();
		FSMBehaviour f= new FSMBehaviour();
		f.registerFirstState(new ObserveBehaviour(this), "observe");
//		f.registerDefaultTransition(s1, s2);
		f.registerState(new MailCheckBehaviour(this), "mailcheck");
		f.registerState(new MoveBehaviour(this), "Deplacement");
		f.registerState(new ShareMapBehaviour(this), "sharemap");
		f.registerState(new PickBehaviour(this), "pick");
		f.registerState(new RandomMoveBehaviour(this), "randomMove");

		f.registerTransition("observe", "randomMove", 20); //interblocage extreme
		f.registerDefaultTransition("randomMove", "observe");

		f.registerDefaultTransition("observe", "mailcheck");
		f.registerTransition("observe", "pick", 5);
		
		f.registerDefaultTransition("pick", "observe");
		
		f.registerDefaultTransition("mailcheck", "Deplacement");
		
		f.registerDefaultTransition("Deplacement", "sharemap");
		
		f.registerDefaultTransition("sharemap", "observe");

		addBehaviour(f);
		System.out.println("ICI");

		
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
		setTankerName();
		this.type=this.getMyTreasureType();
		System.out.println(this.getMyTreasureType());
		System.out.println(tankerName);
		System.out.println(this.getBackPackFreeSpace());
		System.out.println("the agent "+this.getLocalName()+ " is started");

	}
	public boolean isUsefull() {
		if (this.type=="Treasure") {
			return this.getGraphe().containTreasure();
		}else {
			return this.getGraphe().containDiamonds();
			
		}
		
	}
	public String getTankerName() {
		return this.tankerName;
	}
	public void setSearch(boolean b) {
		this.search=b;
	}
	public boolean getSearch() {
		return this.search;
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
		if (this.type=="Diamonds"){
			return n.getDiamonds()>0;
		}
		if (this.type=="Treasure") {
			return n.getTreasure()>0;
		}
		return false;
	}
	public void hasMove(boolean sucess){
		if(sucess) {
			this.path.remove(0);
		}
		boolean b=((abstractAgent)this).emptyMyBackPack(tankerName);
		System.out.println("empty sac"+b);

	}
	public int getCapacity() {
		return capacity;
	}
	public void setTankerName() {
//		if(0==0) return;
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
