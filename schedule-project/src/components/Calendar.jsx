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
      const title = prompt('Enter event title: ');
        const isAllDay = window.confirm('Is this an all-day event?');

        let startDateTime, endDateTime;

        if(isAllDay){
          startDateTime = info.dateStr;
          endDateTime = info.dateStr;
        }else{
          const startTime = prompt('Enter start time (HH:mm). If mornings (1 AM- 9 AM) please input time with the left most digit in the hour as 0. For example, 01:30. If after 12pm, please put in military time:');
          const endTime = prompt('Enter end time: (HH:mm). If mornings (1 AM- 9 AM) please input time with the left most digit in the hour as 0. If after 12pm, please put in military time:');

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
    } 
    export default Calendar;