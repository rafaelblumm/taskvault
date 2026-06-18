<template>
    <div id="app">
        <Header v-if="showHeader" />
        <router-view></router-view>
    </div>
</template>

<script>
import Header from './components/HeaderComponent.vue'

export default {
    name: 'App',
    components: { Header },
    computed: {
        showHeader() {
            return this.$route && this.$route.name !== 'login'
        }
    }
}
</script>

<style>
#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    padding-top: 64px;
}
</style>
