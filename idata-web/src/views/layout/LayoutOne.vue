<template>
  <div class="app-wrapper">
    <app-header />
    <app-nav />
    <app-main />
  </div>
</template>

<script>
import AppHeader from './components/AppHeader.vue'
import AppNav from './components/AppNav.vue'
import AppMain from './components/AppMain.vue'

export default {
  name: 'LayoutOne',
  components: { AppHeader, AppMain, AppNav }
}
</script>

<style lang="scss" scoped>
  @import "~@/styles/mixin.scss";
  @import "~@/styles/variables.scss";
  .app-wrapper {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
</style>
