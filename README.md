# mancala-game

### Backend set up:

1. ktor-mancala-api folder contains API based on Kotlin with Ktor.
2. In IntelliJ IDEA, click on run button to start the application.
3. Navigate to http://0.0.0.0:8080/mancala to check if the application is up and running.
4. The project contains 3 APIs with following endpoints -
   1. GET /mancala - Fetches the state of mancala game.
   2. POST /play - Plays the move of current player and accordingly updates the mancala game state.
   3. POST /reset - Resets the game state.


### Frontend set up:

1. mancala-frontend folder contains Mancala game UI based on React.
2. Run `npm i` followed by `npm start`
