event_id(EId) :- contextual_event(EId,EType,EName,ELevel, ETime, ECoor).
event_type(EId, EType):- contextual_event(EId,EType,EName,ELevel, ETime, ECoor).
event_name(EId, EName):- contextual_event(EId,EType,EName,ELevel, ETime, ECoor).
event_level(EId, ELevel):- contextual_event(EId,EType,EName,ELevel, ETime, ECoor).
event_coordinate(EId, ECoor):- contextual_event(EId,EType,EName,ELevel, ETime, ECoor).

user_id(UId):- user_status(UId,UActivity,UCoor).
user_activity(UId,UActivity):- user_status(UId,UActivity,UCoor).
user_coordinate(UId,UCoor):- user_status(UId,UActivity,UCoor).


weight_critical(EId,UId,N):- user_id(UId), event_name(EId,EName), EName == "PublicParking", event_level(EId, ELevel), N = (ELevel*5)/3.
#show weight_critical/3.