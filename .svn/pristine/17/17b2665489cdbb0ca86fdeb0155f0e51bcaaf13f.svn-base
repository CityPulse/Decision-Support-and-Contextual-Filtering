/**
 *
 */
package org.insight_centre.urq.citypulse.wp5.contextual_filter.main;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.insight_centre.urq.citypulse.wp5.contextual_filter.mockup.ClientEndPoint;

import citypulse.commons.contextual_events_request.ContextualEvent;
import citypulse.commons.data.Coordinate;
import eu.citypulse.uaso.gdiclient.CpGdiInterface;
import eu.citypulse.uaso.gdiclient.persistdata.CpGdiEventStream;
import eu.citypulse.uaso.gdiclient.persistdata.CpGdiPersistable;
import eu.citypulse.uaso.gdiclient.routes.CpRouteRequest;

/**
 * @author Thu-Le Pham
 * @date 26 Oct 2015
 */
public class TestConnection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// connectToGDI();
		//
		// final List<String[]> eventTopicsGeoDB = new ArrayList<String[]>();
		// eventTopicsGeoDB.add(new String[] { "events",
		// "7bf9af8f-68cf-5acc-b20c-4c95741b945e" });
		// eventTopicsGeoDB.add(new String[] { "events",
		// "31dd6e16-c272-564f-a744-1da1f0011ca6" });
		// eventTopicsGeoDB.add(new String[] { "events",
		// "47e18070-bac6-542a-a0ec-c91c90c2424a" });
		//
		// final CityEventConsumer consumer = new CityEventConsumer(
		// eventTopicsGeoDB, null);

		// final String msgBody = new String(body);
		// logger.info("Received message  = " + msgBody + "\n");
		// final ContextualEvent event = this.parse(msgBody);
		final ContextualEvent event = new ContextualEvent();
		event.setCeID("1");
		event.setCeCoordinate(new Coordinate(10.1623157, 56.1255986));
		event.setCeLevel(3);
		final StringBuilder request = new StringBuilder();
		request.append("updateCostCircular(")
				.append(event.getCeCoordinate().getLongitude()).append(",")
				.append(event.getCeCoordinate().getLatitude()).append(",")
				.append("time").append(",").append(500).append(",")
				.append(event.getCeLevel() + 20).append(")");
		final String SENT_MESSAGE = request.toString();
		System.out.println("SENT_MESSAGE = " + SENT_MESSAGE);
		// try {
		// // final String SENT_MESSAGE = request.toString();
		// // System.out.println("SENT_MESSAGE = " + SENT_MESSAGE);
		// // messageLatch = new CountDownLatch(1);
		//
		// final ClientEndpointConfig cec = ClientEndpointConfig.Builder
		// .create().build();
		//
		// final ClientManager client = ClientManager.createClient();
		// client.connectToServer(new Endpoint() {
		//
		// @Override
		// public void onOpen(final Session session,
		// final EndpointConfig config) {
		// try {
		// session.addMessageHandler(new MessageHandler.Whole<String>() {
		// @Override
		// public void onMessage(final String message) {
		// System.out.println("Received message: "
		// + message);
		//
		// // messageLatch.countDown();
		// }
		// });
		// session.getBasicRemote().sendText(SENT_MESSAGE);
		// } catch (final IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }, cec, new URI("ws://" + "127.0.0.1" + ":7686"));
		// // }, cec, new URI("ws://" + "131.227.92.55" + ":7686"));
		// // }, cec, new URI("ws://pcsd-118.et.hs-osnabrueck.de:7686"));
		// // messageLatch.await(100, TimeUnit.SECONDS);
		// } catch (final Exception e) {
		// e.printStackTrace();
		// }

		try {
			// open websocket
			final URI uri = new URI("ws://" + "127.0.0.1" + ":7686");
			final ClientEndPoint clientEndPoint = new ClientEndPoint(uri);
			// add listener
			clientEndPoint
					.addMessageHandler(new ClientEndPoint.MessageHandler() {
						@Override
						public void handleMessage(String message) {
							ClientEndPoint.logger
									.info("Aplication received message: "
											+ message);
						}
					});

			// clientEndPoint.sendMessage(parse(this.ceRequest));
			clientEndPoint.sendMessage(SENT_MESSAGE);

			clientEndPoint.getLatch().await();

		} catch (final URISyntaxException ex) {
			System.err.println("URISyntaxException exception: "
					+ ex.getMessage());
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public static void connectToGDI() {

		/*
		 * Connect to GeoDB to get event streams and sensor stream
		 */
		final CpGdiInterface cgi;
		try {
			cgi = new CpGdiInterface("127.0.0.1", 5432);
			// cgi = new CpGdiInterface("localhost", 5438);

			/*
			 * place of interest
			 */
			final CpRouteRequest cprr2 = cgi.getCityRoutes(10.1, 56.1, 10.2,
					56.2, CpRouteRequest.ROUTE_COST_METRIC_DISTANCE, 3);

			/*
			 * Get event stream to connect with MessageBus
			 */
			CpGdiPersistable[] eventStreams;
			eventStreams = cgi.getEventStreamsForRoute(cprr2.getRoute(1), 500);
			System.out.println("Event_Streams on Route:");
			for (final CpGdiPersistable eventStream : eventStreams) {
				System.out.println(eventStream);
			}

			/*
			 * Get sensor stream that related to place of interest
			 */

			eventStreams = cgi.getSensorsForRoute(cprr2.getRoute(1), 500);
			System.out.println("Sensor_Streams on Route:");
			for (final CpGdiPersistable eventStream : eventStreams) {
				System.out.println(eventStream);
			}

			final CpGdiEventStream cpgdies[] = cgi.getAllEventStreams();
			System.out.println("All on Route:");
			for (final CpGdiEventStream cpgdie : cpgdies) {
				System.out.println(cpgdie);
			}

			System.out
			.println("Events on Route in the last 5 minutes (300 seconds):");
			final CpGdiPersistable[] events = cgi.getEventsForRoute(
					cprr2.getRoute(1),
			new Timestamp(System.currentTimeMillis()), 300, 500.0);
			System.out.println(events.length);
			if (events.length > 0) {
				System.out.println(events[0]);
			}


		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
