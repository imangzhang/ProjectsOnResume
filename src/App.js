import { useState } from 'react';
import color from './App.css'
import Header from './components/Layout/Header';
import Flowers from './components/Flowers/Flowers.js';
import Cart from './components/Cart/Cart';
import CartProvider from './store/CartProvider';

function App() {
  const [cartIsShown, setCartIsShown] = useState(false);

  const showCartHandler = () => {
    setCartIsShown(true);
  };

  const hideCartHandler = () => {
    setCartIsShown(false);
  };

  return (
      <div className="back">
    <CartProvider>
      {cartIsShown && <Cart onClose={hideCartHandler} />}
      <Header onShowCart={showCartHandler} />
      <main>
        <Flowers />
      </main>
    </CartProvider>
      </div>
  );
}

export default App;
