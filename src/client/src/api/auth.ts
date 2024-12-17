import { getHost, getToken } from "../utils/utils";

const url=getHost()
const token = getToken();

export const login=async(username: string, password:string)=>{
const response= await axios.post<{token:string}>(url+"/api/auth/login",{
    "username":username,
    "password":password
   })
  return response.data;
}