import React, { useState } from "react";
import './LoginSignup.css'

import user_icon from './assets/person.png';
import email_icon from './assets/email.png';
import password_icon from './assets/password.png';


const LoginSignup = () => {

    const [action, setAction] = useState("Login");
    const[firstName,setName]=useState('');
    const[lastName,setLastName]=useState('');
    const[email,setEmail]=useState('');
    const[username,setUsername]=useState('');
    const[dob,setDob]=useState('');

    const handleClick = async (e) => {
        e.preventDefault();

        if (action === "Login") {
          // Logic for switching to Sign Up page
            setAction("Sign Up");
        } else if (action === "Sign Up") {
            const nameRegex = /^[A-Za-z]+$/;
            const nameErrors = [];

            if (!nameRegex.test(firstName) || !nameRegex.test(lastName)) {
                nameErrors.push("Invalid First Name or Last Name. Only letters are allowed.");
            }

            const emailRegex = /\S+@\S+/;
            const emailErrors = [];

            if (!emailRegex.test(email)) {
                emailErrors.push("Invalid Email Address. It must contain an @ symbol.");

            }


            const usernameRegex = /^[A-Za-z0-9_]+$/;
            const usernameErrors = [];
            if (!usernameRegex.test(username)) {
                usernameErrors.push("Invalid Username. Only letters, numbers, and underscores are allowed.");
            }

            const dobRegex = /^\d{4}-\d{2}-\d{2}$/;
            const dobErrors = [];

            if (!dobRegex.test(dob)) {
                dobErrors.push("Invalid Date of Birth. It must be in yyyy-mm-dd format.");
            }
    
            const hasErrors = nameErrors.length > 0 || usernameErrors.length > 0 || emailErrors.length > 0 || dobErrors.length > 0;

            if(hasErrors){
                const allErrors = [...nameErrors,...usernameErrors,...emailErrors,...dobErrors];
                window.alert(allErrors.join('\n'));
                return;
            }
            
          // Logic for handling POST method to the database
            const User={firstName,lastName,email,dob,username};
            console.log(User);

            //POST: Connects User data with the Mongo database 
            fetch("http://localhost:8080/api/user", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body:JSON.stringify(User)
    
            }).then(() => {
    
                console.log("New User added to database");
                window.alert("You have successfully made your account! Please login again.")
                setName('');
                setLastName('');
                setEmail('');
                setUsername('');
                setDob('');
            })
    }
};



    return(
        <div className = 'container'>
            <div className = 'header'>
                <div className = 'text'> {action} </div>
                <div className = 'underline'></div>
            </div>

            <div className= 'inputs'>
                {action === "Login"?<div></div>:<div className = 'input'>
                    <img src = {user_icon} alt= 'User Icon'/>
                    <input type = 'text' placeholder = 'First Name' value={firstName} onChange={(e)=>setName(e.target.value)}/>
                </div>}
                {action === "Login"?<div></div>:<div className = 'input'>
                    <img src = {user_icon} alt= 'User Icon'/>
                    <input type = 'text' placeholder = 'Last Name' value ={lastName} onChange={(e)=>setLastName(e.target.value)}/>
                </div>}

                
                {action=== "Login"?<div className = 'input'>
                    <img src = {email_icon} alt= 'Email Icon'/>
                    <input type = 'email' placeholder = 'Email Address' />
                </div>: <div className = 'input'>
                    <img src = {email_icon} alt= 'Email Icon'/>
                    <input type = 'email' placeholder = 'Email Address' value = {email} onChange ={(e)=>setEmail(e.target.value)}/>
                </div>}

                {action === "Login"?<div></div>:<div className = 'input'>
                    <img src = {user_icon} alt= 'User Icon'/>
                    <input type = 'text' placeholder = 'Date of Birth' value = {dob} onChange ={(e)=> setDob(e.target.value)}/>
                </div>}
                
                {action === "Login"?<div></div>:<div className = 'input'>
                    <img src = {user_icon} alt= 'User Icon'/>
                    <input type = 'text' placeholder = 'Username' value={username} onChange={(e)=>setUsername(e.target.value)}/>
                </div>}

                {/* This lets only Login have a password option. This is temporary since we dont have a password field yet in User. This was done so front end can create a User without a password until password field is made (11/16)- Danica */}
                {action === "Login"?<div className = 'input'>
                    <img src = {password_icon} alt= 'Password Icon'/>
                    <input type = 'password' placeholder = 'Password' />
                </div>: <div></div>}
                

            </div>
            {action=== "Sign Up"?<div></div>:<div className = 'forgot-password'>Lost Password? <span>Click Here!</span> </div>}
            <div className= 'submit-container'>
                <div className = {action === "Login"?"submit gray":"submit"} onClick = {handleClick}>Sign Up</div>
                <div className = {action === "Sign Up"?"submit gray": "submit"} onClick = {() => {setAction("Login")}}>Login</div>
            </div>

        </div>
    )
}

export default LoginSignup