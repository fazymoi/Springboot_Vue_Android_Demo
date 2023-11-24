<template>
  <div id="app">
    <!--    <div>-->
    <!--      <router-link to='/index'>index</router-link>-->
    <!--    </div>-->
    <router-view/>
  </div>
</template>

<script>
import Login from './components/Login'
export default {
  name: 'App',
  components: {Login}
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
