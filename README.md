# TRPG
This repository contains code from an incomplete personal side-project I worked on between 2011 and 2013 in my undergraduate days.  It contains the beginning of a game engine which I intended to use to create a video game.  Due to other time obligations which arose, I halted work in early 2013 and when I returned the next year, I realized that it was such an unscalable unmaintainable mess that I would be better off starting from scratch rather than trying to salvage what I had already written.

I have decided post the project to github primarily for personal archival purposes.  Although the code can be compiled using
```
make exe
```
in the top level directory, it does not run very well and I do not recommend anyone try to use it for any projects.

Nonetheless, this project represented an immensely valuable learning experience for me.

It was my first personal project of non-trivial scale (meaning significantly larger than something that one might do as a course project).  Although it contains a great number of novice engineering mistakes which contributed to its unmaintainability, I learned a great deal about best practices in scalable software design the hard way as a result of these mistakes and all of my future work, both in scientific computing and in my other personal projects have benefited from my experiences with this project.

## Dependencies

This game engine is built on top of JMonkey Engine 3, an open source 3D game engine for Java in turn based on the lwjgl library with OpenGL.  There are no other dependencies other than the Java standard libraries.  For convenience, the version of the JMonkey Engine 3 jars used are included in this repository.  Since this repository is for code archival purposes, I have not included any of the test assets (such as character sprites) as many of these were based on characters and regions from the video game I wished to make and there is a possibility that I still might want to make it someday.

## Description

This engine was intended to assist with the development of a particular game which I had planned for quite a long time.  The game in question was to be a story-driven (semi-hard) science fiction RPG with a tactical turn-based battle system somewhere in between Final Fantasy Tactics and more traditional tabletop RPGs.  While navigating the map, the player controls a single character (the designated "party leader") freely in a top-down view while the other characters present follow behind.  Once an "initiative" mode is triggered (e.g. if the players encounter an enemy, are spotted, are trying to sneak past something, or even in certain time-dependent puzzles etc.) the characters' movements are limited to a grid and actions occur in turns.

This engine was built assuming that 3D graphics would be used for the world and for certain easily-modeled static objects while "billboarded" sprites would be used for characters and other entities with more complicated animations.  I experimented with several systems of producing these sprites but had yet to decide upon a final pipeline for producing ones to use in the final game.

## Features

At the time that I discontinued work on this project, these were some of the features that had been implemented:

1) World Editor:  Supports placement of static objects, basic entities and other "events."  Also supports limited terrain mesh manipulation tools were included.

2) Initiative Mode: The battle system is basically functional for a very few simple actions.

3) Character Sprite Animation: A basic system for basic character sprites with walking and idle animations; these are used both in navigation and initiative mode.

4) Pathfinding and AI: basic versions of both implemented for both other party members in map navigation mode and for enemies in initiative mode.  Characters keep track of other entities in initiative mode and will automatically turn their focus to follow the movements of their targets when they move.  Unfortunately the pathfinding algorithms are not handled very efficiently.

5) Event/Scripting system: A very basic scripting system for occurrences in map navigation mode with an interface similar to those used in Enterbrain's RPG Maker games.

6) Journal/Quest: A system for keeping track of important events in the main story and sidequests.  All "quest" variables and flags are tied to this system.

7) Map formats: Developed a basic format for storing game maps.

8) Game Saving system

9) Multithreaded: Two primary threads, both synchronized to the game's refresh cycle, are used along with other "helper" threads not tied to the game's refresh cycle which are occasionally forked (e.g. for pathfinding and event processing - a huge mistake, I think looking back) and joined.  As crude as the system is, I learned a lot about concurrency issues and thread safety from hands-on experience here.
