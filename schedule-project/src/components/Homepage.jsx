import React, {useState, useEffect, useCallback} from 'react';
import CalendarList from './CalendarList';
import './Homepage.css';
import Button from '@mui/material/Button';
import jwt from 'jsonwebtoken';
import discord_logo from './assets/discord-icon.png';

const HomePage = () => {
    const [user_id, setUserId] = useState(null);
    const [calendars, setCalendars] = useState([]);
    const [sortedCalendars, setSortedCalendars] = useState([]);


    useEffect(() => {
        const token = localStorage.getItem("token");
        if(token){
            try{
                const decodedToken = jwt.decode(token);
                console.log(decodedToken);
                const user_id = decodedToken.user_id;
                setUserId(user_id);
                console.log(user_id);
            }catch(error){
                console.error("Error decoding token: ", error);
            } 
        }
    }, []);

    const getToken = () => {
        const token = localStorage.getItem("token");
        if(!token){
            console.error('Token not found. User not authenticated.');
        }
        return token;
    };

    const fetchCalendars = useCallback( async () => {
        if (user_id) {
            const token = getToken();
            try {
                const response = await fetch(`http://localhost:8080/api/user/${user_id}/calendar`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`,
                    },
                });

                if (response.ok) {
                    const userCalendars = await response.json();
                    setCalendars(userCalendars);
                } else if (response.status === 404) {
                    console.log('User or calendars not found.');
                } else {
                    console.error('Failed to fetch user calendars.');
                }
            } catch (error) {
                console.error('Error fetching user calendars:', error);
            }
        }
    }, [user_id]);


    useEffect(() => {
        fetchCalendars();
    }, [fetchCalendars]);


    useEffect(() => {
        const sorted = calendars.slice().sort((a, b) => parseInt(b.calendar_id, 10) - parseInt(a.calendar_id, 10));
        setSortedCalendars(sorted);
    }, [calendars]);


    const handleNewCalendarClick = async () => {
        const title = prompt('Enter the title for the new calendar:');
        if(title){
            const token = getToken();
            try{
                const response = await fetch(`http://localhost:8080/api/user/${user_id}/calendar`, {
                    method: 'POST',
                    headers:{
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`,
                    },
                    body: JSON.stringify({
                        calendarTitle: title,
                        user_id: user_id,
                    }),
                });

                if(response.ok){
                    const newCalendar = await response.json();
                    console.log('New Calendar:', newCalendar);
                    const updatedCalendar = {...newCalendar, title};
                    console.log('Updated Calendar:', updatedCalendar);
                    setCalendars((prevCalendars) => [...prevCalendars, updatedCalendar]);
                    fetchCalendars();
                } else {
                    console.error('Failed to create a new calendar');
                }
            }catch(error){
                console.error('Failed creating a new calendar:', error);
            }
        }
    };

return (
    <div className="home-container">
        <h2> </h2>
        <div className="white-box">
        <div className = "header">
            <h1>My Calendars</h1>
            <div className="button-and-icon-container">
                <Button variant="contained" size="large" onClick = {handleNewCalendarClick}>
                    New Calendar
                </Button>
            
            </div>
        </div>
            <h1> </h1>
            {calendars.length === 0 ? (
                <div className ="empty-message">
                    <p>You have no calendars yet.</p>
                </div>
            ) : (
                <div className="calendar-list" style={{ maxHeight: '300px', overflowY: 'auto' }}>
                    <CalendarList calendars={sortedCalendars} />
                </div>
            )}
        </div>
    </div>
    );
};
export default HomePage;
