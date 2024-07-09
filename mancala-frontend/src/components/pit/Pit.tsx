import React from "react";

interface PitProps {
  count: number;
  clickHandler: (x:number) => {};
  idx: number,
};

const Pit = ({count, clickHandler, idx}: PitProps) => {
  return (
    <div
      className='pit'
      onClick={() => clickHandler(idx)}
      data-testid="pit-component"
    >
      <span className='text-white'>{count}</span>
    </div>
  );
};

export default Pit;