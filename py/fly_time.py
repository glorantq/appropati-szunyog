import sys
import math

if len(sys.argv) < 7:
    print("Need: trainASpeed, trainBSpeed, trainsDistance, flySpeed, windSpeed, accuracy, targetDistance")
    exit()

trainASpeed = float(sys.argv[1])
trainBSpeed = float(sys.argv[2])
trainsDistance = float(sys.argv[3])
targetDistance = float(sys.argv[7])

originalTrainsDistance = trainsDistance

flySpeed = float(sys.argv[4])
windSpeed = float(sys.argv[5])

totalDistance = 0
totalTripTime = 0

if abs(flySpeed) < abs(windSpeed):
    print("The fly is blown away by the wind!")  # Ha a szúnyog lassabban repül mint a szél, el fogja fújni
    exit()

if abs(flySpeed) == abs(windSpeed):
    print("The fly can't fly in one direction!")  # Ha a szúnyog annyira gyors mint a szél, egy helyben fog állni
    exit()

if abs(flySpeed) <= abs(trainASpeed) or abs(flySpeed) <= abs(trainBSpeed) or abs(flySpeed - abs(windSpeed)) - abs(
        trainASpeed) <= 0 or abs(flySpeed - abs(windSpeed)) - abs(trainBSpeed) <= 0:
    print("The fly can't reach the other train!")  # Ha a vonatok gyorsabbak, lehagyják a szúnyogot
    exit()

flyDirection = 1

trainAX = 0
trainBX = trainsDistance
flyX = 0

timestep = 1 / int(sys.argv[6])

while trainsDistance > 0:
    trainAMovement = trainASpeed * timestep
    trainBMovement = -trainBSpeed * timestep

    relativeWindSpeed = windSpeed * flyDirection
    if flyX >= trainBX:
        flyDirection = -1

    if flyX <= trainAX:
        flyDirection = 1

    flyMovement = (flySpeed + relativeWindSpeed) * flyDirection * timestep

    trainAX += trainAMovement
    trainBX += trainBMovement

    trainsDistance -= abs(trainAMovement)
    trainsDistance -= abs(trainBMovement)

    totalDistance += abs(flyMovement)
    totalTripTime += timestep

x = totalTripTime / totalDistance  # Szúnyog átlagsebessége

startDistance = totalDistance - targetDistance  # Távolság amit meg kellene tennie (amikor ezt elérné, akkor indul)
startTime = x * startDistance  # Idő ami alatt megtenné az indulási távolságot

if startTime == totalTripTime or startDistance == 0:
    print("The fly doesn't start at all!")  # Ha a szúnyog 0 km távolságot akar megtenni, nem kell elindulnia
    exit()

if startDistance < 0:
    print(
        "The fly will never cover this distance")  # Ha a szúynog többet akar repülni mint amennyit ezen a távolságon tud
    exit()

startMinutes = startTime * 60  # Az idő óra (sebességek km/h)
startSeconds = (startMinutes - math.floor(startMinutes)) * 60  # Perceknek a tört része

print("The fly has to start {} minutes {} seconds after the trains ({} minutes total trip time)".format(
    round(startTime * 60), round(startSeconds), round(totalTripTime * 60)))
