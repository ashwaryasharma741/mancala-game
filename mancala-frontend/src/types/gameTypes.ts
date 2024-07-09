export enum GameStatus {
    NOT_STARTED='NOT_STARTED',
    IN_PROGRESS='IN_PROGRESS',
    FINISHED='FINISHED'
}

export interface GameState {
    status: GameStatus;
    winner: string;
}

export interface MancalaResponse {
    pits: Array<number>;
    game_state: GameState;
    current_player: number;
}

export interface MancalaGameState {
    player_1_pits: Array<number>;
    player_1_mancala: number;
    player_2_pits: Array<number>;
    player_2_mancala: number;
    current_player: number;
}
