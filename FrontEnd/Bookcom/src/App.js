import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { SignedOut } from '@clerk/clerk-react';
import './styles/components/Navbar.css'; // Import the custom CSS
import Bookcom from './Bookcom';
import SignUpPage from './SignUpPage';
import Card from './components/Card';
import ProtectedRoute from './util/ProtectedRoute'; // Import the ProtectedRoutes component
import Profile from './components/Profile';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import Book from './components/Book';
import Checkout from './components/Checkout';
import ContactForm from './components/ContactForm'; // Import the ContactForm component

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/sign-up" element={<SignUpPage />} />
        <Route path="/sign-out" element={<SignedOut />} />
        
        <Route element={<ProtectedRoute />}>
            <Route path="/" element={<Bookcom />} />
            <Route path="/book" element={<Card />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/postbook" element={<Book />} />
            <Route path="/checkout" element={<Checkout />} />
            <Route path="/contact" element={<ContactForm />} /> 
        </Route>
      </Routes>
      <Footer />
    </Router>
  );
}

export default App;
