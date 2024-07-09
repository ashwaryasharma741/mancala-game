package example.com.model

import kotlinx.serialization.Serializable

enum class GameStatus {
    NOT_STARTED, IN_PROGRESS, FINISHED
}

@Serializable
data class GameState(
    var status: GameStatus,
    var winner: String,
)

@Serializable
data class Mancala(
    var pits: Array<Int>,
    var current_player: Int,
    var game_state: GameState
)

@Serializable
data class MancalaPlay(
    val index: Int
)