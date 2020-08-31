<template>
  <div class="precipitation-survey">
    <div class="precipitation-survey-content">
      <div class="hot-content-header">
        <span>行业应用</span>
        <span>服务应用各行业，发展合作看这里</span>
      </div>
      <div class="servey-content">
        <div class="content-left">
          <img :src="serveyImgurl">
          <div class="content-left-cover">
            <img :src="homeSurveyIcon">
            <span />
            <div>行业应用服务</div>
          </div>
        </div>
        <div class="content-right">
          <!-- <router-link
            v-for="item in dataList"
            :key="item.id"
            class="list-item"
            to="/industryApplication"
            tag="div"
          >
            <img :src="item.imgUrl">
            <div>{{ item.title }}</div>
          </router-link> -->

          <div v-for="item in dataList" :key="item.id" class="list-item" @click="handleToDetal(item)">
            <img :src="item.imgUrl">
            <div>{{ item.title }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getList } from '@/api/industryApplication.js'
export default {
  name: 'PrecipitationSurvey',
  data() {
    return {
      serveyImgurl: require('@/assets/images/homeSurvey.png'),
      homeSurveyIcon: require('@/assets/images/homeSurveyIcon.png'),
      dataList: []
    }
  },
  created() {
    getList().then(res => {
      if (res.data && res.data.list) {
        if (res.data.list.length) {
          // res.data.list.length = 8
          this.dataList = res.data.list.map((item, index) => {
            return {
              id: item.id,
              imgUrl: require(`@/assets/images/homeSurvey${index + 1}.png`) || item.imageurl,
              title: item.title,
              engName: item.entitle,
              content: item.content
            }
          })
        }
      }
    })
  },
  methods: {
    /*
    * 跳转到行业应用详情
    */
    handleToDetal(d) {
      d.id && this.$router.push({ path: `/industryApplication/detail/${d.id}` })
    }
  }
}
</script>

<style scoped  lang="scss">
.precipitation-survey {
  width: 100%;
  height: 740px;
  background-color: #ffffff;
  .precipitation-survey-content {
    width: 1300px;
    height: auto;
    margin: 0 auto;
    .hot-content-header {
      width: 100%;
      height: 140px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      & > span:first-child {
        color: #333;
        font-size: 24px;
        font-weight: bolder;
        padding-bottom: 15px;
        position: relative;
        &::after {
          content: "";
          position: absolute;
          left: -22px;
          top: 2px;
          width: 4px;
          height: 24px;
          background-color: #24d2f0;
        }
      }
      & > span:last-child {
        font-size: 16px;
        color: #666;
      }
    }
    .servey-content {
      display: flex;
      justify-content: space-between;
      height: 510px;
      .content-left {
        height: 100%;
        width: 280px;
        position: relative;
        img {
          width: 100%;
          height: 100%;
        }
        .content-left-cover {
          position: absolute;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
          flex-direction: column;
          img {
            width: 45px;
            height: 45px;
          }
          span {
            margin-top: 15px;
            display: inline-block;
            width: 50px;
            height: 4px;
            background-color: #ffffff;
          }
          div {
            margin-top: 40px;
            font-size: 26px;
            font-weight: bolder;
            color: #ffffff;
            letter-spacing: 2px;
          }
        }
      }
      .content-right {
        width: 990px;
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
        .list-item {
          width: 220px;
          height: 250px;
          position: relative;
          margin-bottom: 10px;
          margin-right: 36px;
          img {
            width: 100%;
            height: 100%;
          }
          & > div {
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            height: 55px;
            line-height: 55px;
            background-color: rgba(0, 0, 0, 0.4);
            font-size: 16px;
            color: #ffffff;
            text-align: center;
          }
        }
        & > div:nth-child(4) {
          margin-right: 0;
        }
        & > div:nth-child(8) {
          margin-right: 0;
        }
      }
    }
  }
}
</style>
