<template>
  <div class="compontent-box h-banner-img product-border">
    <div v-if="imgInfo && imgInfo.link">
      <a :href="imgInfo.link" target="_blank">
        <img :src="imgInfo.url" alt="">
      </a>
    </div>
    <div v-else>
      <img :src="imgInfo? imgInfo.url : imgUrl" alt="">
    </div>
  </div>
</template>

<script>
export default {
  name: 'HBannerImg',
  props: {
    valueData: {
      required: false,
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      // link: null, // 链接
      imgInfo: null, // 图片信息
      imgUrl: require('@/assets/create/1.jpg')
    }
  },
  watch: {
    valueData: {
      handler(newVal, oldVal) {
        if (this.valueData && this.valueData.length) {
          this.imgInfo = this.valueData[0]
        } else {
          this.imgInfo = null
        }
      },
      deep: true
    }
  },
  created() {
    if (this.valueData && this.valueData.length) {
      this.imgInfo = this.valueData[0]
    } else {
      this.imgInfo = null
    }
  }
}
</script>

<style scoped  lang="scss">
@import '../components-header.scss';

  .h-banner-img{
    width:100%;
    height: 100%;
    text-align: center;
    &>div {
      width:100%;
      height: 100%;
    }
    img {
      width:100%;
      height: 100%;
    }
    a {
      display: inline-block;
      width: 100%;
      height: 100%;
    }
  }
</style>
