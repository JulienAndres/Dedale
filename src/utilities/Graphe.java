package utilities;

import jade.util.leap.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.Viewer;

import env.Attribute;
import env.Couple;

public class Graphe {
	
	public static Map<String, Couple<Couple<String,Boolean>,Couple<List<Attribute>, List<String>>>> generateMapFromGraph(Graph graph){
		Map map = new HashMap<>();
		// TODO
		
		
		return null;
	}
	
	public static Graph generateGraphFromMap(Map<String, Couple<Couple<String,Boolean>,Couple<List<Attribute>, List<String>>>> map){
		Graph g=new SingleGraph("Graph from map");
		
		Set<String> keySet = map.keySet();
		for(String s : keySet){
			g.addNode(s);
			Node n = g.getNode(s);
			n.addAttribute("ui.label", map.get(s).getRight().getLeft());
		}
		
		for(String s : keySet){
			List<String> children =  map.get(s).getRight().getRight();
			if(children != null){
				for (String f : children){
					if(g.getEdge(s+f) == null && g.getEdge(f+s)==null){
						g.addEdge(s+f, s, f);
					}
				}
			}
		}
		return g;
	}
	
	public static String getMaxDegreeNode(Graph g){
		return Toolkit.degreeMap(g).get(0).getId();
		
	}
	
}
