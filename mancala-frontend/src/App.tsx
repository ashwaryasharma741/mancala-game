import React, { useCallback, useEffect, useState } from 'react';

import './App.css';
import Pit from './components/pit/Pit';
import API_URLS from './config/appConfig';
import { REQUEST_OPTIONS } from './constants/apiConstants';
import { GameState, GameStatus, MancalaGameState, MancalaResponse } from './types/gameTypes';
import { createState } from './utils/mancalaUtil';

function App() {
  const [state, setState] = useState<MancalaGameState>({
    player_1_pits: [],
    player_1_mancala: 0,
    player_2_pits: [],
    player_2_mancala: 0,
    current_player: 0,
  });
  const [status, setStatus] = useState<GameState>({status: GameStatus.NOT_STARTED, winner: ''});
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(API_URLS.FETCH_MANCALA);
        const result: MancalaResponse = await response.json();
        setState(createState(result));
        setStatus(result.game_state);
      } catch (e: any) {
        console.error('Error', e);
        setError(e);
      }
    }
    fetchData();
  }, []);

  const onPitClick = useCallback(async (idx: number) => {
    try {
      const response = await fetch(API_URLS.PLAY_MOVE, {...REQUEST_OPTIONS, body: JSON.stringify({ index: idx })});
      const result: MancalaResponse = await response.json();
      setState(createState(result));
      setStatus(result.game_state);
    } catch (e: any) {
      console.error('Error', e);
      setError(e);
    }
  }, []);

  const onResetClick = useCallback(async () => {
    try {
      const response = await fetch(API_URLS.RESET, REQUEST_OPTIONS);
      const result: MancalaResponse = await response.json();
      setState(createState(result));
      setStatus(result.game_state);
    } catch (e: any) {
      console.error('Error', e);
      setError(e);
    }
  }, []);

  return (
    <div className="App">
      <button className='reset-btn text-white' onClick={onResetClick}> Reset </button>

      { status?.status !== GameStatus.FINISHED && <div className='text-center text-white current-status'>
        Current Player: {state?.current_player + 1}
      </div>
      }
      { status?.status === GameStatus.FINISHED && <div className='text-center text-white current-status'>
         {Number(status.winner) > -1 && <span>Winner is: Player {Number(status.winner) + 1}</span>}
         {Number(status.winner) === -1 && <span>It's a tie!</span>}
      </div>
      }
      <div className='container'>
        <div className='mancala-board text-white'>
          <div className='main-pit'>
            {state.player_1_mancala}
          </div>
          <div className='mid-section section'>
            <div className='text-center'>Player 1</div>
            <div className='center-grid-reverse'>
              {state?.player_1_pits?.map((count, idx) => {
                return (
                  <Pit key={'player_0_' + (idx + 1)}
                    count={count}
                    idx={idx}
                    clickHandler={() => onPitClick(idx)}
                  />)
              }) }
            </div>
            <div className='center-grid'>
              {state?.player_2_pits?.map((count, idx) => {
                return (
                  <Pit key={'player_1_' + (idx + 1)}
                    count={count}
                    idx={idx}
                    clickHandler={() => onPitClick(7 + idx)}
                  />)
              })}
            </div>
            <div className='text-center'>Player 2</div>
          </div>
          <div className='main-pit'>
            {state.player_2_mancala}
          </div>
        </div>
      </div>
    </div >
  );
}

export default App;
