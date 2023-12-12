import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import LoginPage from './pages/LoginPage';
import CalendarPage from './pages/CalendarPage';
import HomepagePage from './pages/HomepagePage';
import ProfilePage from './pages/ProfilePage';
import AccountPage from './pages/AccountPage';
import LogoutPage from './pages/LogoutPage';
import UpdatedPasswordPage from './pages/UpdatedPasswordPage';

ReactDOM.render(
  <React.StrictMode>
    <Router>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/custom-login" element={<LoginPage />} />
        <Route path = "/calendar" element = {CalendarPage} />
        <Route path="/calendar/:calendar_id" element={<CalendarPage />} />
        <Route path="/homepage" element={<HomepagePage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/account-settings" element = {<AccountPage />} />
        <Route path="/logout" element ={<LogoutPage />} />
        <Route path = "/update-password" element ={<UpdatedPasswordPage />} />
        {/* ... etc. */}
      </Routes>
    </Router>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
