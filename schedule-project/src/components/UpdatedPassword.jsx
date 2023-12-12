import React, { useState } from 'react';
import './UpdatedPassword.css'
import password_icon from './assets/password.png';
import email_icon from './assets/email.png';


const UpdatedPassword = () => {
    const [newPassword, setNewPassword] = useState('');
    const [confirmNewPassword, setConfirmNewPassword] = useState('');

    const handleUpdatePassword = async (e) => {
        e.preventDefault();

        const passwordData = {
            newPassword,
            confirmNewPassword
        };

        setNewPassword('');
        setConfirmNewPassword('');
    };

    return (
        <div className='container'>
            <div className='header'>
                <div className='text'>Update Password</div>
                <div className='underline'></div>
            </div>

            <div className='inputs'>
                <div className='input'>
                    <img src={email_icon} alt='Email Icon' />
                    <input type='password' placeholder='Email Address' value={newPassword} onChange={(e) => setNewPassword(e.target.value)} />
                </div>
                <div className='input'>
                    <img src={password_icon} alt='Password Icon' />
                    <input type='password' placeholder='New Password' value={newPassword} onChange={(e) => setNewPassword(e.target.value)} />
                </div>

                <div className='input'>
                    <img src={password_icon} alt='Password Icon' />
                    <input type='password' placeholder='Confirm New Password' value={confirmNewPassword} onChange={(e) => setConfirmNewPassword(e.target.value)} />
                </div>
            </div>

            <div className='submit-container'>
                <div className='submit' onClick={handleUpdatePassword}>Update Password </div>
            </div>
        </div>
    );
};

export default UpdatedPassword;