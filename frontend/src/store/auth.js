import { defineStore } from "pinia";
import TokenService from "@/service/token";
import AuthService from "@/service/auth";
import UserService from "@/service/user";

/** Store de dados de autenticação */
export const useAuthStore = defineStore("auth", {
    state: () => ({
        user: sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {},
        isAuthenticated: !!TokenService.getToken()
    }),

    getters: {
        currentUser: (state) => state.user
    },

    actions: {
        setAuth(authData) {
            this.isAuthenticated = true;
            TokenService.setToken(authData.token, authData.expiresAt);
        },

        setUser(user) {
            this.user = user;
            sessionStorage.setItem("user", JSON.stringify(user));
        },

        purgeAuth() {
            this.isAuthenticated = false;
            this.user = {};
            TokenService.clear();
        },

        async login(username, password) {
            this.setAuth(await AuthService.login(username, password));
            this.setUser(await UserService.get(username));
        },

        logout() {
            this.purgeAuth();
        },

        isGuest() {
            return this.isAuthenticated && this.user.role === "GUEST";
        },

        isUser() {
            return this.isAuthenticated && this.user.role === "USER";
        },

        isAdmin() {
            return this.isAuthenticated && this.user.role === "ADMIN";
        },

        isSysAdmin() {
            return this.isAuthenticated && this.user.role === "SYSADMIN";
        },

        hasElevatedPermissions() {
            return this.isAdmin() || this.isSysAdmin();
        },

        canCreateTasks() {
            return this.isUser() || this.hasElevatedPermissions();
        },

        canComment() {
            return this.isAuthenticated && !this.isGuest();
        }
    }
});
