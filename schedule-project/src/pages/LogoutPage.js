import React from "react";
import { useNavigate } from "react-router-dom";
import AppBar from '../components/AppBar';

const clearTokens = () => {
    // Clear tokens from localStorage or sessionStorage
    localStorage.removeItem("access_token");
    localStorage.removeItem("refresh_token");
  };
  
  const LogoutPage = () => {
    const navigate = useNavigate();
  
    const handleLogout = () => {
      // Call the clearTokens function to remove tokens
      clearTokens();
  
      // Redirect the user to the login page or another destination
      navigate("/login");
    };
    return (
        <div>
            <AppBar />
            <div>
                <h1>You have been logged out!</h1>
            </div>
        </div>
    )
}
export default LogoutPage