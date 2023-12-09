import React, {useState, useEffect, useCallback} from 'react';
import { useNavigate } from 'react-router-dom';
import CalendarList from './CalendarList';
import './Homepage.css';
import Button from '@mui/material/Button';
import jwt from 'jsonwebtoken';

const HomePage = () => {
    const [user_id, setUserId] = useState(null);
    const [calendars, setCalendars] = useState([]);
    const [sortedCalendars, setSortedCalendars] = useState([]);
    const [calendar_id, setCalendarId] = useState(null);

    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("token");
        if(token){
            try{
                const decodedToken = jwt.decode(token);
                console.log(decodedToken) // for checking purposes
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

    const fetchCalendars = useCallback(async () => {
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
        // Initial fetch when the component mounts
        fetchCalendars();
    }, [fetchCalendars]);

    useEffect(() => {
        const sorted = calendars
            .slice()
            .sort((a, b) => parseInt(b.calendar_id, 10) - parseInt(a.calendar_id, 10));
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
                    navigate(`/calendar/${newCalendar.id}`);
                } else {
                    console.error('Failed to create a new calendar');
                }
            }catch(error){
                console.error('Failed creating a new calendar:', error);
            }
        }
    };

    const handleDeleteCalendarClick = () =>{
        setCalendarId(calendar_id);
    };


    const handleDeleteCalendarConfirm = async () =>{
        if(calendar_id){
            const token = getToken();
            try{
                const response = await fetch (`http://localhost:8080/api/user/${user_id}/calendar/${calendar_id}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`,
                    },
                });
                if(response.ok){
                    const deletedMessage = await response.text();
                    console.log(deletedMessage);

                    fetchCalendars();
                } else{
                    console.error('Failed to delete the calendar');
                }
            } catch(error){
                console.error('Error deleting the calendar:', error);
            }finally{
                setCalendarId(null);
            }
        }
    };

    return (
        <div className="home-container">
            <h2> </h2>
        <div className="white-box">
        <div className = "header">
            <h1>My Calendars</h1>
            <div className="button-container">
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
