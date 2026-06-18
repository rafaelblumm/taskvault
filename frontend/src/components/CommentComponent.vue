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
</script>

<template>
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
    align-items: center;
    width: 100%;
}

.comment {
    border: 1px solid #2f3136;
    padding: 12px;
    border-radius: 6px;
    margin-bottom: 12px;
    min-width: 400px;
    width: 60%;
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
</style>
