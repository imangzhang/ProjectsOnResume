import classes from './FlowersSummary.module.css';

const FlowerSummary = () => {
  return (
    <section className={classes.summary}>
      <h2>About Blossoms Bouquet</h2>
      <p>
        Blossoms Bouquet is a family owned flower shop that offers beautiful and fresh bouquets to locals.
      </p>
      <p>
          Our floral designers focus on the guests specific requests and budget.
          The attention to detail assures impress the guests.
      </p>
    </section>
  );
};

export default FlowerSummary;
