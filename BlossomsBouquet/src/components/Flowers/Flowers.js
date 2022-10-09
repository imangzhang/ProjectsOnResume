import { Fragment } from 'react';

import FlowerSummary from './FlowersSummary.js';
import AvailableMeals from './AvailableFlowers.js';

const Flowers = () => {
  return (
    <Fragment>
      <FlowerSummary />
      <AvailableMeals />
    </Fragment>
  );
};

export default Flowers;
