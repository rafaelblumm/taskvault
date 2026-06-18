<template>
    <header class="app-header">
        <div class="brand">
            <router-link :to="Route.taskList()" class="app-name">TaskVault</router-link>
        </div>

        <nav class="nav">
            <router-link :to="Route.taskList()" class="nav-link">Tarefas</router-link>
            <router-link to="/user" class="nav-link">Usuários</router-link>
            <a href="https://github.com/rafaelblumm/taskvault" target="_blank" rel="noopener" class="nav-link">
                GitHub
            </a>
            <router-link :to="Route.login()" class="nav-link logout" @click="logout">Sair</router-link>
        </nav>

        <div class="user">{{ user }}</div>
    </header>
</template>

<script>
import { getActivePinia } from 'pinia'
import { useAuthStore } from '@/store/auth';
import Route from '../common/route'
import TokenService from '../service/token'

var username = ''
if (getActivePinia()) {
    username = `@${useAuthStore().user.id}`
}

export default {
    name: 'HeaderComponent',
    data() {
        return { Route, user: username }
    },
    methods: {
        logout() {
            try {
                TokenService.clear()
            } catch (e) {
                // ignore
            }
            this.$router.push(Route.login())
        }
    }
}
</script>

<style scoped>
.app-header {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 24px;
    border-bottom: 1px solid #eee;
    background: #fff;
    z-index: 1000;
    box-shadow: 0 1px 0 rgba(0, 0, 0, 0.04);
}

.brand .app-name {
    font-size: 22px;
    font-weight: 600;
    color: #111;
    text-decoration: none;
    display: inline-block;
    padding: 0 8px;
}

.nav {
    display: flex;
    gap: 20px;
    align-items: center;
}

.nav-link {
    color: #111;
    text-decoration: none;
    background: none;
    border: none;
    cursor: pointer;
    padding: 6px 10px;
    font-size: 14px;
}

.nav-link:hover {
    text-decoration: underline;
}

.nav-link.logout {
    background: none;
}

.user {
    color: #666;
    font-size: 14px;
    padding-left: 8px
}
</style>
