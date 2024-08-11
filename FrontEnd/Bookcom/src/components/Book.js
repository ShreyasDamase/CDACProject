import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useClerk } from '@clerk/clerk-react';

function Book() {
    const { session } = useClerk();
    const [formData, setFormData] = useState({
        title: '',
        category: '',
        author: '',
        isbn: '',
        publisher: '',
        year: '',
        price: 0
    });

    const [frontImage, setFrontImage] = useState(null);
    const [backImage, setBackImage] = useState(null);
    const [categoryList, setCategoryList] = useState({});
    const [notification, setNotification] = useState({ visible: false, message: '' });
    const [loading, setLoading] = useState(false); // New state for loading

    async function getCategory() {
        const token = await session.getToken();
        try {
            const response = await axios.get('http://localhost:8080/Bookcom/book/getCategory', {
                headers: {
                    Authorization: `Bearer ${token}`
                },
            });
            setFormData(prevFormData => ({ ...prevFormData, category: Object.keys(response.data)[0] }));
            setCategoryList(response.data);
        } catch (error) {
            console.error('Error fetching categories:', error);
        }
    }

    useEffect(() => {
        getCategory();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleFrontImageChange = (e) => {
        setFrontImage(e.target.files[0]);
    };

    const handleBackImageChange = (e) => {
        setBackImage(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true); // Start loading
        const data = new FormData();
        data.append('title', formData.title);
        data.append('category', formData.category);
        data.append('author', formData.author);
        data.append('isbn', formData.isbn);
        data.append('publisher', formData.publisher);
        data.append('year', formData.year);
        data.append('price', formData.price);
        if (frontImage) {
            data.append('frontimage', frontImage);
        }
        if (backImage) {
            data.append('backimage', backImage);
        }
        const token = await session.getToken();

        try {
            await axios.post('http://localhost:8080/Bookcom/book/create', data, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: `Bearer ${token}`
                },
            });
            setNotification({ visible: true, message: 'Book successfully submitted!' });
            setTimeout(() => setNotification({ visible: false, message: '' }), 3000); // Hide notification after 3 seconds
        } catch (error) {
            console.error('There was an error uploading the book!', error);
        } finally {
            setLoading(false); // End loading
        }
    };

    return (
        <div className="container items-center px-5 py-12 lg:px-20">
            <form 
                className="flex flex-col w-full p-10 px-8 pt-6 mx-auto my-6 mb-4 transition duration-500 ease-in-out transform bg-white border rounded-lg lg:w-1/2" 
                onSubmit={handleSubmit}
            >
        <div className="relative pt-4">
          <label htmlFor="title" className="text-base leading-7 text-blueGray-500">Title</label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleInputChange}
            placeholder="name"
            className="w-full px-4 py-2 mt-2 mr-4 text-base text-black transition duration-500 ease-in-out transform rounded-lg bg-gray-100 focus:border-blueGray-500 focus:bg-white focus:outline-none focus:shadow-outline focus:ring-2 ring-offset-current ring-offset-2"
          />
        </div>
        <div className="relative mt-4">
          <label htmlFor="category" className="text-base leading-7 text-blueGray-500">Category of Book</label>
          <select
            id="category"
            name="category"
            value={formData.category}
            onChange={handleInputChange}
            className="w-full px-4 py-2 mt-2 text-base text-black transition duration-500 ease-in-out transform rounded-lg bg-gray-100 focus:border-blueGray-500 focus:bg-white focus:outline-none focus:shadow-outline focus:ring-2 ring-offset-current ring-offset-2"
          >
            {
            Object.keys(categoryList).map(item=>{
              return <option key={item} value={item}>
              {categoryList[item]}
              </option>
            })
          }
          </select>
        </div>
        <div className="relative pt-4">
          <label htmlFor="author" className="text-base leading-7 text-blueGray-500">Author</label>
          <input
            type="text"
            id="author"
            name="author"
            value={formData.author}
            onChange={handleInputChange}
            placeholder="name"
            className="w-full px-4 py-2 mt-2 mr-4 text-base text-black transition duration-500 ease-in-out transform rounded-lg bg-gray-100 focus:border-blueGray-500 focus:bg-white focus:outline-none focus:shadow-outline focus:ring-2 ring-offset-current ring-offset-2"
          />
        </div>
        <div className="relative pt-4">
          <label htmlFor="isbn" className="text-base leading-7 text-blueGray-500">ISBN</label>
          <input
            type="number"
            id="isbn"
            name="isbn"
            value={formData.isbn}
            onChange={handleInputChange}
            placeholder="ISBN"
            className="w-full px-4 py-2 mt-2 mr-4 text-base text-black transition duration-500 ease-in-out transform rounded-lg bg-gray-100 focus:border-blueGray-500 focus:bg-white focus:outline-none focus:shadow-outline focus:ring-2 ring-offset-current ring-offset-2"
          />
        </div>
        <div className="relative pt-4">
          <label htmlFor="price" className="text-base leading-7 text-blueGray-500">Expected Price</label>
          <input
            type="number"
            id="price"
            name="price"
            value={formData.price}
            onChange={handleInputChange}
            placeholder="Price"
            className="w-full px-4 py-2 mt-2 mr-4 text-base text-black transition duration-500 ease-in-out transform rounded-lg bg-gray-100 focus:border-blueGray-500 focus:bg-white focus:outline-none focus:shadow-outline focus:ring-2 ring-offset-current ring-offset-2"
          />
        </div>
        <div className="relative pt-4">
          <label htmlFor="publisher" className="text-base leading-7 text-blueGray-500">Publisher</label>
          <input
            type="text"
            id="publisher"
            name="publisher"
            value={formData.publisher}
            onChange={handleInputChange}
            placeholder="name"
            className="w-full px-4 py-2 mt-2 mr-4 text-base text-black transition duration-500 ease-in-out transform rounded-lg bg-gray-100 focus:border-blueGray-500 focus:bg-white focus:outline-none focus:shadow-outline focus:ring-2 ring-offset-current ring-offset-2"
          />
        </div>
        <div className="relative pt-4">
          <label htmlFor="year" className="text-base leading-7 text-blueGray-500">Publication Year</label>
          <input
            type="number"
            id="year"
            name="year"
            value={formData.year}
            onChange={handleInputChange}
            placeholder="name"
            className="w-full px-4 py-2 mt-2 mr-4 text-base text-black transition duration-500 ease-in-out transform rounded-lg bg-gray-100 focus:border-blueGray-500 focus:bg-white focus:outline-none focus:shadow-outline focus:ring-2 ring-offset-current ring-offset-2"
            min={1901}
            max={new Date().getFullYear()}
          />
        </div>
        <section className="flex flex-col w-full h-full p-1 overflow-auto">
          <label htmlFor="frontimage" className="text-base leading-7 text-blueGray-500 mb-5">Add Cover Image</label>
          <input type="file" onChange={handleFrontImageChange} />
        </section>
        <section className="flex flex-col w-full h-full p-1 overflow-auto">
          <label htmlFor="backimage" className="text-base leading-7 text-blueGray-500 mb-5">Add End Image of Book</label>
          <input type="file" onChange={handleBackImageChange} />
        </section>
        <div className="flex items-center w-full pt-4 mb-4">
                    <button
                        type="submit"
                        className="w-full py-3 text-base text-white transition duration-500 ease-in-out transform bg-blue-600 border-blue-600 rounded-md focus:shadow-outline focus:outline-none focus:ring-2 ring-offset-current ring-offset-2 hover:bg-blue-800"
                    >
                        Submit
                    </button>
                </div>
            </form>

            {/* Loading Spinner */}
            {loading && (
                <div className="fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 flex items-center justify-center">
                <div className="border-t-4 border-blue-600 border-solid w-16 h-16 border-r-transparent rounded-full animate-spin"></div>
            </div>
            )}

            {/* Notification */}
            {notification.visible && (
                <div className="fixed top-20 left-1/2 transform -translate-x-1/2 bg-green-500 text-white p-4 rounded shadow-lg">
                    <p>{notification.message}</p>
                </div>
            )}
        </div>
    );
}

export default Book;
