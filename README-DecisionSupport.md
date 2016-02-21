## Decision Support
The Decision Support component utilises contextual information available as background knowledge, user patterns and preferences from the application as well as real-time events to provide optimal solutions of smart city applications. Currently, this component supports two modules:
- Routing Module. It provides the best routes between two coordinates.
- Parking Space Module. It provides the best available parking spaces among a set of alternative. 

## Start Decision Support component
The Decision Support component is implemented as a websocket server endpoint (`/reasoning_request`) at port `8005`. It requies a Reasoning Request in json format as an input.  

## Reasoning Request
A Reasoning Request consists of: 
-	User Reference: uniquely identifies the user that made the request. Such reference is related to user credentials that will be used in the final integration activities in order to manage user logins and instances of the CityPulse framework in different cities.
-	Type: indicates the reasoning task required by the application. This is used directly by the Decision support component to select which set of rules to apply, and needs to be identified among a set of available options at design time by the application developer.
-	Functional Details: represent the qualitative criteria used in the reasoning task to produce a solution that best fits the user needs. The Functional Details are composed by: 
-	Functional Parameters, defining mandatory information for the Reasoning Request (such as start and end location in a travel planner scenario); 
-	Functional Constraints, defining a numerical threshold for specific functional aspects of the Reasoning Request (such as cost of a trip, distance or travel time in a travel planner scenario). These restrictions are evaluated as hard constraints, which needs to be fulfilled by each of the alternative solutions offered to the user;
-	Functional Preferences, which encode two types of soft constraints: a qualitative optimisation statement defined on the same functional aspects used in the Functional Constraint (such as minimisation of the travel time); or a qualitative partial order over such optimization statements (such as preference on the minimisation of the distance over minimization of the travel time). Preferences are used by the Decision support component to provide to the user the optimal solution among those verifying the functional constraints.





