<template>
  <div class="data-serve-list">
    <div class="data-serve data-up">
      <div class="data-serve-header">
        <span>数据服务</span>
        <el-button type="success" size="mini" @click="getMoreData('a')">更多</el-button>
      </div>
      <div v-for="(item, index) in cateList" :key="index" class="data-serve-content" @click="toDataDown(item)">
        <div>
          <div class="left-data">
            <span class="left-data-name">{{item.chnname}}</span>
            <span class="left-data-time">{{item.timeseq}}</span>
          </div>
          <div>{{item.datacount}}种</div>
        </div>
      </div>
    </div>
    <div class="data-serve data-down">
      <div class="data-serve-header">
        <span>最新上线(数据服务)</span>
        <el-button type="success" size="mini" @click="getMoreData('b')">更多</el-button>
      </div>
      <div class="api-list">
        <div
          v-for="(item, index) in dataList"
          :key="index"
          class="api-list-item"
          @click="toApiDown(item)"
        >
          <span>{{ item.CUSTOM_API_NAME }}</span>
          <!-- <span>({{ item.GENERAL_API_ID }})</span> -->
          <span>{{ item.PUBLISH_TIME }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getNewInterface, getDataSortCount } from "@/api/home.js";
export default {
  name: "DataServeList",
  data() {
    return {
      dataList: [],
      cateList: []
    };
  },
  created() {
    this.getMoreNewInterface();
    this.getSortCount();
  },
  methods: {
    getSortCount() {
      getDataSortCount().then(res => {
        if (res.data && res.data.length) {
          this.cateList = res.data;
        } else {
          this.cateList = [];
        }
      });
    },
    getMoreNewInterface() {
      getNewInterface().then(res => {
        if (res.data && res.data.length) {
          this.dataList = res.data;
        }
      });
    },
    // 数据服务点击跳转
    toDataDown(d) {
      this.$router.push({ path: `/dataDown`, query: { id: d.categoryid }})
      // this.$router.push({ path: `/dataDown`, query: { id: d.categoryid } });
    },
    // 接口点击跳转
    toApiDown(d) {
      if (!this.$store.state.user.chName) {
        this.$confirm("您还没有登录, 是否前往登录?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.$router.push({
              path: `/api`,
              query: { id: d.id, parentId: d.parentId }
            });
          })
          .catch(() => {
            // this.$message({
            //   type: 'info',
            //   message: '已取消删除'
            // })
          });
      } else {
        this.$router.push({
          path: `/api`,
          query: { id: d.id, parentId: d.parentId }
        });
      }
    },
    // 更多
    getMoreData(a) {
      if (a === "a") {
        this.$router.push({ path: `/dataDown` });
      } else {
        this.$router.push({ path: `/api` });
      }
    }
  }
};
</script>

<style scoped lang="scss">
.data-serve-list {
  width: 420px;
  .data-serve {
    padding: 0 20px;
  }
  .data-up {
    width: 100%;
    height: 440px;
    margin-bottom: 20px;
    border: 1px solid #dadde6;
    overflow: hidden;
  }
  .data-down {
    width: 100%;
    height: 210px;
    border: 1px solid #dadde6;
  }
  .data-serve-header {
    width: 100%;
    height: 50px;
    border-bottom: 1px solid #dadde6;
    margin-right: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    span {
      color: #3d7dea;
      font-weight: bolder;
      font-size: 20px;
      position: relative;
      &::after {
        content: "";
        position: absolute;
        width: 100%;
        height: 2px;
        left: 0;
        top: 34px;
        background-color: #3d7dea;
      }
    }
  }
  .data-serve-content {
    display: flex;
    flex-direction: column;
    justify-self: start;
    // height: 100%;
    overflow: hidden;
    cursor: pointer;
    // padding-left: 20px;
    & > div {
      height: 30px;
      margin-bottom: 6px;
      border-bottom: 1px solid #f2f2f2;
      display: flex;
      justify-content: space-between;
      align-items: center;
      overflow: hidden;
      padding-left: 12px;
      padding-right: 4px;
      font-size: 14px;
      color: #555;
      position: relative;
      cursor: pointer;
      .left-data {
        display: flex;
        align-items: center;
        .left-data-time {
          color: #999;
        }
        .left-data-name{
          display: inline-block;
          width: 120px;
          // overflow: hidden;
        }
      }
      &::after {
        position: absolute;
        content: "";
        width: 6px;
        height: 6px;
        border-radius: 50%;
        background-color: #999;
        left: 0px;
        top: 10px;
      }
      &:hover {
        background-color: #f2f2f2;
      }
    }
    // .content-header {
    //   width: 100%;
    //   height: 50px;
    //   line-height: 50px;
    //   font-weight: bold;
    //   .content-header-title {
    //     font-size: 16px;
    //     color: #333;
    //   }
    //   .content-header-sub {
    //     margin-left: 50px;
    //     font-size: 14px;
    //     span {
    //       color: #a02f1c;
    //     }
    //   }
    // }
    // .content-list {
    //   display: flex;
    //   justify-content: flex-start;
    //   flex-wrap: wrap;
    //   & > span {
    //     cursor: pointer;
    //     width: 33%;
    //     height: 20px;
    //     margin-bottom: 10px;
    //     position: relative;
    //     overflow: hidden;
    //     padding-left: 12px;
    //     color: #999999;
    //     font-size: 14px;
    //     &::after {
    //       position: absolute;
    //       content: "";
    //       width: 6px;
    //       height: 6px;
    //       border-radius: 50%;
    //       background-color: #999;
    //       left: 0;
    //       top: 6px;
    //     }
    //   }
    // }
  }
  .api-list {
    width: 100%;
    height: 150px;
    overflow: hidden;
    padding-top: 4px;
    .api-list-item {
      width: 100%;
      height: 28px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: #999;
      font-size: 14px;
      cursor: pointer;
      & > span:first-child {
        flex: 1;
        position: relative;
        padding-left: 14px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        &::after {
          content: "";
          position: absolute;
          left: 0;
          top: 6px;
          width: 6px;
          height: 6px;
          border-radius: 50%;
          background-color: #999;
        }
      }
      & > span:last-child {
        width: 100px;
        text-align: right;
      }
    }
  }
}
</style>
<style>
.data-serve-header .el-button--success {
  background-color: #3d7dea;
  border: none;
}
</style>
