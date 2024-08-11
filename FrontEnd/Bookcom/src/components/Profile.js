import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useClerk } from '@clerk/clerk-react';

export default function Profile() {
    const { session } = useClerk();
    const [Email, setEmail] = useState('');
    const [formData, setFormData] = useState({
        username: "",
        fname: "",
        lname: "",
        number: "",
        email: "",
        address: ""
    });
    const [loading, setLoading] = useState(false); // State for loading spinner
    const [notification, setNotification] = useState({ visible: false, message: '' }); // State for notification

    useEffect(() => {
        async function fetchUser() {
            const url = 'http://localhost:8080/Bookcom/user/details';
            const token = await session.getToken();

            try {
                const response = await axios.get(url, { headers: { Authorization: `Bearer ${token}` } });
                setFormData({
                    ...formData,
                    email: response.data.email,
                    fname: response.data.fname,
                    lname: response.data.lname,
                    username: response.data.username,
                    address: response.data.address,
                    number: response.data.number
                });
                setEmail(response.data.email);
            } catch (error) {
                console.error('Error fetching user details:', error);
            }
        }
        fetchUser();
    }, [Email]);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const url = 'http://localhost:8080/Bookcom/user/update';
        const token = await session.getToken();
        setLoading(true); // Start loading

        try {
            await axios.post(url, formData, { headers: { Authorization: `Bearer ${token}` } });
            setNotification({ visible: true, message: 'Profile updated successfully!' });
        } catch (error) {
            console.error('Error updating profile:', error);
            setNotification({ visible: true, message: 'Failed to update profile.' });
        } finally {
            setLoading(false); // End loading
            setTimeout(() => setNotification({ visible: false, message: '' }), 3000); // Hide notification after 3 seconds
        }
    };

    return (
        <div>
            <div className="max-w-3xl mt-13 mx-auto p-6 bg-white shadow-lg rounded-lg">
                <div className="text-center mb-6">
                    <h4 className="text-lg font-semibold text-gray-900">Update Your Account</h4>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
                        {/* Form fields */}
                        <div>
                            <label className="block text-sm font-medium text-gray-700">User Name</label>
                            <input
                                name="username"
                                type="text"
                                value={formData.username}
                                onChange={handleChange}
                                className="mt-1 block w-full bg-gray-100 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                                placeholder="Enter username"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">First Name</label>
                            <input
                                name="fname"
                                type="text"
                                value={formData.fname}
                                onChange={handleChange}
                                className="mt-1 block w-full bg-gray-100 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                                placeholder="Enter first name"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Last Name</label>
                            <input
                                name="lname"
                                type="text"
                                value={formData.lname}
                                onChange={handleChange}
                                className="mt-1 block w-full bg-gray-100 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                                placeholder="Enter last name"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Email</label>
                            <input
                                name="email"
                                type="text"
                                value={Email}
                                className="mt-1 block w-full bg-gray-100 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                                disabled
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Address</label>
                            <input
                                name="address"
                                type="text"
                                value={formData.address}
                                onChange={handleChange}
                                className="mt-1 block w-full bg-gray-100 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                                placeholder="Enter address"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Mobile No.</label>
                            <input
                                name="number"
                                type="number"
                                value={formData.number}
                                onChange={handleChange}
                                className="mt-1 block w-full bg-gray-100 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
                                placeholder="Enter mobile number"
                            />
                        </div>
                    </div>
                    <div className="mt-8 flex justify-center">
                        <button
                            type="submit"
                            className="py-2 px-4 text-sm font-semibold rounded-md text-white bg-blue-500 hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            Update
                        </button>
                    </div>
                </form>
            </div>

            {/* Notification */}
            {notification.visible && (
                <div className="fixed bottom-10 right-5 bg-blue-600 text-white p-4 rounded-lg shadow-lg">
                    {notification.message}
                </div>
            )}

            {/* Loading Spinner */}
            {loading && (
                <div className="fixed inset-0 flex items-center justify-center bg-gray-900/50 text-white">
                    <div className="w-16 h-16 border-4 border-t-4 border-blue-500 border-solid rounded-full animate-spin"></div>
                </div>
            )}
        </div>
    );
}
