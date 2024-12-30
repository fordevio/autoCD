import axios from 'axios';
import { CurrentUser, UserModel } from '../models/user';
import { getHost, getToken } from '../utils/utils';

const url = getHost();
const token = getToken();

export const getCurrentUser = async (): Promise<CurrentUser> => {
  const response = await axios.get<CurrentUser>(
    url + '/api/protected/user/me',
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return response.data;
};

export const getUsers = async (): Promise<UserModel[]> => {
  const response = await axios.get<UserModel[]>(
    url + '/api/protected/user/admin/all',
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );

  return response.data;
};

export const createUser = async (
  username: string,
  password: string,
  roles: string[],
  permissions: string[]
): Promise<UserModel> => {
  const response = await axios.post<UserModel>(
    url + '/api/protected/user/admin/add',
    {
      username: username,
      password: password,
      roles: roles,
      permissions: permissions,
    },
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return response.data;
};

export const updateUser = async (
  id: number,
  username: string,
  password: string,
  roles: string[],
  permissions: string[]
): Promise<UserModel> => {
  const response = await axios.put<UserModel>(
    url + '/api/protected/user/admin/update/' + id,
    {
      username: username,
      password: password,
      roles: roles,
      permissions: permissions,
    },
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return response.data;
};

export const deleteUser = async (id: number): Promise<UserModel> => {
  const response = await axios.delete<UserModel>(
    url + '/api/protected/user/admin/delete/' + id,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return response.data;
};
