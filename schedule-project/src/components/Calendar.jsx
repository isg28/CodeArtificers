import React from "react";
import Fullcalendar from "@fullcalendar/react"; 
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";

function Calendar(){
    return(
      <div>
        <Fullcalendar
         plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
         initialView={"dayGridMonth"}
         headerToolbar={{
          start: "today prev,next",
          center: "title",
          end: "dayGridMonth, timeGridWeek, timeGridDay",
         }}
         height={"98vh"}
         />
      </div>
      );
    } 
    
    export default Calendar;