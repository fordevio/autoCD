import { ProjectModel } from "../models/project";
import { getHost, getToken } from "../utils/utils";
import axios from "axios";
const url=getHost()
const token=getToken()

export const getAllProjects = async():Promise<ProjectModel[]>=>{
     const response = await axios.get<ProjectModel[]>(`${url}/api/protected/project/all`, {
        headers:{
            "Authorization": `Bearer ${token}`
        }
     })
     return response.data;
}

export const deleteProject=async(id: Number)=>{
    const response = await axios.delete(`${url}/api/protected/project/delete/${id}`, {
        headers:{
            "Authorization": `Bearer ${token}`
        }
    })
    return response.data;
}

export const addProject = async(name: string, description: string):Promise<ProjectModel>=>{
    const response= await axios.post<ProjectModel>(`${url}/api/protected/project/add`, {
        name: name,
        description: description
    }, {
        headers:{
            "Authorization": `Bearer ${token}`
        }
    })
    return response.data;
}

export const editProject = async(id:Number, name: string, description: string):Promise<ProjectModel>=>{
    const response = await axios.put<ProjectModel>(`${url}/api/protected/project/update/${id}`, {
        name: name,
        description: description
    }, {
        headers:{
            "Authorization": `Bearer ${token}`
        }
    })
    return response.data
}

export const getProject = async(id: Number):Promise<ProjectModel> =>{
    const res = await axios.get<ProjectModel>(`${url}/api/protected/project/get/${id}`,{
        headers:{
            "Authorization": `Bearer ${token}`
        }
    })
    return res.data;
}