import React, { useState, useEffect } from 'react';
import emailjs from 'emailjs-com';
import { useClerk } from '@clerk/clerk-react';

export default function ContactForm() {
  const { session } = useClerk();
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");
  const [submitted, setSubmitted] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    async function fetchUserEmail() {
      const token = await session.getToken();
      const url = 'http://localhost:8080/Bookcom/user/details';

      try {
        const response = await fetch(url, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const data = await response.json();
        setEmail(data.email);
      } catch (error) {
        console.error('Error fetching user details:', error);
      }
    }

    fetchUserEmail();
  }, [session]);

  function submit(e) {
    e.preventDefault();

    emailjs.send(
      'service_0wsybjp', // Your EmailJS service ID
      'template_hmqk2ms', // Your EmailJS template ID
      {
        fromEmail: email, // Use the fetched email
        message: message,
        to_name: 'Recipient Name' // Optional: Replace with the recipient's name
      },
      'wZbkutDOfdnpgVCEt' // Your EmailJS user ID
    )
    .then((result) => {
      setSubmitted(true);
    })
    .catch((error) => {
      console.error('Failed to send message:', error);
      setError("Failed to send message. Please try again.");
    });
  }

  if (error) {
    return (
      <div className="max-w-md mx-auto pt-12  mt-12 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
        <strong className="font-bold mt-12">Error!</strong>
        <span className="block sm:inline">{error}</span>
      </div>
    );
  }

  if (submitted) {
    return (
      <div className="max-w-md mx-auto mt-12 bg-green-100 border border-blue-400 text-blue-700 px-4 py-3 rounded relative" role="alert">
        <strong className="font-bold">Success!</strong>
        <span className="block sm:inline">We've received your message, thank you for contacting us!</span>
      </div>
    );
  }

  return (
    <div className="max-w-md mt-12 mx-auto p-6 bg-white border border-gray-200 rounded-lg shadow-md">
      <h2 className="text-2xl font-semibold mb-4 text-gray-900">Contact Us</h2>
      <form onSubmit={submit} className="space-y-4">
        <div>
          <label htmlFor="email" className="block text-sm font-medium text-gray-700">Your Email</label>
          <input
            id="email"
            type="email"
            value={email}
            readOnly
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50"
          />
        </div>

        <div>
          <label htmlFor="message" className="block text-sm font-medium text-gray-700">Message</label>
          <textarea
            id="message"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            rows="4"
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50"
          />
        </div>

        <button
          type="submit"
          className="inline-flex items-center px-4 py-2 bg-blue-600 text-white font-medium text-sm rounded-md shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
        >
          Send
        </button>
      </form>
    </div>
  );
}
