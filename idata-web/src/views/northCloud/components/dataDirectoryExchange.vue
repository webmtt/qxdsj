<template>
  <div class="hot-product">
    <div class="hot-content">
      <div class="hot-content-header">
        <span>数据目录交换情况</span>
        <!-- <span>DATA DIRECTORY EXCHANGE</span> -->
      </div>
      <div class="dataCatalogue">
        <div class="dataCatalogueClass">
          <div class="box-title">推送</div>
          <hr>
          <div class="catalogueClass">
            <div style="color:#947EFF">
              <span style="float:right;width: 1px;height: 30px; background: #DCDCDC;" />
              <div class="dataNumber">{{ pushFileCount }}</div>
              <div style="text-align: center;">文件目录 (类)</div>
            </div>
            <div style="color:#18B9BF">
              <span style="float:right;width: 1px;height: 30px; background: #DCDCDC;" />
              <div class="dataNumber">{{ pushTableCount }}</div>
              <div style="text-align: center;">库表目录 (类)</div>
            </div>
            <div style="color:#FFB40B">
              <div class="dataNumber">{{ pushInterfaceCount }}</div>
              <div style="text-align: center;">接口目录 (类)</div>
            </div>
          </div>
        </div>
        <div class="dataCatalogueClass">
          <div class="box-title">获取</div>
          <hr>
          <div class="catalogueClass">
            <div style="color:#947EFF">
              <span style="float:right;width: 1px;height: 30px; background: #DCDCDC;" />
              <div class="dataNumber">{{ gainFileCount }}</div>
              <div style="text-align: center;">文件目录 (类)</div>
            </div>
            <div style="color:#18B9BF">
              <span style="float:right;width: 1px;height: 30px; background: #DCDCDC;" />
              <div class="dataNumber">{{ gainTableCount }}</div>
              <div style="text-align: center;">库表目录 (类)</div>
            </div>
            <div style="color:#FFB40B">
              <div class="dataNumber">{{ gainInterfaceCount }}</div>
              <div style="text-align: center;">接口目录 (类)</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import echarts from 'echarts'
import { directoryExchange } from '@/api/northCloud.js'

export default {
  name: 'HotProducts',
  data() {
    return {
      pushFileCount: '',
      pushInterfaceCount: '',
      pushTableCount: '',
      gainFileCount: '',
      gainInterfaceCount: '',
      gainTableCount: ''
    }
  },
  created() {
  },
  mounted() {
    this.getData()
  },
  methods: {
    getData() {
      directoryExchange().then(res => {
        this.pushFileCount = res.data.pushFileCount
        this.pushInterfaceCount = res.data.pushInterfaceCount
        this.pushTableCount = res.data.pushTableCount
        this.gainFileCount = res.data.gainFileCount
        this.gainInterfaceCount = res.data.gainInterfaceCount
        this.gainTableCount = res.data.gainTableCount
      })
    }
  }
}
</script>
<style scoped  lang="scss">
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
  height: 314px;
  position: relative;
  top: -20px;
}
.dataCatalogueClass {
  background: url(../../../assets/images/northCloudBackground1.png);
  background-size: cover;
  padding: 30px;
}
.dataNumber {
  font-size: 38px;
  text-align: center;
  padding-bottom: 10px;
  width: 157px;
}
.box-title {
  font-weight: 700;
  padding: 25px 0px 10px 0px;
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
