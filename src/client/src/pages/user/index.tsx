import { useEffect, useState } from 'react';
import './index.css';
import { UserModel } from '../../models/user';
import { createUser, getUsers } from '../../api/user';
import UserCard from './userCard';
import toast from 'react-hot-toast';

const User = () => {
  const [users, setUsers] = useState<UserModel[]>([]);
  const [open, setOpen] = useState<boolean>(false);
  const [username, setUsername] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [admin, setAdmin] = useState<boolean>(false);
  const [member, setMember] = useState<boolean>(false);
  const [read, setRead] = useState<boolean>(false);
  const [write, setWrite] = useState<boolean>(false);
  const [del, setDel] = useState<boolean>(false);

  const fetchUsers = async () => {
    try {
      const res = await getUsers();
      setUsers(res);
    } catch (e: any) {}
  };

  const submitFunc = async () => {
    try {
      const roles = [admin ? 'ADMIN' : null, member ? 'MEMBER' : null].filter(
        (value): value is string => Boolean(value)
      );

      const members = [
        read ? 'READ' : null,
        write ? 'WRITE' : null,
        del ? 'DELETE' : null,
      ].filter((value): value is string => Boolean(value));
      const response = await createUser(username, password, roles, members);
      setUsers([...users, response]);
      setOpen(false);
      setUsername('');
      setPassword('');
      setAdmin(false);
      setMember(false);
      setRead(false);
      setWrite(false);
      setDel(false);
    } catch (e: any) {
      throw e.response ? e.response.data : { error: 'Request failed' };
    }
  };
  const submitHandler = async () => {
    if (
      username === '' ||
      password === '' ||
      (!admin && !member) ||
      (!read && !write && !del)
    ) {
      toast.error('Please fill all fields');
      return;
    }
    if (password.length < 4) {
      toast.error('Password must be at least 4 characters');
      return;
    }
    toast.promise(submitFunc(), {
      loading: 'Creating user...',
      success: 'User created',
      error: data => `${data.message}`,
    });
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  return (
    <div className="user-page">
      <button className="user-btn" onClick={() => setOpen(true)}>
        Create User
      </button>
      <div className="grid-container">
        {users.map((user, index) => (
          <UserCard key={index} user={user} users={users} setUsers={setUsers} />
        ))}
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
              <p>Roles* </p>
              <input
                type="checkbox"
                checked={admin}
                onChange={e => setAdmin(!admin)}
              />
              <span>ADMIN </span>

              <input
                type="checkbox"
                checked={member}
                onChange={e => setMember(!member)}
              />
              <span>MEMBER </span>
            </div>

            <div>
              <p>Permissions* </p>
              <input
                type="checkbox"
                checked={read}
                onChange={e => setRead(!read)}
              />
              <span>READ </span>

              <input
                type="checkbox"
                checked={write}
                onChange={e => setWrite(!write)}
              />
              <span>WRITE </span>

              <input
                type="checkbox"
                checked={del}
                onChange={e => setDel(!del)}
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

export default User;
