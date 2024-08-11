import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useClerk, useUser } from '@clerk/clerk-react';

function Card() {
  const [imageUrls, setImageUrls] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedBook, setSelectedBook] = useState(null);
  const [formData, setFormData] = useState({
    title: '',
    category: '',
    author: '',
    isbn: '',
    price: '',
    publisher: '',
    year: '',
  });
  const [categoryList, setCategoryList] = useState({});
  const [loading, setLoading] = useState(true);
  const [notification, setNotification] = useState('');
  const [notificationVisible, setNotificationVisible] = useState(false);

  const { session } = useClerk();
  const { isLoaded, isSignedIn, user } = useUser();

  async function getCategory() {
    const token = await session.getToken();
    try {
      const response = await axios.get('http://localhost:8080/Bookcom/book/getCategory', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setFormData({ ...formData, category: Object.keys(response.data)[0] });
      setCategoryList(response.data);
    } catch (error) {
      console.error('There was an error uploading the book!', error);
    }
  }

  useEffect(() => {
    async function fetchImage() {
      const url = 'http://localhost:8080/Bookcom/book/getImage';
      const jwt_token = await session.getToken();

      axios
        .get(url, { headers: { Authorization: `Bearer ${jwt_token}` } })
        .then((response) => {
          setImageUrls(response.data);
          setLoading(false);
        })
        .catch((error) => console.error('Error fetching image:', error));
    }

    getCategory();
    fetchImage();
    const timer = setTimeout(() => setLoading(false), 1500);

    return () => clearTimeout(timer);
  }, []);

  const openModal = (book) => {
    getCategory();
    setSelectedBook(book);
    setFormData({
      title: book.title,
      category: book.category,
      author: book.author,
      isbn: book.isbn,
      price: book.price,
      publisher: book.publisher,
      year: book.year,
    });
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setSelectedBook(null);
    setFormData({
      title: '',
      category: '',
      author: '',
      isbn: '',
      price: '',
      publisher: '',
      year: '',
    });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    const url = selectedBook ? `http://localhost:8080/Bookcom/book/update` : 'http://localhost:8080/Bookcom/book/create';
    const jwt_token = await session.getToken();

    const data = new FormData();
    data.append('title', formData.title);
    data.append('category', formData.category);
    data.append('author', formData.author);
    data.append('isbn', formData.isbn);
    data.append('publisher', formData.publisher);
    data.append('year', formData.year);
    data.append('price', formData.price);
    data.append('id', selectedBook ? selectedBook[0] : '');

    axios
      .post(url, data, { headers: { Authorization: `Bearer ${jwt_token}` } })
      .then((response) => {
        console.log('Book updated:', response.data);
        setNotification('Book updated successfully!');
        setNotificationVisible(true);
        closeModal();
        // Hide notification after 2 seconds
        const timer = setTimeout(() => setNotificationVisible(false), 2000);
        return () => clearTimeout(timer); // Cleanup timeout on unmount
      })
      .catch((error) => console.error('Error updating book:', error));
  };

  const deleteBook = async (bookId) => {
    const url = `http://localhost:8080/Bookcom/book/delete?id=${bookId}`;
    const jwt_token = await session.getToken();
    axios
      .delete(url, { headers: { Authorization: `Bearer ${jwt_token}` } })
      .then((response) => {
        console.log('Book deleted:', response.data);
        setImageUrls((prevUrls) => prevUrls.filter((item) => item[0] !== bookId));
      })
      .catch((error) => console.error('Error deleting book:', error));
  };

  return (
    <div className="card-container">
      {loading ? (
        <div className="flex items-center justify-center h-screen">
          <div className="w-16 h-16 border-t-4 border-blue-500 border-solid rounded-full animate-spin"></div>
        </div>
      ) : (
        <div className="grid h-full bg-gradient-to-br from-white via-white-900 to-white-900 p-5 gap-x-10 gap-y-12 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-5 xl:grid-cols-4 2xl:grid-cols-5 mt-16">
          {imageUrls.length > 0 ? (
            imageUrls.map((item, index) => (
              <div
                key={index}
                className="group flex flex-col w-full max-w-[190px] max-h-[340px] self-center overflow-hidden rounded-lg border border-gray-100/30 bg-gray-800 shadow-lg transform transition-transform duration-500 hover:scale-105"
              >
                <a
                  className="relative mx-3 mt-3 flex h-60 overflow-hidden rounded-xl"
                  href="#"
                >
                  <img
                    className="peer absolute top-0 right-0 h-full w-full object-cover transform transition-transform duration-500 group-hover:scale-110"
                    src={item[1]}
                    alt={index + 1}
                  />
                  <img
                    className="peer peer-hover:right-0 absolute top-0 -right-96 h-full w-full object-cover transition-all delay-100 duration-1000 hover:right-0"
                    src={item[2]}
                    alt="product image"
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
                <div className="mt-1 px-5 pb-5">
                  <div className="flex space-x-2 mt-2">
                    <button
                      onClick={() => openModal(item)}
                      className="max-w-[70px] max-h-[40px] mt-2 mb-1 flex items-center justify-center rounded-md border border-transparent bg-blue-600 px-3 py-2 text-center text-sm font-medium text-white focus:outline-none focus:ring-4 focus:ring-blue-300 transform transition-transform duration-300 hover:scale-105"
                    >
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                        viewBox="0 0 24 24"
                        strokeWidth="1.5"
                        stroke="currentColor"
                        className="w-5 h-5 ml-1"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          d="M11.25 3.75L15 7.5M5.25 17.25v-6a.75.75 0 0 1 .75-.75h6a.75.75 0 0 1 .75.75v6m-8.25-4.5L15 7.5m0 0L13.5 9M3 5.25v13.5a.75.75 0 0 0 .75.75h15a.75.75 0 0 0 .75-.75V5.25a.75.75 0 0 0-.75-.75H3.75a.75.75 0 0 0-.75.75z"
                        />
                      </svg>
                      Edit
                    </button>
                    <button
                      onClick={() => deleteBook(item[0])}
                      className="max-w-[70px] max-h-[40px] mt-2 mb-1 flex items-center justify-center rounded-md border border-transparent bg-red-600 px-3 py-2 text-center text-sm font-medium text-white focus:outline-none focus:ring-4 focus:ring-red-300 transform transition-transform duration-300 hover:scale-105"
                    >
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                        viewBox="0 0 24 24"
                        strokeWidth="1.5"
                        stroke="currentColor"
                        className="w-5 h-5 ml-1"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          d="M6 18L18 6M6 6l12 12"
                        />
                      </svg>
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className="flex items-center justify-center h-60 text-white text-lg">
              No Images Available
            </div>
          )}
        </div>
      )}

      {/* Modal Code */}
      {isModalOpen && (
        <div className="fixed inset-0 z-10 flex items-center justify-center bg-gray-900 bg-opacity-50">
          <div className="relative bg-white p-6 rounded-lg shadow-lg w-1/3 max-h-[79vh] mt-4 overflow-y-auto">
            <button
              onClick={closeModal}
              className="absolute top-2 right-2 text-gray-500 hover:text-gray-800"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth="1.5"
                stroke="currentColor"
                className="w-6 h-6"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </button>
            <h2 className="text-lg font-semibold mb-4">Edit Book</h2>
            <form onSubmit={handleFormSubmit}>
              <div className="grid gap-4">
                {Object.keys(formData).map((key) => (
                  <div key={key}>
                    <label className="block text-gray-700">{key.charAt(0).toUpperCase() + key.slice(1)}</label>
                    <input
                      type="text"
                      name={key}
                      value={formData[key]}
                      onChange={handleInputChange}
                      className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                      required
                    />
                  </div>
                ))}
                <div className="flex space-x-4 mt-4">
                  <button
                    type="submit"
                    className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600"
                  >
                    Save
                  </button>
                  <button
                    type="button"
                    onClick={closeModal}
                    className="bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600"
                  >
                    Cancel
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Notification */}
      {notificationVisible && (
        <div className="fixed bottom-14 right-4 bg-green-500 text-white px-4 py-2 rounded-md shadow-lg">
          {notification}
        </div>
      )}
    </div>
  );
}

export default Card;
