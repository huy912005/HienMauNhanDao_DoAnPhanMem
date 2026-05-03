import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import UserLayout from './layouts/UserLayout';
import HomePage from './pages/HomePage';
import Login from './pages/Login';
import RegisterVolunteer from './pages/RegisterVolunteer';
import OtpVerification from './pages/OtpVerification';
import AboutPage from './pages/AboutPage';
import ChienDichPage from './pages/ChienDichPage';

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        <Routes>
          <Route path="/" element={<UserLayout />}>
            <Route index element={<HomePage />} />
            <Route path="login" element={<Login />} />
            <Route path="register" element={<RegisterVolunteer />} />
            <Route path="otp" element={<OtpVerification />} />
            <Route path="about" element={<AboutPage />} />
            <Route path="chiendich" element={<ChienDichPage />} />
          </Route>
        </Routes>
      </Router>
    </QueryClientProvider>
  );
}

export default App;
