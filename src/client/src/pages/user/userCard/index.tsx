import { UserModel } from "../../../models/user"
import "./index.css"

interface Props {
  user: UserModel
}

const UserCard :React.FC<Props>= ({user}) => {
  return (
    <div className="card">
    <div>
    <h1 className="card-h"><span>Username: </span>{user.username}</h1>
      <h1  className="card-h"><span>Password: </span>{user.password}</h1>
      <h1 className="card-h"><span>Roles: </span>{user.roles.map((role)=>{return role+"  "})}</h1>
      <h1 className="card-h"><span>Permissions: </span>{user.permissions.map((perm)=>perm+" ")}</h1>
    </div>
    <div>
      <button className="card-btn">Edit</button>
      <button className="card-btn">Delete</button>
    </div>
    </div>
  )
}

export default UserCard