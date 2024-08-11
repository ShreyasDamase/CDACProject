import React from 'react';
import Navbar from './Navbar';
import Footer from './Footer';

function Layout({ children }) {
  return (
    <div>
      <Navbar />
      <div>
        {children}
      </div>
    </div>
  );
}

export default Layout;
