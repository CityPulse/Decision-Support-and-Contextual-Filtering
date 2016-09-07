# Decision Support && ContextualFiltering
The Contextual Filtering component and the Decision Support component are implemented in the same jar pakage as two different web-socket endpoints of the same server (we call it User-Centric Decsion Support  server. Therefore, the Decision Support component is automatically deployed when the Contextual Filtering component is deployed and vice versa. 

#############################################
# Decision Support
The main role of the Decision Support component is to enable reactive decision support functionalities to be easily deployed, providing the most suitable answers at the right time by utilizing contextual information available as background knowledge, user patterns and preferences from the application as well as real-time events. The reasoning capabilities needed to support users in making better decisions require handling incomplete, diverse input, as well as constraints and preferences in the deduction process. This expressivity in the Decision Support component is achieved by using a declarative non-monotonic logic reasoning approach based on Answer Set Programming. The Decision Support component produces a set of answers to the reasoning request that satisfy all user’s requirements and preferences in the best possible way. These solutions are computed by applying sets of rules deployed as scenario-driven decision support modules. We currently support three different types of decision support modules, covering a broad range of application scenarios:

- Scenario 1 (Routing APIs): It provides the best solution(s) for a routing task. The users not only identify starting point and ending point but are also able to provide various selection criteria (including constraints and preferences) in order to select the optimal routes.
- Scenario 2 (Parking space APIs): It provides the best selection among a set of available parking places based on optimisation criteria, constraints and preferences of the users.
 
## Component prerequisite
The following applications have to be installed before using the component:
- Clingo4 (available at: http://potassco.sourceforge.net)
- Python 2.7 and package interruptingcow (Installation: $ pip install interruptingcow)

## CityPulse framework dependencies
The Decision Support component in order to run properly needs to have access to the following CityPulse components:
- Routing component
- Triple store
- GDI
- Data Federation

## Start Decision Support component
The Decision Support component is implemented as a websocket server endpoint (`/reasoning_request`) at port `8005`. It requies a Reasoning Request in JSON format as an input. 
The following steps have to be achieved in order to deploy the component:
- Step 1: download the following resources provided at https://github.com/CityPulse/DecisionSupport-ContextualFiltering and place them in same folder: 
  + jar package in folder /target with title CityPulseWP5-jar-with-dependencies.jar	
  + folder res
- Step 2: edit the configuration file config.properties in folder res which includes the following fields: 
  + hostname: the IP address of the server hosting the User-Centric Decsion Support server.
  + port: the main port used by the User-Centric Decsion Support server
  + routing_uri: the uri to access the Routing component (for Routing APIs only)
  + data_federation_uri: the uri to access the Data Federation component
  + knowledge_base_uri: the uri to access the triple store
  + GDI_URI: the URI of GDI component
  + clingo: the path of Clingo
- Step 3: run CityPulseWP5-jar-with-dependencies.jar to start the server. 

## Reasoning Request
A Reasoning Request consists of: 
-	User Reference: uniquely identifies the user that made the request. Such reference is related to user credentials that will be used in the final integration activities in order to manage user logins and instances of the CityPulse framework in different cities.
-	Type: indicates the reasoning task required by the application. This is used directly by the Decision support component to select which set of rules to apply, and needs to be identified among a set of available options at design time by the application developer.
-	Functional Details: represent the qualitative criteria used in the reasoning task to produce a solution that best fits the user needs. The Functional Details are composed by: 
    * Functional Parameters, defining mandatory information for the Reasoning Request (such as start and end location in a travel planner scenario);
    * Functional Constraints, defining a numerical threshold for specific functional aspects of the Reasoning Request (such as cost of a trip, distance or travel time in a travel planner scenario). These restrictions are evaluated as hard constraints, which needs to be fulfilled by each of the alternative solutions offered to the user;
    * Functional Preferences, which encode two types of soft constraints: a qualitative optimisation statement defined on the same functional aspects used in the Functional Constraint (such as minimisation of the travel time); or a qualitative partial order over such optimization statements (such as preference on the minimisation of the distance over minimization of the travel time). Preferences are used by the Decision support component to provide to the user the optimal solution among those verifying the functional constraints.

## Reasoning Request for Routing module
 - Type: `TRAVEL-PLANNER`
 - Funtional parameters:
    * `STARTING_POINT`: the coordinate of starting point
    * `ENDING_POINT`: the coordinate of ending point
    * `STARTING_DATETIME`: the desired date time to start traveling
    * `TRANSPORTATION_TYPE`: the vehicle that the user uses to  travel. This parameter includes `CAR, WALK, BICYCLE`
 - Functional constraints: 
    * `TRAVEL_TIME		LESS_THAN		X`: indicates that the travel time should be less than X 
    * `DISTANCE		LESS_THAN X`: indicates that the distance should be less than X
    * `POLLUTION		LESS_THAN X`: indicates that the pollution amount should be less than X
 - Functional preferences: 
    * `MINIMIZE		TRAVEL_TIME`: indicates that the calculated route should prefer to minimize the time of travel
    * `MINIMIZE		DISTANCE`: indicates that the calculated route should prefer to minimize the distance of travel
    * `MINIMIZE		POLLUTION`: indicates that the calculated route should prefer to minimize the pollution amount on the route

###### Sample Reasoning Request for Routing module
```python
{
   "arType":"TRAVEL_PLANNER",
   "functionalDetails":{
      "functionalConstraints":{
         "functionalConstraints":[
            {
               "name":"POLLUTION",
               "operator":"LESS_THAN",
               "value":"135"
            }
         ]
      },
      "functionalParameters":{
         "functionalParameters":[
            {
               "name":"STARTING_DATETIME",
               "value":"1434110314540"
            },
            {
               "name":"STARTING_POINT",
               "value":"10.103644989430904 56.232567308059835"
            },
            {
               "name":"ENDING_POINT",
               "value":"10.203921 56.162939"
            },
            {
               "name":"TRANSPORTATION_TYPE",
               "value":"car"
            }
         ]
      },
      "functionalPreferences":{
         "functionalPreferences":[
            {
               "operation":"MINIMIZE",
               "value":"TIME",
               "order":1
            }
         ]
      }
   },
   "user":{

   }
}
```

## Reasoning Request for Parking Space module
 - Type: `PARKING_SPACES`
 - Funtional parameters:
    * `STARTING_POINT`: the coordinate of starting point
    * `POINT_OF_INTEREST`: the coordinate of point of interest
    * `STARTING_DATETIME`: the desired date time to start traveling
    * `DISTANCE_RANGE`: the desired distance from the `POINT_OF_INTEREST` to any available parking slot.
    * `TIME_OF_STAY`: the time for parking the car
 - Functional constraints: 
    * `COST		LESS_THAN		X`: indicates that the parking cost should be less than X 
    * `WALK		LESS_THAN X`: indicates that the distance to walk from the parking slot to the `POINT_OF_INTEREST`should be less than X
 - Functional preferences: 
    * `MINIMIZE		COST`: indicates that the calculated parking slot should prefer to minimize the cost
    * `MINIMIZE		WALK`: indicates that the calculated parking slot should prefer to minimize the distance to the `POINT_OF_INTEREST`

###### Sample Reasoning Request for Parking Space module  
```python
{
		"arType":"PARKING_SPACES",
		"functionalDetails":{
		  "functionalConstraints":{
		    "functionalConstraints":[
		      {
		        "name":"COST",
		        "operator":"LESS_THAN",
		        "value":"100"
		      }
		    ]
	  	},
		  "functionalParameters":{
		    "functionalParameters":[
		      {
		        "name":"STARTING_DATETIME",
		        "value":"1455798332761"
		      },
		      {
	        	"name":"POINT_OF_INTEREST",
		        "value":"10.1827464 56.16169679999999"
		      },
		      {
		        "name":"DISTANCE_RANGE",
		        "value":"1000"
		      },
		      {
		        "name":"TIME_OF_STAY",
		        "value":"100"
		      },
		      {
		        "name":"STARTING_POINT",
		        "value":"10.103644989430904 56.232567308059835"
		      }
		    ]
		  },
		  "functionalPreferences":{
		    "functionalPreferences":[
		
		    ]
		  }
		},
		"user":{
		
		}
}
```
#############################################

# Contextual Filtering
The main role of the Contextual Filtering component is to continuously identify and filter events that might affect the optimal results of the decision making task (performed by the Decision Support component). The users need to input their current context such as place of interest, filtering factors, and ranking factors. The Contextual Filtering will subscribe to events only in the place of interest and determine which event is critical to users based on filtering and ranking factors.

Known limitations: 
- User Context Ontology: the current ontology about user’s context includes only the activity of the user. The developer can extend this ontology for their specific scenarios.

## Component prerequisite
The following applications have to be installed before using the component: 
- Clingo4 (available at: http://potassco.sourceforge.net)

## CityPulse framework dependencies
The Contextual Filtering component needs to have access to the following CityPulse components:
- Geospatial Data Infrastructure (GDI)
- Event Detection

## Start Contextual Filtering component
The Contextual Filtering component is implemented as a websocket server endpoint (/contextual_events_request) at port 8005. It requies a Contexual Event Request in json format as an input.

The following steps have to be achieved in order to deploy the component
- Step 1: download the following resources provided at https://github.com/CityPulse/DecisionSupport-ContextualFiltering and place them in the same folder: 
  + jar package in folder /target with title CityPulseWP5-jar-with-dependencies.jar	
  + folder res
- Step 2: edit the configuration file config.properties in the folder res which includes the following fields: 
  + hostname: the IP address of the server hosting the User-Centric Decision Support server.
  + port: the main port used by the User-Centric Decision Support server
  + GDI_URI: the URI of GDI component
  + eventRabbitURI: the URI of rabbitMQ to subscribe events from the Event Detection component
- Step 3: run CityPulseWP5-jar-with-dependencies.jar to start the server.

## Contextual Event Request
- Place of interest: identifies place to subscribe critical events. Currently, we support three types of place
  * Point: one coordinate
  * Route: a list of coordinates in which starting coordinate different to ending coordinate.
  * Area: a list of coordinates which represent to a polygon 
-	Filtering Factors: used to filter unrelated (unwanted) events and includes event’s source, event’s category, and user’s activity
- Ranking Factor: identifies which metric is preferred by the user for ranking the criticality of incoming events. We have currently implemented the Ranking Factor based on two metrics: distance, and gravity of the event. In order to combine these two metrics, we use the linear combination approach, where the user can identify weights (or importance) for each metric.

###### Sample Contextual Event Request
```python
{
    "filteringFactors": [
        {
            "name": "ACTIVITY",
            "values": [
                {
                    "value": "CarCommute"
                }
            ]
        }
    ],
    "messageType": "ContextualEventRequest",
    "place": "{\"route\":[{\"latitude\":56.156399,\"longitude\":10.2138401},{\"latitude\":56.15634,\"longitude\":10.2138534},{\"latitude\":56.1563362,\"longitude\":10.213245},{\"latitude\":56.1563748,\"longitude\":10.2128253},{\"latitude\":56.1563918,\"longitude\":10.2126945},{\"latitude\":56.1564118,\"longitude\":10.212536},{\"latitude\":56.1564327,\"longitude\":10.2123768},{\"latitude\":56.1565177,\"longitude\":10.2119317},{\"latitude\":56.1565617,\"longitude\":10.2115706},{\"latitude\":56.1565614,\"longitude\":10.211488},{\"latitude\":56.1565545,\"longitude\":10.2113544},{\"latitude\":56.1567336,\"longitude\":10.2100302},{\"latitude\":56.1567868,\"longitude\":10.2098974},{\"latitude\":56.1568154,\"longitude\":10.2098261},{\"latitude\":56.156858,\"longitude\":10.2097165},{\"latitude\":56.1574243,\"longitude\":10.2081406},{\"latitude\":56.157453,\"longitude\":10.2081537},{\"latitude\":56.1575063,\"longitude\":10.2080266},{\"latitude\":56.1575171,\"longitude\":10.2079806},{\"latitude\":56.1575512,\"longitude\":10.2078259},{\"latitude\":56.1576084,\"longitude\":10.2075683},{\"latitude\":56.1576479,\"longitude\":10.2076077},{\"latitude\":56.1578465,\"longitude\":10.2070523},{\"latitude\":56.1580631,\"longitude\":10.2070397},{\"latitude\":56.1580983,\"longitude\":10.2070499},{\"latitude\":56.1581829,\"longitude\":10.2067115},{\"latitude\":56.1582932,\"longitude\":10.2065955},{\"latitude\":56.1586254,\"longitude\":10.2057873},{\"latitude\":56.1588432,\"longitude\":10.2058839},{\"latitude\":56.1588377,\"longitude\":10.2057494},{\"latitude\":56.1588326,\"longitude\":10.2056072},{\"latitude\":56.1588183,\"longitude\":10.2052833},{\"latitude\":56.1588392,\"longitude\":10.2046773},{\"latitude\":56.158856,\"longitude\":10.2043227},{\"latitude\":56.1598925,\"longitude\":10.2041303},{\"latitude\":56.1604717,\"longitude\":10.203849},{\"latitude\":56.1610168,\"longitude\":10.2036215},{\"latitude\":56.1613158,\"longitude\":10.203491},{\"latitude\":56.1616331,\"longitude\":10.2033525},{\"latitude\":56.1621053,\"longitude\":10.2030403},{\"latitude\":56.1623841,\"longitude\":10.2028219},{\"latitude\":56.1625122,\"longitude\":10.2027103},{\"latitude\":56.1626964,\"longitude\":10.2025592},{\"latitude\":56.1628792,\"longitude\":10.2023967},{\"latitude\":56.1632132,\"longitude\":10.2020437},{\"latitude\":56.1639994,\"longitude\":10.2012686},{\"latitude\":56.1648281,\"longitude\":10.2004223},{\"latitude\":56.1648783,\"longitude\":10.2003762},{\"latitude\":56.1649309,\"longitude\":10.2003358},{\"latitude\":56.1650306,\"longitude\":10.2001617},{\"latitude\":56.1655192,\"longitude\":10.1995596},{\"latitude\":56.1655597,\"longitude\":10.1995139},{\"latitude\":56.1660496,\"longitude\":10.1989615},{\"latitude\":56.1663768,\"longitude\":10.1985531},{\"latitude\":56.1665834,\"longitude\":10.1983111},{\"latitude\":56.1666353,\"longitude\":10.1982187},{\"latitude\":56.166685,\"longitude\":10.1982755},{\"latitude\":56.167046,\"longitude\":10.1978351},{\"latitude\":56.1671395,\"longitude\":10.1977201},{\"latitude\":56.1679299,\"longitude\":10.1978855},{\"latitude\":56.1680441,\"longitude\":10.1979123},{\"latitude\":56.1690609,\"longitude\":10.1981355},{\"latitude\":56.1694494,\"longitude\":10.1982286},{\"latitude\":56.1699713,\"longitude\":10.1983588},{\"latitude\":56.1705209,\"longitude\":10.1984916},{\"latitude\":56.1707096,\"longitude\":10.1985372},{\"latitude\":56.1712316,\"longitude\":10.1987549},{\"latitude\":56.1719437,\"longitude\":10.1993527},{\"latitude\":56.1723068,\"longitude\":10.1997568},{\"latitude\":56.1723339,\"longitude\":10.1996992},{\"latitude\":56.1726295,\"longitude\":10.1995613},{\"latitude\":56.17265,\"longitude\":10.1995675},{\"latitude\":56.1732618,\"longitude\":10.199766},{\"latitude\":56.173335,\"longitude\":10.1997915},{\"latitude\":56.1740576,\"longitude\":10.2000089},{\"latitude\":56.1746959,\"longitude\":10.2002122},{\"latitude\":56.1747529,\"longitude\":10.200237},{\"latitude\":56.1748186,\"longitude\":10.2002655},{\"latitude\":56.1754552,\"longitude\":10.2005443},{\"latitude\":56.1766362,\"longitude\":10.1998289},{\"latitude\":56.1767297,\"longitude\":10.1998387},{\"latitude\":56.1769232,\"longitude\":10.1998734},{\"latitude\":56.1769869,\"longitude\":10.199895},{\"latitude\":56.1774701,\"longitude\":10.1997941},{\"latitude\":56.1777438,\"longitude\":10.1998466},{\"latitude\":56.1779084,\"longitude\":10.1998623},{\"latitude\":56.1783021,\"longitude\":10.1997759},{\"latitude\":56.1783053,\"longitude\":10.1999791},{\"latitude\":56.178314,\"longitude\":10.2000757},{\"latitude\":56.1783337,\"longitude\":10.2002789},{\"latitude\":56.1783438,\"longitude\":10.2003655},{\"latitude\":56.1794677,\"longitude\":10.1996322},{\"latitude\":56.1799432,\"longitude\":10.1993077},{\"latitude\":56.1799894,\"longitude\":10.1992761},{\"latitude\":56.1800617,\"longitude\":10.1992191},{\"latitude\":56.1805373,\"longitude\":10.1988917},{\"latitude\":56.1809567,\"longitude\":10.1986237},{\"latitude\":56.181048,\"longitude\":10.1985812},{\"latitude\":56.1812222,\"longitude\":10.1983878},{\"latitude\":56.1818518,\"longitude\":10.198047},{\"latitude\":56.1820762,\"longitude\":10.1979061},{\"latitude\":56.182412,\"longitude\":10.1976991},{\"latitude\":56.1824911,\"longitude\":10.1976518},{\"latitude\":56.1826517,\"longitude\":10.1975332},{\"latitude\":56.1827237,\"longitude\":10.1974863},{\"latitude\":56.1830437,\"longitude\":10.1972438},{\"latitude\":56.1866613,\"longitude\":10.1948179},{\"latitude\":56.186769,\"longitude\":10.1947202},{\"latitude\":56.1868427,\"longitude\":10.194659},{\"latitude\":56.1869016,\"longitude\":10.1946099},{\"latitude\":56.1872147,\"longitude\":10.1942935},{\"latitude\":56.1906573,\"longitude\":10.1897376},{\"latitude\":56.1929431,\"longitude\":10.1874953},{\"latitude\":56.1930095,\"longitude\":10.1874473},{\"latitude\":56.1930771,\"longitude\":10.1873985},{\"latitude\":56.1932164,\"longitude\":10.1872939},{\"latitude\":56.2001029,\"longitude\":10.1821443},{\"latitude\":56.200188,\"longitude\":10.1820724},{\"latitude\":56.2002466,\"longitude\":10.1819586},{\"latitude\":56.2002556,\"longitude\":10.1819345},{\"latitude\":56.2007088,\"longitude\":10.1814833},{\"latitude\":56.2021641,\"longitude\":10.1801387},{\"latitude\":56.2040809,\"longitude\":10.1777851},{\"latitude\":56.2043902,\"longitude\":10.1776037},{\"latitude\":56.2045611,\"longitude\":10.1774853},{\"latitude\":56.204686,\"longitude\":10.1774093},{\"latitude\":56.2048607,\"longitude\":10.1773037},{\"latitude\":56.2048915,\"longitude\":10.1772874},{\"latitude\":56.2050991,\"longitude\":10.1771975},{\"latitude\":56.2107223,\"longitude\":10.1738325},{\"latitude\":56.2108079,\"longitude\":10.1737715},{\"latitude\":56.210917,\"longitude\":10.1736937},{\"latitude\":56.2116676,\"longitude\":10.173582},{\"latitude\":56.2118709,\"longitude\":10.1737114},{\"latitude\":56.2120605,\"longitude\":10.1735846},{\"latitude\":56.2122273,\"longitude\":10.1734617},{\"latitude\":56.2121453,\"longitude\":10.1721554},{\"latitude\":56.2126534,\"longitude\":10.1717653},{\"latitude\":56.2129102,\"longitude\":10.1715674},{\"latitude\":56.2132352,\"longitude\":10.1713271},{\"latitude\":56.2136294,\"longitude\":10.1709891},{\"latitude\":56.2141348,\"longitude\":10.1706208},{\"latitude\":56.2141243,\"longitude\":10.1705478},{\"latitude\":56.2141058,\"longitude\":10.1704421},{\"latitude\":56.2140714,\"longitude\":10.170257},{\"latitude\":56.2141203,\"longitude\":10.1702313},{\"latitude\":56.2152105,\"longitude\":10.169502},{\"latitude\":56.2181585,\"longitude\":10.1671354},{\"latitude\":56.2201861,\"longitude\":10.1655583},{\"latitude\":56.2212518,\"longitude\":10.1647114},{\"latitude\":56.2221051,\"longitude\":10.1639978},{\"latitude\":56.2221113,\"longitude\":10.1641622},{\"latitude\":56.2221196,\"longitude\":10.1642921},{\"latitude\":56.2231587,\"longitude\":10.1635355},{\"latitude\":56.2244629,\"longitude\":10.1631107},{\"latitude\":56.2265659,\"longitude\":10.1625412},{\"latitude\":56.2291963,\"longitude\":10.1618581},{\"latitude\":56.233735,\"longitude\":10.160609},{\"latitude\":56.2384985,\"longitude\":10.1593394},{\"latitude\":56.238503,\"longitude\":10.1592065},{\"latitude\":56.2384914,\"longitude\":10.1591056},{\"latitude\":56.2403822,\"longitude\":10.1585937},{\"latitude\":56.2422692,\"longitude\":10.1580403},{\"latitude\":56.2489565,\"longitude\":10.1561247},{\"latitude\":56.249144,\"longitude\":10.1531571},{\"latitude\":56.2503068,\"longitude\":10.1497359},{\"latitude\":56.2504259,\"longitude\":10.1498562},{\"latitude\":56.2504527,\"longitude\":10.1498908},{\"latitude\":56.2519237,\"longitude\":10.1456169},{\"latitude\":56.2520106,\"longitude\":10.1451551},{\"latitude\":56.2520587,\"longitude\":10.1450276},{\"latitude\":56.2521086,\"longitude\":10.1445311},{\"latitude\":56.2517282,\"longitude\":10.1443494},{\"latitude\":56.2514049,\"longitude\":10.1406978},{\"latitude\":56.2514467,\"longitude\":10.1405254},{\"latitude\":56.2513196,\"longitude\":10.1402883},{\"latitude\":56.2505021,\"longitude\":10.1392032},{\"latitude\":56.2493537,\"longitude\":10.1371017},{\"latitude\":56.2488913,\"longitude\":10.1364297},{\"latitude\":56.2483916,\"longitude\":10.1357733},{\"latitude\":56.2481691,\"longitude\":10.1354681},{\"latitude\":56.2480038,\"longitude\":10.1359178},{\"latitude\":56.2471294,\"longitude\":10.1347471},{\"latitude\":56.2456169,\"longitude\":10.1325929},{\"latitude\":56.2453717,\"longitude\":10.1318964},{\"latitude\":56.2450905,\"longitude\":10.1302687},{\"latitude\":56.2449814,\"longitude\":10.129629},{\"latitude\":56.2446727,\"longitude\":10.1272998},{\"latitude\":56.2447356,\"longitude\":10.1245971},{\"latitude\":56.2448443,\"longitude\":10.1239448},{\"latitude\":56.2455244,\"longitude\":10.1211328},{\"latitude\":56.2457956,\"longitude\":10.1193432},{\"latitude\":56.2453847,\"longitude\":10.1151803},{\"latitude\":56.2434974,\"longitude\":10.1110187},{\"latitude\":56.2424405,\"longitude\":10.1078665},{\"latitude\":56.2395514,\"longitude\":10.1076476},{\"latitude\":56.2392371,\"longitude\":10.1076238},{\"latitude\":56.2375059,\"longitude\":10.1074585},{\"latitude\":56.2362385,\"longitude\":10.106818},{\"latitude\":56.2360666,\"longitude\":10.1066354},{\"latitude\":56.2347807,\"longitude\":10.1053608},{\"latitude\":56.2352839,\"longitude\":10.103768},{\"latitude\":56.2357546,\"longitude\":10.0982785},{\"latitude\":56.2358351,\"longitude\":10.097752},{\"latitude\":56.2350833,\"longitude\":10.0966287}],\"length\":16998,\"placeId\":\"0\",\"type\":\"ROUTE\"}",
    "rankingFactor": {
        "rankingElements": [
            {
                "name": "EVENT_LEVEL",
                "value": {
                    "value": 30
                }
            },
            {
                "name": "DISTANCE",
                "value": {
                    "value": 70
                }
            }
        ],
        "type": "LINEAR"
    }
}
```
## Contributors
The Contextual Filtering component has been developed as part of the EU project CityPulse. The Unit of Reasoning and Querying of INSIGHT, NUIG has provided the main contributions for this application.

CityPulse: http://www.ict-citypulse.eu/

INSIGHT: https://www.insight-centre.org



