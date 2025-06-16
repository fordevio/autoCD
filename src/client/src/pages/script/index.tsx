import { useNavigate, useParams } from 'react-router-dom';
import './index.css';
import { useState } from 'react';
import { ProjectModel } from '../../models/project';
import { getProject } from '../../api/project';
import { getScript, updateScript } from '../../api/script';
import MonacoEditor from '@monaco-editor/react';
import toast from 'react-hot-toast';
import { Link } from 'react-router-dom';
import { getHost } from '../../utils/utils';
import { getAuthToken } from '../../api/token';
import { useQuery } from 'react-query';

const Script = () => {
  const { id } = useParams();
  const numericId = Number(id);
  const navigate = useNavigate();
  const [project, setProject] = useState<ProjectModel | null>(null);
  const [script, setScript] = useState<string>('');
  const [newScript, setNewScript] = useState<string>('');
  const [edit, setEdit] = useState<boolean>(false);
  const [token, setToken] = useState<string | null>(null);
  const fetch = async () => {
    try {
      const projectRes = await getProject(numericId);
      setProject(projectRes);
      const scriptRes = await getScript(numericId);
      setScript(scriptRes);
      setNewScript(scriptRes);
    } catch (e) {
      navigate('/');
      return;
    }
  };

  const handleChange = async (value: string | undefined) => {
    if (!edit) {
      setNewScript(script);
      return;
    }
    setNewScript(value || '');
  };

  const handleCancel = async () => {
    setEdit(false);
    setNewScript(script);
  };

  const saveFunc = async () => {
    try {
      await updateScript(numericId, newScript);
      setScript(newScript);
      setEdit(false);
    } catch (e: any) {
      throw e.response ? e.response.data : 'message: Request failed';
    }
  };
  const saveHandler = async () => {
    if (newScript === script) {
      toast.error('Nothing to save');
      return;
    }
    toast.promise(saveFunc(), {
      loading: 'In progress...',
      success: 'Script updated',
      error: data => `${data.message}`,
    });
  };

  const getToke_n = async () => {
    try {
      const res = await getAuthToken();
      setToken(res.token);
    } catch (e) {}
  };
  useQuery('script', fetch, {
    retry: false,
  });

  return (
    <div className="script-page">
      <div>
        <h1 className="project-h1">{project?.name}</h1>
      </div>
      <div className="editor">
        <div>
          <p>
            Note: <br />- No need to use sudo.
            <br />- Use full paths instead of relative paths, (ex:
            /home/ubuntu/project).
          </p>
          <p></p>
        </div>
        <MonacoEditor
          height="400px"
          defaultLanguage="shell"
          defaultValue={newScript}
          value={newScript}
          onChange={handleChange}
          theme="vs-dark"
          options={{
            readOnly: !edit,
            fontSize: 14,
            tabSize: 2,
            minimap: { enabled: true },
            lineNumbers: 'on',
          }}
        />
        <div>
          {!edit && (
            <button className="editor-btn" onClick={() => setEdit(true)}>
              Edit
            </button>
          )}
          {edit && (
            <button className="editor-btn" onClick={handleCancel}>
              Cancel
            </button>
          )}
          {edit && (
            <button className="editor-btn" onClick={saveHandler}>
              Save
            </button>
          )}
          <Link to={`/logs/${project?.id}`} className="log-btn">
            Get Logs
          </Link>
        </div>
      </div>
      <div>
        <div>
          <h2 className="cd-h1">CD Request</h2>
          <div className="cd-command">
            <p>
              curl -X POST \<br />
              -H "Content-Type: application/json" \<br />
              {`${getHost()}/api/deliver/${id}?token=${token ? token : 'generate-token'}`}{' '}
              <br />
            </p>
          </div>
        </div>
      </div>
      <div>
        <button className="token-btn" onClick={() => getToke_n()}>
          Get Auth Token
        </button>
      </div>
      <p>
        <span>Token: </span>
        {token ? token : 'Click the button to generate a token'}
      </p>
    </div>
  );
};

export default Script;
