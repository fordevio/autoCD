import axios from "axios";

export const getCurrentUser = async () => {
    const response = await axios.get("/api/protected/user/me");
    return response.data;
}
