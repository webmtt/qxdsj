<template>
  <div class="api-server">
    <div class="api-server-content">
      <div class="hot-content-header">
        <span>我的服务</span>
        <span>数据、算法、接口、加工流水线全涵盖，一站式为您服务</span>
      </div>
      <div class="server-content">
        <div
          v-for="item in dataList"
          :key="item.id"
          class="api-item"
        >
          <img :src="item.imgUrl">
          <div>{{ item.title }} </div>
          <p>{{ item.content }}</p>
          <span>
            <el-button
              type=""
              size="mini"
              @click="handleSave"
            >展开</el-button>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ApiServer',
  data() {
    return {
      dataList: [
        {
          id: 1,
          title: '数据',
          content: '涵盖观测监测、考察调查、科学研究、试验开发、生产分析、行业交换等方式，获得的大气科学技术领域各种气象数据，涵盖数据和产品。',
          imgUrl: require('@/assets/images/homeAPi1.png'),
          link: ''
        },
        {
          id: 2,
          title: '算法',
          content: '基于中国气象局颁布实施的气象数据算法及开发规范和标准，提供更多气象数据加工流水线算法服务',
          imgUrl: require('@/assets/images/homeAPi2.png'),
          link: ''
        },
        {
          id: 3,
          title: '接口',
          content: '基于中国气象局和各省气象局统一数据环境、统一服务模式，为用户系统提供直接的数据接入支撑服务',
          imgUrl: require('@/assets/images/homeAPi3.png'),
          link: ''
        },
        {
          id: 4,
          title: '典型应用',
          content: '提供系统服务、数据获取、算法应用的示例，便利您的使用',
          imgUrl: require('@/assets/images/homeAPi4.png'),
          link: ''
        }
      ]
    }
  },
  methods: {
    handleSave() {
      if (!this.$store.state.user.chName) {
        this.$confirm('您还没有登录, 是否前往登录?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$router.push({ path: `/api` })
        }).catch(() => {
          // this.$message({
          //   type: 'info',
          //   message: '已取消删除'
          // })
        })
      } else {
        this.$router.push({ path: `/api` })
      }
    }
  }
}
</script>

<style scoped  lang="scss">
.api-server {
  width: 100%;
  height: 600px;
  background-color: #f5f6fa;
  .api-server-content {
    width: 1300px;
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
    .server-content {
      width: 100%;
      height: 380px;
      align-items: center;
      display: flex;
      justify-content: space-between;
    }
    .api-item {
      width: 310px;
      height: 100%;
      background-color: #ffffff;
      border: 1px solid #e2eefa;
      display: flex;
      flex-direction: column;
      align-items: center;
      position: relative;
      img {
        width: 65px;
        height: 65px;
        margin-top: 50px;
      }
      div {
        font-size: 18px;
        color: #333333;
        margin-top: 25px;
        text-align: center;
        padding-bottom: 10px;
        position: relative;
        font-weight: bolder;
        &::after {
          content: "";
          position: absolute;
          width: 36px;
          height: 2px;
          background-color: #e84c3d;
          top: 100%;
          left: 50%;
          transform: translateX(-50%);
        }
      }
      p {
        padding: 15px 32px;
        font-size: 14px;
        color: #666;
        line-height: 20px;
        letter-spacing: 1px;
      }
      span {
        position: absolute;
        bottom: 60px;
        display: inline-block;
        width: 100%;
        text-align: left;
        padding-left: 32px;
        // margin-top: 5px;
      }
    }
  }
}
</style>
<style>
.api-item button {
  border-color: #0d95ff;
  color: #0d95ff;
}
</style>

