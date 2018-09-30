import sys
import math

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

if abs(flySpeed) <= abs(trainASpeed) or abs(flySpeed) <= abs(trainBSpeed) or abs(flySpeed - abs(windSpeed)) - abs(trainASpeed) <= 0 or abs(flySpeed - abs(windSpeed)) - abs(trainBSpeed) <= 0:
    print("The fly can't reach the other train!")  # Ha a vonatok gyorsabbak, lehagyják a szúnyogot
    exit()

lastFlySpeed = 0

for i in range(0, int(sys.argv[6])):
    relativeWindSpeed = windSpeed if i % 2 == 0 else -windSpeed # Pozitív: jobbra, negatív: balra
    relativeFlySpeed = flySpeed + relativeWindSpeed # Szúnyog sebessége ebben a fordulatban

    lastFlySpeed = relativeFlySpeed
    tripTime = trainsDistance / relativeFlySpeed # Ehhez a körhöz szükséges idő
     
    totalTripTime += tripTime # Teljes repülési idő
    
    trainsDistance -= tripTime * trainASpeed # Vonatok új távolsága
    trainsDistance -= tripTime * trainBSpeed # Vonatok új távolsága

    totalDistance += tripTime * relativeFlySpeed # Ennek a körnek a távolsága

if totalDistance != totalDistance or totalDistance < 0:
    print("The fly is blown away by the wind! (-)")
    exit()

print("{} km, simple (no wind speed): {} km".format(round(totalDistance), round(originalTrainsDistance / (trainASpeed + trainBSpeed) * flySpeed)))
print("The fly was flying for: {} hours, last speed: {}".format(round(totalTripTime), round(lastFlySpeed)))
