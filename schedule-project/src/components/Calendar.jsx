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
  const [commonTimeSlots, setCommonTimeSlots] = useState(null);
  const [showCommonTimeSlotsPopup, setShowCommonTimeSlotsPopup] = useState(false);

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
    console.log('User ID from token:', user_id);
  }, [user_id, calendar_id]);

  useEffect(() => {
    const fetchData = async () => {
      const token = getToken();
      const userIdFromToken = getUserIdFromToken(token);
    
      if (userIdFromToken && calendar_id) {
        await fetchAvailabilityEvents(userIdFromToken, calendar_id);
        await fetchMeetingEvents(userIdFromToken, calendar_id);
      }
    };
    
    fetchData();
  }, [calendar_id]);



  const fetchAvailabilityEvents = async () => {
    const token = getToken();
    const userIdFromToken = getUserIdFromToken(token); 

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
        const formattedAvailabilityEvents = data.map(event => ({
          ...event,
          isMeeting: false,
          availability_id: event.availability_id,  
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
      const userChoice = prompt('Choose an action (Input number only): \n1. Add a new event \n2. Edit an existing event\n3. Delete an existing event');

      if(userChoice === '1'){
        addEvent(info);
      } else if(userChoice === '2'){
        const eventOptions = existingEvents.map((event, index) => `${index + 1}. ${event.title}`);
        const userChoiceIndex = parseInt(prompt(`Choose an event to edit (Input number only):\n${eventOptions.join('\n')}`));

        if(!isNaN(userChoiceIndex) && userChoiceIndex >= 1 && userChoiceIndex <= existingEvents.length){
          const chosenIndex = userChoiceIndex - 1;
          const chosenDate = existingEvents[chosenIndex].date;

          if (existingEvents[chosenIndex].isMeeting) {
            
            editMeeting(existingEvents[chosenIndex], chosenIndex, chosenDate);
          } else {
            
            editEvent(existingEvents[chosenIndex], chosenIndex, chosenDate);
          }
        }else {
          alert("Invalid choice. Please choose a valid event number.");
        }
      }else if(userChoice === '3'){
        const eventOptions = existingEvents.map((event, index) => `${index + 1}. ${event.title}`);
        const userChoiceIndex = parseInt(prompt(`Choose an event to delete (Input number only):\n${eventOptions.join('\n')}`));

        if (!isNaN(userChoiceIndex) && userChoiceIndex >= 1 && userChoiceIndex <= existingEvents.length) {
          const chosenIndex = userChoiceIndex - 1;
          const chosenEvent = existingEvents[chosenIndex];

          if (chosenEvent.isMeeting) {
        
            const shouldDelete = window.confirm(`Are you sure you want to delete the event "${chosenEvent.title}" on this date (${info.dateStr})?`);
            if (shouldDelete) {
              deleteMeeting([chosenEvent]); 
            }
            console.log("deleteMeeting", chosenEvent.title);
          } else {

            const unauthorizedEvents = existingEvents.filter(
              (event) => event.user_id !== user_id
              );

              const unauthorizedForDeletion = unauthorizedEvents.filter((event) => event.user_id !== user_id);

              // Check if the user is not the creator of any unauthorized events
              const isUserCreatorOfUnauthorizedEvents = unauthorizedForDeletion.some((event) => event.user_id === user_id);


            if (isUserCreatorOfUnauthorizedEvents) { //you can now edit this but like it deletes everything now so idk
              alert("You don't have permission to delete the event.");
              console.log(unauthorizedEvents);
              return;
            }

            if (chosenEvent.user_id !== user_id) {
              alert("You don't have permission to delete this event.");
              return;
            }
            

            
            const shouldDelete = window.confirm(`Are you sure you want to delete the event "${chosenEvent.title}" on this date (${info.dateStr})?`);
            if (shouldDelete) {
              deleteEvent([chosenEvent]); 
            }
          }
        }
      } else {
        alert('Invalid choice. Please choose 1, 2, or 3.');
    }
  } else{
    const userChoice = prompt('No existing events. Choose an action (Input Number Only): \n1. Add a new event');
    if (userChoice === '1') {
      addEvent(info);
    } else {
        alert('Invalid choice. Please choose 1.');
      }
    }
  };


  const editEvent = async (existingEvent, chosenIndex, chosenDate) => {

    if (existingEvent.user_id !== user_id) {
      alert("You don't have permission to edit this event.");
      return;
    }

    console.log('Existing Event: ', existingEvent);
    const newTitle = prompt('Edit event title:', existingEvent.title);
    let newStartTime, newEndTime;

    newStartTime = prompt('Edit start time (HH:mm). If mornings (1 AM- 9 AM) please input time with the left most digit in the hour as 0. For example, 01:30. If after 12pm, please put in military time:', moment(existingEvent.start).format('HH:mm'));
    newEndTime = prompt('Edit end time (HH:mm). If mornings (1 AM- 9 AM) please input time with the left most digit in the hour as 0. For example, 01:30. If after 12pm, please put in military time:', moment(existingEvent.end).format('HH:mm'));
    
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
          availability_id: existingEvent.availability_id, 
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

    await fetchAvailabilityEvents();


    const existingEvents = findEventByDate(info.dateStr);
    let availability_id = existingEvents.length > 0 ? existingEvents[0].availability_id : null;

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

      if (response.ok) {
        console.log('Event saved successfully');
        window.location.reload();
      } else {
        console.error('Failed to save event');
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
          setEvents(prevEvents => prevEvents.filter(event => !event.isMeeting));

          const formattedMeetingEvents = data.map(meeting => ({
            meeting_id: meeting.meeting_id,
            title: meeting.meeting_Description,
            start: meeting.start,
            end: meeting.end,
            isMeeting: true,
          }));
          setEvents(prevEvents => [...prevEvents, ...formattedMeetingEvents]);
        } else {
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
        console.log('New Meeting:', newMeeting);
        window.location.reload();

        setNewMeeting({
          date: "",
          startTime: "",
          endTime: "",
          location: "",
          meeting_Description: "",
        });
        setShowCreateMeetingForm(false);

        await fetchMeetingEvents();

        console.log('Updated Events:', events);
      
        alert('Meeting created successfully');
      } else {
        console.error("Failed to create meeting");
        console.error("Response status: ", response.status);
        const errorMessage = await response.text();
        console.error("Error message: ", errorMessage);
      }
    } catch (error) {
      console.error("Error creating meeting:", error);
    }

    setNewMeeting({
      date: "",
      startTime: "",
      endTime: "",
      location: "",
      meeting_Description: "",
    });
    setShowCreateMeetingForm(false);
  };


  const deleteMeeting = async (meetingToDelete) => {
    if (!meetingToDelete || !Array.isArray(meetingToDelete)) {
      console.error('Invalid input for meetingToDelete');
      return;
    }

    const token = getToken();
    const userIdFromToken = getUserIdFromToken(token);

    try {
      for (const meeting of meetingToDelete) {
        const response = await fetch(`http://localhost:8080/api/user/${userIdFromToken}/calendar/${calendar_id}/meeting/${meeting.meeting_id}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
        });

        if (response.ok) {
          console.log('Meeting deleted successfully');
        } else {
          console.error('Failed to delete meeting');
          console.error('Response status: ', response.status);
          const errorMessage = await response.text();
          console.error('Error message: ', errorMessage);
        }
      }
    setEvents((prevEvents) => prevEvents.filter((event) => !meetingToDelete.includes(event)));
    } catch (error) {
      console.error('Error deleting meetings:', error);
    }
  };


  const editMeeting = async (existingEvent, chosenIndex) => {
    console.log('Existing Meeting: ', existingEvent);
    const chosenDate = moment(existingEvent.start).format('YYYY-MM-DD');
    const newDescription = prompt('Edit meeting description:', existingEvent.title);
    const newLocation = prompt('Edit meeting location:', existingEvent.location);
    let newStartTime, newEndTime;

    console.log('New Start Time (before assignment):', newStartTime);
    newStartTime = prompt('Edit start time (HH:mm). If mornings (1 AM- 9 AM) please input time with the left most digit in the hour as 0. For example, 01:30. If after 12pm, please put in military time:', moment(existingEvent.start).format('HH:mm'));
    console.log('New Start Time (after assignment):', newStartTime);
    newEndTime = prompt('Edit end time (HH:mm). If mornings (1 AM- 9 AM) please input time with the left most digit in the hour as 0. For example, 01:30. If after 12pm, please put in military time:', moment(existingEvent.end).format('HH:mm'));
  
    if (!isValidInputTimeValue(newStartTime) || !isValidInputTimeValue(newEndTime)) {
      alert('Invalid input. Please make sure to enter valid start/end times.');
      return;
    }
  
    console.log('New Description:', newDescription);
    console.log('New Location:', newLocation);
    console.log('New Start Time:', newStartTime);
    console.log('New End Time:', newEndTime);
  
    const token = getToken();
    const userIdFromToken = getUserIdFromToken(token);
    
    if (newDescription && newLocation !== null && isValidInputTimeValue(newStartTime) && isValidInputTimeValue(newEndTime)) {
      try {
        const updatedMeeting = {
          ...existingEvent,
          meeting_Description: newDescription,
          location: newLocation,
          start: `${chosenDate}T${newStartTime}:00`,
          end: `${chosenDate}T${newEndTime}:00`, 
        };
        console.log('Updated Meeting:', updatedMeeting);
        setEvents((prevEvents) => [
          ...prevEvents.slice(0, chosenIndex),
          updatedMeeting,
          ...prevEvents.slice(chosenIndex + 1),
        ]);
  
        if (!updatedMeeting.meeting_id) {
          console.error('Meeting ID is missing. Cannot update meeting.');
          return;
        }
  
        const response = await fetch(`http://localhost:8080/api/user/${userIdFromToken}/calendar/${calendar_id}/meeting/${existingEvent.meeting_id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
          body: JSON.stringify(updatedMeeting),
        });
  
        if (response.ok) {
          console.log('Meeting updated successfully');
          window.location.reload(); 
        } else {
          console.error('Failed to update meeting');
          console.log('Failed Meeting: ', existingEvent);
        }
      } catch (error) {
        console.error('Error updating meeting:', error);
      }
    } else {
      alert('Invalid input. Please make sure to enter a description and valid start/end time.');
    }
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
    const isMeeting = eventInfo.event.extendedProps.isMeeting;
    const dotColor = isMeeting ? '#52307c' : '#E6E6FA';
    return (
      <>
        <div className= "custom-dot" style={{ backgroundColor: dotColor }} />
          <div className = "fc-content">
            {eventInfo.timeText && (<div className = "fc-time bold-time">{eventInfo.timeText}</div>)}
            <div className={`fc-time ${isMeeting ? 'meeting-event' : ''}`}>{eventInfo.event.title}</div>
          </div>
      </>
    );
  };

  const handleEventClick = (clickInfo) => {
    const event = clickInfo.event;

    if(event.extendedProps.isMeeting){
      showMeetingDetails(event);
    }
  };

  const showMeetingDetails = async(meetingEvent) =>{
    const meeting_id = meetingEvent.extendedProps.meeting_id;

    try{
      const response = await fetch (`http://localhost:8080/api/user/${user_id}/calendar/${calendar_id}/meeting/${meeting_id}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${getToken()}`,
          'Content-Type': 'application/json',
        },
      });
      if(!response.ok){
        console.error(`Error fetching meeting details: ${response.status} - ${response.statusText}`);
        return;
      }
      const meetingDetails = await response.json();
      const localStartTime = moment(meetingDetails.start).local();
      const localEndTime = moment(meetingDetails.end).local();
      const formattedStartTime = localStartTime.format('hh:mm A');
      const formattedEndTime = localEndTime.format('hh:mm A');
      alert(`Meeting Details: \nTitle: ${meetingDetails.meeting_Description}\nStart: ${formattedStartTime}\nEnd: ${formattedEndTime}\nLocation: ${meetingDetails.location} `)
    }catch (error){
      console.error('Error fetching meeting details:', error);
    }
  };


/*                          INVITE FUNCTION                          */
  const inviteUsers = async () => {
    try{
      const token = getToken();
      const userIdFromToken = getUserIdFromToken(token);
      const emailList = prompt('Enter comma-separated email addresses to invite:');

      if(emailList){
        const emails = emailList.split(',').map((email) => email.trim());

        const invitationRequest = {
          user_id: userIdFromToken,
          calendar_id: calendar_id,
          invitedUsers: emails.map((email)=> ({email})),
        };

        const response = await fetch(`http://localhost:8080/api/user/${userIdFromToken}/calendar/${calendar_id}/invite`, {
          method: 'POST',
          headers:{
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
            body: JSON.stringify(invitationRequest),
        });
        if(response.ok){
          console.log('Invitations sent successfully');
          console.log("Invitation Request:", invitationRequest);
        } else{
          console.error('Failed to send invitations');
          console.error('Response status: ', response.status);
          const errorMessage = await response.text();
          console.error('Error message: ', errorMessage);
          console.error('Error message: ', errorMessage);
        }
      }
    } catch (error){
      console.error('Error inviting users:', error);
    }
  };



/*                          COMMON TIMESLOT FUNCTION                          */
  const fetchCommonTimeSlots = async () => {
    try {
      const token = getToken();
      const userIdFromToken = getUserIdFromToken(token);
      const response = await fetch(`http://localhost:8080/api/calendar/${calendar_id}/timeslots`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      if (response.ok) {
        const commonTimeSlots = await response.json();
        const formattedTimeSlots = commonTimeSlots.map(userTimeSlot => ({
          ...userTimeSlot,
          timeslots: userTimeSlot.timeslots.map(timeSlot => ({
          ...timeSlot,
          username: userTimeSlot.username,
          })),
        }));
        console.log('Common Time Slots:', formattedTimeSlots);
        setCommonTimeSlots(formattedTimeSlots);
        setShowCommonTimeSlotsPopup(true);
      } else {
        console.error('Failed to fetch common time slots');
      }
    } catch (error) {
      console.error('Error fetching common time slots:', error);
    }
  };

  const groupedTimeSlots = commonTimeSlots ? commonTimeSlots.reduce((groups, userTimeSlot) => {
    userTimeSlot.timeslots.forEach((timeSlot) => {
      const date = timeSlot.date;
      if (!groups[date]) {
        groups[date] = [];
      }
      groups[date].push({
        userId: userTimeSlot.userId,
        username: timeSlot.username,
        start: timeSlot.start,
        end: timeSlot.end,
      });
    });
    return groups;
  }, {}) : {};


return(
  <div>
    <h1> </h1>
    <Box sx={{ display: 'flex', justifyContent: 'flex-end','& button': { m: 1, backgroundColor: 'black' } }}>
      <Button variant="contained" size="large" onClick={toggleCreateMeetingForm}>
      Create Meeting
      </Button>  
      <div>
        <Button variant="contained" size="large" onClick={fetchCommonTimeSlots}>
          Common TimeSlots
        </Button>
        {showCommonTimeSlotsPopup && commonTimeSlots && commonTimeSlots.length > 0 && (
        <div
          style={{
            position: 'fixed',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            backgroundColor: '#3087CF',
            padding: '20px',
            zIndex: 1000,
            maxHeight: '90vh',
            overflowY: 'auto',
          }}
        >
          <h2 style={{ color: 'white' }}>Common Time Slots</h2>
          {Object.entries(groupedTimeSlots).map(([date, timeSlots]) => (
            <div key={date}>
              <h3>Date: {date}</h3>
              <ul>
                {timeSlots.map((timeSlots, index) => (
                  <li key={index}>
                    <span style={{ fontWeight: 'bold', fontSize: '16px' }}>User: </span> {timeSlots.username}
                    <div style={{ marginLeft: '20px' }}> 
                    <span style={{ fontWeight: 'bold', fontSize: '16px' }}>Start: </span> {new Date(timeSlots.start).toLocaleTimeString('en-US', { hour: 'numeric', minute: 'numeric' })}
                    </div>
                    <div style={{ marginLeft: '20px' }}> 
                    <span style={{ fontWeight: 'bold', fontSize: '16px' }}>End: </span> {new Date(timeSlots.end).toLocaleTimeString('en-US', { hour: 'numeric', minute: 'numeric' })}
                    </div>
                  </li>
                ))}
              </ul>
              <hr style={{ borderColor: 'white', margin: '10px 0' }} />
            </div>
          ))}
          <button style={{
            backgroundColor: 'black', 
            color: 'white',
            padding: '10px 15px',
            border: 'none',
            borderRadius: '4px', 
            cursor: 'pointer',
          }}onClick={() => setShowCommonTimeSlotsPopup(false)}>Close</button>
        </div>
        )}
        <Button variant = "contained" size = "large" onClick= {inviteUsers}> Invite Users </Button>
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
        eventClick = {handleEventClick}
        eventContent={eventContent}
        />
        {showCreateMeetingForm && (
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
          <div>
          <button onClick={() => setShowCreateMeetingForm(false)}>Cancel</button>
          </div>
          </div>
        )}
      </div>
    );
};
export default Calendar;