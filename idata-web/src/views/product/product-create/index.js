import Vue from 'vue'
import HBannerImg from './components-library/HBannerImg.vue' // 海报图
import HTextList from './components-library/HTextList.vue' // 文件列表
import HCarouselImg from './components-library/HCarouselImg.vue' // 图片轮播
import HCarouselMoreImg from './components-library/HCarouselMoreImg.vue' // 图片轮播
// import HImgCard from './components-library/HImgCard.vue' // 图片卡片
import HImgTextList from './components-library/HImgTextList.vue' // 图片文字列表
// import HImgInfo from './components-library/HImgInfo.vue' // 图片和文字说明
import HNavDown from './components-library/HNavDown.vue' // 树形结构
import HIframe from './components-library/HIframe.vue' // iframe 组件
// import HNavRight from './components-library/HNavRight.vue' // 树形结构
import HActuallyHappening from './components-library/HActuallyHappening.vue' // 天气实况
import HWeatherReport from './components-library/HWeatherReport.vue' // 天气预报

// 表单组件
import HBannerImgForm from './components-form/HBannerImgForm.vue' // 单图表单
import HCarouselImgForm from './components-form/HCarouselImgForm.vue' // 单图轮播
import HTextListForm from './components-form/HTextListForm.vue' // 标题列表模块
import HImgTextListForm from './components-form/HImgTextListForm.vue' // 图像文本列表模块
import HNavDownForm from './components-form/HNavDownForm.vue' // 树形结构
import HIframeForm from './components-form/HIframeForm.vue' // iframe 数据编辑
import HActivellyForm from './components-form/HActivellyForm.vue' // 实况和天气预报站点id编辑
import HWeatherForm from './components-form/HWeatherForm.vue' // 实况和天气预报站点id编辑

// 公共组件

import PaginationMini from './pagination-mini/index.vue' // 标题列表模块

const ComponentsList = [
  HIframe, // iframe 组件
  HBannerImg, // 输入框
  HTextList, // 下拉框
  HCarouselImg, // 图片轮播
  HCarouselMoreImg, // 多图轮播
  HWeatherReport, // 天气预报
  HActuallyHappening, // 天气实况
  // HImgCard,
  HImgTextList,
  // HImgInfo,
  HNavDown,
  // HNavRight,
  // 表单
  HIframeForm,
  HBannerImgForm,
  HCarouselImgForm,
  HTextListForm,
  HImgTextListForm,
  HNavDownForm,
  HActivellyForm,
  HWeatherForm,
  // 公共组件
  PaginationMini
]

ComponentsList.forEach((item) => {
  Vue.component(item.name, item)
})
