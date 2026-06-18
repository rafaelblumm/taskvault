<script setup>
import { useRouter } from 'vue-router'
import TaskComponent from '@/components/TaskComponent.vue'
import Route from '@/common/route'
import { useAuthStore } from '@/store/auth'

const router = useRouter()
const authStore = useAuthStore()

const canCreate = authStore.canCreateTasks()

function handleCreated(task) {
    router.push(Route.task(task.id))
}

function handleCancel() {
    router.push(Route.taskList())
}
</script>

<template>
    <div>
        <h1>Nova tarefa</h1>
        <div v-if="!canCreate" class="error">Usuário sem permissão para criar tarefas.</div>
        <TaskComponent v-else :create="true" @task-created="handleCreated" @create-cancelled="handleCancel"/>
    </div>
</template>
