
% *** Data *** %
%
% From RequestRewriter (via Java)
% Parameters
% parameter("starting_point", value)
% parameter("point_of_interest", value)
% parameter("starting_datetime", value)
% parameter("distance_range", value)
% parameter("time_of_stay", value)
% Constraints
% :- violatedConstraint(name).
% violatedConstraint(name) :- valueOf(name, actual_value), value operator actual_value.
% Preferences
% FIXME now is only minimize, we should improve this
% :~ preference(order, name), valueOf(name, actual_value). [actual_value:order]
%
% From KB (via external atoms)
% parking_space(position)
% CHECK I assume that I have the parking slots that are inside the desired range from the POI
% 
% From event snapshot (via external atoms)
% availability(position, availablePS)
%
% From event snapshot (via external atoms)
% FIXME now is custom, but should be generic 
% total_cost(position, value)
%
% From sub-reasoning task (via external atoms)
% distance(position, distance)

parking_space(Position) :- &get_parking_spaces[POI, DR](Position), parameter("POINT_OF_INTEREST", POI), parameter("DISTANCE_RANGE", DR).

availability(Position, AvailablePS) :- &get_availability[Position](AvailablePS), parking_space(Position).

total_cost(Position, Value) :- &get_total_cost[Position, ToS](Value), parking_space(Position), parameter("TIME_OF_STAY", ToS).

distance(Position, Distance) :- &get_walking_distance[Position, POI](Distance), parking_space(Position), parameter("POINT_OF_INTEREST", POI).

1 <= {selected(Position) : parking_space(Position, _, _)} <= 1.

parking_space_selected(Position, AvailablePS, Distance) :- selected(Position), availability(Position, AvailablePS), distance(Position, Distance).

valueOf("COST", Cost) :- total_cost(Cost).
valueOf("WALK", Walk) :- selected(Position), distance(Position, Walk).

