<template>
  <div class="compontent-box h-carousel-img product-border">
    <div
      v-if="title"
      class="h-carousel-title product-border-bottom"
      :style="headerStyle"
    >
      <i class="el-icon-s-grid" />
      {{ title }}
    </div>
    <swiper
      :options="swiperOption"
      style="height:100%"
      class="resize-element"
    >
      <swiper-slide
        v-for="item in imgList"
        :key="item.link"
      >
        <img
          :src="item.url"
          alt
          style="height:100%; width:100%"
        >
      </swiper-slide>
      <div
        slot="pagination"
        class="swiper-pagination"
      />
      <div
        slot="button-prev"
        class="swiper-button-prev"
      />
      <div
        slot="button-next"
        class="swiper-button-next"
      />
    </swiper>
  </div>
</template>

<script>
export default {
  name: 'HCarouselMoreImg',
  props: {
    valueData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      title: null,
      headerStyle: null,
      swiperOption: {
        slidesPerView: 3,
        spaceBetween: 30,
        loop: true,
        pagination: {
          el: '.swiper-pagination',
          clickable: true
        },
        navigation: {
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev'
        }
      },
      timer: null,
      imgList: [],
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
        },
        {
          link: '4',
          url: require('@/assets/create/3.jpg')
        },
        {
          link: '5',
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
      },
      deep: true
    }
  },
  created() {
    if (this.valueData && this.valueData.length > 0) {
      this.imgList = this.valueData[0].list
      this.title = this.this.valueData[0].formData.title
      this.headerStyle = this.valueData[0].style
    } else {
      this.title = null
      this.imgList = this.defaultList
      this.headerStyle = null
    }
  },
  mounted() {
    if (this.$route.path === '/productLibrary/create') {
      this.timer = setInterval(() => {
        this.swiperOption = {}
        this.swiperOption = {
          slidesPerView: 3,
          spaceBetween: 30,
          loop: true,
          pagination: {
            el: '.swiper-pagination',
            clickable: true
          },
          navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev'
          }
        }
      }, 300)
    }
  },
  destroyed() {
    this.timer = null
  }
}
</script>
<style  scoped  lang="scss">
@import "../components-header.scss";
.h-carousel-img {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  // padding: 2px;
  .resize-element {
    width: 100%;
    height: 100%;
    background-color: #99a9bf;
  }
}
</style>
<style>
.swiper-button-prev,
.swiper-button-next {
  top: 50%;
  width: 28px;
  height: 25px;
}
</style>

