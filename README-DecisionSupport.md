## Decision Support
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

## Contributors
The Decision Support component has been developed as part of the EU project CityPulse. The Unit of Reasoning and Querying of INSIGHT, NUIG has provided the main contributions for this application.

CityPulse: http://www.ict-citypulse.eu/

INSIGHT: https://www.insight-centre.org


