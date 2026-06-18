<template>
    <div class="container">
        <h2>Cadastrar novo usuário:</h2>
        <p>Autenticado como: {{ user.id }}</p>
        <p>Cargo: {{ user.role }} </p>
        <div v-if="user.role === 'ADMIN' || user.role === 'SYSADMIN'">
            <input
                type="text"
                placeholder="Nome"
                v-model="nameInput"
            />
            <input
                type="text"
                placeholder="Nome de usuário"
                v-model="usernameInput"
            />
            <input
                type="password"
                placeholder="Senha"
                v-model="passwordInput"
            />
            <input
                type="email"
                placeholder="E-mail"
                v-model="emailInput"
            />
            <select v-model="roleInput">
                <option value="" disabled>Cargo</option>
                <option value="GUEST">Visitante</option>
                <option value="USER">Usuário</option>
                <option value="ADMIN">Administrador</option>
                <option value="SYSADMIN">Administrador do sistema</option>
            </select>
            <button @click="createUser">Cadastrar</button>
        </div>
        <div v-else>
            <p>Somente adiministradores podem cadastrar novos usuários.</p>
        </div>
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

            nameInput: '',
            usernameInput: '',
            passwordInput: '',
            emailInput: '',
            roleInput: '',
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
        async createUser() {
            const response = await UserService.create(this.usernameInput,
                                                      this.nameInput,
                                                      this.emailInput,
                                                      this.roleInput,
                                                      this.passwordInput
                                                      );
            switch (response) {
                case 201:
                    alert('Usuário cadastrado!');
                    break;
                case 403:
                    alert('Cargo incompativel para a requisição.');
                    break;
                case 409:
                    alert('Esses dados já percentem a outro usuário!\nCertifique que o nome de usuário e email sejam únicos.');
                    this.usernameInput = '';
                    this.emailInput = '';
                    break;
                default:
                    alert('Ocorreu um erro inesperado ao fazer a requisição.\nGaranta que todos os dados foram preenchidos.');
                    break;
            }
        }
    }
};
</script>