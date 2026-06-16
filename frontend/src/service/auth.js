import { SERVER_ERROR, UNAUTHORIZED_ERROR } from "@/common/errors";
import apiClient from "./api";
import { useAuthStore } from "@/store/auth";

/** Serviço de autenticação de usuário */
const AuthService = {
    /**
     * Autentica usuário na aplicação
     * @param {String} username
     * @param {String} password
     * @returns {any} Dados da requisição
     */
    async login(username, password) {
        useAuthStore().purgeAuth();
        const payload = { user: username, pass: password };
        const response = await apiClient.post('/auth/login', payload)

        switch (response.status) {
            case 200:
                return response.data;
            case 400:
                throw UNAUTHORIZED_ERROR
            default:
                throw SERVER_ERROR
        }
    },
}

export default AuthService;
