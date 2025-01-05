import { Route, Routes, useLocation, useNavigate } from 'react-router-dom';
import Projects from './pages/project';
import Login from './pages/login';
import './index.css';
import Navbar from './components/navbar';
import { useState } from 'react';
import { getToken } from './utils/utils';
import toast from 'react-hot-toast';
import { getCurrentUser } from './api/user';
import { CurrentUser } from './models/user';
import User from './pages/user';
import Script from './pages/script';
import Logs from './pages/logs';
import { useQuery } from 'react-query';

function App() {
  const navigate = useNavigate();
  const location = useLocation();
  const [currUser, setCurrUser] = useState<CurrentUser | null>(null);
  const fetchCurrentUser = async () => {
    if (!getToken()) {
      if (location.pathname !== '/login') {
        navigate('/login');
        return;
      }
    }
    try {
      const user = await getCurrentUser();
      setCurrUser(user);
      if (location.pathname === '/login') {
        navigate('/');
        return;
      }
    } catch (e) {
      if (location.pathname !== '/login') {
        navigate('/login');
        toast.error('Session expired');
        return;
      }
    }
  };

  useQuery('currentUser', fetchCurrentUser, {
    retry: false,
  });

  return (
    <div>
      <Navbar currUser={currUser} setCurrUser={setCurrUser} />
      <Routes>
        <Route path="/" element={<Projects />} />
        <Route path="/login" element={<Login />} />
        <Route path="/users" element={<User />} />
        <Route path="/project/:id" element={<Script />} />
        <Route path="/logs/:id" element={<Logs />} />
      </Routes>
    </div>
  );
}

export default App;
