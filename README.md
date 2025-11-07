# Smart-Counter
A Minecraft fabric mod that counts redstone ticks, game ticks, and real-life second delays of redstone component lines for you.

## How to use
### Tick Counter
1. Type `/tickcounter` in the chat to enable the **tick** counter. All interactions (right-click and left-click) with blocks are disabled so you don't accidentally change repeater ticks while in this mode.  
2. Right-click on any redstone component to count its tick delay. You can click on multiple consecutive components to add up their ticks together. Special-cases such as two consecutive pistons resulting in an extra game tick are considered.
3. Left-click to reset the tick counter back to 0 or...  
4. Type `/tickcounter` again to reset the counter to 0, disable the tick counter, and re-enable block interactions.

### Node Counter
1. Type `/nodecounter` in the chat to enable the **node** counter. All interactions (right-click and left-click) with blocks are disabled so you don't accidentally change repeater ticks while in this mode.  
2. Right-click on any **redstone dust wire** to set a new node. Set at least two nodes.  
3. Activate the redstone line that changes the power-level of the active nodes. You'll get a message in chat to tell you the tick difference between the two nodes changing power levels.  
4. Left-click to remove all active nodes.  
5. Type `/nodecounter` again to disable the node counter, and re-enable block interactions.  

The download from:  
 - CurseForge https://www.curseforge.com/minecraft/mc-mods/tick-counter  
 - GitHub Releases: https://github.com/RealDevJG/Smart-Counter  

## Discord
**This is not a discord JUST for this mod, it's my whole development discord!**  
For any development related issues/bugs/suggestions please visit: https://discord.gg/b35rQvS  

## Latest changes (v2.1.0)
  - Added nodes you can place to measure the amount of ticks between the two nodes changing power levels (works on redstone dust only)  
  - Fixed chat spam when clearing tick counter with left-click  
  - The chat message of adding new tick counter ticks now only shows up only when new ticks have been added, not on every right-click  
  - Fixed bug with last release that meant commands were server-side. They are now client-side as intended  
