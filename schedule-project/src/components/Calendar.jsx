import React, {useState, useEffect } from "react";
import Fullcalendar from "@fullcalendar/react"; 
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';

function Calendar(){
    const [events, setEvents] = useState([]);
    /*const [userId, setUserId] = useState(null);*/
    //hardcoded user so you can post events, will fix this when we get user authentication working
    const userId = 1;
    /*useEffect(() => {
      const fetchUserData = async () => {
        try{
          const response = await fetch('/api/user/{user_id}');
          const userData = await response.json();
          setUserId(userData.id);
        }catch (error){
          console.error("Error fetching user data: ", error);
        }
      };
      fetchUserData();
    }, []);*/
    /*const UserAuthentication = () => {
      const [userId, setUserId] = useState(null);

      useEffect(() => {
        const authenticateUser = async () => {
          try {
            // Replace this with your actual logic to get the currently logged-in user's email and password
            const currentUserEmail = getCurrentUserEmail();
            const currentUserPassword = getCurrentUserPassword();
    
            const loginRequest = {
              email: currentUserEmail,
              password: currentUserPassword,
            };
    
            const response = await fetch('/api/login', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
              },
              body: JSON.stringify(loginRequest),
            });
    
            if (response.ok) {
              const authenticatedUserId = await response.json();
              setUserId(authenticatedUserId);
            } else {
              // Handle authentication failure
              console.error('Authentication failed');
            }
          } catch (error) {
            console.error('Error authenticating user: ', error);
          }
        };
    
        authenticateUser();
      }, []);
    };*/


    const handleDateClick = async (info) => {
      const title = prompt('Enter event title: ');
        const isAllDay = window.confirm('Is this an all-day event?');

        let startDateTime, endDateTime;

        if(isAllDay){
          startDateTime = info.dateStr;
          endDateTime = info.dateStr;
        }else{
          const startTime = prompt('Enter start time (HH:mm). If after 12pm, please put in military time:');
          const endTime = prompt('Enter end time: (HH:mm). If after 12pm, please put in military time:');

          if(title && isValidInputTimeValue(startTime)&& isValidInputTimeValue(endTime)){
            startDateTime = `${info.dateStr}T${startTime}`;
            endDateTime = `${info.dateStr}T${endTime}`;
          }else{
            alert('Invalid input. Please make sure to enter a title and valid start/end time.');
            return
          }
        }

        const newEvent = {
          title: title,
          userId: userId,
          date: info.dateStr,
          start: startDateTime,
          end: endDateTime,
          allDay: false,
        };
        setEvents((prevEvents) => [...prevEvents, newEvent]);


        try{
          const response = await fetch(`http://localhost:8080/api/user/${userId}/availability`,{
            method: 'POST',
            headers:{
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(newEvent),
          });
          
          if(response.ok){
            console.log('Event saved successfully');
          }else{
            console.error('Failed to save event');
            //Optionally handle error cases
          }
        } catch (error){
          console.error('Error saving event:', error);
        }
      //} else {
        //alert('Invalid input. Please make sure to enter a title and valid start/emd time.')
      //}
    };

    const isValidInputTimeValue = (time) => {
      const regex = /^([01]\d|2[0-3]):([0-5]\d)$/;
      return regex.test(time);
    };


    return(
      <div>
        <h1> </h1>
        <Box sx={{ display: 'flex', justifyContent: 'flex-end','& button': { m: 1, backgroundColor: 'black' } }}>
      <div>
        <Button variant="contained" size="large">
          Common TimeSlots
        </Button>
      </div>
    </Box>
        <h1> </h1>

        <Fullcalendar
          plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
          initialView={"dayGridMonth"}
          headerToolbar={{
          start: "today prev,next",
          center: "title",
          end: "dayGridMonth, timeGridWeek, timeGridDay",
          }}
          height={"98vh"}
          events = {events}
          dateClick={handleDateClick}
          />
          
      </div>
      );
    } 
    export default Calendar;