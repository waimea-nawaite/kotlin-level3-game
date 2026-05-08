# Plan for Testing the Program

The test plan lays out the actions and data I will use to test the functionality of my program.

Terminology:

- **VALID** data values are those that the program expects
- **BOUNDARY** data values are at the limits of the valid range
- **INVALID** data values are those that the program should reject

---

## Movement - Valid

I will test to see if the player can move around the map freely

### Test Data To Use

I will try to move North, East, South, and west around the map

### Expected Test Result

It should let the user move North, East, South, and West to a new index and should display a discription of where the
user is.

---

## Movement into blocked areas - Invalid

I will test to see when the user hits a blocked path

### Test Data To Use

I will go to a blocked path and try to move onto the path

### Expected Test Result

It should not let the user move onto the blocked path and disable the button for e.g. East (if the blocked path is to
the east)

---

## Map Boundary - Boundary

I will test the map boundaries to stop the player from moving out the map

### Test Data To Use

I will try go past all the edges of the map by moving the player to each side and try going North, East, South, and West

### Expected Test Result

It shouldn't let me go past the edge and disable the buttons to show where the boundary is

---

## PDA collection - Valid

I will test to see if PDAS are randomly spawning in different lifepods each game and that when the user collects one

### Test Data To Use

I will try a couple games to see if the PDAS are in new spots

### Expected Test Result

Each PDA is in a new spot each game with a limit of 4 and that when the player collects the PDA it +1 to the score

---

## Oxygen Draining - Valid

I will test to see if oxygen drains with each movement

### Test Data To Use

I will try moving in each direction to see if the oxygen drains with each movement all by the same amount

### Expected Test Result

When the user moves the oxgyen should drain by a certain amount until 0

---

## Winning the game - Valid

I will test to see if the game can be winnable

### Test Data To Use

I will test to see when the player collects all the PDAs around the map does the game see that and let the player win

### Expected Test Result

I should expect to see a window pop up saying congrats you won with an option to play again

---

## Losing the game - valid

I will test to see if the game sees that the player has lost

### Test Data To Use

I will test to see when the player runs out of oxygen that the game recognises this

### Expected Test Result

The game should end asking if the user would like to play again or not