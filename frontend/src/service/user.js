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
    },

    /**
     * Cria usuário
     * @param {String} username
     * @param {String} name
     * @param {String} email
     * @param {String} role
     * @param {String} password
     * @returns Resposta do servidor
     */
    async create(id, name, email, role, password) {
        try {
            const payload = { id: id,
                            name: name,
                            email: email,
                            role: role,
                            password: password
                            };
            const response = await apiClient.post('/user', payload)
            return response.status;
        } catch (error) {
            if (error.response) {
                return error.response.status;
            }
        }
    },

    /**
     * Atualiza usuário
     * @param {String} username
     * @param {String} name
     * @param {String} email
     * @param {String} role
     * @param {String} password
     * @returns Resposta do servidor
     */
    async update(id, name, email, role, password) {
        try {
            const payload = { name: name,
                            email: email,
                            password: password,
                            role: role
                            };
            const response = await apiClient.put(`/user/${id}`, payload)
            return response.status;
        } catch (error) {
            if (error.response) {
                return error.response.status;
            }
        }
    },

    /**
     * Deleta usuário
     * @param {String} username
     * @returns Resposta do servidor
     */
    async delete(id) {
        try {
            const response = await apiClient.delete(`/user/${id}`)
            return response.status;
        } catch (error) {
            if (error.response) {
                return error.response.status;
            }
        }
    }
}

export default UserService;
