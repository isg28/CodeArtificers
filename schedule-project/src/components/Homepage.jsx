import React from 'react';
import CalendarList from './CalendarList';
import './Homepage.css';
import Button from '@mui/material/Button';

const HomePage = () => {
  // Dummy data for calendars
    const calendars = [ //this is hardcoded data, get rid of this when we create calendar api
        { id: 1, name: ' Calendar 1' },
        { id: 2, name: ' CSC131 Meeting Calendar' },
        {id: 3, name: 'Calendaring Calendar'},
    ];

    const sortedCalendars = calendars.slice().sort((a, b) => b.id - a.id);


    console.log('Original Calendars:', calendars);
    console.log('Sorted Calendars:', sortedCalendars);

    return (
        <div className="home-container">
            <h2> </h2>
        <div className="white-box">
        <div className = "header">
            <h1>My Calendars</h1>
            <div className="button-container">
                <Button variant="contained" size="large">
                    New Calendar
                </Button>
                <Button variant="contained" size="large" style={{ backgroundColor: 'grey', color: 'white'}}>
                    Delete Calendar
                </Button>
                </div>
            </div>
            <h1></h1>
        <CalendarList calendars={sortedCalendars} />
        </div>
    </div>
    );
};

export default HomePage;
