<template>
  <div class="application-detail">
    <div
      class="application-detail-html"
      v-html="htmlContent"
    />
  </div>
</template>

<script>
import { getFindexample } from '@/api/industryApplication.js'
export default {
  name: 'ApplicationDetail',
  data() {
    return {
      htmlContent: ''
    }
  },
  mounted() {
    if (this.$route.params.id) {
      getFindexample(this.$route.params.id).then(res => {
        this.htmlContent = res.data.example || '暂无内容'
      })
    }
  }
}
</script>

<style scoped  lang="scss">
.application-detail {
  width: 100%;
  min-height: 80vh;
  padding-top: 20px;
  .application-detail-html {
    width: 1300px;
    margin: 0 auto;
    height: 100%;
  }
}
</style>
