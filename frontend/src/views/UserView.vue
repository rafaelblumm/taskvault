<template>
    <div class="container">
        <h2>Informações sobre usuários:</h2>
        <p>Autenticado como: {{ user.id }}</p>
        <p>Cargo: {{ user.role }} </p>
        <div class="options">
            <button @click="$router.push('/createUser')">Cadastrar novo usuário</button>
            <button @click="$router.push('/getUser')">Obter Informações de um usuário</button>
            <button @click="$router.push('/updateUser')">Atualizar informações de um usuário</button>
            <button @click="$router.push('/deleteUser')"> Remover um usuário</button>
            <button @click="$router.push('/tasks')">Voltar</button>
        </div>
    </div>
</template>

<script>
import TokenService from '@/service/token';
import UserService from '@/service/user';
import { jwtDecode } from 'jwt-decode';
import '@/assets/styles.css';

export default {
    data() {
        return {
            user: {},
        };
    },
    async mounted() {
        try {
            const token = TokenService.getToken();
            const decoded = jwtDecode(token);
            const userSub = decoded.sub;

            const response = await UserService.get(userSub);

            this.user = response.data || response;
        } catch (error) {
            console.error("Erro ao carregar dados do usuário:", error);
        }
    },
};
</script>