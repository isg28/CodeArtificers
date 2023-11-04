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
        const[userName,setUserName]=useState('')
        const[dob,setDob]=useState('')

        
        const handleClick=(e)=>{
            e.preventDefault()
            const User={firstName,lastName,email,userName,dob}
            console.log(User)
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
            value={userName}
            onChange={(e)=>setUserName(e.target.value)}
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
            {userName}
        </Box>
            </Paper>
        </Container>
    );
}
