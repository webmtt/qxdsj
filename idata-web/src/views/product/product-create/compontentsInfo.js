
import ToolLibrary from '@/utils/toolLibrary.js'
const compontentsInfo = [
  {
    compontentName: 'HBannerImg',
    compontentForm: 'HBannerImgForm',
    chName: '静态图片模块',
    type: 1, // ,
    title: false, // 标题
    httpUrl: '',
    navshow: false
  },
  {
    compontentName: 'HCarouselImg',
    compontentForm: 'HCarouselImgForm',
    chName: '单图轮播模块',
    title: true, // 标题
    navshow: true,
    httpUrl: '',
    inputImgType: true,
    inputnumber: true
  },
  {
    compontentName: 'HCarouselMoreImg',
    title: true, // 标题
    compontentForm: 'HCarouselImgForm',
    chName: '多图轮播模块',
    navshow: true,
    inputImgType: true,
    inputnumber: true,
    httpUrl: '/'
  },
  {
    compontentName: 'HTextList',
    chName: '标题列表模块',
    compontentForm: 'HTextListForm',
    title: true, // 标题
    navshow: true,
    httpUrl: '/',
    navUrl: null
  },
  {
    compontentName: 'HImgTextList',
    chName: '图片标题列表模块',
    compontentForm: 'HImgTextListForm',
    title: true, // 标题
    navshow: true,
    httpUrl: '/',
    navUrl: null
  },
  {
    compontentName: 'HNavDown',
    chName: '下拉导航模块',
    compontentForm: 'HNavDownForm',
    title: true, // 标题
    navshow: true,
    httpUrl: '/',
    navUrl: null
  },
  {
    compontentName: 'HImgCard',
    chName: '图片文本模块',
    httpUrl: '/',
    navUrl: null
  },
  {
    compontentName: 'HImgInfo',
    chName: '图片介绍模块',
    httpUrl: '/',
    navUrl: null
  },
  {
    compontentName: 'HNavRight',
    chName: '右侧导航模块',
    httpUrl: '/',
    navUrl: null
  },
  {
    compontentName: 'HIframe',
    chName: 'iframe模块',
    compontentForm: 'HIframeForm'
  },
  {
    compontentName: 'HWeatherReport',
    chName: '天气预报',
    compontentForm: 'HWeatherForm'
  },
  {
    compontentName: 'HActuallyHappening',
    chName: '天气实况',
    compontentForm: 'HActivellyForm'
  }
]
const compontentsList = []
const arr = []

const allList = require.context('./components-library', true, /\.vue$/)
allList.keys().forEach(item => {
  arr.push(ToolLibrary.h_spliceStr(item.replace('.vue', ''), './', 1))
})
arr.forEach(item => {
  const obj = compontentsInfo.find(info => info.compontentName === item) || null
  if (obj) {
    compontentsList.push({
      ...obj, compontentName: item
    })
  }
})
// this.compontentsList = arr

export default compontentsList
