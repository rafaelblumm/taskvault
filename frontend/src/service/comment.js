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
}

export default CommentService;
