<template>
  <div class="app-footer">
    <div class="footer-list">
      <!-- <div class="list-content">
        <div class="content-item">
          <div class="item-title">关于我们</div>
          <ul>
            <li>公司简介</li>
            <li>媒体报道</li>
            <li>大事记</li>
            <li>荣誉与认证</li>
            <li>友情链接</li>
          </ul>
        </div>
        <div class="content-item">
          <div class="item-title">资源与文档</div>
          <ul>
            <li>新手帮助</li>
            <li>产品文档</li>
            <li>开发者资源</li>
            <li>备案帮助</li>
            <li>资源与文档</li>
          </ul>
        </div>
        <div class="content-item">
          <div class="item-title">诚邀合作</div>
          <ul>
            <li>渠道合作</li>
            <li>社区合作</li>
            <li>教育合作</li>
            <li>诚邀合作</li>
            <li>诚邀合作</li>
          </ul>
        </div>
        <div class="content-item">
          <div class="item-title">管理与支持</div>
          <ul>
            <li>控制台</li>
            <li>联系我们</li>
            <li>公告</li>
            <li>管理与支持</li>
            <li>管理与支持</li>
          </ul>
        </div>
      </div>-->
      <div class="footer-content-flag">
        <span>相关链接</span>
        <a v-for="item in dataList" :key="item.id" :href="item.link" target="_blank">{{ item.title }}</a>
      </div>
    </div>
    <div class="footer-flag">
      <span>Copyright 2005-2019 内蒙古气象信息中心(Inner Mongolia Meteorogical Information Certer)</span>
      <span>地址：内蒙古自治区呼和浩特市新城区海拉尔大街49号 | 邮编：010051 | 电话： 0471-3335371</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FooterPage',
  data() {
    return {
      dataList: [
        {
          id: 1,
          title: '业务内网',
          link: 'http://172.18.112.199/'
        },
        {
          id: 2,
          title: '综合信息网',
          link: 'http://172.18.112.200/'
        },
        {
          id: 3,
          title: '降水实况平台',
          link: 'http://172.18.112.53:8080/QCStatistics/sjfx_rain_real'
        },
        {
          id: 4,
          title: '沙尘暴监测平台',
          link: 'http://10.62.46.147:8080/SandDustWeb/JumpPage?pageName=sand'
        }
      ]
    }
  }
}
</script>

<style scoped lang="scss">
.app-footer {
  width: 100%;
  height: 240px;
  background-color: #242a37;
  .footer-list {
    width: 1300px;
    margin: 0 auto;
    height: 60px;
    // height: 140px;
    .list-content {
      width: 100%;
      height: auto;
      display: flex;
      justify-content: space-between;
      margin-top: 30px;
      .item-title {
        font-size: 16px;
        color: #ffffff;
        margin: 30px 0 25px 0;
      }
      ul {
        padding: 0;
        margin: 0;
      }
      ul > li {
        font-size: 14px;
        list-style-type: none;
        margin-bottom: 20px;
        color: #999999;
      }
    }
    .footer-hot {
      width: 100%;
      margin-top: 35px;
      span {
        font-size: 16px;
        color: #ffffff;
        margin-right: 60px;
      }
      a {
        font-size: 14px;
        color: #999999;
        margin-right: 30px;
      }
    }
    .footer-content-flag {
      width: 100%;
      margin-top: 55px;
      span {
        font-size: 16px;
        color: #ffffff;
        margin-right: 60px;
      }
      a {
        font-size: 16px;
        color: #ffffff;
        margin-right: 30px;
      }
    }
  }
  .footer-flag {
    width: 100%;
    margin: 0 auto;
    height: 100px;
    background-color: #172031;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: #999;
    font-size: 12px;
    letter-spacing: 1px;
    & > span:first-child {
      margin-bottom: 6px;
    }
  }
}
</style>
