package example.com.plugins

import example.com.model.*
import example.com.model.MancalaRepository.captureStonesFromOppositePit
import example.com.model.MancalaRepository.determineNextPlayer
import example.com.model.MancalaRepository.determineWinner
import example.com.model.MancalaRepository.getCurrentPlayer
import example.com.model.MancalaRepository.getCurrentState
import example.com.model.MancalaRepository.getGameStatus
import example.com.model.MancalaRepository.getStonesCount
import example.com.model.MancalaRepository.handleRemainingStones
import example.com.model.MancalaRepository.isEveryPitEmpty
import example.com.model.MancalaRepository.makeEveryPitEmpty
import example.com.model.MancalaRepository.resetMancala
import example.com.model.MancalaRepository.setGameStatus
import example.com.model.MancalaRepository.setStonesCount
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*

internal val LOGGER = KtorSimpleLogger("com.example.Mancala")
const val MAX_PIT_PLAYER_0 = 6
const val MAX_PIT_PLAYER_1 = 13

fun Application.configureRouting() {
    routing {
        get("/mancala") {
            try {
                call.respond(HttpStatusCode.OK, getCurrentState())
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("/play") {
            try {
                val payload = call.receive<MancalaPlay>()
                val pitNum = payload.index
                val currentPlayer = getCurrentPlayer()

                // Handle setting game status based on the current status.
                if (getGameStatus() == GameStatus.NOT_STARTED) {
                    setGameStatus(GameStatus.IN_PROGRESS)
                } else if (getGameStatus() == GameStatus.FINISHED) {
                    call.respond(HttpStatusCode.BadRequest, getCurrentState())
                    return@post
                }

                // Check if the move is within the allowed pits for the current player. If not, then don't proceed.
                if ((currentPlayer == 0 && pitNum >= MAX_PIT_PLAYER_0) || (currentPlayer == 1 && pitNum <= MAX_PIT_PLAYER_0)) {
                    LOGGER.trace("Message: This move is not allowed.")
                    call.respond(HttpStatusCode.BadRequest, getCurrentState())
                    return@post
                }

                var stones = getStonesCount(pitNum)

                // If no stone in the clicked pit, then don't proceed.
                if (stones == 0) {
                    LOGGER.trace("Message: No stones in the selected pit.")
                    call.respond(HttpStatusCode.BadRequest, getCurrentState())
                    return@post
                }

                setStonesCount(pitNum, 0) // Remove all stones from clicked pit.

                var index = pitNum
                while(stones > 0) {
                    index = (index + 1) % 14
                    // No stone should be put in opponent's big pit/mancala.
                    if ((currentPlayer == 0 && index == MAX_PIT_PLAYER_1) || (currentPlayer == 1 && index == MAX_PIT_PLAYER_0)) {
                        continue
                    }
                    setStonesCount(index, getStonesCount(index) + 1)
                    stones -= 1
                }

                // If last stone ends up in own empty pit, capture all stones from opponents pit.
                if (getStonesCount(index) == 1 &&
                    ((currentPlayer == 0 && index < MAX_PIT_PLAYER_0) || (currentPlayer == 1 && index > MAX_PIT_PLAYER_0 && index < MAX_PIT_PLAYER_1))) {
                    val oppositePitNumber = 12 - index
                    // Capture opponents pit's stones only if there are any.
                    if (getStonesCount(oppositePitNumber) > 0) {
                        captureStonesFromOppositePit(index)
                    }
                }

                // Determine whose turn it is based on where the last stone index ends up while distributing.
                determineNextPlayer(index)

                // If all pits are empty for either of the players, determine the winner.
                if (isEveryPitEmpty(currentPlayer) || isEveryPitEmpty(1 - currentPlayer)) {
                    // Place all the remaining stones in own pits in own mancala.
                    handleRemainingStones()

                    // Make all the non-mancala pits empty, update game status and determine the winner.
                    makeEveryPitEmpty()
                    setGameStatus(GameStatus.FINISHED)
                    determineWinner()
                    call.respond(HttpStatusCode.OK, getCurrentState())
                    return@post
                }

                call.respond(HttpStatusCode.OK, getCurrentState())
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("/reset") {
            try {
                resetMancala();
                call.respond(HttpStatusCode.OK, getCurrentState())
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
