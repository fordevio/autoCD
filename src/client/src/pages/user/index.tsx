import { useEffect, useState } from "react"
import "./index.css"
import { UserModel } from "../../models/user"
import { getUsers } from "../../api/user"
import UserCard from "./userCard"

const User = () => {
  const [users, setUsers]=useState<UserModel[]>([])
  const fetchUsers = async()=>{
    try{
      const res = await getUsers()
      setUsers(res)
    }catch(e:any){
       
    }
  }

  useEffect(()=>{
    fetchUsers()
  },[])


  return (
    <div className="user-page">
        <button className="user-btn">Create User</button>
      <div className="grid-container">
       {users.map((user, index)=><UserCard key={index}  user={user}/>)}
      </div>
      <div
          className="popup-overlay"
          id="popupOverlay"
          style={open ? { display: 'block' } : { display: 'none' }}
        >
          <div className="popup" id="popup">
            <span
              className="close"
              id="closePopup"
              onClick={() => setOpen(false)}
            >
              &times;
            </span>

            <div className="popup-content">
              <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={e => setUsername(e.target.value)}
              />
              <input
                type="text"
                placeholder="Password"
                value={password}
                onChange={e => setPassword(e.target.value)}
              />
              <input
                type="text"
                placeholder="Permission"
                value={permissions}
                onChange={e => setPermissions(e.target.value)}
              />
              <p>x -execute | w -write | r -read</p>
              <button className="submit" onClick={add_user}>
                Submit
              </button>
            </div>
          </div>
      </div>
    </div>
  )
}

export default User