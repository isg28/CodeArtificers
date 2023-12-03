import React, {useState, useEffect, useRef } from "react";
import Fullcalendar from "@fullcalendar/react"; 
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import jwt from 'jsonwebtoken';
import moment from 'moment-timezone';

function Calendar(){
    const [events, setEvents] = useState([]);
    const [user_id, setUserId] = useState(null);

    useEffect(() => {
      const token = localStorage.getItem("token");

      if(token){
        try{
          const decodedToken = jwt.decode(token);
          console.log(decodedToken) // for checking purposes
          const user_id = decodedToken.user_id;

          setUserId(user_id);
        }catch(error){
          console.error("Error decoding token: ", error);
        }
      }
    }, []);

    useEffect(() => {
      if(user_id){
        fetchEvents();
      }
      console.log('User ID from token:', user_id); //for checking purposes
    }, [user_id]);

    const fetchEvents = async () => {
      try{
        const token = getToken();
        if(!user_id){
          console.error('User ID is undefined');
        }
          
        const response = await fetch(`http://localhost:8080/api/user/${user_id}/availability`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
          }
        });     

        if(response.ok){
          const data = await response.json();
          
          // Update the events state with the fetched data
          const formattedEvents = data.map(event => ({
            ...event,
          }));
          setEvents(formattedEvents);
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
      
      
      


    /*const [userId, setUserId] = useState(null);*/
    //hardcoded user so you can post events, will fix this when we get user authentication working
    const [showCreateMeetingForm, setShowCreateMeetingForm] = useState(false);
    const [newMeeting, setNewMeeting] = useState({
      date: "",
      startTime: "",
      endTime: "",
      location: "",
      meeting_Description: "",
    });
    const handleCreateMeeting = async () => {
      console.log("Meeting created:", newMeeting);
      setNewMeeting({
            date: "",
            startTime: "",
            endTime: "",
            location: "",
            meeting_Description: "",
          });
          setShowCreateMeetingForm(false);
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
      const isAllDay = window.confirm('Is this an all-day event?');
      let newStartTime, newEndTime;

      if (isAllDay) {
        // When isAllDay is true
        newStartTime = '00:00';
        newEndTime = '23:59';
      } else {
        newStartTime = prompt('Edit start time:', moment(existingEvent.start).format('HH:mm'));
        newEndTime = prompt('Edit end time:', moment(existingEvent.end).format('HH:mm'));
    
        if (!isValidInputTimeValue(newStartTime) || !isValidInputTimeValue(newEndTime)) {
          alert('Invalid input. Please make sure to enter valid start/end times.');
          return;
        }
      }

      console.log('New Title:', newTitle);
      console.log('New Start Time:', newStartTime);
      console.log('New End Time:', newEndTime);
      console.log('Is All Day:', isAllDay);

      const token = getToken();
      if (newTitle && isValidInputTimeValue(newStartTime) && isValidInputTimeValue(newEndTime)) {
        try {
          console.log('existingEvent:', existingEvent);

          const updatedEvent = {
            ...existingEvent,
            title: newTitle,
            start: isAllDay ? `${chosenDate}T${newStartTime}:00` : `${chosenDate}T${newStartTime}:00`,
            end: isAllDay ? `${chosenDate}T${newEndTime}:00` : `${chosenDate}T${newEndTime}:00`,
            allDay: isAllDay,
          };

          console.log('updatedEvent:', updatedEvent);
    
          setEvents((prevEvents) => [
            ...prevEvents.slice(0, chosenIndex),
            updatedEvent,
            ...prevEvents.slice(chosenIndex + 1),
          ]);
    
          const response = await fetch(`http://localhost:8080/api/user/${user_id}/availability/${existingEvent.availability_id}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(updatedEvent),
          });
    
          if (response.ok) {
            console.log('Event updated successfully');
          } else {
            console.error('Failed to update event');
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
      await fetchEvents();

       // Find the availability_id from the existing events
      const existingEvents = findEventByDate(info.dateStr);
      const availability_id = existingEvents.length > 0 ? existingEvents[0].availability_id : null;
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
      const isAllDay = window.confirm('Is this an all-day event?');
      console.log('isAllDay:', isAllDay);

      const formattedDate = new Date(info.date);
      formattedDate.setHours(0, 0, 0, 0);
      const formattedDateString = moment(formattedDate).format('YYYY-MM-DD');
      let startDateTime, endDateTime


        if(isAllDay){
          // When isAllDay is true
          startDateTime = moment.tz(`${formattedDateString}T00:00:00.000`, 'local');
          endDateTime = moment.tz(`${formattedDateString}T23:59:59.999`, 'local');
        }else{
          const startTime = prompt('Enter start time (HH:mm). If mornings (1 AM- 9 AM) please input time with the left most digit in the hour as 0. For example, 01:30. If after 12pm, please put in military time:');
          const endTime = prompt('Enter end time: (HH:mm). If mornings (1 AM- 9 AM) please input time with the left most digit in the hour as 0. If after 12pm, please put in military time:');

          if(title && isValidInputTimeValue(startTime)&& isValidInputTimeValue(endTime)){
            startDateTime = moment(`${formattedDateString}T${startTime}`);
            endDateTime = moment(`${formattedDateString}T${endTime}`);                   
          }else{
            alert('Invalid input. Please make sure to enter a title and valid start/end time.');
            return
          }
        }

        startDateTime = startDateTime.tz('local');
        endDateTime = endDateTime.tz('local');


        const newEvent = {
          user_id: user_id,
          title: title,
          date: formattedDateString,
          start: isAllDay
          ? startDateTime.format('YYYY-MM-DDTHH:mm:ss.SSS') // Include time for all-day events
          : startDateTime.format('YYYY-MM-DDTHH:mm:ss.SSS'), // Include time for non-all-day events
        end: isAllDay
          ? endDateTime.format('YYYY-MM-DDTHH:mm:ss.SSS') 
          : endDateTime.format('YYYY-MM-DDTHH:mm:ss.SSS'),       
          allDay: isAllDay,
          availability_id: availability_id,
        };
        
        setEvents((prevEvents) => [...prevEvents, newEvent]);
        console.log("Event to be saved: ", newEvent);
        console.log('Formatted Date (Frontend):', formattedDate);
        console.log('Formatted Start Time (Frontend):', startDateTime.format('YYYY-MM-DDTHH:mm:ss.SSS'));
        console.log('Formatted End Time (Frontend):', endDateTime.format('YYYY-MM-DDTHH:mm:ss.SSS'));

        try{
          const token = getToken();
           const response = await fetch(`http://localhost:8080/api/user/${user_id}/availability`, {
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

        try{
          for (const eventToDelete of eventsToDelete) {
            const response = await fetch(`http://localhost:8080/api/user/${user_id}/availability/${eventToDelete.availability_id}`, {
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
    
    const handleInputChange = (e) => {
      const { name, value } = e.target;
      setNewMeeting((prevMeeting) => ({
        ...prevMeeting,
        [name]: value,
      }));
    };  

    const getToken = () => {
      const token = localStorage.getItem("token");
      if(!token){
        console.error('Token not found. User not authenticated.');
      }
      return token;
    }
    

    const isValidInputTimeValue = (time) => {
      const regex = /^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]([APMapm]{2})?$/;
      return regex.test(time);
    };

    const toggleCreateMeetingForm = () => {
      console.log("Toggling create meeting form");
      setShowCreateMeetingForm(!showCreateMeetingForm);
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
          </div>
        )}
      </div>
      );
    };
    export default Calendar;