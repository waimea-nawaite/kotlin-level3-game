# Results of Testing

The test results show the actual outcome of the testing, following the [Test Plan](test-plan.md)

## The Map of SUBPOD:

![MapOfSUBPOD.png](../../src/main/resources/images/MapOfSUBPOD.png)
---

## Movement - Valid

I will test to see if the player can move around the map freely

### Test Data Used

I will try to move North, East, South, and west around the map

### Test Result

![MovementTest.gif](screenshots/MovementTest.gif)

As you can see the game lets me move north, east, south, and west

---

## Map Boundary - Boundary

I will test the map boundaries to stop the player from moving out the map

### Test data Used

I will try go past all the edges of the map by moving the player to each side and try going North, East, South, and West

### Test Result

![BoundarysTest.gif](screenshots/BoundarysTest.gif)

When I go to the edge of each side of my map the button disables stopping the player from going off the edge of the map

---

## Movement into blocked areas - Invalid

I will test to see when the user hits a blocked path

### Test Data Used

I will go to a blocked path and try to move onto the path

### Test Result

![BlockedPaths.gif](screenshots/BlockedPaths.gif)

As you can see when i try to go north there is a blocked path so it disables the button and doesnt let me to north, I
then try to
go around to the left side of the blocked path and the east button gets disabled stopping me from moving onto the path.

---

## PDA collection - Valid

I will test to see if PDAS are randomly spawning in diferent lifepods each game and that when the user collects one

### Test data Used

I will try a couple games to see if the PDAS are in new spots

### Test Result

![PDACollectionRandom.gif](screenshots/PDACollectionRandom.gif)

As you can see I start the game with 0 PDAS and as I move to the first lifepod it adds 1 PDA lucky me. After I restart
the game
I go back to having 0 PDAS and move to the same spot as before but I didn't get a PDA until I moved to the next lifepod.

---

## Oxygen Draining - Valid

I will test to see if oxygen drains with each movement

### Test data Used

I will try moving in each direction to see if the oxygen drains with each movement all by the same amount

### Test Result

![OxygenDraining.gif](screenshots/OxygenDraining.gif)

As the gif shows I start with 100 oxygen and it drains with each movement by 5 in each direction

---

## Winning the game - Valid

I will test to see if the game can be winnable

### Test Data Used

I will test to see when the player collects all the PDAs around the map does the game see that and let the player win

### Test Result

![WinningTheGame.gif](screenshots/WinningTheGame.gif)

As you can see I had two PDAs and as I went and collected the last one the window popped up asking you
would like to play again and i tried it again but pressed no and it closed my game.

---

## Losing the game - valid

I will test to see if the game sees that the player has lost

### Test Data Used

I will test to see when the player runs out of oxygen that the game recognises this

### Test Result

![LosingTheGame.gif](screenshots/LosingTheGame.gif)

As you can see when I run out of oxygen the game ends asking if the user would like to try again or if not it will end