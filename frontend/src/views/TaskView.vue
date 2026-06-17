<script setup>
import { ref, onMounted } from 'vue'
import TaskComponent from '@/components/TaskComponent.vue'
import TaskService from '@/service/task'

const props = defineProps({
    id: String
})

const task = ref(null)
const loading = ref(true)
const error = ref(null)

onMounted(async () => {
    try {
        loading.value = true
        task.value = await TaskService.get_task(props.id)
    } catch (err) {
        error.value = 'Erro ao carregar tarefa'
    } finally {
        loading.value = false
    }
})
</script>

<template>
    <div>
        <h1>Tarefa</h1>
        <div v-if="loading" class="loading">Carregando...</div>
        <div v-else-if="error" class="error">{{ error }}</div>
        <TaskComponent v-else :task="task" />
    </div>
</template>

<style scoped>
.loading,
.error {
    text-align: center;
    padding: 40px 20px;
    font-size: 18px;
}

.error {
    color: #c00;
}
</style>
