import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => (
  <div className="flex items-center justify-center min-h-screen bg-gradient-to-br from-blue-200 via-purple-300 to-pink-200">
    <div className="text-center p-6 bg-black shadow-lg rounded-lg max-w-md mx-auto">
      <h1 className="text-4xl font-extrabold text-gray-800 mb-4">Welcome to the App</h1>
      <p className="text-lg text-gray-600 mb-6">
        Please <Link to="/sign-up" className="text-blue-500 font-semibold hover:underline">sign up</Link> to access your dashboard.
      </p>
      <div className="flex justify-center">
        <Link
          to="/sign-up"
          className="px-6 py-3 bg-blue-500 text-white font-semibold rounded-lg shadow-md hover:bg-blue-600 transition duration-200"
        >
          Sign Up
        </Link>
      </div>
    </div>
  </div>
);

export default Home;
