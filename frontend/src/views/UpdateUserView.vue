<template>
    <div class="container">
        <h2>Atualizar informações de um usuário:</h2>
        <p>Autenticado como: {{ user.id }}</p>
        <p>Cargo: {{ user.role }} </p>
        <input
            type="text"
            placeholder="nome de usuário"
            v-model="userIdFind"
        />
        <div v-if="userFound" class="resultado-busca" style="margin-top: 20px;">
            <div v-if="user.role === 'ADMIN' || user.role === 'SYSADMIN' || user.id === userFound.id">
                <input type="text" :value=userFound.id disabled>
                <input
                    type="text"
                    :placeholder=userFound.name
                    v-model="nameInput"
                />
                <input
                    type="email"
                    :placeholder=userFound.email
                    v-model="emailInput"
                />
                <div v-if="user.role === 'ADMIN' || user.role === 'SYSADMIN'">
                    <select v-model="roleInput">
                        <option value="" disabled>Cargo</option>
                        <option value="GUEST">Visitante</option>
                        <option value="USER">Usuário</option>
                        <option value="ADMIN">Administrador</option>
                        <option value="SYSADMIN">Administrador do sistema</option>
                    </select>
                </div>
                <div v-if="user.id.toLowerCase() === userFound.id.toLowerCase()">
                    <input
                        type="password"
                        placeholder="Senha"
                        v-model="passwordInput"
                    />
                </div>
            </div>
            <div v-else>
                <p>Somente adiministradores podem atualizar outros usuários.</p>
            </div>
        </div>
        <button @click="userFound ? updateUser() : getUser()">
          {{ userFound ? 'Salvar' : 'Procurar' }}
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

            nameInput: '',
            passwordInput: '',
            emailInput: '',
            roleInput: ''
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
        },

        async updateUser() {
            if (this.userFound.id == this.user.id) this.roleInput = this.user.role;
            const response = await UserService.update(this.userFound.id,
                                                      this.nameInput,
                                                      this.emailInput,
                                                      this.roleInput,
                                                      this.passwordInput
                                                      );
            switch (response) {
                case 200:
                    alert('Usuário atualizado!');
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
    },
};
</script>