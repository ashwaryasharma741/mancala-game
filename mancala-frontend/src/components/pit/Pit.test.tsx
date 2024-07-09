import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';

import Pit from './Pit';

describe('Pit Component', () => {
  const mockClickHandler = jest.fn();
  const props = {
    count: 5,
    clickHandler: mockClickHandler,
    idx: 1
  };

  test('renders the component with given count', () => {
    render(<Pit {...props} />);
    const countElement = screen.getByText('5');
    expect(countElement).toBeInTheDocument();
  });

  test('calls clickHandler with the correct index when clicked', () => {
    render(<Pit {...props} />);
    const pitElement = screen.getByTestId('pit-component');
    fireEvent.click(pitElement);
    expect(mockClickHandler).toHaveBeenCalledWith(1);
  });

  test('renders the correct class names', () => {
    render(<Pit {...props} />);
    const pitElement = screen.getByTestId('pit-component');
    expect(pitElement).toHaveClass('pit');
    const countElement = screen.getByText('5');
    expect(countElement).toHaveClass('text-white');
  });
});
