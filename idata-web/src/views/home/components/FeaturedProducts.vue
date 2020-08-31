<template>
  <div class="featured-products">
    <div class="featured-products-content">
      <div class="hot-content-header">
        <span>特色产品</span>
        <span>生态农业灾害性气象监测与服务，建设北疆靓丽风景线</span>
      </div>
      <div class="list-content">
        <div v-for="item in dataList" :key="item.id" class="list-content-item" @click="handleLink(item)">
          <img :src="item.iconUrl">
          <span>{{ item.title }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getFeaturesProduct } from '@/api/home.js'

export default {
  name: 'FeaturedProducts',
  data() {
    return {
      dataList: [
        {
          id: 1,
          title: '人工影响天气',
          iconUrl: require('@/assets/images/homeFeature1.png'),
          link: ''
        },
        {
          id: 2,
          title: '全国传输失效考核',
          iconUrl: require('@/assets/images/homeFeature2.png'),
          link: ''
        },
        {
          id: 3,
          title: '卫星服务',
          iconUrl: require('@/assets/images/homeFeature3.png'),
          link: ''
        },
        {
          id: 4,
          title: '产品解决方案',
          iconUrl: require('@/assets/images/homeFeature4.png'),
          link: ''
        }
      ]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      getFeaturesProduct().then(res => {
        if (res.data && res.data.length) {
          const list = res.data
          this.dataList.forEach((item, index) => {
            item.title = list[index] ? list[index].product_name : '-'
            item.link = list[index] ? list[index].id : '-'
          })
        }
      })
    },
    handleLink(item) {
      this.$router.push({ path: `/productLibrary` })
      // this.$router.push({ path: `/specialProduct/detail/${item.link}` })
    }
  }
}
</script>

<style scoped  lang="scss">
.featured-products {
  width: 100%;
  height: 720px;
  background-color: #ffffff;
  .featured-products-content {
    width: 1300px;
    height: 100%;
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
      & > span:last-child {
        font-size: 16px;
        color: #666;
      }
    }
    .list-content {
      width: 100%;
      height: 495px;
      display: flex;
      justify-content: space-around;
      // background-image: url('../../../assets/images/homeFeatured.png');
      background-origin: content-box;
      background-position: center;
      background-repeat: no-repeat;
      background-clip: content-box;
      background-size: cover;
      &>div:nth-child(1) {
          background-image: url('../../../assets/images/homeFeaturedbg1.png');
      }
      &>div:nth-child(2) {
          background-image: url('../../../assets/images/homeFeaturedbg2.png');
      }
      &>div:nth-child(3) {
          background-image: url('../../../assets/images/homeFeaturedbg3.png');
      }
      &>div:nth-child(4) {
          background-image: url('../../../assets/images/homeFeaturedbg4.png');
      }
      .list-content-item {
        width: 325px;
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        background-origin: content-box;
        background-position: center;
        background-repeat: no-repeat;
        background-clip: content-box;
        background-size: cover;
        cursor: pointer;
        img {
          width: 52px;
          height: 52px;
        }
        span {
          color: #ffffff;
          font-size: 20px;
          margin-top: 20px;
        }
      }
    }
  }
}
</style>
