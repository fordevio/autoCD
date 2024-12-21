import "./index.css"
import logo from "../../assets/autocd-logo.png"
import { CurrentUser } from "../../models/user"
import React, { useEffect } from "react"
import { Link } from "react-router-dom"

interface Prop {
  currUser : CurrentUser | null
  setCurrUser: (user: CurrentUser | null) => void
}

const Navbar: React.FC<Prop> = ({currUser, setCurrUser}) => {
  useEffect(()=>{},[currUser, setCurrUser])
  return (
    <div className="navbar">
      <img src={logo} alt="logo" className="navbar-logo"/>
      <h1 className="nav-h1">autoCD</h1>
     {currUser&& <div className="nav-links">
        <Link className="link" to={"/"}>Projects</Link>
        {currUser && currUser.roles.includes("ADMIN") &&<Link  to="/users" className="link">Users</Link>}
      </div>}
    </div>
  )
}

export default Navbar