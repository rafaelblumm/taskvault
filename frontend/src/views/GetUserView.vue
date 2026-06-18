<template>
    <div class="container">
        <h2>Obter informação sobre um usuário:</h2>
        <p>Autenticado como: {{ user.id }}</p>
        <p>Cargo: {{ user.role }} </p>
        <input
            type="text"
            placeholder="nome de usuário"
            v-model="userIdFind"
        />
        <div v-if="userFound" class="resultado-busca" style="margin-top: 20px;">
            <h3>Usuário Encontrado:</h3>
            <p><strong>ID:</strong> {{ userFound.id }}</p>
            <p><strong>Nome:</strong> {{ userFound.name }}</p>
            <p><strong>E-mail:</strong> {{ userFound.email }}</p>
            <p><strong>Cargo:</strong> {{ userFound.role }}</p>
        </div>
        <button @click="getUser">Procurar</button>
        <button @click="$router.push('/user')">Voltar</button>
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

            userIdFind: '',
            userFound: null,
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

    methods: {
        async getUser() {
            this.userFound = null;
            if (!this.userIdFind.trim()) {
                alert('Por favor, digite um nome de usuário.');
                return;
            }

            try {
                const response = await UserService.get(this.userIdFind);
                this.userFound = response.data || response;

                if (!this.userFound) {
                    alert('Usuário não encontrado.');
                }
            } catch (error) {
                console.error("Erro ao buscar usuário:", error);
                alert('Erro ao buscar usuário.');
            }
        }
    }
};
</script>