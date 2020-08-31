// import echarts from 'echarts'

export const HotOption = {

  color: ['#3398DB'],
  tooltip: {
    trigger: 'axis'
    // axisPointer: { // 坐标轴指示器，坐标轴触发有效
    //   type: 'line' // 默认为直线，可选为：'line' | 'shadow'
    // }
  },
  grid: {
    left: '4%',
    right: '4%',
    bottom: '0px',
    show: 'false',
    borderWidth: '0',
    containLabel: true
  },
  xAxis: [
    {
      type: 'category',
      data: ['-', '-', '-', '-', '-', '-', '-', '-', '-', '-'],
      axisTick: {
        alignWithLabel: false
      },
      triggerEvent: true,
      axisLabel: {
        borderWidth: 0,
        interval: 'auto',
        rotate: 40
        // formatter:function(value){
        //   return value.split("").join("\n");}
      }
    }
  ],
  yAxis: [
    {
      type: 'value',
      triggerEvent: true
    }
  ],
  series: [
    {
      name: '总阅读量',
      type: 'bar',
      barWidth: '18px',
      data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      itemStyle: {
        // 柱形图圆角，鼠标移上去效果，如果只是一个数字则说明四个参数全部设置为那么多
        emphasis: {
          barBorderRadius: 30
        },
        normal: {
          // 柱形图圆角，初始化效果
          barBorderRadius: [6, 6, 0, 0],
          label: {
          }
        }
      }
    }
  ]
}
