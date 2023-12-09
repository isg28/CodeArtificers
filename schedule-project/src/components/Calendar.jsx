import React, {useState, useEffect } from "react";
import Fullcalendar from "@fullcalendar/react"; 
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import jwt from 'jsonwebtoken';
import moment from 'moment-timezone';
import {useParams} from 'react-router-dom';
import './Calendar.css'

const getToken = () => {
  const token = localStorage.getItem("token");
  if (!token) {
    console.error('Token not found. User not authenticated.');
  }
  return token;
};

const getUserIdFromToken = (token) => {
  try {
    const decodedToken = jwt.decode(token);
    return decodedToken ? decodedToken.user_id : null;
  } catch (error) {
    console.error("Error decoding token: ", error);
    return null;
  }
};

function Calendar(){
    const {calendar_id} = useParams();
    const [events, setEvents] = useState([]);
    const [user_id, setUserId] = useState(null);

    useEffect(() => {
      const token = getToken();
  
      const fetchUserData = async () => {
        if (token) {
          try {
            const decodedToken = jwt.decode(token);
            if (decodedToken) {
              const userIdFromToken = decodedToken.user_id;
              setUserId(userIdFromToken);
  
              if (userIdFromToken && calendar_id) {
                await fetchAvailabilityEvents(userIdFromToken, calendar_id);
                fetchMeetingEvents();
              }
            }
          } catch (error) {
            console.error("Error decoding token: ", error);
          }
        }
      };
  
      fetchUserData();
    }, [calendar_id]);
        
    
    useEffect(() => {
      if (user_id && calendar_id) {
        fetchAvailabilityEvents();
      }
      console.log('User ID from token:', user_id); // for checking purposes
    }, [user_id, calendar_id]);

    useEffect(() => {
      const fetchData = async () => {
        const token = getToken();
        const userIdFromToken = getUserIdFromToken(token);
    
        if (userIdFromToken && calendar_id) {
          await fetchAvailabilityEvents(userIdFromToken, calendar_id);
          fetchMeetingEvents();
        }
      };
    
      fetchData();
    }, [calendar_id]);
    

    const fetchAvailabilityEvents = async () => {
      const token = getToken();
      const userIdFromToken = getUserIdFromToken(token); // Define userIdFromToken here

      try{
        if(!userIdFromToken){
          console.error('User ID is undefined');
        }
        if(!calendar_id){
          console.error('Calendar ID is undefined');
        }
          
        const response = await fetch(`http://localhost:8080/api/user/${userIdFromToken}/calendar/${calendar_id}/availability`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
          }
        });     

        if(response.ok){
          const data = await response.json();
          

      // Update the events state with the fetched data
        const formattedAvailabilityEvents = data.map(event => ({
          ...event,
        })); 
        setEvents(formattedAvailabilityEvents);
        } else{
          console.error('Failed to fetch events');
        }
      
      } catch(error){
          console.error('Error fetching events: ', error);
        }
      };
      
      const findEventByDate = (date) => {
        const clickedDateUtc = moment.tz(date, 'UTC').format('YYYY-MM-DD');

        return events.filter((event) => {
          console.log('Raw event start:', event.start);
          const eventStartDate = moment.tz(event.start, 'UTC').format('YYYY-MM-DD');
          return eventStartDate === clickedDateUtc;
      });

    };
      
    const handleDateClick = async (info) => {
      const existingEvents = findEventByDate(info.dateStr);

      
      console.log('Clicked Date:', info.dateStr);
      console.log('Existing Events:', existingEvents);

      if(existingEvents.length > 0){
        const userChoice = prompt('Choose an action: \n1. Add a new event \n2. Edit an existing event\n3. Delete an existing event');

        if(userChoice === '1'){
          addEvent(info);
        } else if(userChoice === '2'){
          const eventOptions = existingEvents.map((event, index) => `${index + 1}. ${event.title}`);
          const userChoiceIndex = parseInt(prompt(`Choose an event to edit:\n${eventOptions.join('\n')}`));

          if(!isNaN(userChoiceIndex) && userChoiceIndex >= 1 && userChoiceIndex <= existingEvents.length){
            const chosenIndex = userChoiceIndex - 1;
            const chosenDate = existingEvents[chosenIndex].date;
            editEvent(existingEvents[chosenIndex], chosenIndex,chosenDate);
          }else {
            alert("Invalid choice. Please choose a valid event number.");
          }
        }else if(userChoice === '3'){
          const eventOptions = existingEvents.map((event, index) => `${index + 1}. ${event.title}`);
          const userChoiceIndex = parseInt(prompt(`Choose an event to delete:\n${eventOptions.join('\n')}`));
    
          if (!isNaN(userChoiceIndex) && userChoiceIndex >= 1 && userChoiceIndex <= existingEvents.length) {
            const chosenIndex = userChoiceIndex - 1;
            const chosenEvent = existingEvents[chosenIndex];
            const shouldDelete = window.confirm(`Are you sure you want to delete the event "${chosenEvent.title}" on this date (${info.dateStr})?`);
    
            if (shouldDelete) {
              deleteEvent([chosenEvent]); 
            }
        }
    } else {
      alert('Invalid choice. Please choose 1, 2, or 3.');
    }
  } else{
      const userChoice = prompt('No existing events. Choose an action: \n1. Add a new event');
      if (userChoice === '1') {
        addEvent(info);
      } else {
        alert('Invalid choice. Please choose 1.');
      }
    }
  };


    const editEvent = async (existingEvent, chosenIndex, chosenDate) => {
      console.log('Existing Event: ', existingEvent);
      const newTitle = prompt('Edit event title:', existingEvent.title);
      let newStartTime, newEndTime;

      newStartTime = prompt('Edit start time:', moment(existingEvent.start).format('HH:mm'));
      newEndTime = prompt('Edit end time:', moment(existingEvent.end).format('HH:mm'));
    
      if (!isValidInputTimeValue(newStartTime) || !isValidInputTimeValue(newEndTime)) {
        alert('Invalid input. Please make sure to enter valid start/end times.');
        return;
      }
      

      console.log('New Title:', newTitle);
      console.log('New Start Time:', newStartTime);
      console.log('New End Time:', newEndTime);

      const token = getToken();
      const userIdFromToken = getUserIdFromToken(token);
      if (newTitle && isValidInputTimeValue(newStartTime) && isValidInputTimeValue(newEndTime)) {
        try {
          console.log('existingEvent:', existingEvent);

          const updatedEvent = {
            ...existingEvent,
            title: newTitle,
            start: `${chosenDate}T${newStartTime}:00`,
            end: `${chosenDate}T${newEndTime}:00`,
          };

          console.log('updatedEvent:', updatedEvent);
    
          setEvents((prevEvents) => [
            ...prevEvents.slice(0, chosenIndex),
            updatedEvent,
            ...prevEvents.slice(chosenIndex + 1),
          ]);

          if (!updatedEvent.availability_id) {
            console.error('Availability ID is missing. Cannot update event.');
            return;
          }
          
        
    
          const response = await fetch(`http://localhost:8080/api/user/${userIdFromToken}/calendar/${calendar_id}/availability/${existingEvent.availability_id}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`,
            },
            
            body: JSON.stringify(updatedEvent),
          });
    
          if (response.ok) {
            console.log('Event updated successfully');
            window.location.reload(); 
          } else {
            console.error('Failed to update event');
            console.log('Failed Event: ', existingEvent);
          }
        } catch (error) {
          console.error('Error updating event:', error);
        }
      } else {
        alert('Invalid input. Please make sure to enter a title and valid start/end time.');
      }
    };



    
    const addEvent = async(info) =>{
      if(!user_id){
        console.error('User ID is undefined');
        return
      }

      // Fetch events to get the availability_id
      await fetchAvailabilityEvents();

       // Find the availability_id from the existing events
      const existingEvents = findEventByDate(info.dateStr);
      let availability_id = existingEvents.length > 0 ? existingEvents[0].availability_id : null;
      // Check if the availability_id is present in the decoded token
      const token = localStorage.getItem("token");

      if (token) {
        try {
          const decodedToken = jwt.decode(token);
          if (decodedToken && decodedToken.availability_id) {
            availability_id = decodedToken.availability_id;
          }
        } catch (error) {
          console.error("Error decoding token: ", error);
       }
      }
      
      
      const title = prompt('Enter event title: ');

      const formattedDate = new Date(info.date);
      formattedDate.setHours(0, 0, 0, 0);
      const formattedDateString = moment(formattedDate).format('YYYY-MM-DD');
      let startDateTime, endDateTime

      const startTime = prompt('Enter start time (HH:mm). If mornings (1 AM- 9 AM) please input time with the left most digit in the hour as 0. For example, 01:30. If after 12pm, please put in military time:');
      const endTime = prompt('Enter end time: (HH:mm). If mornings (1 AM- 9 AM) please input time with the left most digit in the hour as 0. If after 12pm, please put in military time:');

      if(title && isValidInputTimeValue(startTime)&& isValidInputTimeValue(endTime)){
        startDateTime = moment(`${formattedDateString}T${startTime}`);
        endDateTime = moment(`${formattedDateString}T${endTime}`);                   
      }else{
          alert('Invalid input. Please make sure to enter a title and valid start/end time.');
          return
      }
        

      startDateTime = startDateTime.tz('local');
      endDateTime = endDateTime.tz('local');


      const newEvent = {
        user_id: user_id,
        title: title,
        date: formattedDateString,
        start: startDateTime.format('YYYY-MM-DDTHH:mm:ss.SSS'), 
        end: endDateTime.format('YYYY-MM-DDTHH:mm:ss.SSS'),       
        availability_id: availability_id,
        };
        
        setEvents((prevEvents) => [...prevEvents, newEvent]);
        console.log("Event to be saved: ", newEvent);
        console.log('Formatted Date (Frontend):', formattedDate);
        console.log('Formatted Start Time (Frontend):', startDateTime.format('YYYY-MM-DDTHH:mm:ss.SSS'));
        console.log('Formatted End Time (Frontend):', endDateTime.format('YYYY-MM-DDTHH:mm:ss.SSS'));

        try{
          const token = getToken();
          const userIdFromToken = getUserIdFromToken(token);
           const response = await fetch(`http://localhost:8080/api/user/${userIdFromToken}/calendar/${calendar_id}/availability`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(newEvent),
          });
          
            // Handle the response as needed
            if (response.ok) {
              console.log('Event saved successfully');
            } else {
              console.error('Failed to save event');
              // Optionally handle error cases
              console.error('Response status: ', response.status);
              const errorMessage = await response.text();
              console.error('Error message: ', errorMessage);
            }
          } catch (error) {
          console.error('Error:', error);
        }
      };

      const deleteEvent = async (eventsToDelete) => {
        if (!eventsToDelete || !Array.isArray(eventsToDelete)) {
          console.error('Invalid input for eventsToDelete');
          return;
        }

        const token = getToken();
        const userIdFromToken = getUserIdFromToken(token);

        try{
          for (const eventToDelete of eventsToDelete) {
            const response = await fetch(`http://localhost:8080/api/user/${userIdFromToken}/calendar/${calendar_id}/availability/${eventToDelete.availability_id}`, {
              method: 'DELETE',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
              },
            });

            if(response.ok) {
              console.log('Event deleted successfully');
            } else {
              console.error('Failed to delete event');
              // Optionally handle error cases
              console.error('Response status: ', response.status);
              const errorMessage = await response.text();
              console.error('Error message: ', errorMessage);
            }
        }
        setEvents((prevEvents) => prevEvents.filter((event) => eventsToDelete.indexOf(event) === -1));
      } catch (error) {
          console.error('Error deleting events:', error);
        }
      };


      ////////////////////////////////////////////////////////////////////////////////////////////////
/*                          MEETING  FUNCTIONS                          */
      const [showCreateMeetingForm, setShowCreateMeetingForm] = useState(false);
      const [newMeeting, setNewMeeting] = useState({
        date: "",
        startTime: "",
        endTime: "",
        location: "",
        meeting_Description: "",
      });

      const fetchMeetingEvents = async () => {
        try{
          const token = getToken();
          const userIdFromToken = getUserIdFromToken(token);
          const response = await fetch(`http://localhost:8080/api/user/${userIdFromToken}/calendar/${calendar_id}/meeting`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`,
            },
          });
          
          if(response.ok) {
            const data = await response.json();

            //Update the events state with the fetched meeting data
            const formattedMeetingEvents = data.map(meeting => ({
              id: meeting.meeting_id,
              title: meeting.meeting_Description,
              start: meeting.start,
              end: meeting.end,
            }));
            setEvents(prevEvents => [...prevEvents, ...formattedMeetingEvents]);
          }
          else {
            console.error('Failed to fetch meeting events');
          }
        } catch(error){
          console.error('Error fetching meeting events:', error);
        }
      };

      const handleCreateMeeting = async () => {
        try {
          const token = getToken();
          const userIdFromToken = getUserIdFromToken(token);
  
          const startDateTime = `${newMeeting.date}T${newMeeting.startTime}`;
          const endDateTime = `${newMeeting.date}T${newMeeting.endTime}`;
  
          const response = await fetch(`http://localhost:8080/api/user/${userIdFromToken}/calendar/${calendar_id}/meeting`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          date: newMeeting.date,
          start: startDateTime,
          end: endDateTime,
          location: newMeeting.location,
          meeting_Description: newMeeting.meeting_Description,
        }),
      });
  
      if (response.ok) {
        const createdMeeting = await response.json();
        console.log("Meeting created successfully:", createdMeeting);

        // Reset the form and hide the create meeting form
        setNewMeeting({
          date: "",
          startTime: "",
          endTime: "",
          location: "",
          meeting_Description: "",
        });
        setShowCreateMeetingForm(false);

        // Fetch meeting events to update the state
        await fetchMeetingEvents();

        // Log the updated events state
        console.log('Updated Events:', events);

        alert('Meeting created successfully');
      
        //setEvents((prevEvents) => [...prevEvents, createdMeeting]);
        alert('Meeting created successfully');
      } else {
        console.error("Failed to create meeting");
        // handle some error cases
        console.error("Response status: ", response.status);
        const errorMessage = await response.text();
        console.error("Error message: ", errorMessage);
      }
    } catch (error) {
      console.error("Error creating meeting:", error);
    }
  
    // Reset the form and hide the create meeting form
    setNewMeeting({
      date: "",
      startTime: "",
      endTime: "",
      location: "",
      meeting_Description: "",
    });
    setShowCreateMeetingForm(false);
  };
  
    
    const handleInputChange = (e) => {
      const { name, value } = e.target;
      setNewMeeting((prevMeeting) => ({
        ...prevMeeting,
        [name]: value,
      }));
    }; 
    
    const isValidInputTimeValue = (time) => {
      const regex = /^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]([APMapm]{2})?$/;
      return regex.test(time);
    };

    const toggleCreateMeetingForm = () => {
      console.log("Toggling create meeting form");
      setShowCreateMeetingForm(!showCreateMeetingForm);
    };

    const eventContent = (eventInfo) => {
      //const isMeeting = eventInfo.event.extendedProps.isMeeting;
      return (
        <>
          <div className= "custom-dot" />
          <div className = "fc-content">
            {eventInfo.timeText && (
              <div className = "fc-time bold-time">{eventInfo.timeText}</div>
            )}
            <div className= "fc-time">{eventInfo.event.title}</div>
          </div>
        </>
      );
    };

    return(
      <div>
        <h1> </h1>
        <Box sx={{ display: 'flex', justifyContent: 'flex-end','& button': { m: 1, backgroundColor: 'black' } }}>
        <Button variant="contained" size="large" onClick={toggleCreateMeetingForm}>
        Create Meeting
        </Button>  
      <div>
        <Button variant="contained" size="large">
          Common TimeSlots
        </Button>
        <Button variant = "contained" size = "large"> Invite Users </Button>
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
          eventContent={eventContent}
          />
          {showCreateMeetingForm && (
        // Renders the pop up box to create a meeting
      
        <div
        style={{
        position: "fixed",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        backgroundColor: "#3087CF",
        padding: "20px",
        zIndex: 1000,
          }}
        >
          <label style={{ color: 'white' }}>Date:</label>
          <input
          type="date"
          name="date"
          value={newMeeting.date}
          onChange={handleInputChange}
          />
          <label style={{ color: 'white' }}>Start Time:</label>
          <input
          type="time"
          name="startTime"
          value={newMeeting.startTime}
          onChange={handleInputChange}
          />
          <label style={{ color: 'white' }}>End Time:</label>
          <input
          type="time"
          name="endTime"
          value={newMeeting.endTime}
          onChange={handleInputChange}
          />
          <label style={{ color: 'white' }}>Location:</label>
          <input
          type="text"
          name="location"
          value={newMeeting.location}
          onChange={handleInputChange}
          />
          <label style={{ color: 'white' }}>Meeting Description:</label>
          <textarea
          name="meeting_Description"
          value={newMeeting.meeting_Description}
          onChange={handleInputChange}
          ></textarea>
          <div>
          <button onClick={handleCreateMeeting} >
            Create Meeting</button>
            </div>
            {/* Cancel button */}
          <div>
          <button onClick={() => setShowCreateMeetingForm(false)}>Cancel</button>
          </div>
          </div>
        )}
      </div>
      );
    };
    export default Calendar;