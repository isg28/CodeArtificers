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
        const clickedDateUtc = moment.tz(date, 'UTC').format();

        return events.filter((event) => {
          const eventStartDate = moment.tz(event.start, 'UTC').format();
      
          // Compare dates without considering time for all-day events
          if (event.allDay) {
            return moment(eventStartDate).isSame(clickedDateUtc, 'day');
          }
      
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


      if(existingEvents.length > 0){
        const userChoice = prompt('Choose an action: \n1. Add a new event \n2.Edit existing events');

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
        }else{
          alert('Invalid choice. Please choose 1 or 2.');
        }
    } else {
      addEvent(info);
    }
  };

    const editEvent = async (existingEvent, chosenIndex, chosenDate) => {
      console.log('Existing Event: ', existingEvent);
      const newTitle = prompt('Edit event title:', existingEvent.title);
      const newStartTime = prompt('Edit start time:', existingEvent.start);
      const newEndTime = prompt('Edit end time:', existingEvent.end);

      console.log('New Title:', newTitle);
      console.log('New Start Time:', newStartTime);
      console.log('New End Time:', newEndTime);

      if(newTitle && isValidInputTimeValue(newStartTime) && isValidInputTimeValue(newEndTime)) {
        const updatedEvent = {
          ...existingEvent,
          title: newTitle,
          start: `${chosenDate}T${newStartTime}`,
          end: `${chosenDate}T${newEndTime}`,

        };

        console.log('Updated Event:', updatedEvent);

        setEvents((prevEvents) => [
          ...prevEvents.slice(0, chosenIndex),
          updatedEvent,
          ...prevEvents.slice(chosenIndex + 1),
        ]);
        console.log(' Updated Event: ', existingEvent);

        try{
          const { availability_Id } = existingEvent;
          console.log('Availability ID:', availability_Id);
          const response = await fetch(`http://localhost:8080/api/user/${user_id}/availability/${availability_Id}`,{
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedEvent),
            }
          );

          if(response.ok){
            console.log('Event updated successfully');
          }else{
            console.error('Failed to update event');
            console.log('Failed Event: ', existingEvent);
          }
          } catch (error) {
            console.error('Error updating event:', error);
          }
        }else{
          alert('Invalid input. Please make sure to enter a title and valid start/end time.');
        }
    };
    
    const addEvent = async(info) =>{
      if(!user_id){
        console.error('User ID is undefined');
        return
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
          ? endDateTime.format('YYYY-MM-DDTHH:mm:ss.SSS') // Include time for all-day events
          : endDateTime.format('YYYY-MM-DDTHH:mm:ss.SSS'),   // Include time for non-all-day events      
          allDay: isAllDay,
        };
        
        setEvents((prevEvents) => [...prevEvents, newEvent]);
        console.log("Event to be saved: ", newEvent);
        console.log('Formatted Date (Frontend):', formattedDate);
        console.log('Formatted Start Time (Frontend):', startDateTime.format('YYYY-MM-DDTHH:mm:ss.SSS'));
        console.log('Formatted End Time (Frontend):', endDateTime.format('YYYY-MM-DDTHH:mm:ss.SSS'));

        try{
          const token = getToken();
          const response = await fetch(`http://localhost:8080/api/user/${user_id}/availability`,{
            method: 'POST',
            headers:{
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(newEvent),
          });
          console.log(response);
          if(response.ok){
            console.log('Event saved successfully');
          }else{
            console.error('Failed to save event');
            //Optionally handle error cases
            
            console.error('Response status: ', response.status);
            const errorMessage = await response.text();
            console.error('Error message: ', errorMessage);
          }
        } catch (error) {
          console.error('Error saving event:', error);
          console.error('Response:', error.response);
        }
      //} else {
        //alert('Invalid input. Please make sure to enter a title and valid start/emd time.')
      //}
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