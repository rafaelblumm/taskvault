<script setup>
import { ref, watch, computed } from 'vue'
import TaskService from '@/service/task'
import { statusOptions } from '@/common/task-status'
import { useAuthStore } from '@/store/auth'

const props = defineProps({
    task: Object
})

const emit = defineEmits(['task-updated', 'task-deleted'])

const editMode = ref(false)
const editedTask = ref(null)
const saving = ref(false)
const deleting = ref(false)
const saveError = ref(null)
const deleteError = ref(null)
const authStore = useAuthStore()

const canEditTask = computed(() => {
    return editedTask.value && (
        authStore.hasElevatedPermissions() ||
        (authStore.isUser() && editedTask.value.creator === authStore.currentUser.id)
    )
})

const canDeleteTask = computed(() => {
    return editedTask.value && authStore.hasElevatedPermissions()
})

watch(() => props.task, (newTask) => {
    if (newTask) {
        editedTask.value = { ...newTask }
    }
}, { immediate: true })

const formatDate = (dateString) => {
    return dateString ? new Date(dateString).toLocaleDateString('pt-BR') : '-'
}

const toggleEditMode = () => {
    if (!canEditTask.value) return

    if (editMode.value) {
        editedTask.value = { ...props.task }
    }
    editMode.value = !editMode.value
    saveError.value = null
}

const handleSave = async () => {
    try {
        saving.value = true
        saveError.value = null
        await TaskService.update_task(editedTask.value)

        editMode.value = false
        emit('task-updated', editedTask.value)
    } catch (err) {
        saveError.value = 'Erro ao salvar tarefa'
    } finally {
        saving.value = false
    }
}

const handleDelete = async () => {
    if (!canDeleteTask.value || !confirm('Confirma a remoção da tarefa?')) return

    try {
        deleting.value = true
        deleteError.value = null
        await TaskService.delete_task(editedTask.value.id)
        emit('task-deleted', editedTask.value.id)
    } catch (err) {
        deleteError.value = 'Erro ao deletar tarefa'
    } finally {
        deleting.value = false
    }
}
</script>

<template>
    <div class="task-container" v-if="editedTask">
        <div class="header">
            <div class="header-title">
                <span class="id">#{{ editedTask.id }}</span>
                <span v-if="!editMode" class="title">{{ editedTask.title }}</span>
                <input v-else v-model="editedTask.title" class="title-input" type="text" placeholder="Título da tarefa">
            </div>
        </div>

        <div class="main-content">
            <div class="description-section">
                <label>Descrição</label>
                <div v-if="!editMode" class="description-box">
                    {{ editedTask.description || '-' }}
                </div>
                <textarea v-else v-model="editedTask.description" class="description-input"
                    placeholder="Descrição da tarefa"></textarea>
            </div>

            <div class="info-section">
                <div class="info-field">
                    <label>Status</label>
                    <div v-if="!editMode" class="info-box">{{ TaskService.format_status(editedTask.status) }}</div>
                    <select v-else v-model="editedTask.status" class="info-input">
                        <option v-for="option in statusOptions" :key="option.value" :value="option.value">
                            {{ option.text }}
                        </option>
                    </select>
                </div>

                <div class="info-field">
                    <label>Usuário designado</label>
                    <div v-if="!editMode" class="info-box">{{ editedTask.assignee || '-' }}</div>
                    <input v-else v-model="editedTask.assignee" class="info-input" type="text" placeholder="Usuário">
                </div>

                <div class="info-field">
                    <label>Data prevista</label>
                    <div v-if="!editMode" class="info-box">{{ formatDate(editedTask.dueDate) }}</div>
                    <input v-else v-model="editedTask.dueDate" class="info-input" type="date">
                </div>

                <div class="info-field">
                    <label>Criador</label>
                    <div class="info-box">{{ editedTask.creator || '-' }}</div>
                </div>

                <div class="info-field">
                    <label>Data de criação</label>
                    <div class="info-box">{{ formatDate(editedTask.creationDatetime) }}</div>
                </div>
            </div>
        </div>

        <div v-if="saveError || deleteError" class="error-message">
            {{ saveError || deleteError }}
        </div>

        <div class="button-section">
            <div v-if="editMode" class="button-group">
                <button class="save-button" @click="handleSave" :disabled="saving">
                    {{ saving ? 'Salvando...' : 'Salvar' }}
                </button>
                <button class="cancel-button" @click="toggleEditMode" :disabled="saving">Cancelar</button>
            </div>
            <div v-else class="button-group">
                <button v-if="canDeleteTask" class="delete-button" @click="handleDelete" :disabled="deleting">
                    {{ deleting ? 'Deletando...' : 'Deletar' }}
                </button>
                <button class="edit-button" @click="toggleEditMode" :disabled="!canEditTask">
                    Alterar
                </button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.task-container {
    display: flex;
    flex-direction: column;
    gap: 20px;
    padding: 20px;
    font-family: Avenir, Helvetica, Arial, sans-serif;
}

.header {
    border: 1px solid #333;
    padding: 10px;
    margin-bottom: 5px;
}

.header-title {
    display: flex;
    gap: 10px;
    font-size: 18px;
    font-weight: bold;
    text-align: left;
}

.header-title .id {
    min-width: 30px;
}

.header-title .title {
    flex: 1;
}

.title-input {
    flex: 1;
    font-size: 16px;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 3px;
    font-weight: bold;
}

.main-content {
    display: grid;
    grid-template-columns: 1.5fr 1fr;
    gap: 15px;
    margin-bottom: 20px;
}

.description-section {
    display: flex;
    flex-direction: column;
}

.description-section label {
    font-weight: bold;
    margin-bottom: 8px;
    font-size: 14px;
}

.description-box {
    border: 1px solid #333;
    padding: 15px;
    min-height: 200px;
    background-color: #fafafa;
    word-wrap: break-word;
    white-space: pre-wrap;
}

.description-input {
    border: 1px solid #333;
    padding: 12px;
    font-family: inherit;
    font-size: 14px;
    min-height: 200px;
    resize: vertical;
    border-radius: 3px;
}

.info-section {
    display: flex;
    flex-direction: column;
    gap: 5px;
}

.info-field {
    display: flex;
    flex-direction: column;
}

.info-field label {
    font-weight: bold;
    margin-bottom: 6px;
    font-size: 15px;
}

.info-box {
    border: 1px solid #333;
    padding: 5px;
    min-height: 30px;
    background-color: #fafafa;
    display: flex;
    align-items: center;
}

.info-input {
    border: 1px solid #333;
    padding: 8px;
    font-size: 14px;
    border-radius: 3px;
    background-color: #fff;
}

.info-input[type="text"],
.info-input[type="date"] {
    font-family: inherit;
}

.error-message {
    padding: 12px;
    background-color: #ffe6e6;
    border: 1px solid #cc0000;
    border-radius: 3px;
    color: #cc0000;
    font-weight: bold;
    margin-bottom: 10px;
}

.button-section {
    display: flex;
    justify-content: flex-end;
    margin-top: 5px;
}

.button-group {
    display: flex;
    gap: 10px;
}

.edit-button,
.save-button,
.cancel-button,
.delete-button {
    padding: 15px 15px;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    border-radius: 3px;
    border: none;
    transition: background-color 0.2s;
}

.edit-button {
    background-color: #42b983;
    color: white;
}

.edit-button:hover {
    background-color: #369970;
}

.save-button {
    background-color: #42b983;
    color: white;
}

.save-button:hover:not(:disabled) {
    background-color: #369970;
}

.save-button:disabled {
    background-color: #ccc;
    cursor: not-allowed;
}

.cancel-button {
    background-color: #999;
    color: white;
}

.cancel-button:hover:not(:disabled) {
    background-color: #777;
}

.cancel-button:disabled {
    background-color: #ccc;
    cursor: not-allowed;
}

.delete-button {
    background-color: #d9534f;
    color: white;
}

.delete-button:hover:not(:disabled) {
    background-color: #c9302c;
}

.delete-button:disabled {
    background-color: #e3a6a3;
    cursor: not-allowed;
}
</style>
