package org.insight_centre.urq.citypulse.wp5.decision_support_system.interfaces;

import java.util.List;

import citypulse.commons.reasoning_request.Answers;
import citypulse.commons.reasoning_request.ReasoningRequest;

/**
 * Inteface for classes that manage the communication to the ASP solver
 *
 * @author Stefano Germano
 * @organization INSIGHT@NUIG, Galway, Ireland
 * @email stefano.germano@insight-centre.org
 */
public interface CoreEngineInterface {
	/**
	 * Invokes the ASP solver and returns the answers obtained
	 *
	 * @param reasoningRequest
	 * @param rules
	 * @return
	 */
	public Answers performReasoning(ReasoningRequest reasoningRequest,
			List<String> rules);
}
