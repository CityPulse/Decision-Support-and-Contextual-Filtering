/**
 *
 */
package org.insight_centre.urq.citypulse.wp5.test.CF.main;

import java.util.HashMap;
import java.util.Map;

import citypulse.commons.data.Coordinate;

/**
 * @author Thu-Le Pham
 * @date 29 Sep 2015
 */
/**
 * @author thule.pham
 *
 */
public class TestOntology {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/**
		 * CityEvent example
		 */
		// final CityEvent event = new CityEvent();
		// event.setEventId("event1");
		// event.setEventCategory(new EventCategory("TrafficJam"));
		// event.setEventPlace(new Point(1, 1));
		// event.setEventSource(EventSource.SENSOR);
		// event.setEventTime(System.currentTimeMillis());
		// event.setEventLevel(3);
		//
		// System.out.println("event = " + event.toString());
		//
		// final GsonBuilder builder = new GsonBuilder();
		// builder.registerTypeAdapter(Place.class, new PlaceAdapter());
		// final Gson gson = builder.create();
		//
		// final String gsonStr = gson.toJson(event);
		// System.out.println("gsonStr = " + gsonStr);
		//
		//
		// final CityEvent eventGson = gson.fromJson(gsonStr, CityEvent.class);
		// System.out.println("eventGson = " + eventGson.toString());
		//
		// final CityEvent event1 = new CityEvent();
		// event1.setEventId("event2");
		// event1.setEventCategory(new EventCategory("PublicParking"));
		// event1.setEventPlace(new Point(2, 2));
		// event1.setEventSource(EventSource.SOCIAL_NETWORK);
		// event1.setEventTime(System.currentTimeMillis());
		// event1.setEventLevel(2);
		//
		// final CityEvent event2 = new CityEvent();
		// event2.setEventId("event3");
		// event2.setEventCategory(new EventCategory("HotWeather"));
		// event2.setEventPlace(new Point(3, 3));
		// event2.setEventSource(EventSource.SOCIAL_NETWORK);
		// event2.setEventTime(System.currentTimeMillis());
		// event2.setEventLevel(1);
		//
		// /**
		// * Activity example
		// */
		// final Activity activity = new Activity("activity1");
		// activity.setActivityName("CarCommute");
		// activity.setActivityPlace(new Point(1, 1));
		// activity.setActivityTime(System.currentTimeMillis());
		//
		// System.out.println("activity = " + activity.toString());
		//
		// /**
		// * ContextualEventRequest example
		// */
		//
		// final Place place = new Point(1, 1);
		//
		// final Set<FilteringFactor> filteringFactors = new
		// HashSet<FilteringFactor>();
		//
		// final Set<FilteringFactorValue> filteringFactorValueEventSource = new
		// HashSet<FilteringFactorValue>();
		// filteringFactorValueEventSource.add(new FilteringFactorValue(
		// EventSource.SENSOR.toString()));
		// filteringFactorValueEventSource.add(new FilteringFactorValue(
		// EventSource.SOCIAL_NETWORK.toString()));
		// FilteringFactor filteringFactor = new FilteringFactor(
		// FilteringFactorName.EVENT_SOURCE,
		// filteringFactorValueEventSource);
		// filteringFactors.add(filteringFactor);
		//
		// final Set<FilteringFactorValue> filteringFactorValueCategory = new
		// HashSet<FilteringFactorValue>();
		// filteringFactorValueCategory
		// .add(new FilteringFactorValue(
		// "TrafficJam"));
		// filteringFactorValueCategory.add(new FilteringFactorValue(
		// "PublicParking"));
		// filteringFactor = new FilteringFactor(
		// FilteringFactorName.EVENT_CATEGORY,
		// filteringFactorValueCategory);
		// filteringFactors.add(filteringFactor);
		//
		// final Set<FilteringFactorValue> filteringFactorValueActivity = new
		// HashSet<FilteringFactorValue>();
		// filteringFactorValueActivity
		// .add(new FilteringFactorValue("CarCommute"));
		// filteringFactorValueActivity
		// .add(new FilteringFactorValue("BikeCommute"));
		// filteringFactor = new FilteringFactor(FilteringFactorName.ACTIVITY,
		// filteringFactorValueActivity);
		// filteringFactors.add(filteringFactor);
		//
		//
		// final Set<RankingElement> rankingElements = new
		// HashSet<RankingElement>();
		// rankingElements.add(new RankingElement(RankingElementName.DISTANCE,
		// new RankingElementValue(50)));
		// rankingElements.add(new
		// RankingElement(RankingElementName.EVENT_LEVEL,
		// new RankingElementValue(50)));
		//
		// final RankingFactor rankingFactor = new RankingFactor(
		// RankingFactorType.LINEAR, rankingElements);
		//
		// final ContextualEventRequest request = new
		// ContextualEventRequest(place, filteringFactors, rankingFactor);
		//
		// System.out.println("request = " + request.toString());
		//
		// final String requestStr = gson.toJson(request);
		// System.out.println("requestStr = " + requestStr);
		//
		// final ContextualEventRequest requestGson = gson.fromJson(
		// requestStr, ContextualEventRequest.class);
		// System.out.println("requestGson = " + requestGson.toString());
		//
		// /*
		// * Test ContextualEventRequestRewriter
		// */
		// final List<String> inputProgram = ContextualEventRequestRewriter
		// .getInstance().getRules(requestGson);


		/*
		 * Test UserContext
		 */
		// final UserContext userContext = new UserContext();
		// userContext.setActivity(activity);
		// System.out.println("userContext = " + userContext.toString());
		// /*
		// * Test ContextualEventFilter - consumer
		// */
		// final ContextualEventFilter ceManager = new ContextualEventFilter();
		// ceManager.setCeRequest(requestGson);
		// ceManager.setUserContext(userContext);
		// ceManager.setInputProgram(inputProgram);
		// final List<CityEvent> cityEvents = new ArrayList<CityEvent>();
		// cityEvents.add(eventGson);
		// cityEvents.add(event1);
		// cityEvents.add(event2);
		// ceManager.performReasoning(cityEvents);
		// ceManager.startCEF(requestGson);

		/*
		 * test scaling
		 */
//		System.out.println(scaling(-35, new long[] { 0, 100 },
//				new int[] { 1, 5 }));
//		System.out.println(scaling(-65, new long[] { 0, 100 },
//				new int[] { 1, 5 }));
//
//		System.out.println(scaling(100 - 35, new long[] { 0, 100 }, new int[] {
//				1, 5 }));
//		System.out.println(scaling(100 - 65, new long[] { 0, 100 }, new int[] {
//				1, 5 }));


		final Map<Coordinate, Integer> map = new HashMap<Coordinate, Integer>();
		map.put(new Coordinate(1, 1), 1);

		if (map.keySet().contains(new Coordinate(1, 1))) {
			System.out.println("1");
		} else {
			System.out.println("0");
		}

	}

	/**
	 *
	 * @param x
	 * @param from
	 * @param to
	 * @return
	 */
	public static int scaling(long x, long[] from, int[] to) {
		return (int) Math
				.round((to[0] + ((double) (x - from[0]) * (to[1] - to[0]))
						/ (from[1] - from[0])));
	}

}
