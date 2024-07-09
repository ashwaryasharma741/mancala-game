import React, { act } from 'react';
import { fireEvent, render, screen } from '@testing-library/react';

import App from './App';
import { GameStatus } from './types/gameTypes';

const mockFetchResponse = (data: any) =>
  Promise.resolve({
    json: () => Promise.resolve(data),
  });

describe('App Component', () => {
  beforeEach(() => {
    global.fetch = jest.fn();
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  test('renders current player and mancala pits on initial load', async () => {
    const mockData = {
      pits: [4, 3, 2, 5, 1, 6, 0, 5, 4, 3, 2, 1, 5],
      current_player: 0,
      game_state: {status: GameStatus.IN_PROGRESS, winner: ''},
    };

    (global.fetch as jest.Mock).mockResolvedValueOnce(mockFetchResponse(mockData));

    await act( async () => render(<App/>));

    await screen.findByText('Player 1');
    expect(screen.getByText('Current Player: 1')).toBeInTheDocument();
  });

  test('handles pit click and updates state', async () => {
    const mockData = {
      pits: [4, 3, 2, 5, 1, 6, 0, 5, 4, 3, 2, 1, 5],
      current_player: 1,
      game_state: {status: GameStatus.IN_PROGRESS, winner: ''},
    };

    (global.fetch as jest.Mock).mockResolvedValue(mockFetchResponse(mockData));

    await act( async () => render(<App/>));

    const pitElement = screen.getAllByTestId('pit-component')[0];
    await act(async () => {
      fireEvent.click(pitElement);
    });

    await screen.findByText('Current Player: 2');
    expect(screen.getByText('0')).toBeInTheDocument();
  });
});
