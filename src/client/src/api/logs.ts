import axios from 'axios';
import { getHost, getToken } from '../utils/utils';

const host = getHost();
const token = getToken();

export const getLogs = async (id: number): Promise<string> => {
  const res = await axios.get<string>(
    `${host}/api/protected/project/getLogs/${id}`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return res.data;
};
