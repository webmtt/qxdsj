<template>
  <div class="echarts-page">
    <div ref="myEchart"></div>
  </div>
</template>


<script>
import echarts from 'echarts';
import 'echarts/map/js/china.js'; //node_modules中
// import 'echarts/map/js/china.js';
import '../neimenggu.js'

export default {
  props: {
    dataList: {
      type: Array,
      default: () => []
    }
  },
  name: "echarts",
  data() {
    return {
      chart: null,
      geoCoordMap: {},
      // geoCoordMap: this.dataList.length >0 ?this.dataList: 
    };
  },
  mounted() {
  },
  beforeDestroy() {
  },
  methods: {
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