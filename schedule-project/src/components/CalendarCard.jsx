import React, { useState, useEffect} from 'react';
import { Link } from 'react-router-dom';
import ModeEditIcon from '@mui/icons-material/ModeEdit';
import DeleteIcon from '@mui/icons-material/Delete';
import IconButton from '@mui/material/IconButton';
import './CalendarCard.css';
import jwt from 'jsonwebtoken';

const CalendarCard = ({ calendar }) => {
    const [user_id, setUserId] = useState(null);
    const [calenders, setCalenders] = useState([]);


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

    const onDelete = (calender_id) => {
        // Update state to remove the deleted calendar
        setCalenders((prevCalendars) => prevCalendars.filter((calendar) => calendar.calender_id !== calender_id));

        console.log("Updated Calendars:", calenders);
    };

    const handleDelete = async (clickEvent) => {
        clickEvent.preventDefault();
        const token = getToken();
        try{
            const response = await fetch(`http://localhost:8080/api/user/${calendar.user_id}/calender/${calendar.calender_id}`,{
                method: 'DELETE',
                headers:{
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                }
            });
            if(response.ok){
                onDelete(calendar.calender_id);
                //navigate(`/homepage`);
                window.location.reload();
            } else{
                console.error('Error deleting calendar. Status:', response.status);
            }
        } catch (error){
            console.error('Error deleting calendar:', error);
        }
    }

    const handleEdit = async (clickEvent) => {
        clickEvent.preventDefault();
        const editedTitle = window.prompt('Enter the new calendar title:', calendar.calenderTitle);
        console.log(editedTitle);

        // If the user cancels the prompt or leaves the input empty, do nothing
        if (editedTitle === null || editedTitle.trim() === '') {
            return;
        }
        const token = getToken();
        console.log('Going into PUT method');
        try {
            const response = await fetch(`http://localhost:8080/api/user/${calendar.user_id}/calender/${calendar.calender_id}`, {
                method: 'PUT',
                headers:{
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify({
                    calenderTitle: editedTitle,
                }),
            });
            if(response.ok){
                setCalenders((prevCalendars) =>
                prevCalendars.map((c) => (c.calender_id === calendar.calender_id ? { ...c, calenderTitle: editedTitle } : c))
                );
                window.location.reload();
            } else {
                console.error('Error editing calendar. Status: ', response.status);
                const responseBody = await response.text(); // or response.json() if the error response is in JSON
                console.error('Error response body:', responseBody);
            }

        }catch (error){
            console.error('Error editing calendar: ', error);
        }
    };

    return (
        <Link to={`/calendar/${calendar.calender_id}`} className="calendar-link">
            <div className="calendar-card">
                <div className="card-content">
                <h3>{calendar.calenderTitle}</h3>
                </div>

            <div className= "card-icons">
            <IconButton aria-label = "edit" onClick = {handleEdit}>
                <ModeEditIcon />
            </IconButton>

            <IconButton aria-label = "delete" onClick = {handleDelete}>
                <DeleteIcon />
            </IconButton>

            </div>
        </div>
        </Link>
    );
};

export default CalendarCard;