import React, {useState, useEffect} from 'react';
import AppBar from '../components/AppBar';
import UpdatedPassword from '../components/UpdatedPassword';

function UpdatedPasswordPage() {
    return (
        <div>
            <AppBar />
            <div>
                <h1></h1>
                <UpdatedPassword />
            </div>
        </div>
    )
}
export default UpdatedPasswordPage