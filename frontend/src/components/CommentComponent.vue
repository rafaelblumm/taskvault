<script setup>
import { ref, onMounted } from 'vue'
import CommentService from '@/service/comment'
import { formatDateTime } from '@/common/date-fmt'

const props = defineProps({
    taskId: [String, Number],
    comments: {
        type: Array,
        required: false
    }
})

const localComments = ref(props.comments || [])
const loading = ref(false)
const error = ref(null)
const newMessage = ref('')
const submitting = ref(false)

onMounted(async () => {
    if (!props.comments) {
        try {
            loading.value = true
            localComments.value = await CommentService.get_comments(props.taskId)
        } catch (err) {
            error.value = 'Erro ao carregar comentários'
        } finally {
            loading.value = false
        }
    }
})

const submitComment = async () => {
    if (!newMessage.value || newMessage.value.trim() === '') {
        error.value = 'Mensagem vazia'
        return
    }
    if (newMessage.value.length > 2000) {
        error.value = 'Mensagem muito longa'
        return
    }

    try {
        submitting.value = true
        error.value = null
        const created = await CommentService.create_comment(props.taskId, newMessage.value.trim())
        localComments.value = [...(localComments.value || []), created]
        newMessage.value = ''
    } catch (err) {
        error.value = 'Erro ao enviar comentário'
    } finally {
        submitting.value = false
    }
}
</script>

<template>
    <div class="comment-input-row">
        <input
            v-model="newMessage"
            class="comment-input"
            type="text"
            placeholder="Novo comentário"
            :disabled="submitting"
            @keyup.enter="submitComment"
        />
        <button class="comment-button" @click="submitComment" :disabled="submitting || !newMessage.trim()">
            {{ submitting ? 'Enviando...' : 'Comentar' }}
        </button>
    </div>
    <div class="comment-list">
        <div v-if="loading" class="comment-loading">Carregando comentários...</div>
        <div v-else-if="error" class="comment-error">{{ error }}</div>
        <div v-else>
            <div v-if="localComments.length === 0" class="comment-empty">Sem comentários</div>
            <div v-else>
                <div v-for="c in localComments" :key="c.id" class="comment">
                    <div class="comment-header">
                        <div class="comment-creator">@{{ c.creator }}</div>
                        <div class="comment-date">{{ formatDateTime(c.creationDatetime || c.creation) }}</div>
                    </div>
                    <div class="comment-message">{{ c.message }}</div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.comment-list {
    margin: 20px auto 0;
    display: flex;
    flex-direction: column;
    align-items: right;
    width: 100%;
}

.comment {
    border: 1px solid #2f3136;
    padding: 12px;
    border-radius: 6px;
    margin-bottom: 12px;
    min-width: 400px;
    width: calc(100% - 140px);
}

.comment-creator {
    font-weight: 600;
    color: #0064a2;
    margin-bottom: 0;
}

.comment-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
}

.comment-date {
    font-style: italic;
    color: inherit;
    font-size: 0.9em;
}

.comment-message {
    white-space: pre-wrap;
    color: inherit;
    text-align: left;
}

.comment-empty,
.comment-loading,
.comment-error {
    text-align: center;
    color: #9aa3a6;
    padding: 12px 0;
}

.comment-input-row {
    display: flex;
    flex-direction: row;
    gap: 15px;
    align-items: center;
    justify-content: center;
    width: 100%;
    margin-bottom: 12px;
}

.comment-input {
    flex: 1;
    min-width: 400px;
    padding: 10px 12px;
    border: 1px solid #333;
    border-radius: 4px;
    font-size: 14px;
    box-sizing: border-box;
    height: 42px;
}

.comment-button {
    padding: 0 16px;
    font-size: 14px;
    font-weight: bold;
    border-radius: 3px;
    border: none;
    color: white;
    background-color: #1e90ff;
    cursor: pointer;
    width: 100px;
    height: 42px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
}

.comment-button:hover:not(:disabled) {
    background-color: #187bcd;
}

.comment-button:disabled {
    background-color: #ccc;
    cursor: not-allowed;
}
</style>
