<template>
  <div class="echarts-page">
    <div ref="myEchart"></div>
  </div>
</template>


<script>

import echarts from 'echarts';
import 'echarts/map/js/china.js'; //node_modules中
// import 'echarts/map/js/china.js';
import NeiMengGu from '../neimenggu.js'

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
        legend: {
          left: '2%',
          bottom: '2%',
          data: []
        },
        geo: {//引入贵州省的地图
          map: '内蒙古',
          name: '数据',
          type: 'map',
          mapType: 'china',
          roam: true,
          zoom: 1,
          itemStyle: {
            normal: {
              borderColor: '008B8B',
              shadowColor: 'rgba(0, 0, 0, 0.5)',
              shadowBlur: 10,
              shadowOffsetX: 0
            },
            emphasis: {
              areaColor: '#f2f2f2'
            }
          },
        },
      }
      // geoCoordMap: this.dataList.length >0 ?this.dataList: 
    };
  },
  mounted() {
    if (this.dataList.length) {
      this.setGeoCoordMap()
      this.initChart();
    }
    // this.chinaConfigure();
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    setGeoCoordMap() {
      this.dataList.forEach(item => {
        if (!this.geoCoordMap[item.Station_Name]) {
          this.geoCoordMap[item.Station_Name] = []
          this.geoCoordMap[item.Station_Name].push(item.Lon)
          this.geoCoordMap[item.Station_Name].push(item.Lat)
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
            value: geoCoord.concat(data[i].value)
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
      this.options.legend.data = []
      this.options.legend.data.push('站点')
      this.options.series = [
        {
          name: '站点',
          type: 'scatter',
          coordinateSystem: 'geo',
          data: this.convertData(arr),
          symbolSize: 8,
          emphasis: {
            label: {
              show: true
            }
          },
          label: {
            formatter: '{b}',
            position: 'right',
            show: false
          },
          itemStyle: {
            color: '#008B8B'
          }
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
  background-color: #f2f2f2;
  & > div {
    width: 100%;
    height: 78vh;
    background-color: #f2f2f2;
  }
}
</style>