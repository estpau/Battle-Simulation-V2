Homework team: Paula Esteban, Yadira Calzadilla, Pilar Alvarez, María Jerez.

The battle simulation is a confrontation between two teams that are conformed by warrior and/or wizards.

The Warrior has the following attributes: -Name -Hp: number representing the health points. -Stamina: number to represent a resource the warrior consumes to make an attack. -Strength: number to calculate how strong the warrior attack is.

The Wizard has the following attributes: -Name -Hp: number representing the health points. -Mana: number to represent a resource the wizard consumes to cast spells. -Intelligence: number to calculate how strong the wizard spells are.

The user must input the length of the parties. Each party has to have between 1 and 10 players.

Once the length is defined, the opponent team (team2) will be randomly created from a CSV file.

The user will be able to choose between two options to create their team (team 1):

Randomly: by pressing the letter “r”.

The names are generated from an array of names. The rest of the attributes are generated randomly between the possible range of numbers.

Customize: by pressing the letter “c”.

The user is able to choose which kind of character to include in their team by typing "warrior" o "wizard". The user is also able to choose all the characterists of the players. The name can be whatever, but if it is repeated, the word "Jr" will be add at the end of the name. The rest of the attibutes need to be add in the possible range of numbers. The range is specified when the user is asked to input the value.

The user will be able to choose between two options to run the battle:
Randomly: by pressing the letter “r”.
One player from each team is selected randomly to have a 1 Vs 1 combat. In this case, the user can not interact during the battle, just watch the result at the end.

Choosing the players to battle against each other: by pressing the letter “c”. In this case, the user has to choose the players of each team that are facing each other one by one. In order to do that, the list of the players of each team will be displayed and the user has to input the number of the players that wants to choose.

How battle works: The battle consists on many combats. The combat is a 1 Vs 1 characters figth and it has many differents rounds. The combats are shown in details round by round specifying the attack made by the characters and its effect in the other character. The combat lasted until one or both players died. When a player dies is sent to a graveyard and it is removed from the team (explained in point 6). The end of the battle happends when all the players of one of the teams are dead. Then, it is displayed which team is the winner.

The graveyard: At the end of the battle, the graveyard is displayed with all the players that have died during that battle, listed by teams.

Playing again: At the end of the battle, the user has the option to play a new battle by inputing the word "yes". If the user doesn't wants to play again, just need to write "no".