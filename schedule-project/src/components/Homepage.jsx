import React, {useState, useEffect, useCallback} from 'react';
import { useNavigate } from 'react-router-dom';
import CalendarList from './CalendarList';
import './Homepage.css';
import Button from '@mui/material/Button';
import jwt from 'jsonwebtoken';

const HomePage = () => {
    const [user_id, setUserId] = useState(null);
    const [calenders, setCalenders] = useState([]);
    const [sortedCalenders, setSortedCalenders] = useState([]);
    const [calender_id, setCalenderId] = useState(null);

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

    const fetchCalenders = useCallback(async () => {
        if (user_id) {
            const token = getToken();
            try {
                const response = await fetch(`http://localhost:8080/api/user/${user_id}/calender`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`,
                    },
                });

                if (response.ok) {
                    const userCalenders = await response.json();
                    setCalenders(userCalenders);
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
        fetchCalenders();
    }, [fetchCalenders]);

    useEffect(() => {
        const sorted = calenders
            .slice()
            .sort((a, b) => parseInt(b.calender_id, 10) - parseInt(a.calender_id, 10));
        setSortedCalenders(sorted);
    }, [calenders]);


    const handleNewCalenderClick = async () => {
        const title = prompt('Enter the title for the new calendar:');
        if(title){
            const token = getToken();
            try{
                const response = await fetch(`http://localhost:8080/api/user/${user_id}/calender`, {
                    method: 'POST',
                    headers:{
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`,
                    },
                    body: JSON.stringify({
                        calenderTitle: title,
                        user_id: user_id,
                    }),
                });
                if(response.ok){
                    const newCalender = await response.json();
                    console.log('New Calender:', newCalender);
                    const updatedCalender = {...newCalender, title};
                    console.log('Updated Calender:', updatedCalender);
                    setCalenders((prevCalenders) => [...prevCalenders, updatedCalender]);
                    fetchCalenders();
                    navigate(`/calendar/${newCalender.id}`);
                } else {
                    console.error('Failed to create a new calendar');
                }
            }catch(error){
                console.error('Failed creating a new calendar:', error);
            }
        }
    };

    const handleDeleteCalenderClick = () =>{
        setCalenderId(calender_id);
    };


    const handleDeleteCalenderConfirm = async () =>{
        if(calender_id){
            const token = getToken();
            try{
                const response = await fetch (`http://localhost:8080/api/user/${user_id}/calender/${calender_id}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`,
                    },
                });
                if(response.ok){
                    const deletedMessage = await response.text();
                    console.log(deletedMessage);

                    fetchCalenders();
                } else{
                    console.error('Failed to delete the calendar');
                }
            } catch(error){
                console.error('Error deleting the calendar:', error);
            }finally{
                setCalenderId(null);
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
                <Button variant="contained" size="large" onClick = {handleNewCalenderClick}>
                    New Calendar
                </Button>
                </div>
            </div>
            <h1> </h1>
            {calenders.length === 0 ? (
                <div className ="empty-message">
                    <p>You have no calendars yet.</p>
                </div>
            ) : (
                <div className="calendar-list" style={{ maxHeight: '300px', overflowY: 'auto' }}>
                    <CalendarList calendars={sortedCalenders} />
                </div>
            )}
        </div>
    </div>
    );
};

export default HomePage;
