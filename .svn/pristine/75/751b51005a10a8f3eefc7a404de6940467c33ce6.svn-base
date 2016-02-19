/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Stefano Germano - Insight Centre for Data Analytics NUIG
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package org.insight_centre.urq.citypulse.wp5.decision_support_system;

import java.util.LinkedList;
import java.util.List;

import org.insight_centre.urq.citypulse.wp5.decision_support_system.interfaces.RequestRewriterInterface;

import citypulse.commons.reasoning_request.ReasoningRequest;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalConstraint;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalPreference;
import citypulse.commons.reasoning_request.functional_requirements.FunctionalPreferenceOperation;

/**
 * Derives logic rules from a Reasoning Request
 * It's a singleton
 *
 * @author Stefano Germano
 *
 */
public class RequestRewriter implements RequestRewriterInterface {

	/**
	 * The only instance of the class (Singleton pattern)
	 */
	private static RequestRewriter requestRewriter;

	/**
	 * @return the only instance of the class (Singleton pattern)
	 */
	public static RequestRewriter getInstance() {
		if (RequestRewriter.requestRewriter == null)
			RequestRewriter.requestRewriter = new RequestRewriter();
		return RequestRewriter.requestRewriter;
	}

	/**
	 * Private Constructor (Singleton pattern)
	 */
	private RequestRewriter() {
	}

	/**
	 * @param request a Reasoning Request
	 * @return the translation of Reasoning Request Constraints
	 */
	private List<String> getConstraints(final ReasoningRequest request) {
		final List<String> constraints = new LinkedList<>();

		for (final FunctionalConstraint functionalConstraint : request
				.getFunctionalDetails().getFunctionalConstraints()
				.getFunctionalConstraints()) {
			constraints.add(String.format(":- violatedConstraint(\"%s\").",
					functionalConstraint.getName()));
			// FIXME simplified, just considering integers
			constraints
					.add(String
							.format("violatedConstraint(\"%s\") :- valueOf(\"%s\", AV), AV %s %d.",
									functionalConstraint.getName(),
									functionalConstraint.getName(),
									functionalConstraint.getOperator()
											.getComplementaryOperator(),
							functionalConstraint.getValue().getValue()));
		}

		return constraints;
	}

	/**
	 * @param request a Reasoning Request
	 * @return the translation of Reasoning Request Preferences
	 */
	private List<String> getPreferences(final ReasoningRequest request) {
		final List<String> preferences = new LinkedList<>();

		for (final FunctionalPreference functionalPreference : request
				.getFunctionalDetails().getFunctionalPreferences()
				.getFunctionalPreferences())
			// FIXME simplified, just considering minimization
			if (functionalPreference.getOperation() == FunctionalPreferenceOperation.MINIMIZE)
				preferences.add(String.format(
						":~ valueOf(\"%s\", AV). [AV@%d]",
						functionalPreference.getValue(),
						functionalPreference.getOrder()));

		return preferences;
	}

	/**
	 * @see
	 * org.insight_centre.urq.citypulse.wp5.decision_support_system.interfaces
	 * .RequestRewriterInterface
	 * #getRules(org.insight_centre.urq.citypulse.commons
	 * .reasoning_request.ReasoningRequest)
	 */
	@Override
	public List<String> getRules(final ReasoningRequest request) {
		final List<String> rules = new LinkedList<>();

		rules.addAll(getConstraints(request));
		rules.addAll(getPreferences(request));

		return rules;
	}

}
