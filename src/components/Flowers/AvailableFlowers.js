import Card from '../UI/Card';
import FlowerItem from './FlowerItem/FlowerItem.js';
import classes from './AvailableFlowers.module.css';

const DUMMY_FLOWERS = [
  {
    id: 'm1',
    name: 'The flower of love',
    description: 'Beautiful pink arrangement with roses,Daisies, and mixed greens.',
    price: 25.99,
  },
  {
    id: 'm2',
    name: 'Evelyn',
    description: 'Includes white roses, pink and purple amaryllis, babies breath, pink stock with greens',
    price: 33.27,
  },
  {
    id: 'm3',
    name: 'Summer Sun',
    description: 'Includes pink Gerber daisies, purple stock, pink roses. pink spray roses with greens',
    price: 17.89,
  },
  {
    id: 'm4',
    name: 'Cool Breeze',
    description: ' Includes blue roses, white lilies, purple stock, pink wax flower, purple daisies with greens.',
    price: 18.99,
  },
  {
    id: 'm5',
    name: 'Spring Morning',
    description: 'Includes sunflowers, yellow lilys, purple stock, yellow roses and purple filler with greens.',
    price: 25.99,
  },
];

const AvailableFlowers = () => {
  const mealsList = DUMMY_FLOWERS.map((flower) => (
    <FlowerItem
      key={flower.id}
      id={flower.id}
      name={flower.name}
      description={flower.description}
      price={flower.price}
    />
  ));

  return (
    <section className={classes.flowers}>
      <Card>
        <ul>{mealsList}</ul>
      </Card>
    </section>
  );
};

export default AvailableFlowers;
