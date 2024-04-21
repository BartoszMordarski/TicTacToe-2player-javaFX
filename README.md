# Tic Tac Toe Game

## Project Description

This project is a multiplayer Tic Tac Toe game developed as part of the Java Programming course at the Faculty of Electrical and Computer Engineering. The game allows users to play together, where each player can be a client connected to the server. This project aims to understand networking programming and interprocess communication. Additionally, it helps to develop skills in creating graphical interfaces.

## Functionalities

- Implementation of a server that handles connections from multiple clients, manages the game state, and coordinates actions between players.
- Creation of a graphical interface for the player, which allows joining the game, making moves, receiving information from the server, and presenting the current game state.
- Implementation of communication mechanisms between the client and the server. Use of data streams and serialized objects to transmit information about moves, game state, and messages.
- Introduction of mechanisms for handling different stages of the game, such as joining a player, starting a round, making a move, ending a game, or leaving a game.
- Use of JDBC technology to handle the Oracle database.

## Technologies Used

- **Java**: Java offers security, thread support, and a rich programming community with ready-made libraries.
- **JavaFX**: Used to create a graphical interface for the client, allowing the creation of simple and aesthetic window applications.
- **Network Sockets (ServerSocket and Socket)**: Communication mechanisms between the server and clients are implemented using network sockets.
- **Object Serialization**: Used to transmit information between the client and the server, allowing for simple and efficient data transmission.
- **Multithreading**: Threads are used to handle multiple clients simultaneously on the server, allowing for multiple games to be handled at the same time.
- **Oracle Database**: The Oracle database was used to store user-related data. The choice of this database is due to its reliability, scalability, and rich functionality in the area of data management.

## General Operation

1. The client connects to the server and initializes the connection.
2. The server creates a new thread assigned to the newly opened socket, which will handle communication with the given client from now on.
3. The client provides their username and sends a request to join the game.
4. The server assigns the player to the game, informs him about the opponent (if there is one), and starts the game.
5. Clients make moves, and the server updates the game state and sends feedback.
6. The game ends when one of the players wins or resigns.
7. The possibility of restarting the game or ending it.

## User Documentation

- After starting the server, run TicTacToeApplication to launch the graphical interface of the welcome view. Then enter your username and select Join Game to initiate the game for the first user. You will receive a message waiting for the second player to connect.

- Run TicTacToeApplication on the client side for the second player to join the game and proceed as above. After selecting Join Game, users enter the game.

- The first move belongs to the player whose symbol is 'O'. He can click the appropriate button to draw his symbol there. The player with the 'X' symbol waits for the opponent's move and has no possibility to click any of the buttons.

- After the player2 makes a move, the game board is updated for both players and now it's the opponent's turn.

- The player2 with the 'O' symbol wins. Both players display the appropriate messages, and then the players are added to the database and their balance of victories or defeats is updated.

- Now players can leave the game using Quit Game, or play another game by selecting Restart Game. Then the welcome window will be displayed again with the username field already filled in, such as they gave earlier.

- If one of the players leaves the game using Quit Game, his opponent will receive an appropriate message.

## Conclusion

The main goal of the project was to create a Tic Tac Toe game using the client-server architecture, using Java. The use of multithreading allowed for simultaneous handling of multiple clients. In the application, data streams - textual and object - were used for communication between the client and the server. I decided to communicate using the Command class, which allowed for easy, readable, and systematized communication facilitating understanding and reading the code. Additionally, I used JDBC technology to enable communication between the application and the Oracle database.

In order to implement the graphical user interface, I used JavaFX technology. The introduction of GUI elements significantly facilitates the use of the program and improves the gaming experience. Additionally, I took care of error handling and resource release such as data streams and sockets. Such a solution makes the system stable and safe, which is crucial in communication projects.
