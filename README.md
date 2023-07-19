# Dungeon Mania
> The source code is maintained on GitLab and could be published after 2025 due to UNSW policy. </br>
Feel free to reach out to me to review the code in person.

A 2D adventure game with modified maps and entities. <br/>
[Checkout UML diagram of my implementation here](https://drive.google.com/file/d/1uJqEFKwZRKXHGMQgYjq-tQ8MB0pi_Rb3/view?usp=sharing)

https://github.com/PhotKosee/dungeon-mania/assets/114990364/d60e9f0d-192c-4948-a8c7-fa3d798e2db2

## OOP
Modifying and implementing new functionality from a given MVP

- Refactored the code to improve the quality of the design from code smell
- Implemented entities: Assassin, Swamp Tile, Sun Stone, Sceptre, Midnight Armour, Light Bulb (off), Light Bulb (on), Wire, Switch Door

This project mainly focuses on applying design patterns to provide a sufficient solution.

- Factory method for creating each of the entities
- Strategy pattern to group up entities with similar behaviors
- Composite pattern to stack up armor and attack damages from all types of equipment
- State pattern to handle each type of potion
- Abstract factory to create goals according to the map specification

