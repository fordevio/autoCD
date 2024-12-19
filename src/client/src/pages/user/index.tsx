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
    </div>
  )
}

export default User