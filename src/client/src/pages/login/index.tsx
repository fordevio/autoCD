import "./index.css"
import logo from '../../assets/autocd-logo.png'

const Login = () => {
  return (
    <>
     <div className="log-div">
     <img className="logo" src={logo}/>
     <div className="input-div">
        <div className="input">
            <span className="input-box">Username: </span><input type="text" />
        </div>
        <div className="input">
            <span className="input-box">Password: </span><input type="password" />
        </div>
        <button>Login</button>
     </div>
     </div>
    </>
  )
}

export default Login