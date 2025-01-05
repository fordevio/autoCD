import toast from 'react-hot-toast';
import { deleteUser, updateUser } from '../../../api/user';
import { UserModel } from '../../../models/user';
import './index.css';
import { useState } from 'react';
import { useQuery } from 'react-query';

interface Props {
  user: UserModel;
  users: UserModel[];
  setUsers: (users: UserModel[]) => void;
}

const UserCard: React.FC<Props> = ({ user, users, setUsers }) => {
  const [username, setUsername] = useState<string>(user.username);
  const [password, setPassword] = useState<string>(user.password);
  const [read, setRead] = useState<boolean>(false);
  const [write, setWrite] = useState<boolean>(false);
  const [del, setDel] = useState<boolean>(false);
  const [open, setOpen] = useState<boolean>(false);

  const init = () => {
    if (user.permissions.includes('READ')) {
      setRead(true);
    }
    if (user.permissions.includes('WRITE')) {
      setWrite(true);
    }
    if (user.permissions.includes('DELETE')) {
      setDel(true);
    }
  };

  useQuery('init', init, {
    retry: false,
  });

  const deleteFunc = async () => {
    try {
      await deleteUser(user.id);
      setUsers(users.filter(u => u.id !== user.id));
    } catch (e: any) {
      throw e.response ? e.response.data : { error: 'Request failed' };
    }
  };
  const DeleteHandler = async () => {
    toast.promise(deleteFunc(), {
      loading: 'Deleting...',
      success: () => {
        return 'User deleted';
      },
      error: err => {
        return `Failed: ${err.message}`;
      },
    });
  };

  const submitFunc = async () => {
    if (username === '' || password === '' || (!read && !write && !del)) {
      toast.error('Please fill all fields');
      return;
    }
    try {
      const members = [
        read ? 'READ' : null,
        write ? 'WRITE' : null,
        del ? 'DELETE' : null,
      ].filter((value): value is string => Boolean(value));
      await updateUser(user.id, username, password, members);

      setUsers(
        users.filter(u => {
          if (user.id === u.id) {
            u.username = username;
            u.password = password;
            u.permissions = members;
          }
          return false;
        })
      );

      setOpen(false);
    } catch (e: any) {
      throw e.response ? e.response.data : { error: 'Request failed' };
    }
  };

  const submitHandler = async () => {
    toast.promise(submitFunc(), {
      loading: 'Updating user...',
      success: 'User updated',
      error: data => `${data.message}`,
    });
  };

  return (
    <div className="card">
      <div>
        <h1 className="card-h">
          <span>Username: </span>
          {user.username}
        </h1>
        <h1 className="card-h">
          <span>Password: </span>
          {user.password}
        </h1>
        <h1 className="card-h">
          <span>Roles: </span>
          {user.roles.map(role => {
            return role + '  ';
          })}
        </h1>
        <h1 className="card-h">
          <span>Permissions: </span>
          {user.permissions.map(perm => perm + ' ')}
        </h1>
      </div>
      <div>
        <button className="card-btn" onClick={() => setOpen(true)}>
          Edit
        </button>
        <button className="card-btn" onClick={DeleteHandler}>
          Delete
        </button>
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
              placeholder="Username"
              className="text-pop-input"
              value={username}
              onChange={e => setUsername(e.target.value)}
            />
            <input
              type="text"
              placeholder="Password"
              className="text-pop-input"
              value={password}
              onChange={e => setPassword(e.target.value)}
            />

            <div>
              <p>Permissions* </p>
              <input
                type="checkbox"
                checked={read}
                onChange={e => {
                  if (user.roles.includes('ADMIN')) return;
                  setRead(!read);
                }}
              />
              <span>READ </span>

              <input
                type="checkbox"
                checked={write}
                onChange={e => {
                  if (user.roles.includes('ADMIN')) return;
                  setWrite(!write);
                }}
              />
              <span>WRITE </span>

              <input
                type="checkbox"
                checked={del}
                onChange={e => {
                  if (user.roles.includes('ADMIN')) return;
                  setDel(!del);
                }}
              />
              <span>DELETE </span>
            </div>

            <button className="submit" onClick={submitHandler}>
              Submit
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserCard;
