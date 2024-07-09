package example.com.model

import example.com.plugins.MAX_PIT_PLAYER_0
import example.com.plugins.MAX_PIT_PLAYER_1

object MancalaRepository {
    private val initArray = Array<Int>(6) { 4 } + Array<Int>(1) { 0 } + Array<Int>(6) { 4 } + Array<Int>(1) { 0 }
    private val gameState = GameState(GameStatus.NOT_STARTED, "")
    private var mancala = Mancala(initArray, 0, gameState)

    fun getCurrentState(): Mancala = mancala

    fun getStonesCount(index: Int) = mancala.pits[index]

    fun setStonesCount(index: Int, count: Int) = run {
        mancala.pits[index] = count
    }

    fun getGameStatus() = mancala.game_state.status

    fun setGameStatus(status: GameStatus) = run {
        mancala.game_state.status = status
    }

    fun getCurrentPlayer() = mancala.current_player

    fun isEveryPitEmpty(player: Int): Boolean = run {
        if (player == 0) {
            return mancala.pits.slice(0 until MAX_PIT_PLAYER_0).all { it == 0 }
        } else {
            return mancala.pits.slice(MAX_PIT_PLAYER_0 + 1 until MAX_PIT_PLAYER_1).all { it == 0 }
        }
    }

    fun handleRemainingStones() = run {
        mancala.pits[MAX_PIT_PLAYER_0] += mancala.pits.slice(0 until example.com.plugins.MAX_PIT_PLAYER_0).sum()
        mancala.pits[MAX_PIT_PLAYER_1] += mancala.pits.slice(example.com.plugins.MAX_PIT_PLAYER_0 + 1 until example.com.plugins.MAX_PIT_PLAYER_1).sum()
    }

    fun makeEveryPitEmpty(): Array<Int> = run {
        for (i in mancala.pits.indices) {
            if (i != 6 && i != 13) {
                mancala.pits[i] = 0
            }
        }
        return mancala.pits
    }

    fun determineNextPlayer(currentIndex: Int) = run {
        mancala.current_player = if ((mancala.current_player == 0 && currentIndex != MAX_PIT_PLAYER_0) || (mancala.current_player == 1 && currentIndex != MAX_PIT_PLAYER_1)) 1 - mancala.current_player else mancala.current_player
    }

    fun determineWinner() = run {
        var winner = 0
        if (mancala.pits[MAX_PIT_PLAYER_0] < mancala.pits[MAX_PIT_PLAYER_1]) {
            winner = 1
        } else if (mancala.pits[MAX_PIT_PLAYER_0] == mancala.pits[MAX_PIT_PLAYER_1]) {
            winner = -1
        }
        mancala.game_state.winner = winner.toString()
    }

    fun captureStonesFromOppositePit(currentIndex: Int) = run {
        val capturePit = if (mancala.current_player == 0) MAX_PIT_PLAYER_0 else MAX_PIT_PLAYER_1
        val oppositePitNumber = 12 - currentIndex
        mancala.pits[capturePit] += mancala.pits[oppositePitNumber] + 1
        mancala.pits[currentIndex] = 0
        mancala.pits[oppositePitNumber] = 0
    }

    fun resetMancala() = run {
        for (i in mancala.pits.indices) {
            if (i != 6 && i != 13) {
                mancala.pits[i] = 4
            } else {
                mancala.pits[i] = 0
            }
        }
        mancala.current_player = 0
        mancala.game_state = gameState
    }


}