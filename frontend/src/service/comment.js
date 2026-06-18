import { SERVER_ERROR } from "@/common/errors";
import apiClient from "./api";

/** Serviço de manipulação de comentários */
const CommentService = {
    /**
     * Obtém comentários de uma tarefa
     * @param {Number|String} taskId ID da tarefa
     * @returns {Array} Lista de comentários
     */
    async get_comments(taskId) {
        const response = await apiClient.get(`/task/${taskId}/comment`);
        switch (response.status) {
            case 200:
                return response.data;
            default:
                throw SERVER_ERROR;
        }
    }
,
    /**
     * Cria um comentário para a tarefa
     * @param {Number|String} taskId ID da tarefa
     * @param {String} message Mensagem do comentário
     * @returns {Object} Comentário criado
     */
    async create_comment(taskId, message) {
        const response = await apiClient.post(`/task/${taskId}/comment`, { message });
        switch (response.status) {
            case 201:
                return response.data;
            default:
                throw SERVER_ERROR;
        }
    }
}

export default CommentService;
