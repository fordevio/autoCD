import { ProjectModel } from "../../../models/project"
import "./index.css"

interface Props{
    project: ProjectModel,
    projects: ProjectModel[],
    setProjects:  (projects: ProjectModel[])=>void
}
const ProjectCard:React.FC<Props> = ({project, projects, setProjects}) => {
  return (
    <div>Project</div>
  )
}

export default ProjectCard