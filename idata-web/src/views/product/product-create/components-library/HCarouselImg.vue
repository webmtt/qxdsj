<template>
  <div class="compontent-box h-carousel-img product-border">
    <div v-if="title" class="h-carousel-title product-border-bottom" :style="headerStyle">
      <i class="el-icon-s-grid" />
      {{ title }}
    </div>
    <el-carousel class="h-carousel-img" :interval="5000" arrow="always" :height="height">
      <el-carousel-item v-for="item in imgList" :key="item.link">
        <img class="carousel-img" :src="item.url">
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
export default {
  name: 'HCarouselImg',
  props: {
    valueData: {
      required: false,
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      height: 'auto',
      imgList: [],
      title: null,
      headerStyle: null,
      defaultList: [
        {
          link: '1',
          url: require('@/assets/create/2.jpg')
        },
        {
          link: '2',
          url: require('@/assets/create/3.jpg')
        },
        {
          link: '3',
          url: require('@/assets/create/4.jpg')
        }
      ]
    }
  },
  watch: {
    valueData: {
      handler(newVal, oldVal) {
        if (this.valueData && this.valueData.length) {
          this.imgList = this.valueData[0].list
          this.title = this.valueData[0].formData.title
          this.headerStyle = this.valueData[0].style
        } else {
          this.title = null
          this.headerStyle = null
          this.imgList = this.defaultList
        }
        this.height = this.title ? newVal.h + 40 + 'px' : newVal.h + 'px'
      },
      deep: true
    }
  },
  created() {
    if (this.valueData && this.valueData.length > 0) {
      this.imgList = this.valueData[0].list
      this.title = this.valueData[0].formData.title
      this.headerStyle = this.valueData[0].style
    } else {
      this.title = null
      this.imgList = this.defaultList
      this.headerStyle = null
    }
  }
}
</script>
<style>
.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 100%;
  margin: 0;
}
.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}
.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>
<style  scoped  lang='scss'>
@import "../components-header.scss";
.h-carousel-img {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  .h-carousel-img {
    min-height: 150px;
  }
  .carousel-img {
    width: 100%;
    height: 100%;
    overflow: hidden;
  }
}
</style>
<style>
.h-carousel-img .el-carousel--horizontal {
  height: 100%;
}
.h-carousel-img .el-carousel__container {
  min-height: 200px;
  height: 100%;
}
.h-carousel-img .el-carousel__container {
  /* height: 100% !important; */
  flex: 1;
}
</style>

