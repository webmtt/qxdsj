
<template>
  <div class="about-us">
    <div class="about-us-banner">
      <img :src="bannerImgurl" alt="">
    </div>
    <div class="about-us-title">
      <span>关于我们</span>
    </div>
    <div class="about-us-content">
      <div class="product-list-header">
        <div>
          <span class="header-title-text">平台简介</span>
        </div>
      </div>
      <div>
        <p style="line-height: 30px;text-indent: 2em;font-size:14px"> {{ dataDetail.platformIntroduction }} </p>
      </div>
    </div>
    <div class="about-us-content">
      <div class="product-list-header">
        <div>
          <span class="header-title-text">联系方式</span>
        </div>
      </div>
      <div class="conect-us-box">
        <div v-for="item in dataList" :key="item.id">
          <img :src="item.imgUrl" alt="">
          <span class="conect-title">{{ item.title }}</span>
          <span>{{ dataDetail[item.subTitle] }}</span>
        </div>
      </div>
      <div class="about-map" style="width:100%; height:435px;">
        <img :src="mapImgurl" alt="" style="width:100%; height:435px;">
        <!-- <div ref="basicMapbox" style="width:100%; height:435px;" /> -->
      </div>
    </div>
  </div>
</template>

<script>

import 'mapbox-gl/dist/mapbox-gl.css'
import { getdetail } from '@/api/aboutUs.js'
import mapboxgl from 'mapbox-gl'
import MapboxLanguage from '@mapbox/mapbox-gl-language'

export default {
  name: 'AboutUs',
  data() {
    return {
      bannerImgurl: require('@/assets/images/about.png'),
      mapImgurl: require('@/assets/images/aboutus.jpg'),
      dataDetail: {},
      dataList: [
        {
          id: 1,
          imgUrl: require('@/assets/images/tel.png'),
          title: '电话/Tel',
          subTitle: 'telephone'
        },
        {
          id: 2,
          imgUrl: require('@/assets/images/code.png'),
          title: '邮编/Zip',
          subTitle: 'postcode'
        },
        {
          id: 3,
          imgUrl: require('@/assets/images/email.png'),
          title: '邮箱/Mail',
          subTitle: 'email'
        }
      ]
    }
  },
  created() {
    getdetail().then(res => {
      if (res.data && res.data.length) {
        this.dataDetail = res.data[0]
      }
    })
  },
  mounted() {
    // this.init()
  },
  methods: {
    // 初始化
    init() {
      mapboxgl.accessToken =
        'pk.eyJ1Ijoic2hhbmJhaSIsImEiOiJjazRodDg3MGEwZmNhM25td3YyNmprMWthIn0.yDKoj_rOFxD19H84gL5XcA'
      // 英文标注转换为中文
      mapboxgl.setRTLTextPlugin(
        'https://api.mapbox.com/mapbox-gl-js/plugins/mapbox-gl-rtl-text/v0.1.0/mapbox-gl-rtl-text.js'
      )
      const map = new mapboxgl.Map({
        container: this.$refs.basicMapbox,
        style: 'mapbox://styles/mapbox/streets-v9',
        center: [114, 38.54],
        zoom: 6
      })
      // 设置语言
      var language = new MapboxLanguage({ defaultLanguage: 'zh' })
      map.addControl(language)

      // 地图导航
      var nav = new mapboxgl.NavigationControl()
      map.addControl(nav, 'top-left')
      // 比例尺
      var scale = new mapboxgl.ScaleControl({
        maxWidth: 80,
        unit: 'imperial'
      })
      map.addControl(scale)
      scale.setUnit('metric')
      // 全图
      map.addControl(new mapboxgl.FullscreenControl())
      // 定位
      map.addControl(
        new mapboxgl.GeolocateControl({
          positionOptions: {
            enableHighAccuracy: true
          },
          trackUserLocation: true
        })
      )
    }
  }
}
</script>

<style scoped  lang="scss">

.about-us {
  width: 100%;
  min-height: 400px;
  .about-us-banner {
    min-width: 1300px;
    max-width: 1920px;
    margin: 0 auto;
    height: 600px;
    background-color: #242a37;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .about-us-title {
    min-width: 1300px;
    max-width: 1920px;
    margin: 0 auto;
    height: 60px;
    text-align: center;
    background-color: #242a37;
    span {
      display: inline-block;
      width: 168px;
      height: 60px;
      line-height: 60px;
      color: #ffffff;
      font-size: 18px;
      background-color: #00c1de;
    }
  }
  .about-us-content {
    width: 1300px;
    margin: 0 auto;
    background-color: #ffffff;
    border-bottom: 2px solid #eeeeee;
    .product-list-header {
      width: 100%;
      height: 100px;
      line-height: 100px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-title-text {
        color: #000;
        font-size: 24px;
        font-weight: bolder;
        letter-spacing: 2px;
        position: relative;
        &::after {
          content: '';
          position: absolute;
          top: 40px;
          left: 0;
          width: 100%;
          height: 2px;
          background-color: #00c1de;
        }
      }
    }
    .conect-us-box {
      widows: 100%;
      display: flex;
      justify-content: space-around;
      & > div {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding-top: 10px;
        margin-bottom: 70px;
        img {
          width: 110px;
          height: 110px;
        }
        .conect-title {
          font-size: 18px;
          color: #000;
          margin-top:30px;
        }
        & > span:last-child {
          font-size: 16px;
          color: #333;
          margin-top:25px;
        }
      }
    }
  }
}
</style>

