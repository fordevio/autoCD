import './index.css';
import logo from '../../assets/autocd-logo.png';
import { CurrentUser } from '../../models/user';
import React, { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';

interface Prop {
  currUser: CurrentUser | null;
  setCurrUser: (user: CurrentUser | null) => void;
}

const Navbar: React.FC<Prop> = ({ currUser, setCurrUser }) => {
  const navigate = useNavigate();
  useEffect(() => {}, [currUser, setCurrUser]);
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

export default Navbar;
