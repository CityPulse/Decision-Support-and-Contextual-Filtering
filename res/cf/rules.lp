% author: Thu-Le Pham
% update: 1 Oct 2015

%city_event(EventId, EventCategory, EventSource).
%related_city_event(EventId).

%ranking_multiplier(RankingElementName,Int).

%---- if RankingType is LINEAR------
% value_with_ranking_type(RankingElementName, M):- value(RankingElementName,ValueD),ranking_multiplier(RankingElementName,Int), M = ValueD*Int.
% criticality(C) :- S = #sum{M:value_with_ranking_type(_,M)}, C = S/100.


%---------------------------------------------------------

1 <= {selected_city_event(EventId) : related_city_event(EventId), not expired_event(EventId)} <= 1.

value(RankingElementName,Value):- selected_city_event(EventId),ranking_city_event_data(EventId,RankingElementName,Value).

critical_city_event(EventId,C):- selected_city_event(EventId), criticality(C).
                                                         
#show critical_city_event/2.
                                                         