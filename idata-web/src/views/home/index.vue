<template>
  <div class="home-page">
    <div class="home-banner">
      <div class="home-banner-info">
        <span style="font-size:56px;">Meteorological</span>
        <span style="font-size:52px;margin-top:-40px;">大数据共享服务</span>
        <span style="font-size:18px;margin-top:26px;color: #f2f2f2;">海量气象数据存储服务的云平台，支持多种算法和加工流水线</span>
        <span style="font-size:18px;margin-top:15px;color: #f2f2f2;">为您提供智慧化、全球化、社会化、专业化的气象服务</span>
      </div>
    </div>
    <nav-sub @handleScroll="handleScroll" />
    <div class="tab-card">
      <div>
        <weather-carousel />
      </div>
      <div>
        <data-serve-list />
      </div>
    </div>
    <hot-product id="hotProduct" ref="hotProductRef" />
    <precipitation-survey id="precipitationSurvey" ref="precipitationSurveyRef" />
    <api-server id="apiServer" ref="apiServerRef" />
    <featured-products id="featuredProducts" ref="featuredProductsRef" />
    <data-down id="dataDown" ref="dataDownRef" />
    <service-process id="serviceProcessRef" ref="serviceProcessRef" />
    <!-- <div class="back-top">返回</div> -->
    <!-- <el-backtop target=".page-component__scroll .el-scrollbar__wrap" :bottom="100">
      <div
        style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .12);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }"
      >
        UP
      </div>
    </el-backtop> -->
  </div>
</template>

<script>
import NavSub from './components/NavSub.vue'
import WeatherCarousel from './components/WeatherCarousel.vue'
import DataServeList from './components/DataServeList.vue'
import HotProduct from './components/HotProduct.vue'
import PrecipitationSurvey from './components/PrecipitationSurvey.vue'
import ApiServer from './components/ApiServer.vue'
import FeaturedProducts from './components/FeaturedProducts.vue'
import DataDown from './components/DataDown.vue'
import ServiceProcess from './components/ServiceProcess.vue'
export default {
  name: 'HomePage',
  components: {
    NavSub,
    WeatherCarousel,
    DataServeList,
    HotProduct,
    PrecipitationSurvey,
    ApiServer,
    FeaturedProducts,
    DataDown,
    ServiceProcess
  },
  mounted() {
  },
  methods: {
    handleScroll(item) {
      if (item.subID) {
        document.getElementById(`${item.subID}`).scrollIntoView()
      } else {
        this.$router.push({ path: `${item.link}` })
      }
    }
  }
}
</script>

<style scoped  lang="scss">
.home-page {
  width: 100%;
  height: auto;
  .home-banner {
    min-width: 1300px;
    max-width: 1920px;
    margin: 0 auto;
    height: 400px;
    background-image: url('../../assets/images/banner.png');
    background-origin: content-box;
    background-position: center;
    background-repeat: no-repeat;
    background-clip: content-box;
    background-size: cover;
    color: #ffffff;
    .home-banner-info {
      width: 1300px;
      margin: 0 auto;
      display: flex;
      flex-direction: column;
      span {
        display: inline-block;
      }
      span:first-child {
        text-align: right;
        margin-top: 56px;
      }
    }
  }
  .tab-card {
    width: 1300px;
    margin: 0 auto;
    padding: 20px 0;
    display: flex;
    justify-content: space-between;
  }
}
.back-top{
  position: fixed;
  right: 20px;
  bottom: 20px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
</style>
