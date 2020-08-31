<template>
  <div class="server-products-definition">
    <div class="hot-content">
      <div class="hot-content-header">
        <span>命名规范</span>
        <!-- <span>SERVICE PRODUCTS DEFINITION</span>
        <span>服务产品是指全区各级气象部门制作或联合其他部门制作的可以公开的服务产品，</span>
        <span>主要包括实况监测产品、预报预测预警产品和公共气象服务产品三大类。</span> -->
      </div>
      <div class="hot-contnet-box">
        <img class="server-banner" :src="bannnerImgUrl" alt="">
        <img class="server-time-line" :src="timelineImgUrl" alt="">
        <div class="server-list">
          <div>
            <span>01</span>
            <p>为规范公共气象服务产品文件的命名，实现全区公共气象服务产品的自动传输、存储、检索、共享，参照《全国公共气象服务产品库产品文件命名规范》，结合内蒙古实际，区局减灾处制定了《内蒙古自治区公共气象服务产品文件命名规范（试行）》。</p>
          </div>
          <div>
            <span>02</span>
            <p>气象服务产品文件名包括：气象服务产品标识、产品制作单位、产品业务门类、产品内容、产品内容地理高度、产品覆盖区域、产品起始时间、产品内容起止时间和产品存储格式九段内容，产品文件命名一律使用大写字母和数字。</p>
          </div>
          <div>
            <span>03</span>
            <p>文件命名表达式如下：气象服务产品标识_产品制作单位_产品业务门类_产品内容_产品内容地理高度_产品覆盖区域_产品起始时间_产品内容起止时间.产品存储格式。</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'ServerProductsDefinition',
  data() {
    return {
      bannnerImgUrl: require('@/assets/images/pic.png'),
      timelineImgUrl: require('@/assets/images/timeLine.png')
    }
  },
  mounted() {
  },
  methods: {
  }
}
</script>

<style scoped  lang="scss">
.server-products-definition {
  width: 100%;
  padding-bottom: 70px;
  .hot-content {
    width: 1300px;
    margin: 0 auto;
    .hot-content-header {
      width: 100%;
      height: 100px;
      display: flex;
      justify-content: center;
      align-items: center;
      & > span:first-child {
        color: #333;
        font-size: 24px;
        // margin-top: 67px;
        font-weight: bolder;
        // padding-bottom: 15px;
        position: relative;
        &::after {
          content: '';
          position: absolute;
          left: -22px;
          top: 2px;
          width: 4px;
          height: 24px;
          background-color: #24d2f0;
        }
      }
      & > span:nth-child(2) {
        font-size: 20px;
        color: #666;
      }
      & > span:nth-child(3) {
        margin-top: 25px;
        font-size: 16px;
        color: #666;
      }
      & > span:nth-child(4) {
        margin-top: 4px;
        font-size: 16px;
        color: #666;
      }
    }
    .hot-contnet-box {
      width: 100%;
      display: flex;
      // margin-top: 43px;
      justify-content: flex-start;
      align-items: center;
      .server-banner {
        width: 320px;
        height: 320px;
      }
      .server-time-line {
        width: 28px;
        height: 244px;
        margin-left: 64px;
      }
      .server-list {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        height: 320px;
        flex: 1;
        & > div {
          display: flex;
          justify-content: flex-start;
          align-items: flex-start;
          height: 80px;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 3;
          // overflow: hidden;
          span {
            color: #7a7a7a;
            font-size: 28px;
            padding: 0 30px;
          }
          p {
            margin: 0;
            font-size: 16px;
            color: #333;
            line-height: 28px;
            overflow: hidden;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
          }
        }
        & > div:nth-child(1) {
          margin-top: 36px;
        }
        & > div:nth-child(2) {
          margin-top: 44px;
        }
        & > div:nth-child(3) {
          margin-top: 40px;
        }
      }
    }
  }
}
</style>

