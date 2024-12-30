export interface CurrentUser {
  id: number;
  username: string;
  roles: string[];
  permissions: string[];
}

export interface UserModel {
  id: number;
  username: string;
  roles: string[];
  permissions: string[];
  password: string;
}
