import axios from "axios"
import { getHost, getToken } from "../utils/utils"

const url=getHost()
const token = getToken();

export const script =  async(id: string):Promise<string>=>{
    const res = await axios.get<string>(`${url}/api/protected/project/getScript/${id}`, {
        headers:{
            "Authorization": `Bearer ${token}`
        }
    })
    return res.data;
}

export const updateScript=async(id: string, script: string):Promise<string>=>{
    const res = await axios.put<string>(`${url}/api/protected/project/updateScript/${id}`, {
        data: script
    }, {
        headers:{
            "Authorization": `Bearer ${token}`
        }
    })
    return res.data;
}