import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useClerk } from '@clerk/clerk-react';
import { Link } from 'react-router-dom';
import { FaTrashAlt, FaShoppingCart } from 'react-icons/fa';
import { AiOutlineArrowRight } from 'react-icons/ai';

export default function Checkout() {
    const [Books, setBooks] = useState([]);
    const { session } = useClerk();
    const [TotalAmount, setTotalAmount] = useState(0);
    const [notification, setNotification] = useState({ visible: false, message: '' });

    // Fetch all books and calculate total amount
    async function fetchAllBooks() {
        const url = 'http://localhost:8080/Bookcom/orders/getBooks';
        const jwt_token = await session.getToken();

        try {
            const response = await axios.get(url, { headers: { Authorization: `Bearer ${jwt_token}` } });
            const booksData = response.data;
            setBooks(booksData);

            // Calculate the total amount
            const total = booksData.reduce((acc, item) => {
                const price = parseFloat(item[5]); // Ensure item[5] is the price
                return !isNaN(price) ? acc + price : acc;
            }, 0);
            setTotalAmount(total);
        } catch (error) {
            console.error('Error fetching books:', error);
        }
    }

    // Handle book removal
    async function deleteBook(bookId) {
        const url = `http://localhost:8080/Bookcom/orders/deleteOrder?id=${bookId}`;
        const jwt_token = await session.getToken();

        try {
            await axios.delete(url, { headers: { Authorization: `Bearer ${jwt_token}` } });

            // Update state and recalculate total amount
            setBooks(prevBooks => {
                const updatedBooks = prevBooks.filter(item => item[0] !== bookId);

                // Recalculate total amount
                const total = updatedBooks.reduce((acc, item) => {
                    const price = parseFloat(item[5]); // Ensure item[5] is the price
                    return !isNaN(price) ? acc + price : acc;
                }, 0);
                setTotalAmount(total);

                // Show notification
                setNotification({ visible: true, message: 'Book removed successfully!' });
                setTimeout(() => setNotification({ visible: false, message: '' }), 2000);

                return updatedBooks;
            });
        } catch (error) {
            console.error('Error deleting book:', error);
        }
    }

    // Fetch books on component mount
    useEffect(() => {
        fetchAllBooks();
    }, []);

    return (
        <section className="bg-white py-8 antialiased dark:bg-gray-900 md:py-16">
            <div className="mx-auto max-w-screen-xl px-4 2xl:px-0">
                <h2 className="text-xl font-semibold text-gray-900 dark:text-white sm:text-2xl flex items-center gap-2">
                    <FaShoppingCart className="text-blue-600" /> Shopping Cart
                </h2>

                <div className="mt-6 sm:mt-8 md:gap-6 lg:flex lg:items-start xl:gap-8">
                    <div className="mx-auto w-full flex-none lg:max-w-2xl xl:max-w-4xl">
                        <div className="space-y-6">
                            {Books.length !== 0 ? Books.map((item, index) => (
                                <div className="relative rounded-lg border border-gray-200 bg-white p-4 shadow-sm dark:border-gray-700 dark:bg-gray-800 md:p-6 flex items-center gap-4" key={index}>
                                    <img className="h-20 w-20 object-cover rounded-lg" src="https://flowbite.s3.amazonaws.com/blocks/e-commerce/imac-front.svg" alt="Book cover" />
                                    <div className="w-full flex flex-col md:flex-row md:items-center md:justify-between">
                                        <Link to={"#"} className="text-base font-medium text-gray-900 hover:underline dark:text-white">{item[0]} | {item[1]} | {item[2]} | {item[3]}</Link>
                                        <div className="text-base font-bold text-gray-900 dark:text-white mt-2 md:mt-0">${item[5]}</div>
                                        <button type="button" className="flex items-center text-red-600 hover:text-red-800 dark:text-red-500 dark:hover:text-red-400 mt-2 md:mt-0" onClick={() => deleteBook(item[0])}>
                                            <FaTrashAlt className="mr-2" /> Remove
                                        </button>
                                    </div>
                                </div>
                            )) : <p className="text-gray-500 dark:text-gray-400">No Items present..</p>}
                        </div>
                    </div>

                    <div className="mx-auto mt-6 max-w-4xl flex-1 space-y-6 lg:mt-0 lg:w-full">
                        <div className="space-y-4 rounded-lg border border-gray-200 bg-white p-4 shadow-sm dark:border-gray-700 dark:bg-gray-800 sm:p-6">
                            <p className="text-xl font-semibold text-gray-900 dark:text-white">Order summary</p>

                            <div className="space-y-4">
                                <div className="space-y-2">
                                    <dl className="flex items-center justify-between gap-4">
                                        <dt className="text-base font-normal text-gray-500 dark:text-gray-400">Original price</dt>
                                        <dd className="text-base font-medium text-gray-900 dark:text-white">${TotalAmount.toFixed(2)}</dd>
                                    </dl>
                                </div>

                                <dl className="flex items-center justify-between gap-4 border-t border-gray-200 pt-2 dark:border-gray-700">
                                    <dt className="text-base font-bold text-gray-900 dark:text-white">Total</dt>
                                    <dd className="text-base font-bold text-gray-900 dark:text-white">${TotalAmount.toFixed(2)}</dd>
                                </dl>
                            </div>

                            <a href="#" className="flex w-full items-center justify-center rounded-lg bg-blue-700 px-5 py-2.5 text-sm font-medium text-white hover:bg-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                                Proceed to Checkout
                            </a>

                            <div className="flex items-center justify-center gap-2">
                                <span className="text-sm font-normal text-gray-500 dark:text-gray-400">or</span>
                                <Link to={"/"} title="" className="inline-flex items-center gap-2 text-sm font-medium text-blue-700 underline hover:no-underline dark:text-blue-500">
                                    Continue Shopping
                                    <AiOutlineArrowRight className="h-5 w-5" />
                                </Link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Notification */}
            {notification.visible && (
                <div className="fixed top-20 left-1/2 transform -translate-x-1/2 bg-green-500 text-white p-4 rounded-lg shadow-lg">
                    <p>{notification.message}</p>
                </div>
            )}
        </section>
    );
}
