import axios from "axios";

const apiClient = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});


apiClient.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");

    //console.log("Gönderilen token:", token);

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});


apiClient.interceptors.response.use(
    (response) => {

        if (response.data?.data !== undefined) {
            return response.data.data;
        }

        return response.data;
    },
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem("token");
        }

        return Promise.reject(error);
    }
);

export default apiClient;