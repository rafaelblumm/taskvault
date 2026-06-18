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
        return '/task'
    },

    /**
     * Manutenção de tarefa
     * @param {String} id ID da tarefa
     * @returns {String}
     */
    task(id) {
        return `/task/${id}`
    },

    /**
     * Criação de tarefa
     * @returns {String}
     */
    newTask() {
        return this.task('new')
    }
}

export default Route
