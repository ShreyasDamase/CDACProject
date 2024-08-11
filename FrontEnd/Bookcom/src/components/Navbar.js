import React, { useState, useEffect, useRef } from 'react';
import { useUser, SignedOut, SignOutButton } from '@clerk/clerk-react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome, faAddressBook, faUser, faBook, faCartPlus, faPlus, faSignOutAlt } from '@fortawesome/free-solid-svg-icons';
import '../styles/components/Navbar.css';
import 'flowbite';

const Navbar = () => {
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const dropdownRef = useRef(null);

  const toggleDropdown = () => {
    setDropdownOpen(!dropdownOpen);
  };

  const handleClickOutside = (event) => {
    if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
      setDropdownOpen(false);
    }
  };

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  return (
<nav className="h-12  bg-slate-300 border-gray-200 dark:bg-gray-900 dark:border-gray-700 fixed top-0 left-0 w-full z-50 p-0 text-sm">
<div className="max-w-screen-xl flex flex-wrap items-center justify-between mx-auto p-1 mr-7">
<a href="#" className="flex flex-col items-center space-y-2 rtl:space-y-reverse">
  <div className="flex items-center space-x-1 rtl:space-x-reverse">
    <img src="/Logo.png" className="h-8" alt="Flowbite Logo" />
    <span className="text-3xl font-semibold whitespace-nowrap" style={{ color: 'rgb(61, 47, 1)', fontFamily: 'EB Garamond, serif' }}>Book Com</span>
  </div>
 
</a>

        <button
          type="button"
          className="inline-flex items-center p-2 w-10 h-10 justify-center text-sm text-gray-500 rounded-lg md:hidden hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-200 dark:text-gray-400 dark:hover:bg-gray-700 dark:focus:ring-gray-600"
          aria-controls="navbar-dropdown"
          aria-expanded={dropdownOpen}
          onClick={toggleDropdown}
        >
          <span className="sr-only">Open main menu</span>
          <svg className="w-5 h-5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 17 14">
            <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M1 1h15M1 7h15M1 13h15" />
          </svg>
        </button>
        <div className={`${dropdownOpen ? 'block' : 'hidden'} w-full md:block md:w-auto`} id="navbar-dropdown">
          <ul className="flex flex-col font-medium p-4 md:p-1 mt-4  border border-gray-100 rounded-lg bg-slate-300 md:flex-row md:space-x-8 md:mt-0 md:border-0 dark:bg-gray-800 md:dark:bg-gray-900 dark:border-gray-700">
            <li>
              <Link to="/" className="block py-2 px-3 text-white bg-blue-700 rounded md:bg-transparent md:text-blue-700 md:p-0 md:dark:text-blue-500 dark:bg-blue-600 md:dark:bg-transparent" aria-current="page">
                <FontAwesomeIcon icon={faHome} /> Home
              </Link>
            </li>
            <li>
  <Link 
    to="/contact" 
    className="block py-2 px-3 text-gray-900 rounded hover:bg-gray-100 md:hover:bg-transparent md:border-0 md:hover:text-blue-700 md:p-0 dark:text-white md:dark:hover:text-blue-500 dark:hover:bg-gray-700 dark:hover:text-white md:dark:hover:bg-transparent"
  >
    <FontAwesomeIcon icon={faAddressBook} /> Contact
  </Link>
</li>

            <li className="relative" ref={dropdownRef}>
            <button
  id="dropdownNavbarLink"
  onClick={toggleDropdown}
  className="flex items-center justify-between w-full py-2 px-3 text-gray-900 rounded hover:bg-gray-100 md:hover:bg-transparent md:border-0 md:hover:text-blue-700 md:p-0 md:w-auto dark:text-white md:dark:hover:text-blue-500 dark:focus:text-white dark:border-gray-700 dark:hover:bg-gray-700 md:dark:hover:bg-transparent"
>
  <FontAwesomeIcon icon={faUser} className="me-2" /> {/* Added margin-end here */}
  Profile
  <svg className="w-2.5 h-2.5 ms-2.5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 10 6">
    <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="m1 1 4 4 4-4" />
  </svg>
</button>

              <div id="dropdownNavbar" className={`absolute left-0 z-10 ${dropdownOpen ? 'block' : 'hidden'} font-normal bg-black divide-y divide-gray-100 rounded-lg shadow  w-28 dark:bg-gray-700 dark:divide-gray-600`} >
                <ul className="py-2 text-sm text-gray-700 dark:text-gray-400" aria-labelledby="dropdownLargeButton">
                  <li>
                    <Link to="/profile" className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">
                      <FontAwesomeIcon icon={faUser} /> Profile
                    </Link>
                  </li>
                  <li>
                    <a href="/book" className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">
                      <FontAwesomeIcon icon={faBook} /> Book
                    </a>
                  </li>
                  <li>
                    <Link to="/checkout" className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">
                      <FontAwesomeIcon icon={faCartPlus} /> Cart
                    </Link>
                  </li>
                  <li>
                    <Link to="/postbook" className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">
                      <FontAwesomeIcon icon={faPlus} /> Post Book
                    </Link>
                  </li>
                </ul>
                <div className="py-1">
                  <SignOutButton>
                    <div className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:hover:bg-gray-600 dark:text-gray-200 dark:hover:text-white">
                      <FontAwesomeIcon icon={faSignOutAlt} /> Sign Out
                    </div>
                  </SignOutButton>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
