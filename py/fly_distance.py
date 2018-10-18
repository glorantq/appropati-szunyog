import sys

if len(sys.argv) < 7:
    print("Need: trainASpeed, trainBSpeed, trainsDistance, flySpeed, windSpeed, accuracy")
    exit()

trainASpeed = float(sys.argv[1])
trainBSpeed = float(sys.argv[2])
trainsDistance = float(sys.argv[3])

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
    
    flyX += flyMovement

    trainsDistance -= abs(trainAMovement)
    trainsDistance -= abs(trainBMovement)

    totalDistance += abs(flyMovement)
    totalTripTime += timestep

if totalDistance != totalDistance or totalDistance < 0:
    print("The fly is blown away by the wind! (-)")
    exit()

print("{} km, simple (no wind speed): {} km".format(round(totalDistance), round(
    originalTrainsDistance / (trainASpeed + trainBSpeed) * flySpeed)))
