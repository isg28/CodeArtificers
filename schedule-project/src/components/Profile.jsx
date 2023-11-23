import React, {useState, useEffect} from 'react';
import profile_icon from './assets/profile.png';
import person from './assets/person.png';
import './Profile.css'
import Avatar from '@mui/material/Avatar';
import Stack from '@mui/material/Stack';
import { deepOrange, deepPurple } from '@mui/material/colors';


const UserProfile = () => {
  // Initial user data
  const initialUserData = {
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    dob: '',
  };

  // State to track user data
  const [userData, setUserData] = useState(initialUserData);

  // State to track which field is being edited
  const [editingField, setEditingField] = useState(null);

  // Function to handle field editing
  const handleFieldEdit = (fieldName) => {
    setEditingField(fieldName);
  };

  // Function to handle changes in input fields
  const handleInputChange = (e) => {
    setUserData({
      ...userData,
      [editingField]: e.target.value,
    });
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];

    if (file && (file.type === 'image/jpeg' || file.type === 'image/png')) {
      const reader = new FileReader();

      reader.onload = (event) => {
        setUserData((prevUserData) => ({
          ...prevUserData,
          profilePicture: event.target.result,
        }));
      };

      reader.readAsDataURL(file);
    } else {
      alert('Please upload a valid JPEG or PNG image.');
    }
  };

  const changeAvatar = () => {
    // Trigger the file input when the avatar is clicked
    document.getElementById('avatarInput').click();
  };


  // Function to save changes and end editing
  const saveChanges = () => {
    setEditingField(null);
    // Add logic to save changes to the server or perform other actions
  };

  useEffect(() => {
    const fetchUserData = async () => {
      try{
        const response = await fetch("http://localhost/api/user/{user_id}",{
          method: "GET",
          headers:{
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ${yourAuthToken}',
          },
        });
        if(response.ok){
          const userDataFromServer = await response.json();
          setUserData(userDataFromServer);
        } else{
          console.error('Failed to fetch user data:', response.statusText);
        }
      }catch(error){
        console.error('Error during fetch:', error);
      }
    };
    fetchUserData();
  }, []);

  return (
    <div className="profile-container">
      <h2>User Profile</h2>

      <div className="avatar-container" onClick={changeAvatar}>
      {userData.profilePicture ? (
          <img className="profile-picture" src={userData.profilePicture} alt="Profile Icon" />
        ) : (
          <img className="profile-picture" src={profile_icon} alt="Placeholder Icon" />
        )}
      </div>

      <input
        id="avatarInput"
        type="file"
        accept="image/jpeg, image/png"
        onChange={handleFileChange}
        style={{ display: 'none' }}
      />


      <label htmlFor="firstName">First Name:</label>
      <input
        type="text"
        id="firstName"
        value={editingField === 'firstName' ? userData.firstName : userData.firstName}
        onClick={() => handleFieldEdit('firstName')}
        onChange={handleInputChange}
        disabled={!editingField || editingField !== 'firstName'}
      />

      <label htmlFor="lastName">Last Name:</label>
      <input
        type="text"
        id="lastName"
        value={editingField === 'lastName' ? userData.lastName : userData.lastName}
        onClick={() => handleFieldEdit('lastName')}
        onChange={handleInputChange}
        disabled={!editingField || editingField !== 'lastName'}
      />

      <label htmlFor="username">Username:</label>
      <input
        type="text"
        id="username"
        value={editingField === 'username' ? userData.username : userData.username}
        onClick={() => handleFieldEdit('username')}
        onChange={handleInputChange}
        disabled={!editingField || editingField !== 'username'}
      />

      <label htmlFor="email">Email:</label>
      <input
        type="email"
        id="email"
        value={editingField === 'email' ? userData.email : userData.email}
        onClick={() => handleFieldEdit('email')}
        onChange={handleInputChange}
        disabled={!editingField || editingField !== 'email'}
      />

      <label htmlFor="dob">Date of Birth:</label>
      <input
        type="text"
        id="dob"
        value={editingField === 'dob' ? userData.dob : userData.dob}
        onClick={() => handleFieldEdit('dob')}
        onChange={handleInputChange}
        disabled={!editingField || editingField !== 'dob'}
      />

      {editingField && (
        <button onClick={saveChanges}>Save Changes</button>
      )}
    </div>
  );
};

export default UserProfile;
