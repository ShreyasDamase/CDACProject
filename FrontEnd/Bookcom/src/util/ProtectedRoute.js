import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useUser,useClerk } from '@clerk/clerk-react';
import axios from 'axios';
const ProtectedRoute = () => {
  const { isLoaded, isSignedIn,user } = useUser();
    const {session}=useClerk();
  if (!isLoaded) {
    return <div>Loading...</div>;
  }
 
  return isSignedIn ? <Outlet /> : <Navigate to="/sign-up" />;
};

export default ProtectedRoute;
