import { defineStore } from "pinia";
import TokenService from "@/service/token";
import AuthService from "@/service/auth";
import UserService from "@/service/user";

/** Store de dados de autenticação */
export const useAuthStore = defineStore("auth", {
    state: () => ({
        user: {},
        isAuthenticated: !!TokenService.getToken()
    }),

    getters: {
        currentUser: (state) => state.user
    },

    actions: {
        setAuth(token, user) {
            this.isAuthenticated = true;
            this.user = user;
            TokenService.setToken(token);
        },

        purgeAuth() {
            this.isAuthenticated = false;
            this.user = {};
            TokenService.clear();
        },

        async login(username, password) {
            const authData = await AuthService.login(username, password);
            TokenService.setToken(authData.token, authData.expiresAt);
            this.user = await UserService.get(username);

            return this.user;
        },

        logout() {
            this.purgeAuth();
        },
    }
});
