<script setup>
import GridComponent from '@/components/GridComponent.vue'
import Modal from '@/components/ModalComponent.vue'
import TaskService from '@/service/task'
import '@/assets/styles.css';
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const showAdvancedFilters = ref(false)
const filters = ref({
    titleContains: '',
    status: '',
    creator: '',
    assignee: '',
    createdBefore: '',
    createdAfter: '',
    dueDateBefore: '',
    dueDateAfter: ''
})

const statusOptions = ref([
    { text: TaskService.format_status('PENDING'), value: 'PENDING' },
    { text: TaskService.format_status('IN_PROGRESS'), value: 'IN_PROGRESS' },
    { text: TaskService.format_status('DONE'), value: 'DONE' },
    { text: 'Nenhum', value: '' }
])

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

async function cancelFilters() {
    showAdvancedFilters.value = false
    loadTasks()
}

async function submitFilters() {
    showAdvancedFilters.value = false
    console.log('Submitted filters:', filters.value)
    const response = await TaskService.list_tasks(filters.value)
    tasks.value = Array.isArray(response) ? response : response?.data ?? []
}

function clearFilters() {
    filters.value = {
        titleContains: '',
        status: '',
        creator: '',
        assignee: '',
        createdBefore: '',
        createdAfter: '',
        dueDateBefore: '',
        dueDateAfter: ''
    }
}

onMounted(loadTasks)
</script>

<template>
    <h1>Listagem de tarefas</h1>
    <div>
        Buscar <input name="query" v-model="searchQuery">
        <button @click="newTask">Criar</button>
        <button id="show-modal" @click="showAdvancedFilters = true">Filtros avançados</button>
    </div>
    <GridComponent
        :data="gridData"
        :columns="gridColumns"
        :filter-key="searchQuery"
        @row-click="handleRowClick"
    />
    <Modal :show="showAdvancedFilters" @cancel="cancelFilters()" @submit="submitFilters()">
        <template #header>
            <h3>Filtros avançados</h3>
        </template>

        <template #body>
            <form @submit.prevent="submitFilters">
                <div>
                    <label for="titleContains">Título contém</label>
                    <input id="titleContains" type="text" v-model="filters.titleContains">
                </div>
                <div>
                    <label for="status">Status</label>
                    <select v-model="filters.status">
                        <option v-for="option in statusOptions" :value="option.value" v-bind:key="option.value">
                            {{ option.text }}
                        </option>
                    </select>
                </div>
                <div>
                    <label for="creator">Criador</label>
                    <input id="creator" type="text" v-model="filters.creator">
                </div>
                <div>
                    <label for="assignee">Responsável</label>
                    <input id="assignee" type="text" v-model="filters.assignee">
                </div>
                <div>
                    <label for="createdBefore">Criado antes de</label>
                    <input id="createdBefore" type="date" v-model="filters.createdBefore">
                </div>
                <div>
                    <label for="createdAfter">Criado depois de</label>
                    <input id="createdAfter" type="date" v-model="filters.createdAfter">
                </div>
                <div>
                    <label for="dueDateBefore">Data prevista antes de</label>
                    <input id="dueDateBefore" type="date" v-model="filters.dueDateBefore">
                </div>
                <div>
                    <label for="dueDateAfter">Data prevista depois de</label>
                    <input id="dueDateAfter" type="date" v-model="filters.dueDateAfter">
                </div>
            </form>
        </template>

        <template #footer>
            <button class="modal-default-button" @click="submitFilters()">OK</button>
            <button class="modal-default-button" @click="showAdvancedFilters = false">Cancelar</button>
            <button class="modal-default-button" @click="clearFilters()">Limpar</button>
        </template>
    </Modal>
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
