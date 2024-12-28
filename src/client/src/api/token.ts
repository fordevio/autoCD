import { getHost, getToken } from "../utils/utils"
import axios from "axios"
const host = getHost()
const token= getToken()

export const getAuthToken=async():Promise<{
    token: string
}>=>{
   const res= await axios.post<{token:string}>(`${host}/api/protected/auth/getToken`,{}, {
    headers:{
         "Authorization": `Bearer ${token}`
    }
   })
   return res.data
}