import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useClerk, useUser } from '@clerk/clerk-react';

export default function Content() {
  const [imageUrls, setImageUrls] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedBook, setSelectedBook] = useState(null);
  const [bookDescription, setBookDescription] = useState({
    title: '',
    category: '',
    author: '',
    isbn: '',
    price: '',
    publisher: '',
    year: '',
    coverImage: '',
  });

  const [loading, setLoading] = useState(false); // State for loading spinner
  const [notification, setNotification] = useState({ visible: false, message: '' }); // State for notification

  const { session } = useClerk();
  const { isLoaded, isSignedIn, user } = useUser();

  async function getDescription(book) {
    const token = await session.getToken();
    try {
      const response = await axios.post('http://localhost:8080/Bookcom/book/getDescription', { "bookId": book }, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      response.data['year'] = new Date(response.data['year']).getFullYear();
      setBookDescription({
        ...response.data,
        coverImage: response.data.coverImage
      });
      return response.data;
    } catch (error) {
      console.error('There was an error fetching the book description!', error);
    }
  }

  useEffect(() => {
    async function fetchImage() {
      const url = 'http://localhost:8080/Bookcom/book/getAllImages';
      const jwt_token = await session.getToken();

      axios
        .get(url, { headers: { Authorization: `Bearer ${jwt_token}` } })
        .then((response) => {
          setImageUrls(response.data);
        })
        .catch((error) => console.error('Error fetching images:', error));
    }

    fetchImage();
  }, []);

  const openModal = (book) => {
    getDescription(book[1]);
    setSelectedBook(book);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setSelectedBook(null);
    setBookDescription({
      title: '',
      category: '',
      author: '',
      isbn: '',
      price: '',
      publisher: '',
      year: '',
      coverImage: '',
    });
  };

  const OrderBook = async (bookId) => {
    setLoading(true); // Start loading
    let data = await getDescription(bookId);
    data['bookid'] = bookId;
    const url = `http://localhost:8080/Bookcom/orders/create`;
    const jwt_token = await session.getToken();
    axios
      .post(url, data, { headers: { Authorization: `Bearer ${jwt_token}` } })
      .then((response) => {
        console.log('Book Ordered:', response.data);
        setNotification({ visible: true, message: 'Book successfully ordered!' });
        setTimeout(() => setNotification({ visible: false, message: '' }), 3000); // Hide notification after 3 seconds
      })
      .catch((error) => {
        console.error('Error ordering book:', error);
        setNotification({ visible: true, message: 'Failed to order book.' });
        setTimeout(() => setNotification({ visible: false, message: '' }), 3000); // Hide notification after 3 seconds
      })
      .finally(() => {
        setLoading(false); // End loading
      });
  };

  return (
    <>
      <div className="mr-3 ml-3 min-h-screen bg-gradient-to-br from-purple-000 via-violet-900 to-gray-900 p-5 mt-20 mb-20 max-w-[1150px] max-h-[40px]">
        <div className="grid gap-16 lg:grid-cols-5 md:grid-cols-2 sm:grid-cols-1 justify-items-center">
          {imageUrls.length !== 0 ? (
            imageUrls.map((item, index) => (
              <div
                key={index}
                className="group border-gray-100/30 flex w-full max-w-xs flex-col overflow-hidden rounded-lg border bg-gray-800 shadow-lg transform transition-transform duration-500 hover:scale-105"
              >
                <a
                  className="relative mx-3 mt-3 flex h-60 overflow-hidden rounded-xl"
                  href="#"
                >
                  <img
                    className="peer absolute top-0 right-0 h-full w-full object-cover transform transition-transform duration-500 group-hover:scale-110"
                    src={item[2]}
                    alt={`Image ${index + 1}`}
                  />
                  <img
                    className="peer peer-hover:right-0 absolute top-0 -right-96 h-full w-full object-cover transition-all delay-100 duration-1000 hover:right-0"
                    src={item[3]}
                    alt="Product Image"
                  />
                  <svg
                    className="group-hover:animate-ping group-hover:opacity-30 peer-hover:opacity-0 pointer-events-none absolute inset-x-0 bottom-5 mx-auto text-3xl text-white transition-opacity"
                    xmlns="http://www.w3.org/2000/svg"
                    aria-hidden="true"
                    role="img"
                    width="1em"
                    height="1em"
                    preserveAspectRatio="xMidYMid meet"
                    viewBox="0 0 32 32"
                  >
                    <path
                      fill="currentColor"
                      d="M2 10a4 4 0 0 1 4-4h20a4 4 0 0 1 4 4v10a4 4 0 0 1-2.328 3.635a2.996 2.996 0 0 0-.55-.756l-8-8A3 3 0 0 0 14 17v7H6a4 4 0 0 1-4-4V10Zm14 19a1 1 0 0 0 1.8.6l2.7-3.6H25a1 1 0 0 0 .707-1.707l-8-8A1 1 0 0 0 16 17v12Z"
                    />
                  </svg>
                </a>
                <div className="mt-4 px-5 pb-5">
                  <button
                    onClick={() => openModal(item)}
                    className="hover:border-white/40 flex items-center justify-center rounded-md border border-transparent bg-blue-600 px-5 py-2.5 text-center text-sm font-medium text-white focus:outline-none focus:ring-4 focus:ring-blue-300 transform transition-transform duration-300 hover:scale-105"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      className="mr-2 h-6 w-6"
                      fill="none"
                      viewBox="0 0 24 24"
                      stroke="currentColor"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth="2"
                        d="M15 3v2a2 2 0 002 2h2M5 21v-2a2 2 0 012-2h14a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 0 1-2 2z"
                      />
                    </svg>
                    Description
                  </button>
                  <button
                    onClick={() => OrderBook(item[1])}
                    className="hover:border-white/40 mt-2 flex items-center justify-center rounded-md border border-transparent bg-blue-600 px-5 py-2.5 text-center text-sm font-medium text-white focus:outline-none focus:ring-4 focus:ring-blue-300 transform transition-transform duration-300 hover:scale-105"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      className="mr-2 h-6 w-6"
                      fill="none"
                      viewBox="0 0 24 24"
                      stroke="currentColor"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth="2"
                        d="M3 7h18M3 11h18m-7 4h7M3 15h4m-4 4h18"
                      />
                    </svg>
                    Order
                  </button>
                </div>
              </div>
            ))
          ) : (
            <div className="flex items-center justify-center h-64 text-white">
              No images found
            </div>
          )}
        </div>
      </div>

      {/* Modal */}
      {isModalOpen && (
        <div
          id="defaultModal"
          tabIndex="-1"
          aria-hidden="true"
          className="fixed top-0 left-0 z-50 w-full h-full bg-gray-900/50 flex items-center justify-center"
        >
          <div className="modal-content bg-gray-800 p-6 rounded-lg max-w-lg mx-auto relative flex">
            <button
              type="button"
              className="absolute top-3 right-3 text-white bg-gray-500 rounded-full p-1.5 hover:bg-gray-700"
              onClick={closeModal}
            >
              <svg
                className="w-5 h-5"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
                strokeWidth="2"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </button>
            <div className="flex">
              <img
                src={bookDescription.coverImage}
                alt="Book Cover"
                className="w-48 h-64 object-cover rounded-lg"
              />
              <div className="ml-6">
                <h3 className="text-xl font-semibold text-white">Description</h3>
                <div className="space-y-4 mt-4">
                  <p className="modal-input block w-full p-2.5 border border-gray-300 rounded-md bg-gray-700 text-white placeholder-gray-400">
                    <strong>Title:</strong> {bookDescription.title}
                  </p>
                  <p className="modal-input block w-full p-2.5 border border-gray-300 rounded-md bg-gray-700 text-white placeholder-gray-400">
                    <strong>Category:</strong> {bookDescription.category}
                  </p>
                  <p className="modal-input block w-full p-2.5 border border-gray-300 rounded-md bg-gray-700 text-white placeholder-gray-400">
                    <strong>Author:</strong> {bookDescription.author}
                  </p>
                  <p className="modal-input block w-full p-2.5 border border-gray-300 rounded-md bg-gray-700 text-white placeholder-gray-400">
                    <strong>ISBN:</strong> {bookDescription.isbn}
                  </p>
                  <p className="modal-input block w-full p-2.5 border border-gray-300 rounded-md bg-gray-700 text-white placeholder-gray-400">
                    <strong>Price:</strong> {bookDescription.price}
                  </p>
                  <p className="modal-input block w-full p-2.5 border border-gray-300 rounded-md bg-gray-700 text-white placeholder-gray-400">
                    <strong>Publisher:</strong> {bookDescription.publisher}
                  </p>
                  <p className="modal-input block w-full p-2.5 border border-gray-300 rounded-md bg-gray-700 text-white placeholder-gray-400">
                    <strong>Year:</strong> {bookDescription.year}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Notification */}
      {notification.visible && (
        <div className="fixed bottom-10 right-5 bg-blue-600 text-white p-4 rounded-lg shadow-lg">
          {notification.message}
        </div>
      )}

      {/* Loading Spinner */}
      {loading && (
        <div className="fixed inset-0 flex items-center justify-center bg-gray-900/50 text-white" >
          <div className="loader">Loading...</div>
        </div>
      )}
    </>
  );
}
