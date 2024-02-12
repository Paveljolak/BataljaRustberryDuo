DATE: 11/02/2024 - 12/02/2024 - Idk, how it works, idk why it works, bro just works. AI at it's finest.

WORK: Created bot2. Tried to make it so it shoots the closest targets. 

PROBLEMS: For some reason it works for some time and then it says index out of bound and the game breaks. Adding try and catch fixes the out of bounds issue but it makes the bot act weird where not all the planets are shooting and some are waiting. 

NEXT: Try to figure out where the calculations are wrong. Try to figure out if the arrays are synched (myPlanets index 1, with myFleets index 1, with myCoordinatesX and Y index 1 are for the same planet).

----

DATE: 11/02/2024 - Created and submitted Player1.0. Tweeks of this bot are required.

WORK: Player1.0: We find the bigest enemy planet we divide it by 2. If our planets have larger fleets than that value then we attack with them. We attack enemy or neutral planet with smallest fleet size. 

PROBLEMS: How much troops to send. Where exactly to send them so our planets don't get obliterated. 
- How to calculate which of our planets should shoot, which one should rest and which one would require reinforcements.
Possible solution: - Have a global balance - all planets have same ammount of fleet size, and they balance each other.
                   - Have support/attacker bots. Where weaker supports stronger or stronger supports weaker and they alternate.

NEXT: Make another bot which will focus the closest planets. Find solution to the problems of the first bot.

----

DATE: 09/02/2024 - 10/02/2024

WORK: Basic brainstorming, trying to read basic information in the game, trying to set up attacks(Which planet will attack).

PROBLEMS: After several turns, planets start shooting off screen.

NEXT: Work on which planet to attack. 

----  
