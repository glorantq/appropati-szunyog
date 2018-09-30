from graphics import *
import sys

if len(sys.argv) != 8:
	print("Need: trainASpeed, trainBSpeed, trainsDistance, flySpeed, windSpeed, fps, startSeconds")
	exit()

trainASpeed = int(sys.argv[1])
trainBSpeed = int(sys.argv[2])
flySpeed = int(sys.argv[4])
windSpeed = int(sys.argv[5])
startSeconds = float(sys.argv[7])

kmToPixelNum = 5

fps = int(sys.argv[6])

timestep = 1 / fps

def kmToPx(km):
	return km * kmToPixelNum
	
def pxToKm(px):
	return px / kmToPixelNum

def main():
	time = 0
	trainsDistance = int(sys.argv[3])
	
	window = GraphWin("Szunyogfitnesz", kmToPx(trainsDistance) + 100, 300)
    
	trainA = Rectangle(Point(0, window.getHeight() / 2 - 25), Point(50, window.getHeight() / 2 + 25))
	trainA.setFill("red")
	trainA.draw(window)
    
	trainB = Rectangle(Point(kmToPx(trainsDistance) + 50, window.getHeight() / 2 - 25), Point(kmToPx(trainsDistance) + 100, window.getHeight() / 2 + 25))
	trainB.setFill("blue")
	trainB.draw(window)
    
	fly = Circle(Point(50, window.getHeight() / 2), 10)
	fly.setFill("green")
	fly.draw(window)
	
	flyDirection = 1
	
	trainAX = 50
	trainBX = kmToPx(trainsDistance) + 50
	flyX = 50
	
	distanceTravelled = 0
	
	debugLabel = Text(Point(window.getWidth() / 2, 40), "A: {} km/h\nB: {} km/h\nFly: {} km/h\nWind: {} km/h".format(trainASpeed, trainBSpeed, flySpeed, windSpeed))
	debugLabel.draw(window)
	
	while trainsDistance > 0:
		trainAMovement = kmToPx(trainASpeed) * timestep
		trainBMovement = -kmToPx(trainBSpeed) * timestep
		
		if startSeconds <= time:
			relativeWindSpeed = windSpeed * flyDirection
			
			if flyX >= trainBX:
				flyDirection = -1
				
			if flyX <= trainAX:
				flyDirection = 1
				
			flyMovement = kmToPx(flySpeed + relativeWindSpeed) * flyDirection * timestep
			
			flyX += flyMovement
			fly.move(flyMovement, 0)
			
			distanceTravelled += pxToKm(abs(flyMovement))
		else:
			fly.move(trainAMovement, 0)
			flyX += trainAMovement
		
		trainAX += trainAMovement
		trainBX += trainBMovement
		
		trainA.move(trainAMovement, 0)
		trainB.move(trainBMovement, 0)
		
		trainsDistance -= abs(pxToKm(trainAMovement))
		trainsDistance -= abs(pxToKm(trainBMovement))
		
		time += timestep
		
		update(fps)
	
	resultText = Text(Point(window.getWidth() / 2, window.getHeight() - 10), "The fly travelled {} km!".format(round(distanceTravelled)))
	resultText.draw(window)
	
	print("Travelled {} km!".format(distanceTravelled))
	
	window.getMouse()
	window.close()

main()
