import './App.css';
import React from 'react';
import AppBar from './components/AppBar';
import User from './components/User';

const headingStyle = {
  fontSize: '2rem',
  fontWeight: 'bold',
  margin: '20px 0', 
  textAlign: 'center', 
  color: '#333', 
};


function App() {
  return (
    <div className="App">
      <AppBar />
      <div style={headingStyle}>
      Schedule Your Availabilities Now
      </div>
      <User />
    </div>
  );
}

export default App;


  

