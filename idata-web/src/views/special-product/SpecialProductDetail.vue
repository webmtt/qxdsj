<template>
  <div class="special-product-setail">
    <div class="special-product-setail-content">
      <div class="special-nav">
        <side-bar v-if="navList.length" :nav-list="navList" @navSelected="navSelected" />
      </div>
      <div class="special-detail-content">
        <div class="special-header">
          <div>
            <span class="header-title-line" />
            <span class="header-title-text">专题产品</span>
            <span class="header-sub-title">新的惊喜，新的期待</span>
          </div>
        </div>
        <div class="special-body">
          <div class="special-search">
            <div class="special-search-left">{{activeName}}</div>
            <div class="special-search-right">
              <span title="上一天" @click="timerChange('+')">
                <i class="el-icon-caret-left"></i>
              </span>
              <el-date-picker
                v-model="timer"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
                @change="hangletime"
              ></el-date-picker>
              <span title="下一天" @click="timerChange('-')">
                <i class="el-icon-caret-right"></i>
              </span>
              <el-button type="primary" @click="downImage">下载</el-button>
            </div>
          </div>
          <div class="img-list">
            <div class="img-list-left">
              <el-image :src="activeUrl" :preview-src-list="srcList" />
            </div>
            <div class="img-list-right" style="width: 120px">
              <el-button
                v-for="(item, index) in Imgurl"
                :key="index"
                plain
                @click="showTheImage(item)"
              >{{ item.times }}</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SideBar from "./components/SideBar.vue";

import { getServerUrl } from "@/api/user.js";
import moment from "moment";

import FileDownload from "@/utils/fileDownload";
import { getTreeProduct, getProductImg } from "@/api/specialProduct.js";

export default {
  name: "SpecialProductDetail",
  components: { SideBar },
  data() {
    return {
      baseUrl: "",
      productId: null,
      productType: null,
      Imgurl: [],
      activeId: "",
      timerNum: 1, // 前天
      timer: moment()
        .subtract(1, "days")
        .format("YYYY-MM-DD"),
      navList: [],
      srcList: [],
      timeList: [], // 时间
      activeUrl: "",
      activeName: ""
    };
  },
  created() {
    this.productId = this.$route.query.id;
    this.productType = this.$route.query.type || null;
    this.getCreatedData();
  },
  mounted() {},
  methods: {
    /*
     * 下载
     */
    downImage() {
      this.activeUrl &&
        new FileDownload().createDownloadElement(this.activeUrl, {
          download: this.activeUrl
        });
    },
    /*
     * 时间选择
     */
    showTheImage(d) {
      const index = this.srcList.findIndex(item => item === d.url);
      index > -1 && this.srcList.splice(index, 1);
      this.activeUrl = d.url;
      this.activeName = d.name;
      this.srcList.push(this.activeUrl);
    },
    hangletime() {
      const n = Date.parse(
        new Date(
          moment()
            .subtract(0, "days")
            .format("YYYY-MM-DD") + " 00:00:00"
        )
      );
      const N = Date.parse(new Date(this.timer + " 00:00:00")); //  this.timer.replace('-','').replace('-','')

      const s = n - N;
      const p = 24 * 60 * 60 * 1000;
      const d = parseInt(s / p);
      this.timerNum = d;
      this.getData();
    },
    // 改变日期
    timerChange(t) {
      this.timerNum = t === "+" ? this.timerNum + 1 : this.timerNum - 1;
      this.timer = moment()
        .subtract(this.timerNum, "days")
        .format("YYYY-MM-DD");
      this.getData();
    },
    //  点击获取图片
    handleCliceImg(item) {
      this.activeUrl = item.url;
      const index = this.srcList.findIndex(item => item === this.activeUrl);
      this.srcList.splice(index, 1);
      this.srcList.push(this.activeUrl);
    },
    getCreatedData() {
      getServerUrl().then(res => {
        if (res.code === "200") {
          this.baseUrl = res.data;
        }
      });
      this.getProductTree();
    },
    getProductTree() {
      getTreeProduct({
        id: this.productId,
        type: this.productType
      }).then(resNav => {
        if (resNav.data && resNav.data.length) {
          this.navList = resNav.data.map(item => {
            return {
              id: item.id,
              title: item.product_name,
              name: item.product_name,
              children:
                item.child && item.child.length
                  ? item.child.map(citem => {
                      return {
                        id: citem.id,
                        title: citem.product_name,
                        name: citem.product_name,
                        children:
                          citem.child && citem.child.length
                            ? citem.child.map(citemInner => {
                                return {
                                  id: citemInner.id,
                                  title: citemInner.product_name,
                                  name: citemInner.product_name,
                                  kind: citemInner.kind,
                                  procode: citemInner.procode
                                };
                              })
                            : []
                      };
                    })
                  : []
            };
          });
        }
      });
    },
    searchData() {
      this.getData();
    },
    navSelected(id) {
      this.navList.forEach(item => {
        if (item.children && item.children.length) {
          item.children.forEach(itemInner => {
            if (itemInner.children) {
              itemInner.children.forEach(itemInnerSign => {
                if (itemInnerSign.id === id) {
                  this.activeId = itemInnerSign;
                }
              });
            }
          });
        }
      });
      this.getData();
      this.Imgurl = [];
    },
    getData() {
      const obj = {
        procode: this.activeId.procode,
        kind: this.activeId.kind,
        start_time: moment()
          .subtract(this.timerNum, "days")
          .format("YYYY-MM-DD 00:00:00"),
        end_time: moment()
          .subtract(this.timerNum, "days")
          .format("YYYY-MM-DD 23:59:59")
      };
      getProductImg(obj).then(res => {
        if (res.data && res.data.length) {
          this.Imgurl = res.data.map(item => {
            return {
              id: item.id,
              url: this.baseUrl + item.d_storage_site,
              times: parseInt(item.v04004),
              name: item.v_file_name
            };
          });
          this.Imgurl.sort((a, b) => {
            return b.times - a.times;
          });
          this.Imgurl.map(item => {
            item.times =
              item.times <= 9 ? "0" + item.times + ":00" : item.times + ":00";
            return item;
          });
          this.srcList = this.Imgurl.map((item, index) => {
            if (index === 0) {
              this.activeName = item.name;
              this.activeUrl = item.url;
            }
            return item.url;
          });
        } else {
          this.Imgurl = [];
          this.srcList = [];
          this.activeUrl = null;
          this.timer = "";
          this.$message({
            message: "暂无数据",
            type: "success"
          });
        }
      });
    }
  }
};
</script>
<style >
.el-icon-circle-close {
  color: #ffffff;
}
</style>
<style>
.el-carousel__item,
.el-image {
  overflow: auto;
}
</style>

<style scoped  lang="scss">
.special-product-setail {
  width: 100%;
  height: auto;
  min-height: calc(100vh - 100px);
  padding-bottom: 30px;
  .special-product-setail-content {
    min-width: 1200px;
    max-width: 1920px;
    margin: 0 auto;
    min-height: calc(100vh - 100px);
    display: flex;
    justify-content: flex-start;
    .special-nav {
      width: 15%;
      min-height: calc(100vh - 100px);
      background-color: #31394b;
      overflow: hidden;
    }
    .special-detail-content {
      width: 85%;
      height: auto;
      background-color: #f1f6fa;
      padding: 0 30px;
      .special-header {
        width: 100%;
        height: 80px;
        line-height: 80px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .header-title-line {
          display: inline-block;
          position: relative;
          top: 3px;
          width: 4px;
          height: 20px;
          background-color: #24d2f0;
          margin-right: 20px;
        }
        .header-title-text {
          color: #333;
          font-size: 20px;
          font-weight: bolder;
          letter-spacing: 2px;
        }
        .header-sub-title {
          margin-left: 40px;
          margin-top: 4px;
          color: #999;
          font-size: 16px;
          letter-spacing: 2px;
        }
      }
      .special-body {
        width: 100%;
        min-height: 300px;
        background-color: #ffffff;
        .special-search {
          width: 100%;
          height: 50px;
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 0 20px;
          border-bottom: 1px solid #f2f2f2;
          .special-search-right {
            span {
              display: inline-block;
              width: 30px;
              height: 30px;
              border: 1px solid #f2f2f2;
              border-right: 4px;
              text-align: center;
              line-height: 30px;
              margin: 0 6px;
              background-color: #f2f2f2;
              cursor: pointer;
              &:hover {
                i {
                  color: #1890ff;
                }
              }
            }
          }
        }
        .img-list {
          // width: 100%;
          // min-height: 110vh;
          display: flex;
          justify-content: flex-start;
          flex-wrap: wrap;
          .img-list-left {
            flex: 1;
            min-height: 600px;
            padding: 10px;
            overflow-y: auto;
            &::-webkit-scrollbar-track-piece {
              background: #d3dce6;
            }

            &::-webkit-scrollbar {
              width: 6px;
            }

            &::-webkit-scrollbar-thumb {
              background: #99a9bf;
              border-radius: 20px;
            }
            // & > div {
            //   display: flex;
            //   flex-direction: column;
            //   justify-content: flex-start;
            //   align-items: center;
            //   width: 120px;
            //   margin: 10px;
            // }
            // & > img {
            //   width: 100px;
            //   cursor: pointer;
            // }
          }
          .img-list-right {
            padding: 10px;
            // display: flex;
            // flex-direction: column;
            // justify-content: start;
            // align-items: flex-end;
            overflow-y: auto;
            &::-webkit-scrollbar-track-piece {
              background: #d3dce6;
            }

            &::-webkit-scrollbar {
              width: 6px;
            }

            &::-webkit-scrollbar-thumb {
              background: #99a9bf;
              border-radius: 20px;
            }
            & > button {
              // margin-bottom: 10px;
              display: block;
              margin: 0 auto 10px;
            }
            // flex: 1;
          }
        }
      }
    }
  }
}
</style>
