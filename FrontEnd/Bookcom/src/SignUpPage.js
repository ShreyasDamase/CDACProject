import React from 'react';
import { useNavigate } from 'react-router-dom';
import { SignUp, SignedOut } from '@clerk/clerk-react';

function SignUpPage() {
  const navigate = useNavigate();

  const handleSignUpComplete = () => {
    navigate('/dashboard');
  };

  return (
    <SignedOut>
      <div className="mt-12 flex items-center justify-center min-h-screen bg-gradient-to-br from-gray-100 via-gray-200 to-gray-300">
        <div className="w-full max-w-md bg-dadde2 p-8 shadow-lg rounded-lg">
          <h2 className="text-2xl font-semibold text-gray-800 mb-6 text-center">Sign Up</h2>
          <SignUp 
            signInUrl="/dashboard"
            afterSignUpUrl="/dashboard"
            onSignUpComplete={handleSignUpComplete}
            className="space-y-4"
          />
          <div className="text-center mt-4">
            <p className="text-gray-600 text-sm">
              Already have an account? 
              <a href="/sign-in" className="text-blue-500 font-semibold hover:underline"> Sign In</a>
            </p>
          </div>
        </div>
      </div>
    </SignedOut>
  );
}

export default SignUpPage;
