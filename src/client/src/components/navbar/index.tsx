import "./index.css"
import logo from "../../assets/autocd-logo.png"
const Navbar = () => {
  return (
    <div className="navbar">
      <img src={logo} alt="logo" className="navbar-logo"/>
      <h1 className="nav-h1">autoCD</h1>
    </div>
  )
}

export default Navbar