<template>
  <div id="app">
    <router-view />
    <!-- <div class="back-top" @click="backTop">回导顶部</div> -->
  </div>
</template>

<script>
export default {
  name: 'App',
  mounted() {
    // window.addEventListener('scroll', this.scrollToTop)
  },
  destroyed() {
    // window.removeEventListener('scroll', this.scrollToTop)
  }
}
</script>
<style>
#app {
  margin: 0;
  min-width: 1200px;
  height: 100%;
  overflow-y: scroll;
}
.back-top {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 2000;
  color: #fff;
}
</style>

