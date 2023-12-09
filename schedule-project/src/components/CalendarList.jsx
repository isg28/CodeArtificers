import React from 'react';
import { Link } from 'react-router-dom';
import CalendarCard from './CalendarCard'; // Create a new component for each calendar card

const CalendarList = ({ calendars }) => {
    return (
        <div className="calendar-list">
            {calendars.map((calendar) => (
                <CalendarCard key={calendar.calendar_id} calendar={calendar} />
            ))}
        </div>
    );
};
    
export default CalendarList;