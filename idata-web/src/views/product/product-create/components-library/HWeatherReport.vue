<template>
  <div class="compontent-box h-weather-report product-border">
    <div class="weather-report-header">
      <img :src="yubaoImgUrl" alt />
      <span>天气预报{{stationnum}}</span>
      <span>更新时间:{{theTimer}}</span>
    </div>
    <div class="weather-report-body">
      <div class="weather-item" v-for="(item, index) in dataList" :key="index">
        <span>{{item.time}}</span>
        <img :src="item.iconUrl" alt />
        <span>{{item.wendu}}</span>
        <label>{{item.tianqi}}</label>
        <span>{{item.feng}}</span>
      </div>
    </div>
  </div>
</template>

<script>
import moment from "moment";
export default {
  name: "HWeatherReport",
  props: {
    valueData: {
      required: false,
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      yubaoImgUrl: require("@/assets/images/tianqi-copy.png"),
      theTimer: null,
      stationnum: null,
      dataList: [],
      weatherIconMap: {
        晴: "01",
        多云: "07",
        阴: "03",
        小雨: "45",
        中雨: "46",
        阵雨: "14",
        雷阵雨: "15",
        小雪: "19",
        中雪: "22",
        大雪: "22",
        雨夹雪: "24"
      }
    };
  },
  watch: {
    valueData: {
      handler(newVal, oldVal) {
        if (this.valueData && this.valueData.length) {
          this.stationnum = this.valueData[0].list[0];
          (this.theTimer = moment(new Date()).format("YYYY-MM-DD hh:MM")),
            this.getData();
        } else {
          this.stationnum = null;
        }
      },
      deep: true
    }
  },
  created() {
    window["callbackName"] = data => {
      this.callbackName(data);
    };
  },
  mounted() {
    if (
      this.valueData &&
      this.valueData.length &&
      this.valueData[0].list.length
    ) {
      this.stationnum = this.valueData[0].list[0];
      (this.theTimer = moment(new Date()).format("YYYY-MM-DD hh:MM")),
        this.getData();
    }
  },
  methods: {
    getData() {
      this.newtimer = moment(new Date()).format("hh:MM");
      const timer = moment(new Date()).format("YYYYMMDD");
      this.$jsonp(
        `http://10.62.89.55/cimiss-web/api?userId=BEHT_XXZX_ispoc&pwd=symantec5227&interfaceId=getSevpEleByTimeRangeAndStaID&dataCode=SEVP_CHN_WEFC_RFFC&elements=RYMDHM,Prod_Code,Datetime,Station_Id_C,Validtime,TEM_Max_24h,TEM_Min_24h,WEP_Past_12h,WIN_PD_12h,WIN_S_Max_12h&timeRange=[${timer}000000,${timer}230000]&staIds=${this.stationnum}&eleValueRanges=Validtime:12,24,36,48,60,72;Prod_Code:SPCC&orderBy=RYMDHM:asc&dataFormat=jsonp&callbackName=callbackName`
      );
    },
    callbackName(d) {
      if (d.DS && d.DS.length) {
        const m = new Map();
        const m_fs = new Map();
        const m_fx = new Map();
        const value_w = new Array(
          "晴",
          "多云",
          "阴",
          "阵雨",
          "雷阵雨",
          "雷阵雨并伴有冰雹",
          "雨夹雪",
          "小雨",
          "中雨",
          "大雨",
          "暴雨",
          "大暴雨",
          "特大暴雨",
          "阵雪",
          "小雪",
          "中雪",
          "大雪",
          "暴雪",
          "雾",
          "冻雨",
          "沙尘暴",
          "小雨转中雨",
          "中雨转大雨",
          "大雨转暴雨",
          "暴雨转大暴雨",
          "大暴雨转特大暴雨",
          "小雪转中雪",
          "中雪转大雪",
          "大雪转暴雪",
          "浮尘",
          "扬沙",
          "强沙尘暴"
        );
        for (let i = 0; i < value_w.length; ++i) {
          m.set(i + "", value_w[i]);
        }
        const value_fx = new Array(
          "不发风向",
          "东北",
          "东",
          "东南",
          "南",
          "西南",
          "西",
          "西北",
          "北",
          "旋转、不定"
        );
        for (let i = 0; i < value_fx.length; ++i) {
          m_fx.set(i + "", value_fx[i]);
        }
        const value_fs = new Array(
          "小于3级",
          "3-4级",
          "4-5级",
          "5-6级",
          "6-7级",
          "7-8级",
          "8-9级",
          "9-10级",
          "10-11级",
          "11-12级"
        );
        for (let i = 0; i < value_fs.length; ++i) {
          m_fs.set(i + "", value_fs[i]);
        }
        const result = d.DS;

        const obj = new Map();
        for (let n = 0; n < result.length; ++n) {
          const value1 = result[n];
          //RYMDHM,Prod_Code,Datetime,Station_Id_C,Validtime,TEM_Max_24h,TEM_Min_24h,WEP_Past_12h,WIN_PD_12h,WIN_S_Max_12h
          obj.set(value1.Validtime, value1);
        }
        for (let k = 0; k < 6; ++k) {
          const value = obj.get(12 * (k + 1) + "");
          this.dataList.push({
            time: `未来${value.Validtime}小时`,
            iconUrl: require(`@/assets/images/weatherIcon/${
              this.weatherIconMap[m.get(value.WEP_Past_12h)]
            }.gif`), //  `assets/${this.weatherIconMap[m.get(value.WEP_Past_12h)]}`,
            wendu:
              value.TEM_Min_24h === "999999"
                ? ""
                : `${value.TEM_Min_24h} | ${value.TEM_Max_24h}`,
            tianqi: `${m.get(value.WEP_Past_12h)}`,
            feng: `${m_fs.get(value.WIN_S_Max_12h)} | ${m_fx.get(
              value.WIN_PD_12h
            )}`
          });
        }
      }
    }
  }
};
</script>

<style scoped  lang='scss'>
@import "../components-header.scss";

.h-weather-report {
  width: 100%;
  height: 100%;
  padding: 4px;
  display: flex;
  flex-direction: column;
  .weather-report-header {
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
  .weather-report-body {
    width: 100%;
    flex: 1;
    border: 1px solid #f2f2f2;
    display: flex;
    justify-content: space-around;
    align-items: center;
    box-sizing: border-box;
    & > div {
      flex: 1;
      height: 100%;
      box-sizing: border-box;
      border: 1px solid #f2f2f2;
      font-size: 12px;
      color: #999;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      align-items: center;
      padding: 10px 0;
      & > img {
        width: 40px;
        height: 40px;
      }
    }
  }
}
</style>
