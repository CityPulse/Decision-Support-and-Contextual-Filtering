package org.insight_centre.urq.citypulse.wp5.decision_support_system.interfaces;

import java.util.List;

import citypulse.commons.reasoning_request.ReasoningRequest;

/**
 * Inteface for classes that derive logic rules from Reasoning Requests
 *
 * @author Stefano Germano
 * @organization INSIGHT@NUIG, Galway, Ireland
 * @email stefano.germano@insight-centre.org
 */
public interface RequestRewriterInterface {
	/**
	 * @param request a Reasoning Request
	 * @return logic rules automatically obtained from the Reasoning Request
	 */
	public List<String> getRules(ReasoningRequest request);
}
