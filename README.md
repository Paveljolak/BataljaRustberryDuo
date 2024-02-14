DATE: 13/02/2024 - Zhivko changed the way we read the gameState, by using hashmap. Genius move saves a lot of code. And we made a bot with delay and eventually flanks the enemies by shooting the furthest one.

WORK: Created Bot3, a bot with delay the smaller the planet the bigger the delay since we need more time to regenerate our fleet_size. Given that most people (just a guess) would use the attack closest planet strategy, this bot uses the strategy where when all of the netural planets are gone, it counts ~20 turns (this value can be changed) and it shoots the planet that is globally furthest from ours. It goes through all distances of enemy planets given our planets, finds the closest one, and then calculates which one of our planet is furthest from this one enemy planet that is closest one of our planets, and then shoots the furthest planet from our chosen planet. Meaning globally we would shoot with our planet in one corner an enemy planet in the opposite corner.

PROBLEMS: Some value tweeking is needed, even so if the bot is tested against the closest planet strategy it is not tested in public games. 

NEXT: See how the bot does in public games tweek some values and see improvements. Enhance the strategy, where we would have the planets in the back would regenerate more than the planets in the front. 


----

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
