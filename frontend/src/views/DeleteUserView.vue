<template>
    <div class="container">
        <h2>Deletar um usuário:</h2>
        <p>Autenticado como: {{ user.id }}</p>
        <p>Cargo: {{ user.role }} </p>
        <input
            type="text"
            placeholder="nome de usuário"
            v-model="userIdFind"
        />
        <div v-if="userFound" class="resultado-busca" style="margin-top: 20px;">
            <h3>Deseja deletar a conta de {{ userFound.name }}?</h3>
        </div>
        <button @click="userFound ? deleteUser() : getUser()">
          {{ userFound ? 'Deletar' : 'Procurar' }}
        </button>
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
                alert('Por favor, digite um nome de usuário ou ID.');
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
        },

        async deleteUser() {
            const response = await UserService.delete(this.userFound.id);
            switch (response) {
                case 204:
                    alert('Usuário deletado.');
                    break;
                case 403:
                    alert('Cargo incompativel para a requisição.');
                    break;
                default:
                    alert('Ocorreu um erro inesperado ao fazer a requisição.\nGaranta que todos os dados foram preenchidos.');
                    break;
            }
        }
    }
};
</script>