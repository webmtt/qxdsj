<template>
  <div class="echarts-page">
    <div ref="myEchart"></div>
  </div>
</template>
<script>
import echarts from 'echarts';
import 'echarts/map/js/china.js'; //node_modules中
import NeiMengGu from '../neimenggu.js'
import { geoCoordMapFN, geoNameFN } from '../nmg.js'

export default {
  props: {
    dataList: {
      type: Array,
      default: () => []
    }
  },
  created() {
    echarts.registerMap('内蒙古', NeiMengGu);
  },
  name: "EchartsGis",
  data() {
    return {
      chart: null,
      geoCoordMap: {},
      options: {
        tooltip: {
          show: true,
          formatter: function (params) {
            if (params.value && params.value[2].param) {
              let str = JSON.stringify(params.value[2].param).replace(/\{/, '')
              str.replace(/\}/, '')
              return str.replace(/\,/g, "<br/>")
            } else {
              return '';
            }
          },
        },
        geo: {//引入贵州省的地图
          map: '内蒙古',
          name: '数据',
          type: 'map',
          mapType: 'china',
          zoom: 1,
          roam: false,
          itemStyle: {
            emphasis: {
              areaColor: '#f2f2f2'
            }
          },
        },
      }
    };
  },
  mounted() {
    this.geoCoordMap = geoCoordMapFN()
    if (this.dataList.length) {
      this.setGeoCoordMap()
      this.initChart();
    }
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    initChart() {
    },
    setGeoCoordMap() {
      this.dataList.forEach(item => {
        if (!this.geoCoordMap[item.Station_Name]) {
          this.geoCoordMap[item.Station_Name] = []
          this.geoCoordMap[item.Station_Name].push(item.Lon)
          this.geoCoordMap[item.Station_Name].push(item.Lat)
          this.geoCoordMap[item.Station_Name].push({
            param: item
          })
        }
      })
    },
    convertData(data) {
      var res = [];
      for (var i = 0; i < data.length; i++) {
        var geoCoord = this.geoCoordMap[data[i].name];
        if (geoCoord) {
          res.push({
            name: data[i].name,
            value: geoCoord.concat(data[i].value),
            param: geoCoord.param
          });
        }
      }
      return res;
    },
    initChart() {
      this.chart = echarts.init(this.$refs.myEchart);
      window.onresize = echarts.init(this.$refs.myEchart).resize;
      const arr = Object.keys(this.geoCoordMap).map(item => {
        return {
          name: item,
          value: 80
        }
      })
      this.options.series = [
        {
          name: '站点',
          type: 'scatter',
          coordinateSystem: 'geo',
          data: this.convertData(arr),
          symbolSize: 10,
          encode: {
            value: 2
          },
          label: {
            formatter: '{b}',
            position: 'right',
            show: false
          },
          itemStyle: {
            color: 'purple'
          },
          roam: false,
          emphasis: {
            label: {
              show: true
            }
          },
          zlevel: 1
        },
        {
          name: 'Top 5',
          type: 'scatter',
          coordinateSystem: 'geo',
          data: this.convertData(geoNameFN()),
          symbolSize: function (val) {
            return 10
          },
          showEffectOn: 'render',
          label: {
            formatter: '{b}',
            position: 'right',
            show: true
          },
          itemStyle: {
            color: '#999',
            shadowColor: '#333'
          },
          zlevel: 10
        }
      ];
      this.chart.setOption(this.options);
    }
  }
}
</script>

<style scoped  lang="scss">
.echarts-page {
  width: 100%;
  height: 80vh;
  // background-color: #f2f2f2;
  & > div {
    width: 100%;
    height: 78vh;
    border: 1px solid #f2f2f2;
    // background-color: #f2f2f2;
  }
}
</style>