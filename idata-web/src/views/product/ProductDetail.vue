<template>
  <div class="product-detail">
    <div class="product-detail-content" :style="{ height: parentHeight + 'px' }">
      <preview-compontent v-if="componentsList.length" :components-list="componentsList" />
    </div>
  </div>
</template>

<script>
import { getProducts } from '@/api/productLibrary.js'
import PreviewCompontent from './components/PreviewCompontent.vue'
export default {
  name: 'ProductDetail',
  components: {
    PreviewCompontent
  },
  data() {
    return {
      shopId: null,
      parentHeight: 200,
      componentsList: []
    }
  },
  created() {
    getProducts({ id: this.$route.params.id || null }).then(res => {
      if (res.data && res.data.product) {
        this.componentsList = JSON.parse(res.data.product)
        const arr = []
        this.componentsList.forEach(item => {
          arr.push(item.h + item.y)
        })
        arr.sort((a, b) => {
          return b - a
        })
        this.parentHeight = arr[0] ? arr[0] + 50 : 400
      }
    })
  }
}
</script>
<style>
#app {
  min-width: 320px;
}
</style>

<style scoped  lang="scss">
  .product-detail{
    width: 100%;
    min-height: 100vh;
    position: relative;
    .product-content {
      // width: 1200px;
      margin: 0 auto;
      position: relative;
      background-color: #fff;
    }
  }
</style>
