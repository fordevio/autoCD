import { useState } from 'react';
import { ProjectModel } from '../../../models/project';
import './index.css';
import { deleteProject, editProject } from '../../../api/project';
import toast from 'react-hot-toast';
import { Link } from 'react-router-dom';

interface Props {
  project: ProjectModel;
  projects: ProjectModel[];
  setProjects: (projects: ProjectModel[]) => void;
}
const ProjectCard: React.FC<Props> = ({ project, projects, setProjects }) => {
  const [open, setOpen] = useState(false);
  const [projectName, setProjectName] = useState<string>(project.name);
  const [description, setDescription] = useState<string>(project.description);

  const deleteFunc = async () => {
    try {
      await deleteProject(project.id);
      setProjects(projects.filter(pr => pr.id !== project.id));
    } catch (e: any) {
      throw e.response ? e.response.data : { message: 'Request failed' };
    }
  };

  const DeleteHandler = async () => {
    toast.promise(deleteFunc(), {
      loading: 'Deleting...',
      success: 'Project deleted',
      error: data => `${data.message}`,
    });
  };

  const editFunc = async () => {
    try {
      await editProject(project.id, projectName, description);
      setProjects(
        projects.map(pr => {
          if (pr.id === project.id) {
            pr.name = projectName;
            pr.description = description;
            return pr;
          }
          return pr;
        })
      );
      setOpen(false);
      setProjectName('');
      setDescription('');
    } catch (e: any) {
      throw e.response ? e.response.data : { message: 'Request failed' };
    }
  };
  const submitHandler = async () => {
    if (project.name === '' || project.description === '') {
      toast.error('Please fill all the feilds');
      return;
    }
    toast.promise(editFunc(), {
      loading: 'In progress...',
      success: 'Project edited',
      error: data => `${data.message}`,
    });
  };

  return (
    <div className="card">
      <div>
        <h1 className="card-h">{project.name}</h1>
        <h1 className="card-h">{project.description}</h1>
      </div>
      <div>
        <button className="card-btn" onClick={() => setOpen(true)}>
          Edit
        </button>
        <button className="card-btn" onClick={DeleteHandler}>
          Delete
        </button>
        <Link to={`/project/${project.id}`} className="readmore">
          Read more
        </Link>
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
              placeholder="ProjectName"
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
              <button className="submit" onClick={submitHandler}>
                Submit
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProjectCard;
