import './App.css';
import React from 'react';
import AppBar from './components/AppBar';
import Button from '@mui/material/Button';
import { Link } from 'react-router-dom';


const headingStyle = {
  fontSize: '5rem',
  fontWeight: 'bold',
  margin: '20px 0',
  textAlign: 'center',
  color: '#FFFFFF',
};

const headerContainerStyle = {
  backgroundColor: '#1434A4', // Dark blue background color
  padding: '200px',
  //borderRadius: '8px', // Optional: Add rounded corners
};




function App() {
  return (
    <div className="App">
      <AppBar />
      <div style={headerContainerStyle}>
      <div style={{ maxWidth: '1200px', margin: 'auto', padding: '20px' }}>
        <header style={{ textAlign: 'center', marginBottom: '40px' }}>
          <h1 style={{ fontSize: '5rem', fontWeight: 'bold', color: '#FFFFFF' }}>
            Scheduling, 
            made with ease
          </h1>
          <p style={{ fontSize: '1.2rem', color: '#FFFFFF' }}>
            Create calendars by yourself or with friends. Keep track of meetings and your availabilities as well as being able to create meetings at a touch of a button.
          </p>
        </header>

      </div>

      <Button component={Link} to="/custom-login" sx={{ fontSize: '1.5rem', padding: '15px 40px', backgroundColor: '#F0FFFF'}}> Get Started </Button>
      
    </div>
    </div>
  );
}

export default App;


