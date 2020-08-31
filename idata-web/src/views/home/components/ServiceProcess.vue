<template>
  <div class="service-process">
    <div class="hot-content-header">
      <span>服务流程</span>
      <span>数据获取，服务应用，为您导航</span>
    </div>
    <img :src="serviceProcessUrl">
  </div>
</template>

<script>
export default {
  name: 'ServiceProcess',
  data() {
    return {
      serviceProcessUrl: require('@/assets/images/serveflow.png')
    }
  }
}
</script>

<style scoped  lang="scss">
.service-process {
  width: 1300px;
  margin: 0 auto;
  .hot-content-header {
      width: 100%;
      height: 140px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      & > span:first-child {
        color: #333;
        font-size: 24px;
        font-weight: bolder;
        padding-bottom: 15px;
        position: relative;
        &::after {
          content: '';
          position: absolute;
          left: -22px;
          top: 2px;
          width: 4px;
          height: 24px;
          background-color: #24d2f0;
        }
      }

  }
  img {
    width: 100%;
    height: auto;
  }
}
</style>
