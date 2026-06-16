import { SERVER_ERROR } from "@/common/errors";
import apiClient from "./api";
import { useAuthStore } from "@/store/auth";

const authStore = useAuthStore();

/** Serviço de manipulação de tarefas */
const TaskService = {
    /**
     * Lista tarefas
     * @returns {Array}
     */
    async list_my_tasks() {
        const assignedTo = authStore.currentUser.id
        const response = await apiClient.get(`/task?assignedTo=${encodeURIComponent(assignedTo)}`)
        switch (response.status) {
            case 200:
                return response.data;
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
