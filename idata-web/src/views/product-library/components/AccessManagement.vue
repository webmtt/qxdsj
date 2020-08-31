<template>
  <div class="access-management">
    <div class="hot-content">
      <div class="hot-content-header">
        <span>准入退出管理</span>
        <!-- <span>ACCESS AND EXIT MANAGEMENT</span>
        <span>为加强全区气象部门公共气象服务产品的业务运行管理，区局减灾处制定了《全区公共气象服务产品准入退出管理办法》。准入是指新产品在具备业务化制作、服务等业务运行条件后，经审批纳入全区公共气象服务产品范围的过程，退出是指现有服务产品经审批正式退出全区公共气象服务产品范围的过程。</span> -->
      </div>
      <div class="hot-contnet-box">
        <img :src="imgUrl1" alt="">
        <img :src="imgUrl2" alt="">
        <img :src="imgUrl3" alt="">
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'AccessManagement',
  data() {
    return {
      imgUrl1: require('@/assets/images/access01.png'),
      imgUrl2: require('@/assets/images/access02.png'),
      imgUrl3: require('@/assets/images/access03.png')
    }
  },
  mounted() {
  },
  methods: {
  }
}
</script>

<style scoped  lang="scss">
.access-management {
  width: 100%;
  padding-bottom: 70px;
  background-color: #ffffff;
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
        width: 942px;
        line-height: 28px;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
      }
    }
    .hot-contnet-box {
      width: 100%;
      display: flex;
      // margin-top: 50px;
      justify-content: space-between;
      img {
        width: 387px;
        height: 334px;
      }
    }
  }
}
</style>

