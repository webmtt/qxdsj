<template>
  <div class="hot-product">
    <div class="hot-content">
      <div class="hot-content-header">
        <span>热门产品</span>
        <span>实况预报和预警，天气信息早知道</span>
      </div>
      <div class="hot-contnet-list">
        <div v-for="item in hotDataList" :key="item.id" class="hot-content-list-item">
          <img :src="item.imgUrl">
          <div class="hot-content-list-item-title"> {{ item.title }} </div>
          <div class="hot-content-list-item-link" @click="handleLink(item)">查看详情
            <span /> </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>

import { getNewInterface } from '@/api/home.js'

export default {
  name: 'HotProduct',
  data() {
    return {
      hotDataList: [
        {
          id: 1,
          title: '当日气温',
          imgUrl: require('@/assets/images/homeHotTemperature.png'),
          link: ''
        },
        {
          id: 2,
          title: '风速年际变化',
          imgUrl: require('@/assets/images/homeHotwind.png'),
          link: ''
        },
        {
          id: 3,
          title: '火候诊断',
          imgUrl: require('@/assets/images/homeHotWeather.png'),
          link: ''
        },
        {
          id: 4,
          title: '干旱预报',
          imgUrl: require('@/assets/images/homeHotDrought.png'),
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
      getNewInterface().then(res => {
        if (res.data && res.data.length) {
          const list = res.data
          this.hotDataList.forEach((item, index) => {
            item.title = list[index].name
            item.id = list[index].id
            item.parentId = list[index].parentId
          })
        }
      })
    },
    handleLink(d) {
      this.$router.push({ path: `/productLibrary` })
    }
  }
}
</script>

<style scoped  lang="scss">
.hot-product {
  width: 100%;
  height: 610px;
  background-image: url('../../../assets/images/homeHotProduct.png');
  background-origin: content-box;
  background-position: center;
  background-repeat: no-repeat;
  background-clip: content-box;
  background-size: center center;
  .hot-content {
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
      & > span:last-child {
        font-size: 16px;
        color: #666;
      }
    }
    .hot-contnet-list {
      width: 100%;
      height: auto;
      display: flex;
      justify-content: space-between;
    }
    .hot-content-list-item {
      width: 310px;
      height: 400px;
      border-radius: 6px;
      box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.4);
      position: relative;
      img {
        width: 100%;
        height: 236px;
      }
      .hot-content-list-item-title {
        width: 100%;
        margin-top: 24px;
        font-size: 18px;
        text-align: center;
        font-weight: bolder;
        color: #333;
      }
      .hot-content-list-item-link {
        margin: 30px auto 0;
        width: 120px;
        height: 36px;
        line-height: 36px;
        text-align: center;
        border-radius: 20px;
        border: 1px solid #dadde6;
        color: #999;
        background-color: #fff;
        font-size: 14px;
        cursor: pointer;
        & > span {
          display: inline-block;
          width: 14px;
          height: 14px;
          background-image: url('../../../assets/images/hoverLeft.png');
          background-position: center;
          background-repeat: no-repeat;
          background-clip: content-box;
          background-size: center center;
        }
      }
      &:hover {
        box-shadow: 0px 0px 10px 0px #24d2f0;
        .hot-content-list-item-title {
          font-size: 20px;
        }
        .hot-content-list-item-link {
          border-color: transparent;
          box-shadow: 0px 0px 4px 0px #24d2f0;
          background-color: #28cce9;
          color: #fff;
          & > span {
            background-image: url('../../../assets/images/hoveredLeft.png');
          }
        }
      }
      &:hover::after {
        position: absolute;
        content: '';
        top: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 60px;
        height: 3px;
        background-color: #24d2f0;
      }
    }
  }
}
</style>
