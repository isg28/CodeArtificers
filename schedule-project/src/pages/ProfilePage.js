import React, {useState, useEffect} from 'react';
import AppBarLogin from '../components/AppBarLogin';
import Profile from '../components/Profile';


function ProfilePage() {
    return (
        <div>
            <AppBarLogin />
            <div>
                <h1></h1>
                <Profile />
            </div>
        </div>
    )
}
export default ProfilePage