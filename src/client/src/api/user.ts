import axios from "axios";
import { CurrentUser } from "../models/user";
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

