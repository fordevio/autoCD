import axios from "axios";
import { CurrentUser, UserModel } from "../models/user";
import { getHost, getToken } from "../utils/utils";

const url=getHost()
const token = getToken();

export const getCurrentUser = async ():Promise<CurrentUser> => {
    const response = await axios.get<CurrentUser>(url+"/api/protected/user/me",{
        headers:{
            Authorization: `Bearer ${token}`
        }
    });
    return response.data;
}


export const getUsers = async():Promise<UserModel[]> =>{
    const response = await axios.get<UserModel[]>(url+"/api/protected/user/admin/all",{
        headers:{
            Authorization: `Bearer ${token}`
        }
    })
    return response.data;
}

export const createUser = async(username: string, password: string, roles: string[], permissions: string[])=>{
    const response = await axios.post(url+"/api/protected/user/admin/create",{
        "username":username,
        "password":password,
        "roles":roles,
        "permissions":permissions
    },{
        headers:{
            Authorization: `Bearer ${token}`
        }
    })
    return response.data;
}