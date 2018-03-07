package mas.agents;

import jade.core.behaviours.FSMBehaviour;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;

import utilities.Graphe;
import utilities.World;
import env.Attribute;
import env.Couple;
import env.EntityType;
import env.Environment;
import mas.abstractAgent;
import mas.behaviours.*;


public class ExploreAgent extends abstractAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1784844593772918359L;
	private Map<String, Couple<Couple<String,Boolean>,Couple<List<Attribute>, List<String>>>> map;
	private World world;

	/**
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1) set the agent attributes 
	 *	 		2) add the behaviours
	 *          
	 */
	protected void setup(){

		super.setup();
		
		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		this.map=new HashMap<String, Couple<Couple<String,Boolean>,Couple<List<Attribute>, List<String>>>>();
		if(args!=null && args[0]!=null && args[1]!=null){
			deployAgent((Environment) args[0],(EntityType)args[1]);
		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}
		
		//Finite State Machine Behaviours
		FSMBehaviour f= new FSMBehaviour();
		f.registerFirstState(new DFSBehaviour(this, map), "Deplacement");
		//f.registerState(new StopBehaviour(), "Stop");
		f.registerTransition("Deplacement", "Stop", 1);
		addBehaviour(f);
		//Add the behaviours
		//addBehaviour(new DFSBehaviour(this, map));
		
		System.out.println("the agent "+this.getLocalName()+ " is started");

	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}