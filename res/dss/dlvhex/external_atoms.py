# import csv
import dlvhex

import sys
import re
import time

from twisted.python import log
# from twisted.internet import reactor

# from autobahn.twisted.websocket import WebSocketClientProtocol, WebSocketClientFactory, connectWS

# srcXlon = None
# srcYlat = None
# trgXlon = None
# trgxlat = None
# costMode = None
# numberOfRoutes = None

class Route(object):
	"""docstring for Route"""
	def __init__(self, points, distance, time):
		super(Route, self).__init__()
		self.points = points
		self.distance = distance
		self.time = time

routes = None

# class RoutingProtocol(WebSocketClientProtocol):

# 	def onConnect(self, response):
# 		print("Server connected: {0}".format(response.peer))

# 	def onOpen(self):
# 		print("WebSocket connection open.")
# 		print("%s" % [srcXlon,srcYlat,trgXlon,trgxlat,costMode,numberOfRoutes])

# 	def onMessage(self, payload, isBinary):
# 		global routes
# 		global numberOfRoutes
				
# 		if isBinary:
# 			print("Binary message received: {0} bytes".format(len(payload)))
# 		else:
# 			print("Text message received: {0}".format(payload.decode('utf8')))

# 		message = payload.decode('utf8')

# 		if message.startswith('Registred') :
# 			time.sleep(2) # delay, trick needed to make it work (I don't know why)
# 			self.sendMessage((u"getCityRoute(%s,%s,%s,%s,%s,%d)" % (srcXlon,srcYlat,trgXlon,trgxlat,costMode,numberOfRoutes)).encode('utf8'))
# 		else :
# 			p = re.compile('\w+;\w+;\w+;\w+\n(LINESTRING|MULTILINESTRING)\((.+)\);(\d+);(\d+);(\d+.\d+)')
# 			m = p.match(payload.decode('utf8'))
# 			if m :
# 				# print(m.group(1),m.group(3),m.group(4),m.group(5))
				
# 				if routes is None :
# 					routes = []

# 				if m.group(1) == u'LINESTRING' :
# 					# print(m.group(1))
# 					route = Route(m.group(2).split(','),int(m.group(3)),int(m.group(4)))
			
# 					routes.append(route)

# 				numberOfRoutes -= 1
# 				if numberOfRoutes == 0 :
# 					self.sendClose()

# 	def onClose(self, wasClean, code, reason):
# 		print("WebSocket connection closed: {0}".format(reason))
# 		reactor.stop()

def register():
	dlvhex.addAtom("get_routes", (dlvhex.CONSTANT, dlvhex.CONSTANT, dlvhex.CONSTANT, dlvhex.CONSTANT), 3)
	dlvhex.addAtom("get_routes_data", (dlvhex.CONSTANT, dlvhex.CONSTANT, dlvhex.CONSTANT, dlvhex.CONSTANT), 3)
	dlvhex.addAtom("get_max_pollution", (dlvhex.CONSTANT, ), 1)
	dlvhex.addAtom("get_parking_spaces", (dlvhex.CONSTANT, dlvhex.CONSTANT), 1)
	dlvhex.addAtom("get_availability", (dlvhex.CONSTANT, ), 1)
	dlvhex.addAtom("get_total_cost", (dlvhex.CONSTANT, dlvhex.CONSTANT), 1)
	dlvhex.addAtom("get_walking_distance", (dlvhex.CONSTANT, dlvhex.CONSTANT), 1)

# def readCSV():
# 	with open(filename, accessMode) as myCSVFile :
# 	dataFromFile = csv.reader(myCSVFile, delimiter=",")
# 	for row in dataFromFile :
# 		...
# 		for value in row :
# 			...

def get_routes(starting_point, ending_point, cost_mode, number_of_routes):
	# local_test()

	# if routes is not None :
	# 	print('NOT None!')

	initRoutes(starting_point.value(), ending_point.value(), cost_mode.value(), number_of_routes.intValue())

	if routes is not None :
		routeNumber = 1
		for route in routes :
			stepNumber = 1
			for point in route.points :
				# if stepNumber % 10 == 0:
				pointE = '"' + point.encode() + '"'
				# print('route(%d, %d, %s)' % (routeNumber, stepNumber, pointE))
				# print(pointE)
				dlvhex.output((routeNumber, stepNumber, pointE))
				# print(pointE)
				stepNumber += 1
			routeNumber +=1

def get_routes_data(starting_point, ending_point, cost_mode, number_of_routes):

	initRoutes(starting_point.value(), ending_point.value(), cost_mode.value(), number_of_routes.intValue())

	if routes is not None :
		routeNumber = 1
		for route in routes :
			# print((routeNumber, route.time, route.distance))
			dlvhex.output((routeNumber, route.time, route.distance))
			routeNumber +=1

def get_max_pollution(routeID):
	
	if routes is not None :
		if routeID.intValue() <= len(routes) :
			dlvhex.output((max(getPollutionValues(routes[routeID.intValue()-1].points)),))
		else :
			raise ValueError('Wrong routeID in get_max_pollution')
	else :
		raise ValueError('get_max_pollution is called before routes is initialized')

		

def initRoutes(starting_point, ending_point, cost_mode, number_of_routes):
	global routes

	if routes is None :

		log.startLogging(sys.stdout)

		print("Route: FROM %s TO %s" % (starting_point, ending_point))

		sSplit = starting_point.strip('"').split()
		eSplit = ending_point.strip('"').split()

		# print("strip %s" % (sSplit))
		# print("strip %s" % (eSplit))

		if len(sSplit) != 2 :
			raise ValueError('Wrong starting point')
		if len(eSplit) != 2 :
			raise ValueError('Wrong ending point')

		srcXlon = sSplit[0]
		srcYlat = sSplit[1]
		trgXlon = eSplit[0]
		trgxlat = eSplit[1]
		costMode = cost_mode.lower().strip('"')
		numberOfRoutes = number_of_routes

		from websocket import create_connection
		ws = create_connection("ws://pcsd-118.et.hs-osnabrueck.de:7686")
		print "Sending 'Hello, World'..."
		result =  ws.recv()
		print "Received '%s'" % result
		ws.send((u"getCityRoute(%s,%s,%s,%s,%s,%d)" % (srcXlon,srcYlat,trgXlon,trgxlat,costMode,numberOfRoutes)).encode('utf8'))
		print "Sent"
		for x in xrange(numberOfRoutes):
			print "Reeiving..."
			result =  ws.recv()
			print "Received '%s'" % result
			p = re.compile('\w+;\w+;\w+;\w+\n(LINESTRING|MULTILINESTRING)\((.+)\);(\d+);(\d+);(\d+.\d+)')
			m = p.match(result.decode('utf8'))
			if m :
				# print(m.group(1),m.group(3),m.group(4),m.group(5))
				
				if routes is None :
					routes = []

				if m.group(1) == u'LINESTRING' :
					# print(m.group(1))
					route = Route(m.group(2).split(','),int(m.group(3)),int(m.group(4)))
			
					routes.append(route)
		ws.close()

		# factory = WebSocketClientFactory()
		# # factory = WebSocketClientFactory("ws://pcsd-118.et.hs-osnabrueck.de:7686", debug = False)
		# factory.protocol = RoutingProtocol
		# # connectWS(factory)

		# print("Created factory")

		# reactor.connectTCP("pcsd-118.et.hs-osnabrueck.de", 7686, factory)
		# print("Connected")
		# # if not reactor.running:
		# # for x in xrange(1,10):
		# # 	try:
		# reactor.run()
		# # 		break
		# # 	except Exception as e:
		# # 		print type(inst)     # the exception instance
		# # 		print inst.args      # arguments stored in .args
		# # 		print inst           # __str__ allows args to be printed directly
		# # 		time.sleep(1)
		# # 	else:
		# # 		pass
		# # 	finally:
		# # 		pass

		# # if routes is not None :
		# # 	for route in routes :
		# # 		print(route.points)
			
	else :
		print("already called")

def getPollutionValues(points):
		# Just for testing
	return [100,50,80,90]


def get_parking_spaces(point_of_interest, distance_range):
	dlvhex.output(('"10.1171296 56.2261545"',))
	dlvhex.output(('"10.1442042 56.2136402"',))

def get_availability(position):
    dlvhex.output((13,))

def get_total_cost(position, time_of_stay):
    dlvhex.output((10,))

def get_walking_distance(position, point_of_interest):
    dlvhex.output((100,))


def local_test():
	dlvhex.output((1, 1, 'a'))
	dlvhex.output((1, 2, 'b'))
	dlvhex.output((1, 3, 'c'))
	dlvhex.output((1, 4, 'q'))
	dlvhex.output((1, 5, 'w'))
	dlvhex.output((1, 6, 'e'))
	dlvhex.output((2, 1, 'c'))
	dlvhex.output((2, 2, 'd'))
	dlvhex.output((2, 3, 'c'))
	dlvhex.output((2, 4, 'q'))
	dlvhex.output((2, 5, 'w'))
	dlvhex.output((2, 6, 'e'))
	dlvhex.output((2, 7, 'ca'))
	# dlvhex.output((2, 8, 'da'))
	# dlvhex.output((2, 9, 'ca'))
	# dlvhex.output((2, 10, 'qa'))
	# dlvhex.output((2, 11, 'wa'))
	# dlvhex.output((2, 12, 'ea'))
	# dlvhex.output((1, 1, '"10.1171296 56.2261545"'))
	# dlvhex.output((1, 2, '"10.1173131 56.2262719"'))
	# dlvhex.output((1, 3, '"10.1179751 56.22596"'))
	# dlvhex.output((1, 4, '"10.1184204 56.2257401"'))
	# dlvhex.output((1, 5, '"10.119267 56.2253345"'))
	# dlvhex.output((1, 6, '"10.1206511 56.2246605"'))
	# dlvhex.output((1, 7, '"10.1222433 56.2238635"'))
	# dlvhex.output((1, 8, '"10.1224477 56.2237574"'))
	# dlvhex.output((1, 9, '"10.1294667 56.2191488"'))
	# dlvhex.output((1, 10, '"10.1385889 56.215447"'))
	# dlvhex.output((1, 11, '"10.138753 56.215385"'))
	# dlvhex.output((1, 12, '"10.1389079 56.2153196"'))
	# dlvhex.output((1, 13, '"10.1391345 56.215234"'))
	# dlvhex.output((1, 14, '"10.1409243 56.2146254"'))
	# dlvhex.output((1, 15, '"10.1442042 56.2136402"'))
	# dlvhex.output((1, 16, '"10.1542546 56.2105575"'))
	# dlvhex.output((1, 17, '"10.1550606 56.2100706"'))
	# dlvhex.output((1, 18, '"10.1551788 56.2100419"'))
	# dlvhex.output((1, 19, '"10.1665352 56.2065535"'))
	# dlvhex.output((1, 20, '"10.1707496 56.2024565"'))
	# dlvhex.output((1, 21, '"10.1711449 56.2020846"'))
	# dlvhex.output((1, 22, '"10.1656727 56.1993786"'))
	# dlvhex.output((1, 23, '"10.1616863 56.1949102"'))
	# dlvhex.output((1, 24, '"10.1615732 56.1946955"'))
	# dlvhex.output((1, 25, '"10.1644688 56.1858759"'))
	# dlvhex.output((1, 26, '"10.1664282 56.184393"'))
	# dlvhex.output((1, 27, '"10.1681394 56.1836261"'))
	# dlvhex.output((1, 28, '"10.1684345 56.1835016"'))
	# dlvhex.output((1, 29, '"10.1693309 56.183125"'))
	# dlvhex.output((1, 30, '"10.169099 56.1825661"'))
	# dlvhex.output((1, 31, '"10.1684403 56.1808002"'))
	# dlvhex.output((1, 32, '"10.1682853 56.1804142"'))
	# dlvhex.output((1, 33, '"10.1677781 56.1791537"'))
	# dlvhex.output((1, 34, '"10.1677485 56.1790586"'))
	# dlvhex.output((1, 35, '"10.1675108 56.1784605"'))
	# dlvhex.output((1, 36, '"10.1670359 56.1772586"'))
	# dlvhex.output((1, 37, '"10.1669596 56.1770726"'))
	# dlvhex.output((1, 38, '"10.1668795 56.1768502"'))
	# dlvhex.output((1, 39, '"10.1666258 56.1762321"'))
	# dlvhex.output((1, 40, '"10.1665517 56.1760426"'))
	# dlvhex.output((1, 41, '"10.166039 56.1747597"'))
	# dlvhex.output((1, 42, '"10.1657622 56.1740339"'))
	# dlvhex.output((1, 43, '"10.1657042 56.1739093"'))
	# dlvhex.output((1, 44, '"10.1656185 56.1737636"'))
	# dlvhex.output((1, 45, '"10.16559 56.1736984"'))
	# dlvhex.output((1, 46, '"10.1654823 56.1734573"'))
	# dlvhex.output((1, 47, '"10.1654312 56.1732668"'))
	# dlvhex.output((1, 48, '"10.1653892 56.1731668"'))
	# dlvhex.output((1, 49, '"10.1652607 56.1728642"'))
	# dlvhex.output((1, 50, '"10.1650869 56.1723822"'))
	# dlvhex.output((1, 51, '"10.1649656 56.1721055"'))
	# dlvhex.output((1, 52, '"10.1649281 56.172021"'))
	# dlvhex.output((1, 53, '"10.164791 56.1716818"'))
	# dlvhex.output((1, 54, '"10.1646902 56.1714217"'))
	# dlvhex.output((1, 55, '"10.1646478 56.1713148"'))
	# dlvhex.output((1, 56, '"10.1644711 56.1708721"'))
	# dlvhex.output((1, 57, '"10.164385 56.1706575"'))
	# dlvhex.output((1, 58, '"10.1642056 56.1701992"'))
	# dlvhex.output((1, 59, '"10.164082 56.1698591"'))
	# dlvhex.output((1, 60, '"10.1640356 56.1696621"'))
	# dlvhex.output((1, 61, '"10.1639963 56.1695121"'))
	# dlvhex.output((1, 62, '"10.1639781 56.1694131"'))
	# dlvhex.output((1, 63, '"10.1639605 56.1691109"'))
	# dlvhex.output((1, 64, '"10.1639532 56.1687273"'))
	# dlvhex.output((1, 65, '"10.1640122 56.1677663"'))
	# dlvhex.output((1, 66, '"10.1639859 56.167716"'))
	# dlvhex.output((1, 67, '"10.1639151 56.1676259"'))
	# dlvhex.output((1, 68, '"10.1636926 56.1674486"'))
	# dlvhex.output((1, 69, '"10.1636015 56.1673916"'))
	# dlvhex.output((1, 70, '"10.1634936 56.1673328"'))
	# dlvhex.output((1, 71, '"10.1633745 56.16724"'))
	# dlvhex.output((1, 72, '"10.1642159 56.1667904"'))
	# dlvhex.output((1, 73, '"10.1641172 56.1667292"'))
	# dlvhex.output((1, 74, '"10.1641043 56.1662809"'))
	# dlvhex.output((1, 75, '"10.1639197 56.1654299"'))
	# dlvhex.output((1, 76, '"10.163744 56.1646144"'))
	# dlvhex.output((1, 77, '"10.1635899 56.1639632"'))
	# dlvhex.output((1, 78, '"10.1635677 56.1638077"'))
	# dlvhex.output((1, 79, '"10.1632749 56.1620248"'))
	# dlvhex.output((1, 80, '"10.1633519 56.1615805"'))
	# dlvhex.output((1, 81, '"10.1634534 56.1612429"'))
	# dlvhex.output((1, 82, '"10.1638214 56.1603345"'))
	# dlvhex.output((1, 83, '"10.1639543 56.1599625"'))
	# dlvhex.output((1, 84, '"10.1640306 56.1597854"'))
	# dlvhex.output((1, 85, '"10.1641209 56.1595744"'))
	# dlvhex.output((1, 86, '"10.1642348 56.1592503"'))
	# dlvhex.output((1, 87, '"10.1642886 56.1591152"'))
	# dlvhex.output((1, 88, '"10.1643595 56.158935"'))
	# dlvhex.output((1, 89, '"10.1646285 56.1582476"'))
	# dlvhex.output((1, 90, '"10.1649969 56.1572352"'))
	# dlvhex.output((1, 91, '"10.1651711 56.1567443"'))
	# dlvhex.output((1, 92, '"10.1652172 56.1565983"'))
	# dlvhex.output((1, 93, '"10.1652327 56.156546"'))
	# dlvhex.output((1, 94, '"10.1653096 56.1563413"'))
	# dlvhex.output((1, 95, '"10.1654664 56.1559222"'))
	# dlvhex.output((1, 96, '"10.1657311 56.1552347"'))
	# dlvhex.output((1, 97, '"10.1657609 56.1551607"'))
	# dlvhex.output((1, 98, '"10.1661565 56.1541197"'))
	# dlvhex.output((1, 99, '"10.166493 56.1532342"'))
	# dlvhex.output((1, 100, '"10.1666856 56.1527444"'))
	# dlvhex.output((1, 101, '"10.1669244 56.1521274"'))
	# dlvhex.output((1, 102, '"10.165728 56.1519037"'))
	# dlvhex.output((1, 103, '"10.1656465 56.1518893"'))
	# dlvhex.output((1, 104, '"10.1655285 56.1518681"'))
	# dlvhex.output((1, 105, '"10.1656105 56.1514049"'))
	# dlvhex.output((1, 106, '"10.1656151 56.151338"'))
	# dlvhex.output((1, 107, '"10.1656171 56.1513098"'))
	# dlvhex.output((1, 108, '"10.1655812 56.15084"'))
	# dlvhex.output((1, 109, '"10.1654136 56.1500354"'))
	# dlvhex.output((1, 110, '"10.163448 56.1495916"'))
	# dlvhex.output((1, 111, '"10.1624482 56.1492717"'))
	# dlvhex.output((1, 112, '"10.1622671 56.1492059"'))
	# dlvhex.output((1, 113, '"10.1604515 56.1485649"'))
	# dlvhex.output((1, 114, '"10.1591864 56.1481156"'))
	# dlvhex.output((2, 1, '"10.1591864 56.1481156"'))
	# dlvhex.output((2, 2, '"10.1604515 56.1485649"'))
	# dlvhex.output((2, 3, '"10.1622671 56.1492059"'))
	# dlvhex.output((2, 4, '"10.16216 56.149337"'))
	# dlvhex.output((2, 5, '"10.160882 56.1509696"'))
	# dlvhex.output((2, 6, '"10.1622038 56.1512531"'))
	# dlvhex.output((2, 7, '"10.1634633 56.1514987"'))
	# dlvhex.output((2, 8, '"10.1633865 56.1517892"'))
	# dlvhex.output((2, 9, '"10.1633332 56.1519911"'))
	# dlvhex.output((2, 10, '"10.1633234 56.1520221"'))
	# dlvhex.output((2, 11, '"10.1626302 56.1542235"'))
	# dlvhex.output((2, 12, '"10.1623944 56.1549467"'))
	# dlvhex.output((2, 13, '"10.1622664 56.1549374"'))
	# dlvhex.output((2, 14, '"10.1621103 56.155137"'))
	# dlvhex.output((2, 15, '"10.1614739 56.1558673"'))
	# dlvhex.output((2, 16, '"10.1610089 56.1564043"'))
	# dlvhex.output((2, 17, '"10.1600313 56.1579919"'))
	# dlvhex.output((2, 18, '"10.1599244 56.1582246"'))
	# dlvhex.output((2, 19, '"10.1598661 56.15835"'))
	# dlvhex.output((2, 20, '"10.1595218 56.1606742"'))
	# dlvhex.output((2, 21, '"10.1594515 56.1608118"'))
	# dlvhex.output((2, 22, '"10.159167 56.1613689"'))
	# dlvhex.output((2, 23, '"10.1596283 56.1614441"'))
	# dlvhex.output((2, 24, '"10.1597917 56.1614706"'))
	# dlvhex.output((2, 25, '"10.1611099 56.1616784"'))
	# dlvhex.output((2, 26, '"10.1609888 56.1623453"'))
	# dlvhex.output((2, 27, '"10.1608676 56.1630882"'))
	# dlvhex.output((2, 28, '"10.1607558 56.1637497"'))
	# dlvhex.output((2, 29, '"10.160719 56.1640117"'))
	# dlvhex.output((2, 30, '"10.1606393 56.1645164"'))
	# dlvhex.output((2, 31, '"10.1605225 56.1652739"'))
	# dlvhex.output((2, 32, '"10.1604577 56.1656708"'))
	# dlvhex.output((2, 33, '"10.1604448 56.1665257"'))
	# dlvhex.output((2, 34, '"10.1604428 56.1666283"'))
	# dlvhex.output((2, 35, '"10.1604369 56.1667744"'))
	# dlvhex.output((2, 36, '"10.1603634 56.1671194"'))
	# dlvhex.output((2, 37, '"10.160244 56.1672957"'))
	# dlvhex.output((2, 38, '"10.1600881 56.1674599"'))
	# dlvhex.output((2, 39, '"10.1597423 56.1677875"'))
	# dlvhex.output((2, 40, '"10.1596315 56.1678989"'))
	# dlvhex.output((2, 41, '"10.1593208 56.168202"'))
	# dlvhex.output((2, 42, '"10.1592569 56.1683434"'))
	# dlvhex.output((2, 43, '"10.1592501 56.168683"'))
	# dlvhex.output((2, 44, '"10.1598342 56.1686962"'))
	# dlvhex.output((2, 45, '"10.1609482 56.169129"'))
	# dlvhex.output((2, 46, '"10.1609947 56.1691916"'))
	# dlvhex.output((2, 47, '"10.160487 56.1694338"'))
	# dlvhex.output((2, 48, '"10.1605343 56.1694875"'))
	# dlvhex.output((2, 49, '"10.160593 56.1695508"'))
	# dlvhex.output((2, 50, '"10.1607016 56.1696834"'))
	# dlvhex.output((2, 51, '"10.1607683 56.1697981"'))
	# dlvhex.output((2, 52, '"10.1608533 56.1699453"'))
	# dlvhex.output((2, 53, '"10.1609045 56.1700896"'))
	# dlvhex.output((2, 54, '"10.1609413 56.1702547"'))
	# dlvhex.output((2, 55, '"10.1609431 56.1703174"'))
	# dlvhex.output((2, 56, '"10.1609459 56.1704109"'))
	# dlvhex.output((2, 57, '"10.1609456 56.1704413"'))
	# dlvhex.output((2, 58, '"10.1609163 56.1707096"'))
	# dlvhex.output((2, 59, '"10.1608817 56.1708147"'))
	# dlvhex.output((2, 60, '"10.1608691 56.1708552"'))
	# dlvhex.output((2, 61, '"10.1601795 56.1718232"'))
	# dlvhex.output((2, 62, '"10.1596654 56.1724476"'))
	# dlvhex.output((2, 63, '"10.1595694 56.1725635"'))
	# dlvhex.output((2, 64, '"10.1595472 56.1726068"'))
	# dlvhex.output((2, 65, '"10.1594131 56.1727663"'))
	# dlvhex.output((2, 66, '"10.1590134 56.1732754"'))
	# dlvhex.output((2, 67, '"10.1582524 56.1741949"'))
	# dlvhex.output((2, 68, '"10.1577483 56.1748376"'))
	# dlvhex.output((2, 69, '"10.156838 56.176512"'))
	# dlvhex.output((2, 70, '"10.1569276 56.1774447"'))
	# dlvhex.output((2, 71, '"10.1570353 56.1782793"'))
	# dlvhex.output((2, 72, '"10.1570876 56.1787222"'))
	# dlvhex.output((2, 73, '"10.1570911 56.1787686"'))
	# dlvhex.output((2, 74, '"10.1570304 56.1787674"'))
	# dlvhex.output((2, 75, '"10.1569267 56.1787706"'))
	# dlvhex.output((2, 76, '"10.1569826 56.1792047"'))
	# dlvhex.output((2, 77, '"10.1569531 56.1793233"'))
	# dlvhex.output((2, 78, '"10.1569028 56.1794392"'))
	# dlvhex.output((2, 79, '"10.1568399 56.1795244"'))
	# dlvhex.output((2, 80, '"10.1558909 56.1800301"'))
	# dlvhex.output((2, 81, '"10.1558225 56.1800581"'))
	# dlvhex.output((2, 82, '"10.1554222 56.1802848"'))
	# dlvhex.output((2, 83, '"10.1550594 56.1805326"'))
	# dlvhex.output((2, 84, '"10.1547994 56.1807586"'))
	# dlvhex.output((2, 85, '"10.1547341 56.1810823"'))
	# dlvhex.output((2, 86, '"10.154848 56.1812151"'))
	# dlvhex.output((2, 87, '"10.1548734 56.1819319"'))
	# dlvhex.output((2, 88, '"10.1549346 56.182166"'))
	# dlvhex.output((2, 89, '"10.1553236 56.1830948"'))
	# dlvhex.output((2, 90, '"10.1558021 56.1842618"'))
	# dlvhex.output((2, 91, '"10.1562097 56.1852639"'))
	# dlvhex.output((2, 92, '"10.1568939 56.1868911"'))
	# dlvhex.output((2, 93, '"10.1566466 56.1869406"'))
	# dlvhex.output((2, 94, '"10.1564011 56.1869999"'))
	# dlvhex.output((2, 95, '"10.1563251 56.1870206"'))
	# dlvhex.output((2, 96, '"10.15575 56.1871692"'))
	# dlvhex.output((2, 97, '"10.1563119 56.1879096"'))
	# dlvhex.output((2, 98, '"10.1572865 56.1895898"'))
	# dlvhex.output((2, 99, '"10.1531165 56.190268"'))
	# dlvhex.output((2, 100, '"10.1529482 56.190365"'))
	# dlvhex.output((2, 101, '"10.1505434 56.1911485"'))
	# dlvhex.output((2, 102, '"10.1500323 56.1916471"'))
	# dlvhex.output((2, 103, '"10.1405124 56.1977658"'))
	# dlvhex.output((2, 104, '"10.1403877 56.1978381"'))
	# dlvhex.output((2, 105, '"10.1381018 56.1994173"'))
	# dlvhex.output((2, 106, '"10.1314511 56.2060548"'))
	# dlvhex.output((2, 107, '"10.1312607 56.2066419"'))
	# dlvhex.output((2, 108, '"10.1313095 56.2073198"'))
	# dlvhex.output((2, 109, '"10.1315315 56.2073744"'))
	# dlvhex.output((2, 110, '"10.1329586 56.2077696"'))
	# dlvhex.output((2, 111, '"10.13555 56.2115435"'))
	# dlvhex.output((2, 112, '"10.1357732 56.2117129"'))
	# dlvhex.output((2, 113, '"10.1438668 56.213459"'))
	# dlvhex.output((2, 114, '"10.1408531 56.2145466"'))
	# dlvhex.output((2, 115, '"10.1388075 56.2152141"'))
	# dlvhex.output((2, 116, '"10.1384614 56.2153322"'))
	# dlvhex.output((2, 117, '"10.1285943 56.2194295"'))
	# dlvhex.output((2, 118, '"10.128916 56.2196172"'))
	# dlvhex.output((2, 119, '"10.1289722 56.2196582"'))
	# dlvhex.output((2, 120, '"10.1207047 56.2247291"'))
	# dlvhex.output((2, 121, '"10.1193529 56.2253971"'))
	# dlvhex.output((2, 122, '"10.1177001 56.2265038"'))
	# dlvhex.output((2, 123, '"10.1173131 56.2262719"'))
	# dlvhex.output((2, 124, '"10.1171296 56.2261545"'))