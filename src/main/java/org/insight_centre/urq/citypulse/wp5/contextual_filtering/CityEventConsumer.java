/**
 *
 */
package org.insight_centre.urq.citypulse.wp5.contextual_filtering;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.insight_centre.urq.citypulse.wp5.Configuration;

import citypulse.commons.contextual_filtering.city_event_ontology.CityEvent;
import citypulse.commons.contextual_filtering.city_event_ontology.EventCategory;
import citypulse.commons.contextual_filtering.city_event_ontology.EventSource;
import citypulse.commons.contextual_filtering.city_event_ontology.EventType;
import citypulse.commons.contextual_filtering.contextual_event_request.Place;
import citypulse.commons.contextual_filtering.contextual_event_request.PlaceAdapter;
import citypulse.commons.contextual_filtering.contextual_event_request.Point;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * This class implements the RabbitMq consumer in order to consume city events
 * detected by the Event Detection component
 *
 * @author Thu-Le Pham
 * @organization INSIGHT, NUIG, Galway, Ireland
 * @email thule.pham@insight-centre.org
 */
public class CityEventConsumer implements Consumer {

	public static Logger logger = Logger.getLogger(CityEventConsumer.class
			.getPackage().getName());

	/**
	 *
	 */
	Connection connection;

	/**
	 *
	 */
	Channel channel;

	/**
	 *
	 */
	List<String[]> exchangeTopics;

	/**
	 *
	 */
	ContextualEventFilter ceFilter;

	/**
	 *
	 */
	String consumerTag;

	/**
	 * This method starts a consumer
	 *
	 * @param exchangeTopics
	 *            - a list of exchange topics which used to subscribe events
	 * @param ceFilter
	 *            - an instance of ContextualEventFilter
	 */
	public CityEventConsumer(List<String[]> exchangeTopics,
			ContextualEventFilter ceFilter) {
		super();
		this.exchangeTopics = exchangeTopics;
		this.ceFilter = ceFilter;

		// Create a connection factoryßß
		final ConnectionFactory factory = new ConnectionFactory();

		// Replace with the correct connection uri
		// final String uri = Configuration.getInstance().getEventRabbitURI();

		// final String uri = "amqp://guest:guest@127.0.0.1:5672";
		final String uri = Configuration.getInstance().getEventRabbitURI();
		try {
			factory.setUri(uri);
			// factory.setHost("localhost");
			// getting a connection
			connection = factory.newConnection();

			// creating a channel
			channel = connection.createChannel();
			logger.info("Consumer opened a connection");

			final String queueName = channel.queueDeclare().getQueue();
			// channel.queueBind(queueName, "events",
			// "31dd6e16-c272-564f-a744-1da1f0011ca6");
			// channel.basicConsume(queueName, true, this);

			for (int i = 0; i < this.exchangeTopics.size(); i++) {
				final String[] temp = this.exchangeTopics.get(i);
				final String exchange = temp[0];
				final String topic = temp[1];
				logger.info("exchange topics = "
						+ this.exchangeTopics.get(i)[0] + "--"
						+ this.exchangeTopics.get(i)[1]);
				channel.queueBind(queueName, exchange, topic);

			}
			this.consumerTag = channel.basicConsume(queueName, true, this);
			logger.info("Start consumming city events");
		} catch (final Exception e) {
			logger.severe("Error connecting to MQ Server." + e.toString());
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.rabbitmq.client.Consumer#handleConsumeOk(java.lang.String)
	 */
	@Override
	public void handleConsumeOk(String consumerTag) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.rabbitmq.client.Consumer#handleCancelOk(java.lang.String)
	 */
	@Override
	public void handleCancelOk(String consumerTag) {
		try {
			this.connection.close();
			logger.info("Consumer closed");
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.rabbitmq.client.Consumer#handleCancel(java.lang.String)
	 */
	@Override
	public void handleCancel(String consumerTag) throws IOException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.rabbitmq.client.Consumer#handleDelivery(java.lang.String,
	 * com.rabbitmq.client.Envelope, com.rabbitmq.client.AMQP.BasicProperties,
	 * byte[])
	 */
	@Override
	public void handleDelivery(String arg0, Envelope arg1,
			BasicProperties arg2, byte[] arg3) throws IOException {
		final String msgBody = new String(arg3);
		logger.info("Received message  = " + msgBody + "\n");

		/*
		 * parse from RDF to JavaObject
		 */
		final CityEvent cityEvent = parseRDFtoJava(msgBody);

		logger.info(cityEvent.toString());
		// final CityEvent cityEvent = parse(msgBody);
		// TODO:consume events,will implement window here
		final List<CityEvent> cityEvents = new ArrayList<CityEvent>();
		if (cityEvent.getEventLevel() > 0) {
			cityEvents.add(cityEvent);
		}
		if (!cityEvents.isEmpty()) {
			ceFilter.performReasoning(cityEvents);
		}
	}

	/**
	 * @param msgBody
	 * @return
	 */
	private CityEvent parseRDFtoJava(String msgBody) {
		final Model newmodel = ModelFactory.createDefaultModel();
		newmodel.read(new ByteArrayInputStream(msgBody.getBytes()), null, "N3");
		// newmodel.write(System.out, "RDF/XML");

		// Create a new query

		final String queryStr = "PREFIX  ec:<http://purl.oclc.org/NET/UNIS/sao/ec#>\n"
				+ "PREFIX  sao: <http://purl.oclc.org/NET/UNIS/sao/sao#>\n"
				+ "PREFIX  xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX  geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n"
				+ "PREFIX  tl: <http://purl.org/NET/c4dm/timeline.owl#>\n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "SELECT ?eventId ?name ?type ?time ?level ?source ?lon ?lat\n"
				+ "WHERE {"
				+ "      ?eventId rdf:type ?name.\n"
				+ "      ?eventId sao:hasType ?type.\n"
				+ "      ?eventId tl:time ?time.\n"
				+ "      ?eventId sao:hasLevel ?level.\n"
				+ "      ?eventId ec:hasSource ?source.\n"
				+ "      ?eventId sao:hasLocation ?loc.\n"
				+ "      ?loc geo:lat ?lat.\n"
				+ "      ?loc geo:lon ?lon.\n"
				+ "      }\n";
		// System.out.println(queryStr);
		final Query query = QueryFactory.create(queryStr);

		// Execute the query and obtain results
		final QueryExecution qe = QueryExecutionFactory.create(query, newmodel);
		final com.hp.hpl.jena.query.ResultSet results = qe.execSelect();

		// ResultSetFormatter.out(System.out, results, query);
		// Formating the result

		final CityEvent event = new CityEvent();
		for (; results.hasNext();) {

			final QuerySolution r = (QuerySolution) results.next();

			final String eventId = r.get("eventId").toString();
			final String eventName = r.get("name").toString();
			final String eventType = r.get("type").toString();
			// final String eventSource = r.get("source").toString();

			RDFNode l = r.get("level");
			Literal ll = (Literal) l.as(Literal.class);
			final long eventLevel = ll.getLong();

			l = r.get("time");
			ll = (Literal) l.as(Literal.class);
			// System.out.println(ll.getValue().toString());
			final String time = ll.getValue().toString();
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss");
			long eventTime = 0;
			try {
				final Date date = sdf.parse(time);

				 eventTime= date.getTime();
			} catch (final ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			l = r.get("lat");
			ll = (Literal) l.as(Literal.class);
			final double lat = ll.getDouble();

			l = r.get("lon");
			ll = (Literal) l.as(Literal.class);
			final double lon = ll.getDouble();

			l = r.get("source");
			ll = (Literal) l.as(Literal.class);
			final String eventSource = ll.getString().toUpperCase();

			// final Coordinate eventCoor = new Coordinate(lon, lat);

			System.out.println("Here eventId = " + eventId);
			System.out.println("Here eventName = " + eventName);
			System.out.println("Here eventType = " + eventType);
			System.out.println("Here eventLevel = " + eventLevel);
			System.out.println("Here eventSource = " + eventSource);
			System.out.println("Here eventCoor = " + lat + "--" + lon);

			event.setEventId(eventId);
			event.setEventCategory(new EventCategory(eventName));
			event.setEventType(new EventType(eventType));
			event.setEventLevel((int) eventLevel);
			event.setEventTime(eventTime);
			event.setEventPlace(new Point(lon, lat));
			if (eventSource.contains("SENSOR")) {
				event.setEventSource(EventSource.SENSOR);
			} else {
				event.setEventSource(EventSource.SOCIAL_NETWORK);
			}


		}

		// Important - free up resources used running the query
		qe.close();

		return event;
	}

	/**
	 *
	 * @param message
	 * @return
	 */
	public CityEvent parse(String message) {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Place.class, new PlaceAdapter());
		final Gson gson = builder.create();
		return gson.fromJson(message, CityEvent.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.rabbitmq.client.Consumer#handleShutdownSignal(java.lang.String,
	 * com.rabbitmq.client.ShutdownSignalException)
	 */
	@Override
	public void handleShutdownSignal(String consumerTag,
			ShutdownSignalException sig) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.rabbitmq.client.Consumer#handleRecoverOk(java.lang.String)
	 */
	@Override
	public void handleRecoverOk(String consumerTag) {
		// TODO Auto-generated method stub

	}

}
