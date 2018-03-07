package mas.behaviours;

import mas.agents.ExploreAgent;
import jade.core.behaviours.FSMBehaviour;

public class ExploreFSMBehaviour extends FSMBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3206090280374285892L;

	public ExploreFSMBehaviour(ExploreAgent myAgent){
		super(myAgent);
		
		this.registerFirstState(new ObserveBehaviour(myAgent), "OBSERVE");
		this.registerLastState(new StopBehaviour(myAgent), "STOP");
		
		this.registerState(new MoveBehaviour(myAgent), "MOVE");
		this.registerState(new ShareBehaviour(myAgent), "SHARE");
		this.registerState(new CheckMessagesBehaviour(myAgent), "CHECK_MESSAGES");
		
		this.registerTransition("OBSERVE", "SHARE", 1);
		
		this.registerTransition("OBSERVE", "CHECK_MESSAGES", 3);
		this.registerTransition("CHECK_MESSAGES", "MOVE", 0);
		
	}
}