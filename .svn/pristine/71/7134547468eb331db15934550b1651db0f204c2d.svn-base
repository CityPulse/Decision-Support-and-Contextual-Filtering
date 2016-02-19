#script (python)

from gringo import Fun

import sys
import re
import time

class ParkingSpace(object):
	"""docstring for ParkingSpace"""
	def __init__(self, position):
		super(ParkingSpace, self).__init__()
		self.position = position
		self.availability = None
		self.totalCost = None
		self.walkingDistance = None

parking_spaces = None

def get_parking_spaces(point_of_interest, distance_range):
	try:
		starting_time_clock = time.clock()
		starting_time_time = time.time()

		initParkingSpaces(point_of_interest, distance_range)

		output = []

		if parking_spaces is not None :
			parkingNumber = 1
			for parking_space in parking_spaces :
				output.append((parkingNumber, parking_space.position))
				parkingNumber +=1

		print ("TIME get_parking_spaces: %f;%f" % (time.clock() - starting_time_clock, time.time() - starting_time_time))
		return output
	except Exception, e:
		print('ERROR get_parking_spaces: %s' % str(e))
		return []

def get_availability(parkingID):
	try:
		starting_time_clock = time.clock()
		starting_time_time = time.time()
		
		if parking_spaces is not None :
			if parkingID <= len(parking_spaces) :
				parking_space = parking_spaces[parkingID-1]
				initAvailability(parking_space)
				if parking_space.availability is not None :
					print ("TIME get_availability: %f;%f" % (time.clock() - starting_time_clock, time.time() - starting_time_time))
					return parking_space.availability
			else :
				raise ValueError('Wrong parkingID in get_availability')
		else :
			raise ValueError('get_availability is called before parking_spaces is initialized')
	except Exception, e:
		print('ERROR get_availability: %s' % str(e))
		return 1

def get_total_cost(parkingID, time_of_stay):
	try:
		starting_time_clock = time.clock()
		starting_time_time = time.time()
		print ("TIME get_total_cost: %f;%f" % (time.clock() - starting_time_clock, time.time() - starting_time_time))
		return 10
	except Exception, e:
		print('ERROR get_total_cost: %s' % str(e))
		return 10

def get_walking_distance(parkingID, point_of_interest):
	try:
		starting_time_clock = time.clock()
		starting_time_time = time.time()
		
		if parking_spaces is not None :
			if parkingID <= len(parking_spaces) :
				parking_space = parking_spaces[parkingID-1]
				initWalkingDistance(parking_space, point_of_interest)
				if parking_space.walkingDistance is not None :
					print ("TIME get_walking_distance: %f;%f" % (time.clock() - starting_time_clock, time.time() - starting_time_time))
					return parking_space.walkingDistance
			else :
				raise ValueError('Wrong parkingID in get_walking_distance')
		else :
			raise ValueError('get_walking_distance is called before parking_spaces is initialized')
	except Exception, e:
		print('ERROR get_walking_distance: %s' % str(e))
		return 100


def initParkingSpaces(point_of_interest, distance_range):
	global parking_spaces

	if parking_spaces is None :
		parking_spaces = []

		# output = []
		# output.append('10.2162612 56.159469')
		# output.append('10.2049 56.15679')
		# output.append('10.206 56.15561')
		# output.append('10.2138581 56.1532627')
		# output.append('10.21353 56.15659')
		# output.append('10.20596 56.14951')
		# output.append('10.20818 56.15441')
		# output.append('10.216667 56.15')
		# output.append('10.206 56.15561')
		# output.append('10.197 56.1527')
		# output.append('10.21149 56.14952')
		# output.append('10.21284 56.16184')
		# output.append('10.216667 56.15')

		# for o in output:
		# 	parking_space = ParkingSpace(o)
		# 	parking_spaces.append(parking_space)

		try:

			from SPARQLWrapper import SPARQLWrapper, JSON, XML, N3, RDF

			sparql = SPARQLWrapper("http://localhost:8890/sparql") #iot.ee.surrey.ac.uk
			queryString = 	"""
							PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>
							PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>
							PREFIX ct: <http://ict-citypulse.eu/city#>
							SELECT ?lat ?lon
							WHERE { ?foi ssn:hasLocation ?loc. ?loc geo:lat ?lat. ?loc geo:long ?lon. ?x a ct:ParkingVacancy. ?x ssn:isPropertyOf ?foi.}
							"""
			sparql.setQuery(queryString)

			# JSON example
			# print('*** JSON Result')
			sparql.setReturnFormat(JSON)
			results = sparql.query().convert()
			for result in results["results"]["bindings"]:
				position = result["lon"]["value"] + ' ' + result["lat"]["value"]
				parking_space = ParkingSpace(position.encode())
				parking_spaces.append(parking_space)
				# print(parking_spaces[-1].position)

		except Exception, e:
			raise IOError('Cannot get the static data: %s' % str(e))

		# # XML example
		# print '\n\n*** XML Example'
		# sparql.setReturnFormat(XML)
		# results = sparql.query().convert()
		# print results.toxml()

		# # N3 example
		# print '\n\n*** N3 Example'
		# sparql.setReturnFormat(N3)
		# results = sparql.query().convert()
		# print results

		# # RDF example
		# print '\n\n*** RDF Example'
		# sparql.setReturnFormat(RDF)
		# results = sparql.query().convert()
		# print results.serialize()

	else :
		print("initParkingSpaces already called")


def initAvailability(parking_space):
	global parking_spaces

	if parking_space.availability is None :

		try:
			coordinates = '{"latitude":' + str(float(parking_space.position.split()[1]) - 0.0001) + ',"longitude":' + str(float(parking_space.position.split()[0]) - 0.0001) + '}' + ',' + '{"latitude":' + str(float(parking_space.position.split()[1]) + 0.0001) + ',"longitude":' + str(float(parking_space.position.split()[0]) + 0.0001) + '}'
			# print(coordinates)
			request = '{"continuous":false,"route":[' + coordinates + '],"propertyTypes":["parking_availability"]}'
			# print(request)
		except Exception, e:
			parking_space.availability = 0
			# print(parking_space.availability)
			raise ValueError('position not correct %s' % str(e))

		try:
			from websocket import create_connection
			ws = create_connection("ws://localhost:8002/websockets/DataFederation")
			# print "Created connection"
			ws.send(request.encode('utf8'))
			# print "Sent"
			# print "Reeiving..."
			result =  ws.recv()
			# print "Received '%s'" % result

			# print(result)

			p = re.compile('\{"result":\{"parking_availability":\[(.+?)\]\}\}')
			m = p.match(result.decode('utf8'))
			if m :
				parking_space.availability = (int)(m.group(1).encode().strip('"'))
			else :
				parking_space.availability = 0
			# print(parking_space.availability)

			ws.close()
		except Exception, e:
			parking_space.availability = 0
			# print(parking_space.availability)
			raise IOError('Cannot connect to the DataFederation component: %s' % str(e))

	else :
		print("initPollutionValues already called")

def initWalkingDistance(parking_space, point_of_interest):
	global parking_spaces

	if parking_space.walkingDistance is None :

		sSplit = parking_space.position.strip('"').split()
		eSplit = point_of_interest.strip('"').split()

		if len(sSplit) != 2 :
			raise ValueError('Wrong starting point')
		if len(eSplit) != 2 :
			raise ValueError('Wrong ending point')

		srcXlon = sSplit[0]
		srcYlat = sSplit[1]
		trgXlon = eSplit[0]
		trgxlat = eSplit[1]
		
		# data validation
		float(srcXlon)
		float(srcYlat)
		float(trgXlon)
		float(trgxlat)

		costMode = 'distance'
		numberOfRoutes = 1

		from websocket import create_connection
		ws = create_connection("ws://localhost:7686")
		# print "Created connection"
		result =  ws.recv()
		# print "Received '%s'" % result
		ws.send((u"getCityRoute(%s,%s,%s,%s,%s,%d)" % (srcXlon,srcYlat,trgXlon,trgxlat,costMode,numberOfRoutes)).encode('utf8'))
		# print "Sent"
		for x in xrange(numberOfRoutes):
			# print "Reciving..."
			result =  ws.recv()
			# print "Received '%s'" % result
			p = re.compile('\w+;\w+;\w+;\w+\n(LINESTRING|MULTILINESTRING)\((.+)\);(\d+);(\d+);(\d+.\d+)')
			m = p.match(result.decode('utf8'))
			if m :
				# print(m.group(1),m.group(3),m.group(4),m.group(5))

				# if m.group(1) == u'LINESTRING' :
					# print(m.group(1))
				parking_space.walkingDistance = int(m.group(3))
			
		ws.close()

	else :
		print("initWalkingDistance already called")

#end.