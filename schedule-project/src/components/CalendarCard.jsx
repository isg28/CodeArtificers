import React, { useState, useEffect} from 'react';
import { Link } from 'react-router-dom';
import ModeEditIcon from '@mui/icons-material/ModeEdit';
import DeleteIcon from '@mui/icons-material/Delete';
import IconButton from '@mui/material/IconButton';
import PeopleIcon from '@mui/icons-material/People';
import './CalendarCard.css';
import jwt from 'jsonwebtoken';

const CalendarCard = ({ calendar }) => {
    const [user_id, setUserId] = useState(null);
    const [calendars, setCalendars] = useState([]);
    const [showUserList, setShowUserList] = useState(false);
    const [userList, setUserList] = useState([]);

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

    const onDelete = (calendar_id) => {
        setCalendars((prevCalendars) => prevCalendars.filter((calendar) => calendar.calendar_id !== calendar_id));

        console.log("Updated Calendars:", calendars);
    };

    const handleDelete = async (clickEvent) => {
        clickEvent.preventDefault();
        const token = getToken();
        try{
            const response = await fetch(`http://localhost:8080/api/user/${calendar.user_id}/calendar/${calendar.calendar_id}`,{
                method: 'DELETE',
                headers:{
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                }
            });
            if(response.ok){
                onDelete(calendar.calendar_id);
                //navigate(`/homepage`);
                window.location.reload();
            } else{
                console.error('Error deleting calendar. Status:', response.status);
            }
        } catch (error){
            console.error('Error deleting calendar:', error);
        }
    };

    const handleEdit = async (clickEvent) => {
        clickEvent.preventDefault();
        const editedTitle = window.prompt('Enter the new calendar title:', calendar.calendarTitle);
        console.log(editedTitle);

        if (editedTitle === null || editedTitle.trim() === '') {
            return;
        }
        const token = getToken();
        try {
            const response = await fetch(`http://localhost:8080/api/user/${calendar.user_id}/calendar/${calendar.calendar_id}`, {
                method: 'PUT',
                headers:{
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify({
                    calendarTitle: editedTitle,
                }),
            });
            if(response.ok){
                setCalendars((prevCalendars) =>
                prevCalendars.map((c) => (c.calendar_id === calendar.calendar_id ? { ...c, calendarTitle: editedTitle } : c))
                );
                window.location.reload();
            } else {
                console.error('Error editing calendar. Status: ', response.status);
                const responseBody = await response.text();
                console.error('Error response body:', responseBody);
            }

        }catch (error){
            console.error('Error editing calendar: ', error);
        }
    };
    const handleUserListClick = async (clickEvent) => {
        clickEvent.preventDefault();
        setShowUserList(true);
        await fetchUserList();
    };
    const fetchUserList = async () => {
        const token = getToken();
        try {
            const response = await fetch(`http://localhost:8080/api/user/${user_id}/calendar/${calendar.calendar_id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
            });
            if (response.ok) {
                const users = await response.json();
                setUserList(users);
            } else {
                console.error('Error fetching user list. Status:', response.status);
            }
        } catch (error) {
            console.error('Error fetching user list:', error);
        }
    };

return (
    <Link to={`/calendar/${calendar.calendar_id}`} className="calendar-link">
        <div className="calendar-card">
            <div className="card-content">
                <h3>{calendar.calendarTitle}</h3>
            </div>

            <div className= "card-icons">
                <IconButton aria-label="users" onClick={handleUserListClick}>
                    <PeopleIcon />
                </IconButton>

                <IconButton aria-label = "edit" onClick = {handleEdit}>
                    <ModeEditIcon />
                </IconButton>

                <IconButton aria-label = "delete" onClick = {handleDelete}>
                    <DeleteIcon />
                </IconButton>
            </div>
            {showUserList && (
                <div className="user-list-popup">
                <h4>Users with access to the calendar:</h4>
                <ul>
                {userList.map((user) => (
                    <li key={user.user_id}>{user.username}</li>
                ))}
                </ul>
                <button onClick={(e) => { e.preventDefault(); setShowUserList(false); }}>Close</button>
                </div>
            )}
        </div>
    </Link>
    );
};
export default CalendarCard;