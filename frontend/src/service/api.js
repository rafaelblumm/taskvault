import axios from "axios";
import TokenService from "./token";
import { API_URL } from "@/common/config";

const apiClient = axios.create({
    baseURL: API_URL
});

apiClient.interceptors.request.use(request => {
    const token = TokenService.getToken();
    if (token && request.url != '/auth/login') {
        request.headers['Authorization'] = `Bearer ${token}`;
    }

    return request;
}, error => {
    return Promise.reject(error);
});

export default apiClient;
