import { Route, Routes, useLocation, useNavigate } from "react-router-dom";
import Projects from "./pages/project";
import Login from "./pages/login";
import "./index.css"
import Navbar from "./components/navbar";
import { useEffect } from "react";
import { getToken } from "./utils/utils";
import toast from "react-hot-toast";
import { getCurrentUser } from "./api/user";

function App() {
  const navigate = useNavigate();
  const location = useLocation();

  const fetchCurrentUser = async()=>{
    if(!getToken()){

        if(location.pathname !== "/login"){ 
            navigate("/login");
            return;
        }
    }
    try{
      const user = await getCurrentUser();
      if(location.pathname === "/login"){ 
        navigate("/");
        return;
      }
  
    }catch(e){
      if(location.pathname !== "/login"){ 
        navigate("/login");
        toast.error("Session expired")
        return;
    }
    }
  }
  useEffect(()=>{
    fetchCurrentUser()
  },[])
  return (
    <div>
      <Navbar/>
      <Routes>
      <Route path="/" element={<Projects />} />
      <Route path="/login" element={<Login/>} />
      </Routes>
    </div>
  );
}

export default App;
