package org.flowerplatform.codesync.regex.action;

import java.util.ArrayList;

import org.flowerplatform.codesync.regex.State;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Elena Posea
 */
public class CheckStateAction extends RegexAction {
	String[] states;

	/**
	 * @param csvListOfStates a comma separated list of states; this action checks whether you are in one of this statea, otherwise, stop executing other actions
	 */
	public CheckStateAction(String csvListOfStates) {
		this.states = csvListOfStates.split(",");
	}

	@Override
	public void executeAction(RegexProcessingSession param) {
		// if current state/top of the stack is not in this list => set
		// DO_NOT_EXECUTE_OTHER_ACTIONS true
		boolean aux = true;
		ArrayList<Object> stateStack = (ArrayList<Object>) param.context.get("stateStack");
		State top = (State) stateStack.get(0);
		String currentStateType = ((Node) top.node).getType();
		for (String state : states) {
			if (currentStateType.equals(state)) {
				aux = false;
				break;
			}
		}
		param.context.put("DO_NOT_EXECUTE_OTHER_ACTIONS", aux);	
	}

}
