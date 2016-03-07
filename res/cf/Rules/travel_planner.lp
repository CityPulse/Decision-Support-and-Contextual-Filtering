% author: Thu-Le Pham

%------------ contextual events
event_id(EId) :- contextual_event(EId,EType,EName,ELevel, ETime, ECoor).
event_type(EId, EType):- contextual_event(EId,EType,EName,ELevel, ETime, ECoor).
event_name(EId, EName):- contextual_event(EId,EType,EName,ELevel, ETime, ECoor).
event_level(EId, ELevel):- contextual_event(EId,EType,EName,ELevel, ETime, ECoor).
event_coordinate(EId, ECoor):- contextual_event(EId,EType,EName,ELevel, ETime, ECoor).

%------------ user status
user_id(UId):- user_status(UId,UActivity,UCoor).
user_activity(UId,UActivity):- user_status(UId,UActivity,UCoor).
user_coordinate(UId,UCoor):- user_status(UId,UActivity,UCoor).

%------------ map user's coordinate & event's coordinate with path_node coordinate
user_on_path(UId, Nu) :- user_coordinate(UId, UCoor), path_node(NId, Nu), UCoor == NId.
event_on_path(EId, Ne):- event_coordinate(EId, ECoor), path_node(NId, Ne), ECoor == NId. 



%------------ filter related events based on the ontology
related_event(EId, UId) :- event_type(EId, EType), event_name(EId, EName), user_activity(UId, UActivity), effect(EType, EName, UActivity).

%------------ rules for filtering
out_of_context(EId,UId) :- not related_event(EId, UId), event_id(EId), user_id(UId).

out_of_context(EId,UId) :- not user_on_path(UId,_), event_id(EId), user_id(UId).

out_of_context(EId,UId) :- user_on_path(UId, Nu), event_on_path(EId, Ne), Ne - Nu <0.

out_of_context(EId,UId) :- not event_on_path(EId,_), event_id(EId), user_id(UId).

related(EId, UId):- not out_of_context(EId,UId), event_id(EId), user_id(UId). 


%------------ rank related events based on location
weight_location(EId,UId,D):- related(EId, UId), user_on_path(UId, Nu), event_on_path(EId, Ne), D = n-(Ne-Nu).  

weight_critical_location(EId,UId,M) :- weight_location(EId,UId,N), M = N*5/n.
    
weight_critical(EId,UId,M) :- weight_critical_location(EId,UId,N), event_level(EId, ELevel), M =(N+ELevel)/2.


#show weight_critical/3.                                                                 