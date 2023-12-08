import React from 'react';
import { Link } from 'react-router-dom';

const CalendarCard = ({ calendar }) => {
    return (
        <Link to={`/calendar/${calendar.calender_id}`} className="calendar-link">
            <div className="calendar-card">
            <h3>{calendar.calenderTitle}</h3>
            {/* Add additional information if needed */}
        </div>
        </Link>
    );
};

export default CalendarCard;