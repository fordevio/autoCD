import { useEffect, useState } from "react"
import "./index.css"
import { ProjectModel } from "../../models/project"
import { addProject, getAllProjects } from "../../api/project"
import ProjectCard from "./project-card"
import toast from "react-hot-toast"



const Projects = () => {
  const [projects, setProjects]=useState<ProjectModel[]>([])
  const [open, setOpen] = useState<boolean>(false)
  const [projectName, setProjectName] = useState<string>("")
  const [description, setDescription]= useState<string>("")
  const fetchProjects= async()=>{
    try{
       const response = await getAllProjects()
       setProjects(response)
    }catch(e){
     console.log(e)
    }
  }

  const addProjectFunc = async()=>{
    try{
      const response = await addProject(projectName, description)
      setProjects([...projects, response])
      setOpen(false)
      setProjectName("")
      setDescription("")
    }catch(e:any){
      throw e.response ? e.response.data : { error: 'Request failed' };
    }
  }
  const submitHandler = async()=>{
    if(projectName===""||description===""){
      toast.error("Please fill all fields")
      return
    }
    toast.promise(addProjectFunc(), {
      loading: 'Adding...',
      success: 'Project added',
      error: data => `${data.message}`,
    });
  }
  useEffect(()=>{
    fetchProjects()
  },[])
  return (
    <div className="project-page">
      <button className="project-btn" onClick={()=>setOpen(true)}>Create Project</button>
      <div className="grid-container">
       {projects.map((project,index)=><ProjectCard key={index} project={project} projects={projects} setProjects={setProjects}/>)}
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
                placeholder="Name"
                className="text-pop-input"
                value={projectName}
                onChange={e => setProjectName(e.target.value)}
              />
              <input
                type="text"
                placeholder="Description"
                className="text-pop-input"
                value={description}
                onChange={e => setDescription(e.target.value)}
              />
              <div>

              <button className="submit" onClick={submitHandler} >
                Submit
              </button>
            </div>
          </div>
        </div>
    </div>
    </div>

  )
}

export default Projects