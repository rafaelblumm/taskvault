<script setup>
import GridComponent from '@/components/GridComponent.vue'
import TaskService from '@/service/task'
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
