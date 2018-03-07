package mas.behaviours;

import java.util.List;
import java.util.Map;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;

public class StopBehaviour extends SimpleBehaviour {
	
	private Graph graph;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public StopBehaviour (final mas.abstractAgent myagent, Graph graph) {
		super(myagent);
		this.graph = graph;
	}
	
	@Override
	public void action() {
		Viewer viewer = graph.display();
	}
	
	
	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
