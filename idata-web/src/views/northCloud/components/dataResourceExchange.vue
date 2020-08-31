<template>
  <div class="hot-product">
    <div class="hot-content">
      <div class="hot-content-header">
        <span>数据资源交换情况</span>
        <!-- <span>DATA RESOURCE EXCHANGE STATUS</span> -->
      </div>
      <div class="dataCatalogue">
        <div class="dataCatalogueClass">
          <div class="box-title">文件</div>
          <hr>
          <div class="backgroundClass">
            <div style="margin: 10px;">
              <img :src="filePushToday" alt="">
              <span class="dataNumber">{{ pushFileToday }}</span>
              <span>今日推送 (个)</span>
            </div>
            <div style="margin: 10px;">
              <img :src="filePushToday" alt="">
              <span class="dataNumber">{{ gainFileToday }}</span>
              <span>今日接收 (个)</span>
            </div>
          </div>
          <div style="margin-top:10px;" class="backgroundClass">
            <div style="margin: 10px;">
              <img :src="filePushTotal" alt="">
              <span class="dataNumber">{{ pushFileAll }}</span>
              <span>累计推送 (个)</span>
            </div>
            <div style="margin: 10px;font-size: 14px;">
              <img :src="filePushTotal" alt="">
              <span class="dataNumber">{{ gainFileAll }}</span>
              <span>累计接收 (个)</span>
            </div>
          </div>
        </div>
        <div class="dataCatalogueClass">
          <div class="box-title">库表</div>
          <hr>
          <div class="backgroundClass">
            <div style="margin: 10px;font-size: 14px;">
              <img :src="tablePushToday" alt="">
              <span style="color:#18B9BF" class="dataNumber">{{ pushTableToday }}</span>
              <span>今日推送 (条)</span>
            </div>
            <div style="margin: 10px;font-size: 14px;">
              <img :src="tablePushToday" alt="">
              <span style="color:#18B9BF" class="dataNumber">{{ gainTableToday }}</span>
              <span>今日接收 (条)</span>
            </div>
          </div>
          <div style="margin-top:10px;" class="backgroundClass">
            <div style="margin: 10px;">
              <img :src="tablePushTotal" alt="">
              <span style="color:#18B9BF" class="dataNumber">{{ pushTableAll }}</span>
              <span>累计推送 (条)</span>
            </div>
            <div style="margin: 10px;">
              <img :src="tablePushTotal" alt="">
              <span style="color:#18B9BF" class="dataNumber">{{ gainTableAll }}</span>
              <span>累计接收 (条)</span>
            </div>
          </div>
        </div>
        <div class="dataCatalogueClass">
          <div class="box-title">接口</div>
          <hr>
          <div class="backgroundClass">
            <div style="margin: 10px;">
              <img :src="interfacePushToday" alt="">
              <span style="color:#FFB40B" class="dataNumber">{{ pushInterfaceToday }}</span>
              <span>今日推送 (次)</span>
            </div>
            <div style="margin: 10px;">
              <img :src="interfacePushToday" alt="">
              <span style="color:#FFB40B" class="dataNumber">{{ gainInterfaceToday }}</span>
              <span>今日接收 (次)</span>
            </div>
          </div>
          <div style="margin-top:10px;font-size: 14px;" class="backgroundClass">
            <div style="margin: 10px;">
              <img :src="interfacePushTotal" alt="">
              <span style="color:#FFB40B" class="dataNumber">{{ pushInterfaceAll }}</span>
              <span>累计推送 (次)</span>
            </div>
            <div style="margin: 10px;">
              <img :src="interfacePushTotal" alt="">
              <span style="color:#FFB40B" class="dataNumber">{{ gainInterfaceAll }}</span>
              <span>累计接收 (次)</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import echarts from 'echarts'
import { resourceExchange } from '@/api/northCloud.js'

export default {
  name: 'HotProducts',
  data() {
    return {
      gainFileAll: '',
      gainFileToday: '',
      gainInterfaceAll: '',
      gainInterfaceToday: '',
      gainTableAll: '',
      gainTableToday: '',
      pushFileAll: '',
      pushFileToday: '',
      pushInterfaceAll: '',
      pushInterfaceToday: '',
      pushTableAll: '',
      pushTableToday: '',
      filePushToday: require('@/assets/images/todayPurple.png'),
      filePushTotal: require('@/assets/images/totalPurple.png'),
      tablePushToday: require('@/assets/images/todayGreen.png'),
      tablePushTotal: require('@/assets/images/totalGreen.png'),
      interfacePushToday: require('@/assets/images/todayYellow.png'),
      interfacePushTotal: require('@/assets/images/totalYellow.png')
    }
  },
  created() {
  },
  mounted() {
    this.getData()
  },
  methods: {
    getData() {
      resourceExchange().then(res => {
        this.gainFileAll = res.data.gainFileAll
        this.gainFileToday = res.data.gainFileToday
        this.gainInterfaceAll = res.data.gainInterfaceAll
        this.gainInterfaceToday = res.data.gainInterfaceToday
        this.gainTableAll = res.data.gainTableAll
        this.gainTableToday = res.data.gainTableToday
        this.pushFileAll = res.data.pushFileAll
        this.pushFileToday = res.data.pushFileToday
        this.pushInterfaceAll = res.data.pushInterfaceAll
        this.pushInterfaceToday = res.data.pushInterfaceToday
        this.pushTableAll = res.data.pushTableAll
        this.pushTableToday = res.data.pushTableToday
      })
    }
  }
}
</script>
<style scoped  lang="scss">
.backgroundClass {
  background-color: #f2f2f2;
  border-radius: 5px;
  padding: 3px;
  margin-top: 26px;
  font-size: 14px;
  & > div > span:last-child {
    display: inline-block;
    width: 120px;
    text-align: right;
  }
}
img {
  width: 18px;
}
hr {
  background-color: #dcdcdc;
  height: 1px;
  border: none;
}
.catalogueClass {
  display: flex;
  margin-top: 35px;
}
.catalogueClass > div {
  padding: 20px;
}
.dataCatalogue {
  display: flex;
  justify-content: space-between;
  height: 330px;
  position: relative;
  top: -20px;
}
.dataCatalogueClass {
  background: url(../../../assets/images/northCloudBackground2.png);
  background-size: 100% 100%;
  padding: 30px 60px 40px 60px;
}
.dataNumber {
  font-size: 22px;
  color: #8b72ff;
  margin: 0 20px;
  display: inline-block;
  width: 40px;
  text-align: center;
}
.box-title {
  font-weight: 700;
  padding: 8px 0;
  font-size: 18px;
  color: #555;
}
.hot-product {
  width: 100%;
  // height: 846px;
  background-color: #fff;
  .hot-content {
    width: 1300px;
    margin: 0 auto;
    .hot-content-header {
      width: 100%;
      height: 100px;
      display: flex;
      font-weight: bolder;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      & > span:first-child {
        color: #333;
        font-size: 23px;
        margin-top: 10px;
        padding-bottom: 15px;
        position: relative;
        &::after {
          content: '';
          position: absolute;
          left: -22px;
          top: 2px;
          width: 4px;
          height: 23px;
          background-color: #24d2f0;
        }
      }
      & > span:last-child {
        font-size: 20px;
        color: #666;
      }
    }
  }
}
</style>
