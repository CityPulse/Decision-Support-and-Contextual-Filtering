
% *** Data *** %
%
% From RequestRewriter (via Java)
% Parameters
% parameter("starting_point", value)
% parameter("ending_point", value)
% parameter("starting_datetime", value)
% parameter("transportation_type", value)
% Constraints
% :- violatedConstraint(name).
% violatedConstraint(name) :- valueOf(name, actual_value), value operator actual_value.
% Preferences
% FIXME now is only minimize, we should improve this
% :~ preference(order, name), valueOf(name, actual_value). [actual_value:order]
%
% From sub-reasoning task (via external atoms)
% route(routeID, step, pointID)
% pointIDs are "latitude longitude"
% travel_time(routeID, time)
% distance(routeID, distance)
% CHECK I assume that I have the routes that starts from the starting_point and ends in the ending_point
%
% From event snapshot (via external atoms)
% FIXME now is custom, but should be generic
% max_pollution(pointID, value)


route_costMode(V) :- preference(1,"MINIMIZE",V).
esists_preference_1 :- preference(1,"MINIMIZE",_).
route_costMode("combined") :- not esists_preference_1.
input_get_routes(SP, EP, V, 5) :- parameter("STARTING_POINT", SP), parameter("ENDING_POINT", EP), route_costMode(V).

route(RouteID, Step, P) :- &get_routes[SP, EP, V, N](RouteID, Step, P), input_get_routes(SP, EP, V, N).
route_data(RouteID, Time, Distance) :- &get_routes_data[SP, EP, V, N](RouteID, Time, Distance), input_get_routes(SP, EP, V, N).

max_pollution(Value) :- &get_max_pollution[RouteID](Value), selected(RouteID).

%% routeIDs(RouteID) :- route(RouteID, _, _), RouteID > 0.
%% 1 <= {selected(RouteID) : routeIDs(RouteID)} <= 1.
1 <= {selected(RouteID) : route(RouteID, _, _)} <= 1.

valueOf("TRAVEL_TIME", ApproximatedTime) :- selected(RouteID), route_data(RouteID, Time, _), ApproximatedTime = Time / 60.
valueOf("DISTANCE", ApproximatedDistance) :- selected(RouteID), route_data(RouteID, _, Distance), ApproximatedDistance = Distance / 100.
valueOf("POLLUTION", Pollution) :- max_pollution(Pollution).


route_selected(Step, P) :- selected(RouteID), route(RouteID, Step, P).
route_selected_data(Time, Distance, Pollution) :- selected(RouteID), route_data(RouteID, Time, Distance), max_pollution(Pollution).
