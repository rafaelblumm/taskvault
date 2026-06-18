/** Auxiliar de rotas da aplicação */
const Route = {
    /**
     * Autenticação de usuário
     * @returns {String}
     */
    login() {
        return '/login'
    },

    /**
     * Listagem de tarefas
     * @returns {String}
     */
    taskList() {
        return '/tasks'
    },

    /**
     * Manutenção de tarefa
     * @param {String} id ID da tarefa
     * @returns {String}
     */
    task(id) {
        return `/task/${id}`
    }
}

export default Route
