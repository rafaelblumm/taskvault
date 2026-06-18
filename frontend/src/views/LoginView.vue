<template>
    <form name="login-form">
        <div class="mb-3">
            <h1>TaskVault</h1>
            <h2>Login</h2>
        </div>
        <div class="mb-3">
            <label for="username">Usuário</label>
            <input id="username" v-model="username" type="text" />
        </div>
        <div class="mb-3">
            <label for="password">Senha</label>
            <input id="senha" v-model="password" type="password" />
        </div>
        <button class="btn btn-outline-dark" type="submit" v-on:click.prevent="login()">
            Login
        </button>
    </form>
</template>

<script>
import Route from '@/common/route';
import { useAuthStore } from '@/store/auth';

const authStore = useAuthStore();

export default {
    data() {
        return {
            username: '',
            password: ''
        };
    },

    methods: {
        login() {
            authStore.login(this.username, this.password)
                .then(() => this.$router.push(Route.taskList()))
                .catch(() => alert("Login falhou"));
        }
    }
};
</script>
