<template>
  <div class="default-info">
    <div class="body-info">
      <img :src="layImgurl" alt="" srcset="">
      <div class="info-content">
        <p>内蒙古气象信息中心作为内蒙古气象学科的数据中心， 负责承担内蒙古和全球范围的气象数据及其产品的收集、处理、存储、检索和服务； 研究与应用最新数据处理技术；加工和开发各类气象数据产品； 承担内蒙古气象档案馆的任务职责，负责内蒙古气象记录档案和工作档案的收集、归档、管理和服务； 承担数据和档案业务对省级的技术指导。依据中国气象局“气象资料共享管理办法”的规定， 根据不同用户需求，向国内外提供各类气象数据及其产品的共享服务。
        </p>
        <p>数据产品下载服务栏目基于CIMISS统一数据环境， 提供国家气象信息中心收集和管理的各类实时和非实时气象资料， 以及各类数据加工产品的在线查询和下载服务。 数据种类覆盖气象行业标准《气象资料分类与编码》 （QX/T102-2009）全部14大类资料，并提供便捷的数据分类导航、定位和数据检索下载服务。
        </p>

        <ul>
          <!-- <li>
            <span>1) 检索定制：</span>基于订单方式提供大数据下载服务，用户可以通过检索获取相应的结果内容并将提交订单，系统自动获取和生成订单文件供用户FTP下载</li>
          <li>
            <span>2) 直接下载：</span>用户可以分目录通过FTP服务直接下载存储在文件系统的数据及文件</li> -->
          <!-- <li> -->
            <!-- <span>API接口：</span>为第三方应用程序提供各类气象资料的数据接口服务</li> -->
        </ul>
      </div>
    </div>
    <div class="info-list">
      <div v-for="item in dataList" :key="item.id" class="info-item">
        <img src="" alt="" srcset="">
        <span>{{ item.title }}</span>
      </div>

    </div>
  </div>
</template>

<script>
export default {
  name: 'DefaultInfo',
  props: {
    dataList: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      layImgurl: require('@/assets/images/lay.png')
    }
  }
}
</script>

<style scoped  lang="scss">
.default-info {
  width: 100%;
  .body-info {
    width: 100%;
    min-height: 300px;
    border-bottom: 1px solid #bfbfbf;
    display: flex;
    justify-content: flex-start;
    padding: 30px;
    img {
      width: 26px;
      height: 26px;
      margin-right: 20px;
    }
    .info-content {
      p {
        text-indent: 36px;
        padding: 0;
        margin: 0;
        font-size: 14px;
        color: #333;
        line-height: 30px;
        margin-bottom: 10px;
      }
      ul {
        margin: 0;
        padding: 0;
        margin-top: 20px;
        li {
          color: #333;
          list-style: none;
          font-size: 14px;
          height: 30px;
          line-height: 30px;
          & > span:first-child {
            color: #0d95ff;
          }
        }
      }
    }
  }
  .info-list {
    width: 100%;
    padding: 30px 90px;
    min-height: 300px;
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
    .info-item {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      margin-right: 6%;
      // width: 90px;
      margin-bottom: 20px;
      img {
        width: 90px;
        height: 90px;
      }
      span {
        display: inline-block;
        width: 100%;
        text-align: center;
        margin-top: 18px;
        color: #666;

        font-size: 16px;
      }
    }
  }
  & > .info-item:nth-child(6n) {
    margin-right: 0px;
  }
}
</style>
