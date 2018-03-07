package utilities;

import jade.core.Agent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.graphstream.graph.Graph;

import env.Attribute;
import env.Couple;

public class World implements Serializable{
	
	private static final long serialVersionUID = -3723465736124146085L;
	private HashMap map;
	private Agent me;
	
	public World() {
		this.map = new HashMap<Object, Object>();
	}
}

