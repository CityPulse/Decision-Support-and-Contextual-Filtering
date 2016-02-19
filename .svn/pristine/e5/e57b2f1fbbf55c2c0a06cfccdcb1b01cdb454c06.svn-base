package org.insight_centre.urq.citypulse.wp5.contextual_filter.main;
import java.sql.SQLException;

import eu.citypulse.uaso.gdiclient.CpGdiInterface;
import eu.citypulse.uaso.gdiclient.persistdata.CpGdiEventStream;
import eu.citypulse.uaso.gdiclient.persistdata.CpGdiPersistable;
import eu.citypulse.uaso.gdiclient.routes.CpRouteRequest;

/**
 * Test and Example calass for Geospatial Data Infrastructure(GDI) client of CityPulse
 * @author Daniel Kuemper, University of Applied Sciences Osnabrueck
 * @version 0.1 draft
 *
 */
public class CpClientExample {

	/**
	 * Simple Class that shows functionality of the Requests
	 * @param args are not used
	 */
	public static void main(String[] args) {

		CpGdiInterface cgi;
		try {
			cgi = new CpGdiInterface("localhost", 5432);
			// cgi = new CpGdiInterface("131.227.92.55 ", 5432);
//			cgi.simpleQuery();
//
//				System.out.println("Inserted 1? "+cgi.registerStream(UUID.randomUUID(), "900913", "testCategory", 10.0, 56.0));
//				System.out.println("Inserted 2? "+cgi.registerStream(UUID.randomUUID(), "900913", "testCategory", 10.0, 56.0, 4326));
//				System.out.println("Inserted 3? "+cgi.registerStream(UUID.randomUUID(), "900913", "testCategory", "POINT(10.0 56.0)", 4326));
//				System.out.println("Inserted 4? "+cgi.registerStream(UUID.randomUUID(), "900913", "testCategory", "POINT(10.0 56.0)", CpGdiInterface.EPSG_WGS84));
//				//Romania
//				System.out.println("Inserted 5? "+cgi.registerStream(UUID.randomUUID(), "900913", "testCategory", 25.55691529433881, 45.587838521405224 , CpGdiInterface.EPSG_WGS84));
//				System.out.println("Inserted 6? "+cgi.registerStream(UUID.randomUUID(), "900913", "testCategory", "POINT(543596.363572 454379.287188)", CpGdiInterface.EPSG_STEREO70));
//				UUID sensorUUID = UUID.randomUUID();
//				System.out.println("Inserted 7? "+cgi.registerStream(sensorUUID, "900913", "testCategory",  543596.363572, 454379.287188, CpGdiInterface.EPSG_STEREO70));
//				System.out.println("Deleted 7? "+cgi.removeStream(sensorUUID));
//
//
//				System.out.println("Inserted ES1? "+cgi.registerEventStream(UUID.randomUUID(), "aaa/bbb/ccc",  10.0, 56.0));
//				System.out.println("Inserted ES2? "+cgi.registerEventStream(UUID.randomUUID(), "aaa/bbb/ccc",10.0, 56.0, 4326));
//				System.out.println("Inserted ES3? "+cgi.registerEventStream(UUID.randomUUID(), "aaa/bbb/ccc", "POINT(10.0 56.0)", 4326));
//				System.out.println("Inserted ES4? "+cgi.registerEventStream(UUID.randomUUID(), "aaa/bbb/ccc", "POINT(10.0 56.0)", CpGdiInterface.EPSG_WGS84));
//				//Romania
//				System.out.println("Inserted ES5? "+cgi.registerEventStream(UUID.randomUUID(), "aaa/bbb/ccc", 25.55691529433881, 45.587838521405224 , CpGdiInterface.EPSG_WGS84));
//				System.out.println("Inserted ES6? "+cgi.registerEventStream(UUID.randomUUID(), "aaa/bbb/ccc", "POINT(543596.363572 454379.287188)", CpGdiInterface.EPSG_STEREO70));
//				UUID eventUUID = UUID.randomUUID();
//				System.out.println("Inserted ES7? "+cgi.registerEventStream(eventUUID, "aaa/bbb/ccc", 543596.363572, 454379.287188, CpGdiInterface.EPSG_STEREO70));
//				System.out.println("Deleted 7S? "+cgi.removeEventStream(eventUUID));
//				System.out.println("Deleted 7S? "+cgi.removeEventStream(eventUUID)); //false since it's not available anymore
//
//
//			UUID eventUUID1 = UUID.randomUUID();
//			System.out.println("Inserted Event 1? "+cgi.registerEvent(eventUUID1, "aaa/bbb/ccc", new Timestamp(System.currentTimeMillis()),"POINT(10.0 56.0)", CpGdiInterface.EPSG_WGS84));
//			//Romania
//			System.out.println("Inserted E2? "+cgi.registerEvent(UUID.randomUUID(), "aaa/bbb/ccc",  new Timestamp(System.currentTimeMillis()), 25.55691529433881, 45.587838521405224 , CpGdiInterface.EPSG_WGS84));
//			System.out.println("Inserted E3? "+cgi.registerEvent(UUID.randomUUID(), "aaa/bbb/ccc",  new Timestamp(System.currentTimeMillis()-3600000),"POINT(543596.363572 454379.287188)", CpGdiInterface.EPSG_STEREO70));
//			UUID eventUUID2 = UUID.randomUUID();
//			System.out.println("Inserted E4? "+cgi.registerEvent(eventUUID2, "aaa/bbb/ccc",new Timestamp(System.currentTimeMillis()-3600000), 543596.363572, 454379.287188, CpGdiInterface.EPSG_STEREO70));
//			System.out.println("Deleted E4? "+cgi.deregisterEvent(eventUUID2));
//			System.out.println("Deleted E4? "+cgi.deregisterEvent(eventUUID2)); //false since it's not available anymore
//			System.out.println("Close Event 1? "+cgi.closeEvent(eventUUID1,  new Timestamp(System.currentTimeMillis())));
//
//
//
//
			final CpRouteRequest cprr2 = cgi.getCityRoutes(10.1, 56.1, 10.2,
					56.2, CpRouteRequest.ROUTE_COST_METRIC_DISTANCE, 3);
			System.out.println("cprr2:\n" + cprr2);
//
//			System.out.println(cgi.updateCostMultiplicatorRadial(10.1, 56.1, 300.0, CpRouteRequest.ROUTE_COST_METRIC_POLLUTION, 5.0));
//			System.out.println(cgi.updateCostMultiplicatorRadial(10.2, 56.1, 333.3, CpRouteRequest.ROUTE_COST_METRIC_DISTANCE, 5.0));
//			System.out.println(cgi.updateCostMultiplicatorRadial(10.2, 56.2, 350.0, CpRouteRequest.ROUTE_COST_METRIC_TIME, 5.0));
//			System.out.println(cgi.updateCostMultiplicatorArea(9.9, 56.17,9.95, 56.07, CpRouteRequest.ROUTE_COST_METRIC_POLLUTION, 5.0));
//			try {
//				System.out.println(cgi.updateCostMultiplicatorArea(9.9, 56.07,9.95, 56.17, CpRouteRequest.ROUTE_COST_METRIC_COMBINED, 5.0));
//			} catch (Exception e) {
//				System.err.println("Unknown metric!" +e.getLocalizedMessage());
//			}
//
//			//reset one cost multiplicator layer
//			System.out.println(cgi.resetCostMultiplicators(CpRouteRequest.ROUTE_COST_METRIC_POLLUTION));
//
//			//reet all cost multiplicator layers
//			System.out.println(cgi.resetCostMultiplicators(CpRouteRequest.ROUTE_COST_METRIC_POLLUTION));
//
//			//let's place sth. near route 2:
//			System.out.println("Inserted current Event  on route? "+cgi.registerEvent(UUID.randomUUID(), "aaa/bbb/ccc",  new Timestamp(System.currentTimeMillis()), 10.0980917,  56.1054429));
//			System.out.println("Inserted old Event (500 Seconds old) on route? "+cgi.registerEvent(UUID.randomUUID(), "aaa/bbb/ccc",  new Timestamp(System.currentTimeMillis()-500000), 10.0980917,  56.1054429));
//			System.out.println("Inserted EventStream on route? "+cgi.registerEventStream(UUID.randomUUID(), "aaa/bbb/ccc", 10.0980917, 56.1054429));
//			System.out.println("Inserted sensorStream on route? "+cgi.registerStream(UUID.randomUUID(), "900913", "testCategory", 10.0980917, 56.1054429));
//
//
//
			CpGdiPersistable[] events;
			System.out.println("Event_Streamsnon Route:");
			events = cgi.getEventStreamsForRoute(cprr2.getRoute(1), 500);
			System.out.println(events.length);
			if (events.length > 0)
			 {
				System.out.println(events[0]);

				System.out.println("Sensor_Streams on Route:");
				events = cgi.getSensorsForRoute(cprr2.getRoute(1), 500);
				System.out.println(events.length);
				if (events.length > 0) {
					System.out.println(events[0]);
				}

//			System.out.println("Events on Route in the last 5 minutes (300 seconds):");
//			events = cgi.getEventsForRoute(cprr2.getRoute(1), new Timestamp(System.currentTimeMillis()),300, 500.0);
//			System.out.println(events.length);
//			if (events.length>0) System.out.println(events[0]);
			}

			final CpGdiEventStream cpgdies[] = cgi.getAllEventStreams();
			for (final CpGdiEventStream cpgdie : cpgdies) {
				System.out.println(cpgdie);

			}
			//cgi.removeAllEventStream();

			//additional stuff
			// long nodeFrom = cgi.getNearestNodeID(10.1,56.1);
			//		long nodeTo = cgi.getNearestNodeID(10.2,56.2);



			cgi.closeConnection();
		} catch (final ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		}

}
