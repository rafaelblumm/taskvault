<script setup>
import GridComponent from '@/components/GridComponent.vue'
import TaskService from '@/service/task'
import '@/assets/styles.css';
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const searchQuery = ref('')
const gridColumns = ['ID', 'Título', 'Status', 'Responsável', 'Data prevista']
const tasks = ref([])
const router = useRouter()

const gridData = computed(() => tasks.value.map(task => ({
    'ID': String(task.id),
    'Título': task.title,
    'Status': TaskService.format_status(task.status),
    'Responsável': task.assignee ?? '',
    'Data prevista': task.dueDate ? new Date(task.dueDate).toLocaleDateString('pt-BR') : ''
})))

const loadTasks = async () => {
    const response = await TaskService.list_my_tasks()
    tasks.value = Array.isArray(response) ? response : response?.data ?? []
}

function handleRowClick(entry) {
    router.push('/task/' + entry.ID)
}

function newTask() {
    router.push('/createTask')
}

onMounted(loadTasks)
</script>

<template>
    <h1>Listagem de tarefas</h1>
    <div>
        Buscar <input name="query" v-model="searchQuery">
        <button @click="newTask">Criar</button>
    </div>
    <GridComponent :data="gridData" :columns="gridColumns" :filter-key="searchQuery" @row-click="handleRowClick">
    </GridComponent>
</template>

<style scoped>
.grid-wrapper {
    border: 1px solid #2e8b57;
    border-radius: 4px;
    overflow: hidden;
    width: 100%;
    max-width: 800px;
}

:deep(table) {
    width: 100%;
    border-collapse: collapse;
    background-color: #eceff1;
}

:deep(th) {
    background-color: #3cb371;
    color: #ffffff;
    font-weight: normal;
    padding: 10px 14px;
    text-align: left;
    border-bottom: 1px solid #2e8b57;
    border-right: 1px solid #2e8b57;
    white-space: nowrap;
}

:deep(th:last-child) {
    border-right: none;
}

:deep(td) {
    padding: 12px 14px;
    color: #cccccc;
    border-bottom: 1px solid #333333;
    border-right: 1px solid #333333;
}

:deep(td:last-child) {
    border-right: none;
}

:deep(tr:last-child td) {
    border-bottom: none;
}

:deep(tr:hover td) {
    background-color: #252525;
    cursor: pointer;
}
</style>
