import * as React from 'react';
import {useState} from 'react'
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Container from '@mui/material/Container';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import SendIcon from '@mui/icons-material/Send';



export default function User() {
    
        const[firstName,setName]=useState('')
        const[lastName,setLastName]=useState('')
        const[email,setEmail]=useState('')
        const[username,setUsername]=useState('')
        const[dob,setDob]=useState('')

        //HandleClick event handler - function for clicking the submit button on the UI
        const handleClick=(e)=>{
            e.preventDefault()
            const User={firstName,lastName,email,dob,username}
            console.log(User)
            //POST: Connects User data with the Mongo database 
            fetch("http://localhost:8080/api/user", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body:JSON.stringify(User)
        
        }).then(() => {
            console.log("New User added to database");
        })
    
}
    
    return (
        <Container maxWidth="md">
            <Paper elevation= {3}>
                <Typography variant="h5" component="div">
                    User Identification
                </Typography>

        <Box
        component="form"
        sx={{
            '& > :not(style)': { m: 2 },
        }}
        noValidate
        autoComplete="off"
        >
            <TextField id="outlined-basic" label="First Name" variant="outlined" 
            value={firstName}
            onChange={(e)=>setName(e.target.value)}
            />
            <TextField id="outlined-basic" label="Last Name" variant="outlined" 
            value={lastName}
            onChange={(e)=>setLastName(e.target.value)}
            />
            <div></div>
            <TextField id="outlined-basic" label="Email Address" variant="outlined" 
            value={email}
            onChange={(e)=>setEmail(e.target.value)}
            />
            <TextField id="outlined-basic" label="Date of Birth" variant="outlined" 
            value={dob}
            onChange={(e)=>setDob(e.target.value)}
            />
            <div></div>
            <TextField id="outlined-basic" label="Username" variant="outlined" 
            value={username}
            onChange={(e)=>setUsername(e.target.value)}
            />
            <div></div>
            <Button variant="contained" endIcon={<SendIcon />} onClick={handleClick}>
            Submit
            </Button>
            <div></div> 
            {firstName}
            <div></div> 
            {email}
            <div></div> 
            {dob}
            <div></div> 
            {username}
        </Box>
            </Paper>
        </Container>
    );
}
