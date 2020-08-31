<template>
  <div class="weather-carousel">
    <el-tabs>
      <el-tab-pane v-for="(item, index) in dataList" :key="index" :label="item.label">
        <img :src="item.imgUrl" alt />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { getHomeProduct } from "@/api/home.js";
import { getServerUrl } from "@/api/user.js";
export default {
  name: "WeatherCarousel",
  data() {
    return {
      baseUrl: null,
      temperatureImgurl: require("@/assets/images/temperature.png"),
      droughtImgurl: require("@/assets/images/drought.png"),
      precipitationImgurl: require("@/assets/images/precipitation.png"),
      dataList: []
    };
  },
  created() {
    getServerUrl().then(res => {
      if (res.code === "200") {
        this.baseUrl = res.data;
      }
    });
    this.getTab();
  },
  methods: {
    getTab() {
      getHomeProduct().then(res => {
        if (res.data && res.data.data && res.data.data.length) {
          const list = res.data.data;
          list.forEach((item, index) => {
            this.dataList.push({
              id: index + 1,
              label: item.product_name,
              imgUrl: this.baseUrl + item.bigPng
            });
          });
          this.dataList.length = 3;
        }
      });
    }
  }
};
</script>
<style>
.weather-carousel .el-tabs__content {
  padding: 0;
}
.weather-carousel .el-tabs__content {
  height: 100%;
}
.weather-carousel .el-tabs__content img {
  height: 620px;
  width: 100%;
}
.weather-carousel .el-tabs__item {
  height: 50px;
  line-height: 50px;
}
.weather-carousel .el-tabs__nav-scroll {
  display: flex;
  justify-content: space-around;
  align-items: center;
}
.weather-carousel .el-tabs__nav-scroll > div {
  width: 100%;
}
.weather-carousel .el-tabs__nav-scroll > div > div {
  width: 286px;
  text-align: center;
}
.weather-carousel .el-tabs__header {
  margin: 0;
}
.weather-carousel .el-tabs__active-bar {
  /* width:60px !important; */
  /* left:100px; */
  height: 3px;
}
.weather-carousel .is-active {
  font-weight: bolder;
}
</style>

<style scoped  lang="scss">
.weather-carousel {
  width: 860px;
  height: 670px;
  border: 1px solid #dadde6;
  .border-card {
    width: 100%;
    height: 100%;
  }
}
</style>
