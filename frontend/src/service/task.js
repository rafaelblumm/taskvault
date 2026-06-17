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
