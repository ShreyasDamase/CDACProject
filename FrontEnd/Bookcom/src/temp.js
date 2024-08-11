// App.js
import React from 'react';
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom';
import { RedirectToSignIn, SignedIn, SignedOut, useClerk } from '@clerk/clerk-react';
import Dashboard from './Dashboard';
import Home from './Home';

function App() {
  const { isLoaded, user } = useClerk();

  if (!isLoaded) {
    return <div>Loading...</div>;
  }

  return (
    <Router>
      <Switch>
        <Route exact path="/">
          {user ? <Redirect to="/dashboard" /> : <Home />}
        </Route>
        <Route path="/dashboard">
          <SignedIn>
            <Dashboard />
          </SignedIn>
          <SignedOut>
            <RedirectToSignIn />
          </SignedOut>
        </Route>
      </Switch>
    </Router>
  );
}

export default App;

// import React, { useState } from 'react';
// import axios from 'axios';

// export default function App() {
//   // const [selectedFile, setSelectedFile] = useState(null);

//   // const handleFileChange = (event) => {
//   //   setSelectedFile(event.target.files[0]);
//   // };

//   // const handleSubmit = async (event) => {
//   //   event.preventDefault();
//   //   if (!selectedFile) {
//   //     alert("Please select a file first!");
//   //     return;
//   //   }

//   //   const formData = new FormData();
//   //   formData.append('file', selectedFile);

//   //   try {
//   //     const response = await axios.post('http://localhost:8080/Bookcom/upload', formData, {
//   //       headers: {
//   //         'Content-Type': 'multipart/form-data',
//   //       },
//   //     });
//   //     console.log(response.data);
//   //     alert('File uploaded successfully!');
//   //   } catch (error) {
//   //     console.error(error.message);
//   //     alert('File upload failed!');
//   //   }
//   // };

//   return (
//     <div>
//       <form onSubmit={handleSubmit}>
//         <input type="file" name="file" onChange={handleFileChange} />
//         <button type="submit">Upload</button>
//       </form>
//     </div>
//   );
// }
