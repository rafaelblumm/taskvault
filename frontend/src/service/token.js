const TOKEN_KEY = "token";
const EXPIRES_AT_KEY = "expires_at";

/** Serviço de manipulação de token */
const TokenService = {
    /**
     * Busca token armazenado
     * @returns {undefined|String}
     */
    getToken() {
        return sessionStorage.getItem(TOKEN_KEY)
    },

    /**
     * Armazena token
     * @param {String} token
     * @param {Date} expiresAt
     */
    setToken(token, expiresAt) {
        sessionStorage.setItem(TOKEN_KEY, token);
        sessionStorage.setItem(EXPIRES_AT_KEY, expiresAt);
    },

    /**
     * Limpa registros de token
     */
    clear() {
        sessionStorage.removeItem(TOKEN_KEY);
        sessionStorage.removeItem(EXPIRES_AT_KEY);
    },
}

export default TokenService;
