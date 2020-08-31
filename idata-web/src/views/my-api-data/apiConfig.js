// import echarts from 'echarts'

export const LineOption = {
  title: {
    text: 'API图表'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: []
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  toolbox: {
    feature: {
      saveAsImage: {}
    }
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ["50136",
    "50137",
    "50246",
    "50247",
    "50349",
    "50353",
    "50425",
    "50429",
    "50431",
    "50434"]
  },
  yAxis: {
    type: 'value'
  },
  series: [
    // {
    //   name: '气压',
    //   type: 'line',
    //   stack: '总量',
    //   data: [
    //     955,
    //     972,
    //     964,
    //     947,
    //     947,
    //     985,
    //     940,
    //     929,
    //     924,
    //     922
    //   ]
    // }
  ]
}
