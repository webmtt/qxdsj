<template>
  <div class="server-products-list">
    <div class="hot-content">
      <div class="hot-content-header">
        <span>产品清单</span>
        <!-- <span>SERVICE PRODUCTS LIST DISPLAY</span>
        <span>全区定义服务产品种类超过3000类，其中区局定义124类，盟市局定义700类，旗县定义2800类</span> -->
      </div>
      <div class="hot-contnet-box">
        <div
          v-for="item in dataList"
          :key="item.id"
          class="service-list"
        >
          <img :src="item.imgUrl" alt="">
          <label>{{ item.title }}</label>
          <div>
            <span v-for="(itemInner, index) in item.list" :key="index" :title="itemInner">{{ itemInner }}</span>
          </div>
        </div>
        <div class="service-list">
          <div style="display: flex;justify-content: center;">
            <el-button
              size="mini"
              type="primary"
              @click="weatherAWSEvent"
            >更多清单列表</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getDisplay } from '@/api/productLibrary.js'
export default {
  name: 'ServerProductsList',
  data() {
    return {
      dataList: []
    }
  },
  created() {
    this.getNewDisplay()
  },
  mounted() { },
  methods: {
    getNewDisplay() {
      getDisplay().then(res => {
        if (res.data && JSON.stringify(res.data) !== '{}') {
          Object.keys(res.data).forEach((key, index) => {
            this.dataList.push({
              id: index,
              title: key,
              imgUrl: require(`@/assets/images/newproductitem${index + 1}.png`),
              list: res.data[key]
            })
          })
        }
      })
    },
    weatherAWSEvent() {
      const w = window.open('')
      w.location.href = 'http://172.18.112.199/nmidata/w/product/tokensIndex?showType=0'
    }
    // basicWFEvent() {
    //   const t = window.open('')
    //   t.location.href = 'http://172.18.112.199/nmidata/w/product/tokensIndex?showType=0&typeCode=WF'
    // },
    // publicCLCAPEvent() {
    //   const s = window.open('')
    //   s.location.href = 'http://172.18.112.199/nmidata/w/product/tokensIndex?showType=0&typeCode=CLCAP'
    // }
  }
}
</script>

<style scoped  lang="scss">
.server-products-list {
  width: 100%;
  // padding-bottom: 40px;
  background-color: #ffffff;
  .hot-content {
    width: 1300px;
    margin: 0 auto;
    .hot-content-header {
      width: 100%;
      height: 100px;
      display: flex;
      justify-content: center;
      align-items: center;
      & > span:first-child {
        color: #333;
        font-size: 24px;
        // margin-top: 67px;
        font-weight: bolder;
        // padding-bottom: 15px;
        position: relative;
        &::after {
          content: "";
          position: absolute;
          left: -22px;
          top: 2px;
          width: 4px;
          height: 24px;
          background-color: #24d2f0;
        }
      }
    }
    .hot-contnet-box {
      width: 100%;
      // display: flex;
      // margin-top: 50px;
      // justify-content: space-between;
      .service-list {
        flex: 1;
        height: 60px;
        display: flex;
        justify-self: start;
        align-items: center;
        margin: 20px 0;
        & > img {
          width: 32px;
          height: 32px;
          margin-right: 20px;
        }
        & > label {
          width: 130px;
          margin-right: 20px;
        }
        & > div {
          height: 60px;
          flex: 1;
          overflow: hidden;
          text-overflow: ellipsis;
          & > span {
            display: inline-block;
            height: 60px;
            line-height: 60px;
            margin-right: 15px;
            overflow: hidden;
          }
        }
        & > div > button {
          height: 40px;
          background: transparent;
          border: none;
          border-bottom: 2px solid #ccc;
          border-radius: initial;
          font-size: 18px;
          color: #ccc;
          &:hover {
            color: #24d2f0;
            border-bottom: 2px solid #24d2f0;
          }
        }
      }
      // .service-list {
      //   width: 390px;
      //   height: 200px;
      //   border: 1px solid #dadde6;
      //   border-radius: 4px;
      //   padding: 20px;
      //   display: flex;
      //   align-items: flex-start;
      //   img {
      //     width: 32px;
      //     height: 32px;
      //   }
      //   & > div {
      //     flex: 1;
      //   }
      //   .hot-inner {
      //     padding: 0 20px;
      //     .hot-title {
      //       font-size: 14px;
      //       color: #202020;
      //       font-weight: bold;
      //       padding: 15px 0;
      //     }
      //     .hot-item-box {
      //       display: flex;
      //       display: -webkit-flex;
      //       justify-content: flex-start;
      //       flex-direction: row;
      //       flex-wrap: wrap;
      //       height: 90px;
      //       overflow: hidden;
      //       font-size: 14px;
      //       span {
      //         width: 33%;
      //         overflow: hidden;
      //         text-overflow: ellipsis;
      //         white-space: nowrap;
      //         margin: 6px 0;
      //       }
      //     }
      //     .service-btn {
      //       text-align: right;
      //     }
      //   }
      // }
    }
  }
}
</style>

