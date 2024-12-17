import "./index.css"
import logo from '../../assets/autocd-logo.png'
import { useState } from "react"
import toast from "react-hot-toast"
import { login } from "../../api/auth"
import { useNavigate } from "react-router-dom"

const Login = () => {
  const [username, setUsername] = useState<string>('')
  const [password, setPassword] = useState<string>('')
  const navigate = useNavigate()

  const loginFunc = async ()=>{
    try{

      const res = await login(username, password)
      localStorage.setItem('token', res.token)
      navigate('/')
      
    }catch(e:any){
      
      throw e.response ? e.response.data : { error: 'Request failed' };
    }
  }
  const submitHandler = async () =>{
    if(username==='' || password===''){
      toast.error("Please fill all the fields")
      return
    }
    if(password.length<4){
      toast.error("Password must be at least 4 characters")
      return
    }
    toast.promise(loginFunc(), {
      loading: 'Login in...',
      success: 'LOgin sucessful',
      error: data => `${data.message}`,
    });

  }
  return (
    <>
     <div className="log-div">
     <img className="logo" src={logo}/>
     <div className="input-div">
        <div className="input">
            <span className="input-box">Username: </span><input type="text" value={username}  onChange={(e)=>setUsername(e.target.value)}/>
        </div>
        <div className="input">
            <span className="input-box">Password: </span><input type="password" value={password} onChange={(e)=>setPassword(e.target.value)} />
        </div>
        <button onClick={submitHandler}>Login</button>
     </div>
     </div>
    </>
  )
}

export default Login