<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'app',
  created() {
    document.title = '企鹅农场SARO交易管理平台'
  },
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
}
#app {
  background-color: #dadef1;
}
</style>
