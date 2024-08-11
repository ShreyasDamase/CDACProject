import React from 'react';

function Footer() {
  return (
    <footer className="fixed bottom-0 left-0 z-20 w-full p-2 bg-white border-t border-gray-200 shadow md:flex md:items-center md:justify-between md:p-3 dark:bg-gray-800 dark:border-gray-600">
      <span className="text-sm text-gray-500 sm:text-center dark:text-gray-400">© 2024 <a href="http://localhost:3000/book#" className="hover:underline">Book Com™</a>. All Rights Reserved.</span>
      <ul className="flex flex-wrap items-center mt-3 text-sm font-medium text-gray-500 dark:text-gray-400 sm:mt-0">
        <li>
          <a href="#" className="hover:underline me-4 md:me-6">About</a>
        </li>
        <li>
          <a href="#" className="hover:underline me-4 md:me-6">Privacy Policy</a>
        </li>
        <li>
          <a href="#" className="hover:underline me-4 md:me-6">Licensing</a>
        </li>
        <li>
          <a href="#" className="hover:underline">Contact</a>
        </li>
      </ul>
    </footer>
  );
}

function App() {
  return (
    <div className="flex flex-col min-h-screen">
      <main className="flex-grow">
        {/* Your content here */}
      </main>
      <Footer />
    </div>
  );
}

export default App;
