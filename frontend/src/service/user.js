import { SERVER_ERROR } from "@/common/errors";
import apiClient from "./api";

/** Serviço de manipulação de usuários */
const UserService = {
    /**
     * Busca usuário
     * @param {String} username
     * @returns {*}
     */
    async get(username) {
        const response = await apiClient.get(`/user/${username}`)
        switch (response.status) {
            case 200:
                return response.data;
            case 404:
                return {};
            default:
                throw SERVER_ERROR;
        }
    }
}

export default UserService;
