import { useParams } from 'react-router-dom';
import './index.css';
import { useState } from 'react';
import { getLogs } from '../../api/logs';
import MonacoEditor from '@monaco-editor/react';
import { useQuery } from 'react-query';
const Logs = () => {
  const { id } = useParams();
  const [logs, setLogs] = useState<string>('');

  const Fetch = async () => {
    try {
      if (!id) return;
      const numericId = parseInt(id);
      const res = await getLogs(numericId);
      setLogs(res);
    } catch (e) {}
  };

  useQuery('logs', Fetch, {
    retry: false,
  });

  return (
    <div>
      <MonacoEditor
        height="400px"
        defaultLanguage="shell"
        defaultValue={logs}
        value={logs}
        theme="vs-dark"
        options={{
          readOnly: true,
          fontSize: 14,
          tabSize: 2,
          minimap: { enabled: true },
          lineNumbers: 'on',
        }}
      />
    </div>
  );
};

export default Logs;
