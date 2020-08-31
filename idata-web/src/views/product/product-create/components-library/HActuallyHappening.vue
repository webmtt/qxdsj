<template>
  <div class="compontent-box h-actually-happening product-border">
    <div class="actually-happening">
      <img :src="flageImgUrl" alt />
      <span>实况{{stationnum}}</span>
      <span>更新时间:{{newtimer}}</span>
    </div>
    <div class="actually-happening-body">
      <div class="wen-du">
        <div class="wen-du-up">
          <span>{{newWendu}}℃</span>
          <img :src="wenduImgUrl" alt />
        </div>
        <div class="wen-du-down">
          <span style="margin-right:4px">最高{{maxWendu}}℃</span>|
          <span style="margin-left:2px">最低{{minWendu}}℃</span>
        </div>
      </div>
      <div class="weather-list">
        <div class="weather-list-item">
          <img :src="qiyaImgUrl" alt />
          <label>气压</label>
          <span>{{qiya}}Hpa</span>
        </div>
        <div class="weather-list-item">
          <img :src="shiduImgUrl" alt />
          <label>湿度</label>
          <span>{{shidu}}%</span>
        </div>
        <div class="weather-list-item">
          <img :src="fengsuImgUrl" alt />
          <label>风</label>
          <span>{{fengsu}}m/s ({{fengxiang}})</span>
        </div>
        <div class="weather-list-item" style="margin-top:14px;">
          <img :src="huabanfubenImgUrl" alt />
          <label>临近06小时降水</label>
          <span>{{PRE_6h}} mm</span>
        </div>
        <div class="weather-list-item">
          <img :src="huabanfubenImgUrl" alt />
          <label>临近12小时降水</label>
          <span>{{PRE_12h}} mm</span>
        </div>
        <div class="weather-list-item">
          <img :src="huabanfubenImgUrl" alt />
          <label>临近24小时降水</label>
          <span>{{PRE_24h}} mm</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import { getActuallyHappening } from "../createApi.js";

import moment from "moment";
export default {
  name: "HActuallyHappening",
  props: {
    valueData: {
      required: false,
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      flageImgUrl: require("@/assets/images/hongqi.png"),
      wenduImgUrl: require("@/assets/images/wendu.png"),
      shiduImgUrl: require("@/assets/images/shidu.png"),
      fengsuImgUrl: require("@/assets/images/fengsu.png"),
      huabanfubenImgUrl: require("@/assets/images/huabanfuben.png"),
      qiyaImgUrl: require("@/assets/images/qiya.png"),
      stationnum: null,
      newtimer: "",
      maxWendu: null,
      minWendu: null,
      newWendu: null,
      qiya: null, // 气压
      shidu: null, // 湿度
      fengsu: null, // 风速
      fengxiang: null,
      PRE_6h: null,
      PRE_12h: null,
      PRE_24h: null
    };
  },
  watch: {
    valueData: {
      handler(newVal, oldVal) {
       
        if (this.valueData && this.valueData.length) {
          this.stationnum = this.valueData[0].list[0];
          this.getData();
        } else {
          this.stationnum = null;
          this.maxWendu = null;
          this.minWendu = null;
          this.newWendu = null;
          this.qiya = null;
          this.shidu = null;
          this.fengsu = null;
          this.fengxiang = null;
          this.PRE_6h = null;
          this.PRE_12h = null;
          this.PRE_24h = null; // WIN_D_Avg_10mi
        }
      },
      deep: true
    }
  },
  created() {
    window["callbackNameActive"] = data => {
      this.callbackNameActive(data);
    };
  },
  mounted() {
    if (
      this.valueData &&
      this.valueData.length &&
      this.valueData[0].list.length
    ) {
      this.stationnum = this.valueData[0].list[0];
      this.getData();
    } else {
      this.stationnum = null;
      this.maxWendu = null;
      this.minWendu = null;
      this.newWendu = null;
      this.qiya = null;
      this.shidu = null;
      this.fengsu = null;
      this.fengxiang = null;
      this.PRE_6h = null;
      this.PRE_12h = null;
      this.PRE_24h = null; // WIN_D_Avg_10mi
    }
  },
  methods: {
    getData() {
      this.newtimer = moment(new Date()).format("hh:MM");
      const timer = moment(new Date()).format("YYYYMMDDhh:00:00");
      this.$jsonp(
        `http://10.62.89.55/cimiss-web/api?userId=user_imwb&pwd=symantec5227&interfaceId=getSurfEleByTimeAndStaID&dataCode=SURF_CHN_MUL_HOR&elements=Station_Id_C,Hour,TEM,TEM_Max,TEM_Min,PRS,PRE_6h,PRE_12h,PRE_24h,RHU,WIN_D_Avg_10mi,WIN_S_Avg_10mi&times=20200701030000&staIDs=53463&dataFormat=jsonp&callbackName=callbackNameActive`
      );
    },
    callbackNameActive(d) {
      if (d.DS && d.DS.length) {
        const data = d.DS[0];
        this.maxWendu = data.TEM_Max;
        this.minWendu = data.TEM_Min;
        this.newWendu = data.TEM;
        this.qiya = data.PRS;
        this.shidu = data.RHU;
        this.fengsu = data.WIN_S_Avg_10mi;
        this.fengxiang =
          this.convertfsChina(this.convertDegree(data.WIN_D_Avg_10mi)) + "风";
        this.PRE_6h = data.PRE_6h;
        this.PRE_12h = data.PRE_12h;
        this.PRE_24h = data.PRE_24h; // WIN_D_Avg_10mi
      }
      // $("#realtime_wd").html('('+convertfsChina(convertDegree(result.WIN_D_Avg_10mi))+"风)");
    },
    convertDegree(fx) {
      var degree = fx;
      if ((degree > 348.76 && degree < 360) || (degree > 0 && degree < 11.25)) {
        return "N";
      } else if (degree > 11.26 && degree < 33.75) {
        return "NNE";
      } else if (degree > 33.76 && degree < 56.25) {
        return "NE";
      } else if (degree > 56.26 && degree < 78.75) {
        return "ENE";
      } else if (degree > 78.76 && degree < 101.25) {
        return "E";
      } else if (degree > 101.26 && degree < 123.75) {
        return "ESE";
      } else if (degree > 123.76 && degree < 146.25) {
        return "SE";
      } else if (degree > 146.26 && degree < 168.75) {
        return "SSE";
      } else if (degree > 168.76 && degree < 191.25) {
        return "S";
      } else if (degree > 191.26 && degree < 213.75) {
        return "SSW";
      } else if (degree > 213.76 && degree < 236.25) {
        return "SW";
      } else if (degree > 236.26 && degree < 258.75) {
        return "WSW";
      } else if (degree > 258.76 && degree < 281.25) {
        return "W";
      } else if (degree > 281.26 && degree < 303.75) {
        return "WNW";
      } else if (degree > 303.76 && degree < 326.25) {
        return "NW";
      } else if (degree > 326.26 && degree < 348.75) {
        return "NNW";
      }
      return "C";
    },
    convertfsChina(fx) {
      var tj = "";
      if (fx == "N") tj = "北";
      else if (fx == "NNE") tj = "东北偏北";
      else if (fx == "NE") tj = "东北";
      else if (fx == "ENE") tj = "东北偏东";
      else if (fx == "E") tj = "东";
      else if (fx == "ESE") tj = "东南偏东";
      else if (fx == "SE") tj = "东南";
      else if (fx == "SSE") tj = "东南偏南";
      else if (fx == "S") tj = "南";
      else if (fx == "SSW") tj = "西南偏南";
      else if (fx == "SW") tj = "西南";
      else if (fx == "WSW") tj = "西南偏西";
      else if (fx == "W") tj = "西";
      else if (fx == "WNW") tj = "西北偏西";
      else if (fx == "NW") tj = "西北";
      else if (fx == "NNW") tj = "西北偏北";
      else tj = "静风";
      return tj;
    }
  }
};
</script>

<style scoped  lang='scss'>
@import "../components-header.scss";

.h-actually-happening {
  width: 100%;
  height: 100%;
  background-color: #f2f2f2;
  padding: 4px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  .actually-happening {
    width: 100%;
    height: 40px;
    display: flex;
    justify-self: start;
    align-items: center;
    img {
      width: 15px;
      height: 15px;
    }
    & > span {
      font-size: 16px;
      color: #555;
      margin-left: 5px;
    }
    & > span:last-child {
      color: #999;
      margin-left: 20px;
      font-size: 14px;
    }
  }
  .actually-happening-body {
    width: 100%;
    flex: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    & > div {
      flex: 1;
      height: 100%;
    }
    & > div:first-child {
      border-right: 1px solid #cccccc;
    }
    .wen-du {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      .wen-du-down {
        margin-top: 4px;
        font-size: 12px;
        color: #999;
      }
    }
    .wen-du-up {
      display: flex;
      font-size: 30px;
      font-weight: bolder;
      img {
        width: 30px;
        height: 30px;
      }
    }
    .weather-list {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: flex-start;
      padding: 20px;
      .weather-list-item {
        display: flex;
        align-items: center;
        font-size: 12px;
        margin: 2px 0;
        img {
          width: 14px;
          height: 14px;
          margin-right: 4px;
        }
        span {
          margin-left: 10px;
          color: #999;
        }
      }
    }
  }
}
</style>
