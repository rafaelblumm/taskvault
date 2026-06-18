import { SERVER_ERROR } from "@/common/errors";
import apiClient from "./api";
import { useAuthStore } from "@/store/auth";

const authStore = useAuthStore();

/** Serviço de manipulação de tarefas */
const TaskService = {
    /**
     * Lista tarefas com filtros
     * @param {*} filters Filtros da tarefas
     * @returns {Array} Lista de tarefas
     */
    async list_tasks(filters) {
        const params = Object.fromEntries(
            Object.entries(filters || {}).filter(
                ([, value]) => value !== null && value !== undefined && value !== ''
            )
        );
        const response = await apiClient.get('/task', { params });
        switch (response.status) {
            case 200:
                return response.data;
            default:
                throw SERVER_ERROR;
        }
    },

    /**
     * Lista tarefas
     * @returns {Array}
     */
    async list_my_tasks() {
        return this.list_tasks({ assignee: authStore.currentUser.id })
    },

    /**
     * Obtém uma tarefa pelo ID
     * @param {Number} id ID da tarefa
     * @returns {Object} Dados da tarefa
     */
    async get_task(id) {
        const response = await apiClient.get(`/task/${id}`);
        switch (response.status) {
            case 200:
                return response.data;
            default:
                throw SERVER_ERROR;
        }
    },

    /**
     * Atualiza uma tarefa
     * @param {Object} taskData Dados da tarefa a atualizar
     * @returns {Object} Tarefa atualizada
     */
    async update_task(taskData) {
        const response = await apiClient.put(`/task/${taskData.id}`, taskData);
        switch (response.status) {
            case 200:
                return response.data;
            default:
                throw SERVER_ERROR;
        }
    },

    /**
     * Cria uma nova tarefa
     * @param {Object} taskData
     * @returns {Object} Tarefa criada
     */
    async create_task(taskData) {
        const response = await apiClient.post('/task', taskData);
        switch (response.status) {
            case 201:
                return response.data;
            default:
                throw SERVER_ERROR;
        }
    },

    /**
     * Remove uma tarefa pelo ID
     * @param {Number} taskId ID da tarefa
     */
    async delete_task(taskId) {
        const response = await apiClient.delete(`/task/${taskId}`);
        switch (response.status) {
            case 204:
                return;
            default:
                throw SERVER_ERROR;
        }
    },

    /**
     * Formata status de tarefa para exibição
     * @param {String} status
     * @returns {String} Status formatado
     */
    format_status(status) {
        switch (status) {
        case 'PENDING':
            return 'A fazer'
        case 'IN_PROGRESS':
            return 'Em andamento'
        case 'DONE':
            return 'Feito'
        default:
            return '?'
        }
    }
}

export default TaskService;
