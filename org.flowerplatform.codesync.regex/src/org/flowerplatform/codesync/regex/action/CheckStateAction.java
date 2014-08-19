package org.flowerplatform.codesync.regex.action;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

public class CheckStateAction  extends RegexAction{
	String []states;
	
	public CheckStateAction(String csvListOfStates) {
		this.states = csvListOfStates.split(",");
	}
	
	@Override
	public void executeAction(RegexProcessingSession param) {
		// if current state/top of the stack is not in this list => set
		// DO_NOT_EXECUTE_OTHER_ACTIONS true
		boolean aux = true;
		String currentStateType = ((Node) param.stateStack.get(0).node).getType();
		for (String state : states) {
			if (currentStateType.equals(state)) {
				aux = false;
				break;
			}
		}
		param.DO_NOT_EXECUTE_OTHER_ACTIONS = aux;
	}

}
