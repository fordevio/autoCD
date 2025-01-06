import './index.css';
import logo from '../../assets/autocd-logo.png';
import { CurrentUser } from '../../models/user';
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

interface Prop {
  currUser: CurrentUser | null;
  setCurrUser: (user: CurrentUser | null) => void;
}

const Navbar: React.FC<Prop> = ({ currUser, setCurrUser }) => {
  const navigate = useNavigate();
  const logoutHandler = () => {
    setCurrUser(null);
    localStorage.removeItem('token');
    navigate('/login');
    return;
  };
  return (
    <div className="navbar">
      <img src={logo} alt="logo" className="navbar-logo" />
      <h1 className="nav-h1">autoCD</h1>
      {currUser && (
        <div className="nav-links">
          <Link className="link" to={'/'}>
            Projects
          </Link>
          {currUser && currUser.roles.includes('ADMIN') && (
            <Link to="/users" className="link">
              Users
            </Link>
          )}
          <button className="btn-logout" onClick={logoutHandler}>
            Log out
          </button>
        </div>
      )}
    </div>
  );
};

/* Updated CSS for Navbar */

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  position: relative;
  background-color: #f8f9fa; /* Optional background color */
}

.navbar-logo {
  height: 40px;
}

.nav-h1 {
  font-size: 24px;
  margin-left: 10px;
}

.nav-links {
  display: flex;
  gap: 15px;
}

.btn-logout {
  position: absolute;
  top: 10px;
  right: 20px;
  background-color: #ff4d4d;
  border: none;
  color: white;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.btn-logout:hover {
  background-color: #ff3333;
}


export default Navbar;
