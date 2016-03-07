%% :- violatedConstraint("POLLUTION").
%% violatedConstraint("POLLUTION") :- valueOf("POLLUTION", AV), 135 < AV.
%% :~ valueOf("DISTANCE", AV). [AV@2]
%% :~ valueOf("TRAVEL_TIME", AV). [AV@1]
%% parameter("ENDING_POINT","10.1591864 56.1481156").
%% parameter("STARTING_POINT","10.116919 56.226144").
%% preference(2,"MINIMIZE","DISTANCE").
%% preference(1,"MINIMIZE","TIME").


route_costMode(V) :- preference(1,"MINIMIZE",V).
esists_preference_1 :- preference(1,"MINIMIZE",_).
route_costMode("combined") :- not esists_preference_1.
input_get_routes(SP, EP, V, 5) :- parameter("STARTING_POINT", SP), parameter("ENDING_POINT", EP), route_costMode(V).

route(@get_routes(SP, EP, V, N)) :- input_get_routes(SP, EP, V, N).
route_data(@get_routes_data(SP, EP, V, N)) :- input_get_routes(SP, EP, V, N).
max_pollution(@get_max_pollution(RouteID)) :- selected(RouteID).

1 <= {selected(RouteID) : route((RouteID, _, _))} <= 1.

valueOf("TRAVEL_TIME", ApproximatedTime) :- selected(RouteID), route_data((RouteID, Time, _)), ApproximatedTime = Time / 60.
valueOf("DISTANCE", ApproximatedDistance) :- selected(RouteID), route_data((RouteID, _, Distance)), ApproximatedDistance = Distance / 100.
valueOf("POLLUTION", Pollution) :- max_pollution(Pollution).
%% exists_max_pollution :- max_pollution(_).
%% valueOf("POLLUTION", 0) :- not exists_max_pollution.


route_selected(Step, P) :- selected(RouteID), route((RouteID, Step, P)).
route_selected_data(Time, Distance, Pollution) :- selected(RouteID), route_data((RouteID, Time, Distance)), max_pollution(Pollution).

#show route_selected/2.
#show route_selected_data/3.


