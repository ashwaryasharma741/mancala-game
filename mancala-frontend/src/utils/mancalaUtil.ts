import { MancalaGameState, MancalaResponse } from "../types/gameTypes";

export function createState(result: MancalaResponse): MancalaGameState {
    return {
        player_1_pits: result.pits.slice(0, 6),
        player_1_mancala: result.pits[6],
        player_2_pits: result.pits.slice(7, 13),
        player_2_mancala: result.pits[13],
        current_player: result.current_player,
      }
};