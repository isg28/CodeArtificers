import './App.css';
import React from 'react';
import AppBar from './components/AppBar';

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
      Welcome to the SCHEDULING APP!
      </div>
      
    </div>
  );
}

export default App;


  

