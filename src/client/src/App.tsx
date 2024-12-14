import { Route, Routes } from "react-router-dom";
import User from "./pages/user/User";

function App() {
  return (
    <div>
      <h1>Hello world My name is yash</h1>
      <Routes>
      <Route path="/user" element={<User />} />
      </Routes>
    </div>
  
  );
}

export default App;
