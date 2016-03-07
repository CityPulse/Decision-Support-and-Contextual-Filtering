%% :- violatedConstraint("COST").
%% violatedConstraint("COST") :- valueOf("COST", AV), 100 < AV.
%% :~ valueOf("COST", AV). [AV@2]
%% :~ valueOf("WALK", AV). [AV@1]
%% parameter("DISTANCE_RANGE",100).
%% parameter("POINT_OF_INTEREST","10.116919 56.226144").
%% preference(2,"MINIMIZE","COST").
%% preference(1,"MINIMIZE","WALK").


parking_space(@get_parking_spaces(POI, DR)) :- parameter("POINT_OF_INTEREST", POI), parameter("DISTANCE_RANGE", DR).

availability(@get_availability(ParkingID)) :- selected(ParkingID).
total_cost(@get_total_cost(ParkingID, ToS)) :- selected(ParkingID), parameter("TIME_OF_STAY", ToS).
distance(@get_walking_distance(ParkingID, POI)) :- selected(ParkingID), parameter("POINT_OF_INTEREST", POI).

1 <= {selected(ParkingID) : parking_space((ParkingID, _))} <= 1.

parking_space_selected(Position, AvailablePS, Distance) :- selected(ParkingID), parking_space((ParkingID, Position)), availability(AvailablePS), distance(Distance).

valueOf("COST", Cost) :- total_cost(Cost).
valueOf("WALK", Walk) :- distance(Walk).

#show parking_space_selected/3.