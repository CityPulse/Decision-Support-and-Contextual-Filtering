/**
 *
 */
package org.insight_centre.urq.citypulse.wp5.contextual_filtering;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.insight_centre.urq.citypulse.wp5.Configuration;

import citypulse.commons.contextual_filtering.contextual_event_request.ContextualEventRequest;
import citypulse.commons.contextual_filtering.contextual_event_request.FilteringFactor;
import citypulse.commons.contextual_filtering.contextual_event_request.FilteringFactorName;
import citypulse.commons.contextual_filtering.contextual_event_request.FilteringFactorValue;
import citypulse.commons.contextual_filtering.contextual_event_request.RankingElement;
import citypulse.commons.contextual_filtering.contextual_event_request.RankingElementName;
import citypulse.commons.contextual_filtering.contextual_event_request.RankingFactor;
import citypulse.commons.contextual_filtering.contextual_event_request.RankingFactorType;

/**
 * This class implements the rewriter in order to map the ContextualEventRequest
 * to Asp rules
 *
 * @author Thu-Le Pham
 * @organization INSIGHT, NUIG, Galway, Ireland
 * @email thule.pham@insight-centre.org
 */
public class ContextualEventRequestRewriter {
	public static Logger logger = Logger
			.getLogger(ContextualEventRequestRewriter.class.getPackage()
					.getName());
	/**
	 *
	 */
	private static ContextualEventRequestRewriter requestRewriter;

	/**
	 * @return
	 */
	public static ContextualEventRequestRewriter getInstance() {
		if (ContextualEventRequestRewriter.requestRewriter == null) {
			ContextualEventRequestRewriter.requestRewriter = new ContextualEventRequestRewriter();
		}
		return ContextualEventRequestRewriter.requestRewriter;
	}

	/**
	 *
	 */
	private ContextualEventRequestRewriter() {
	}

	/**
	 * This function maps FilteringFactors to Asp rules
	 *
	 * @param request
	 * @return
	 */
	private List<String> getFilteringFactors(
			final ContextualEventRequest request) {

		final List<String> rules = new LinkedList<String>();


		Set<FilteringFactorValue> eventSourceValues = new HashSet<FilteringFactorValue>();
		Set<FilteringFactorValue> eventCategoryValues = new HashSet<FilteringFactorValue>();
		Set<FilteringFactorValue> activityValues = new HashSet<FilteringFactorValue>();

		for (final FilteringFactor filteringFactor : request
				.getFilteringFactors()) {
			/*
			 *
			 */
			if(filteringFactor.getName().equals(FilteringFactorName.EVENT_SOURCE)){
				eventSourceValues = filteringFactor.getValues();
				logger.info("eventSourceValues = "
						+ eventSourceValues.toString());
			} else if (filteringFactor.getName().equals(
					FilteringFactorName.ACTIVITY)) {
				activityValues = queryKB(filteringFactor.getValues());
				logger.info("activityValues = " + activityValues.toString());
			} else if (filteringFactor.getName().equals(
					FilteringFactorName.EVENT_CATEGORY)) {
				eventCategoryValues = filteringFactor.getValues();
				logger.info("eventCategoryValues = "
						+ eventCategoryValues.toString());
			}
		}

		/*
		 * intersect event category
		 */
		Set<FilteringFactorValue> categoryValues = new HashSet<FilteringFactorValue>();
		if (activityValues.isEmpty()) {
			categoryValues = eventCategoryValues;
		} else {
			if (eventCategoryValues.isEmpty()) {
				categoryValues = activityValues;
			} else {
				for (final FilteringFactorValue value : activityValues) {
					for (final FilteringFactorValue cvalue : eventCategoryValues) {
						if (cvalue.getValue().equals(value.getValue())) {
							categoryValues.add(value);
							break;
						}
					}
				}
			}
		}
		logger.info("categoryValues = " + categoryValues.toString());

		/*
		 * form related event
		 */
		final String preStr = "filtering_event(";
		if (!categoryValues.isEmpty() && !eventSourceValues.isEmpty()) {
			for (final FilteringFactorValue cvalue : categoryValues) {
				for (final FilteringFactorValue svalue : eventSourceValues) {
					final StringBuilder strBuilder = new StringBuilder();
					strBuilder.append(preStr).append("\"")
							.append(cvalue.getValue()).append("\",")
							.append("\"").append(svalue.getValue())
							.append("\").");
					rules.add(strBuilder.toString());
				}
			}
			final String filterRule = "related_city_event(Id) :-filtering_event(Category, Source), city_event(Id, Category, Source).";
			rules.add(filterRule);
		} else if (!categoryValues.isEmpty() && eventSourceValues.isEmpty()) {
			for (final FilteringFactorValue cvalue : categoryValues) {
				final StringBuilder strBuilder = new StringBuilder();
				strBuilder.append(preStr).append("\"")
						.append(cvalue.getValue())
						.append("\").");
				rules.add(strBuilder.toString());
			}
			final String filterRule = "related_city_event(Id) :-filtering_event(Category), city_event(Id, Category, Source).";
			rules.add(filterRule);
		} else if (categoryValues.isEmpty() && !eventSourceValues.isEmpty()) {
			for (final FilteringFactorValue svalue : eventSourceValues) {
				final StringBuilder strBuilder = new StringBuilder();
				strBuilder.append(preStr).append("\"")
						.append(svalue.getValue())
						.append("\").");
				rules.add(strBuilder.toString());
			}
			final String filterRule = "related_city_event(Id) :-filtering_event(Source), city_event(Id, Category, Source).";
			rules.add(filterRule);
		}

		return rules;
	}


	/**
	 * This function gets all event categories that affect user's activity from
	 * KB
	 *
	 * @param values
	 * @return
	 */
	public Set<FilteringFactorValue> queryKB(Set<FilteringFactorValue> values) {
		/*
		 * TODO: query KB to get which event categories affect activities
		 */
		final Set<FilteringFactorValue> cats = new HashSet<FilteringFactorValue>();
		for (final FilteringFactorValue value : values) {
			cats.addAll(this.readEventAffectActivity(value.getValue()));
			// if (value.getValue().equals("CarCommute")) {

				// cats.add(new FilteringFactorValue(
				// "http://purl.oclc.org/NET/UNIS/sao/ec#" + "TrafficJam"));
				// cats.add(new FilteringFactorValue(
				// "http://purl.oclc.org/NET/UNIS/sao/ec#"
				// + "PublicParking"));
			// }
		}
		return cats;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	public List<String> getRankingEventData(
			Map<String, Integer> rankingData, RankingElementName name) {

		final List<String> rules = new LinkedList<String>();

		final String preStr = "ranking_city_event_data(";

		for (final Map.Entry<String, Integer> entry : rankingData
				.entrySet()) {
			final StringBuilder strBuilder = new StringBuilder();
			strBuilder.append(preStr).append("\"").append(entry.getKey())
					.append("\",\"").append(name).append("\",")
					.append(entry.getValue()).append(").");
			rules.add(strBuilder.toString());
		}
		return rules;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	private List<String> getRankingFactor(ContextualEventRequest request) {
		final List<String> rules = new LinkedList<String>();

		final RankingFactor rankingFactor = request.getRankingFactor();


		final String preStr = "ranking_multiplier(";
		for (final RankingElement e : rankingFactor.getRankingElements()) {
			final StringBuilder strBuilder = new StringBuilder();
			strBuilder.append(preStr).append("\"").append(e.getName())
					.append("\",").append(e.getValue().getValue()).append(").");
			rules.add(strBuilder.toString());
		}

		if (rankingFactor.getType().equals(RankingFactorType.LINEAR)) {
			rules.add("value_with_ranking_type(RankingElementName, M):- value(RankingElementName,Value),ranking_multiplier(RankingElementName,Int), M = Value*Int.");

			// #sum statement in Clingo does not work for same values --> can
			// not use sum here
			int i = 1;
			final StringBuilder strB1 = new StringBuilder();
			strB1.append("sum(C) :- ");
			final StringBuilder strB2 = new StringBuilder();
			for (final RankingElement e : rankingFactor.getRankingElements()) {
				strB1.append("value_with_ranking_type(").append("\"")
						.append(e.getName()).append("\",Value").append(i)
						.append("),");
				strB2.append("Value").append(i).append("+");
				i++;
			}
			String str2 = strB2.toString();
			str2 = str2.substring(0,str2.length()-1) + ".";
			strB1.append("C = ").append(str2);
			rules.add(strB1.toString());

			rules.add("criticality(C) :- C = M/100, sum(M).");
			// rules.add("criticality(C) :- S = #sum{M:value_with_ranking_type(_,M)}, C = S/100.");
		} else if (rankingFactor.getType().equals(RankingFactorType.ORDER)) {
			// TODO: add weak constraints
		}
		return rules;
	}

	/**
	 * This method maps a contextual event request to ASP rules
	 * 
	 * @param request
	 * @return a list of rules in ASP format
	 */
	public final List<String> getRules(final ContextualEventRequest request) {
		final List<String> rules = new LinkedList<String>();

		rules.addAll(getFilteringFactors(request));
		rules.addAll(getRankingFactor(request));

		return rules;
	}

	/**
	 * @param expiredEvents
	 * @return
	 */
	public List<String> getExpiredEvent(List<String> expiredEvents) {
		final List<String> rules = new LinkedList<String>();

		final String preStr = "expired_event(";

		for(final String str: expiredEvents){
			final StringBuilder strBuilder = new StringBuilder();
			strBuilder.append(preStr).append("\"").append(str).append("\").");
			rules.add(strBuilder.toString());
		}
		return rules;
	}

	public Set<FilteringFactorValue> readEventAffectActivity(String activity) {
		final Set<FilteringFactorValue> cats = new HashSet<FilteringFactorValue>();

		final String csvFile = Configuration.getInstance()
				.getCFResourceFolderPath() + "EventAffectActivity.csv";
		BufferedReader br = null;
		String line = "";
		final String cvsSplitBy = ",";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				final String[] effect = line.split(cvsSplitBy);
				if (effect[1].equals(activity)) {
					cats.add(new FilteringFactorValue(
							"http://purl.oclc.org/NET/UNIS/sao/ec#"
									+ effect[0].trim()));
				}

			}

		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}

		return cats;
	}

}
