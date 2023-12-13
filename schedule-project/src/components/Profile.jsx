import React, {useState, useEffect} from 'react';
import profile_icon from './assets/profile.png';
import './Profile.css'
import jwt from 'jsonwebtoken';
import { useNavigate} from 'react-router-dom'

const UserProfile = () => {

  const initialUserData = {
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    dob: '',
  };

  const [user_id, setUserId] = useState(null);
  const [isEditMode, setIsEditMode] = useState(false);
  const [userData, setUserData] = useState(initialUserData);
  const [editingField, setEditingField] = useState(null);
  const [fieldErrors, setFieldErrors] = useState({});
  const [validationErrors, setValidationErrors] = useState([]);
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
  const [confirmUsername, setConfirmUsername] = useState('');

  const toggleEditMode = () => {
    setIsEditMode((prevMode) => !prevMode);
    setFieldErrors({});
  };

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (token) {
      try {
        const decodedToken = jwt.decode(token);
        const user_id = decodedToken.user_id;
        setUserId(user_id);
      } catch (error) {
        console.error("Error decoding token: ", error);
      }
    }
  }, []);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const token = getToken();
        if (!user_id) {
          console.error('User ID is undefined');
        }

        const response = await fetch(`http://localhost:8080/api/user/${user_id}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
        });
        if (response.ok) {
          const userDataFromServer = await response.json();
          setUserData(userDataFromServer);
        } else {
          console.error('Failed to fetch user data:', response.statusText);
        }
      } catch (error) {
        console.error('Error during fetch:', error);
      }
    };
    if (user_id) {
      fetchUserData();
    }
  }, [user_id, isEditMode]); 

  
  const handleFieldEdit = (fieldName) => {
    setEditingField(fieldName);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserData({
      ...userData,
      [name]: value,
    });

    validateField(name, value);
  };


  const validateField = (name, value) => {
    const nameRegex = /^[A-Za-z\s-]+$/;
    const emailRegex = /\S+@\S+/;
    const usernameRegex = /^[A-Za-z0-9_]+$/;
    const dobRegex = /^\d{4}-\d{2}-\d{2}$/;

    setValidationErrors([]);

    let errorMessage = '';

    switch (name) {
      case 'firstName':
      case 'lastName':
        if (!nameRegex.test(value)) {
          validationErrors.push("Invalid First Name or Last Name. Only letters are allowed.");
        }
        break;
      case 'email':
        if (!emailRegex.test(value)) {
          validationErrors.push("Invalid Email Address. It must contain an @ symbol.");
        }
        break;
      case 'username':
        if (!usernameRegex.test(value)) {
          validationErrors.push("Invalid Username. Only letters, numbers, and underscores are allowed.");
        }
        break;
      case 'dob':
        if (!dobRegex.test(value)) {
          validationErrors.push("Invalid Date of Birth. It must be in yyyy-mm-dd format.");
        }
        break;
      default:
        break;
      }
    setFieldErrors((prevErrors) => ({
      ...prevErrors,
      [name]: errorMessage,
    }));
    if (errorMessage) {
      setValidationErrors((prevErrors) => [...prevErrors, errorMessage]);
    }
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
    document.getElementById('avatarInput').click();
  };

  const saveChanges = async () => {
    validateAllFields();

    if (validationErrors.length > 0) {
      alert('Please correct the following errors before saving changes:\n\n' + validationErrors.join('\n'));
      setValidationErrors([]);
      return;
    }

    try {
      const token = getToken();
      const response = await fetch(`http://localhost:8080/api/user/${user_id}`, {
        method: "PUT",
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(userData),
      });

      if (response.ok) {
        console.log('User profile updated successfully');
      } else {
        console.error('Failed to update user data:', response.statusText);
      }
    } catch (error) {
      console.error('Error during update:', error);
    } finally {
      setEditingField(null);
      toggleEditMode();
    }
  };

  const validateAllFields = () => {
    // Validate all fields and update the state
    for (const [name, value] of Object.entries(userData)) {
      validateField(name, value);
    }
  };

  const handleDeleteButtonClick = () => {
    setShowDeleteConfirmation(true);
  };

  const navigate = useNavigate();  

  const handleDeleteConfirm = async () => {
    try {
      if (confirmUsername === userData.username) {
        const token = getToken();
        const response = await fetch(`http://localhost:8080/api/user/${user_id}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
        });

        if (response.ok) {
          navigate('/');
        } else {
          console.error('Failed to delete user:', response.statusText);
        }
      } else {
        alert('Username does not match. Deletion canceled.');
      }
    } catch (error) {
      console.error('Error during deletion:', error);
    } finally {
      setShowDeleteConfirmation(false);
    }
  };

  const handleDeleteCancel = () => {
    setShowDeleteConfirmation(false);
  };
  
  const getToken = () => {
    const token = localStorage.getItem("token");
    if(!token){
      console.error('Token not found. User not authenticated.');
    }
    return token;
  }


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
      name="firstName"
      value={userData.firstName}
      onClick={() => handleFieldEdit('firstName')}
      onChange={handleInputChange}
      disabled={!isEditMode}
    />

    <label htmlFor="lastName">Last Name:</label>
    <input
        type="text"
        id="lastName"
        name = 'lastName'
        value={userData.lastName}
        onClick={() => handleFieldEdit('lastName')}
        onChange={handleInputChange}
        disabled={!isEditMode}
    />

    <label htmlFor="username">Username:</label>
    <input
      type="text"
      id="username"
      name = 'username'
      value={userData.username}
      onClick={() => handleFieldEdit('username')}
      onChange={handleInputChange}
      disabled={!isEditMode}
    />

    <label htmlFor="email">Email:</label>
    <input
      type="email"
      id="email"
      name = 'email'
      value={userData.email}
      onClick={() => handleFieldEdit('email')}
      onChange={handleInputChange}
      disabled={!isEditMode}
    />

    <label htmlFor="dob">Date of Birth:</label>
    <input
      type="text"
      id="dob"
      name = 'dob'
      value={userData.dob}
      onClick={() => handleFieldEdit('dob')}
      onChange={handleInputChange}
      disabled={!isEditMode}
    />

    <div className = "button-container">
    {isEditMode &&
    <button className = 'save-changes-btn' onClick={saveChanges}>Save Changes</button>}
    {!isEditMode && <button className = "edit-btn" onClick={toggleEditMode}>Edit</button>}

    <button className = "delete-btn" onClick={handleDeleteButtonClick}>Delete Account</button>
    </div>

    {showDeleteConfirmation && (
        <div className="delete-confirmation">
          <p>Are you sure you want to delete your account?</p>
          <p>Please enter your username to confirm:</p>
        <div className = "input-container">
        <input
          type="text"
          value={confirmUsername}
          onChange={(e) => setConfirmUsername(e.target.value)}
        />
        </div>
        <div className = "button-container">
        <button className = "confirm-btn" onClick={handleDeleteConfirm}>Confirm</button>
        <button className = "cancel-btn" onClick={handleDeleteCancel}>Cancel</button>
        </div>
      </div>
    )}
  </div>
  );
};
export default UserProfile;

