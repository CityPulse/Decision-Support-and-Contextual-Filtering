/**
 *
 */
package org.insight_centre.urq.citypulse.wp5.decision_support_system.answer_atoms.travel_planner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.insight_centre.urq.citypulse.wp5.Configuration;

import citypulse.commons.contextual_filtering.city_event_ontology.CityEvent;

/**
 * @author Thu-Le Pham
 * @date 27 Oct 2015 This class subcribes events from EventDetection and send
 *       request to update the graph of city
 */
public class GraphUpdater {


	private static ClientEndPoint openConnectionForUpdating() {
		ClientEndPoint clientEndPoint = null;
		try {
			final URI uri = new URI(Configuration.getInstance().getRoutingURI());
			clientEndPoint = new ClientEndPoint(uri);
			// add listener
			clientEndPoint
					.addMessageHandler(new ClientEndPoint.MessageHandler() {
						@Override
						public void handleMessage(String message) {
							ClientEndPoint.logger.info("Updated Graph: "
									+ message);
							ClientEndPoint.getLatch().countDown();
						}
					});
			clientEndPoint.getLatch().await();

		} catch (final URISyntaxException ex) {
			System.err.println("URISyntaxException exception: "
					+ ex.getMessage());
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientEndPoint;
	}
	
	
	public void updateGraph(CityEvent event){
		final StringBuilder request = new StringBuilder();
		request.append("updateCostCircular(")
				.append(event.getEventPlace().getCentreCoordinate()
						.getLongitude()).append(",")
				.append(event.getEventPlace().getCentreCoordinate()
						.getLatitude()).append(",")
				// .append("time")
				.append("time").append(",").append(200)
				.append(",").append(event.getEventLevel() * 5).append(")");
		final String SENT_MESSAGE = request.toString();

		final ClientEndPoint clientEndPoint = this.openConnectionForUpdating();
		clientEndPoint.sendMessage(SENT_MESSAGE);
		try {
			clientEndPoint.userSession.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void resetGraph() {
		System.out.println("Reset Graph!!!");
		final ClientEndPoint clientEndPoint = openConnectionForUpdating();
		final String message = "resetAllMultiplicators(time)";
		clientEndPoint.sendMessage(message);
		try {
			clientEndPoint.userSession.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
