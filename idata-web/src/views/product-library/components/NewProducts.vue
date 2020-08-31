<template>
  <div class="new-product">
    <div class="hot-content">
      <div class="hot-content-header">
        <span>最新产品</span>
        <!-- <span>LATEST PRODUCTS DISPLAY</span> -->
      </div>
      <div class="hot-contnet-box">
        <div class="new-content">
          <div class="new-title">
            <span>{{ product }}</span>
            <el-button
              size="mini"
              type="primary"
              @click="productListDetails"
            >更多</el-button>
          </div>
          <div class="news-list">
            <ul>
              <li
                v-for="(item, index) in productList"
                :key="index"
                @click="handlePreview(item)"
              >
                <span>
                  {{ item.fileshowname }}
                  <svg
                    v-if="item.isFlag"
                    t="1592373035895"
                    class="icon"
                    viewBox="0 0 1024 1024"
                    version="1.1"
                    xmlns="http://www.w3.org/2000/svg"
                    p-id="2363"
                    xmlns:xlink="http://www.w3.org/1999/xlink"
                    width="20"
                    height="20"
                    style="position: relative;top: 4px;"
                  >
                    <path
                      d="M 860.4 263.1 H 163.6 c -54.8 0 -99.6 44.8 -99.6 99.6 v 298.7 c 0 54.8 44.8 99.6 99.6 99.6 h 696.9 c 54.8 0 99.6 -44.8 99.6 -99.6 V 362.7 c -0.1 -54.8 -44.9 -99.6 -99.7 -99.6 Z M 359.2 627.6 h -41.9 L 206.8 457.7 c -2.8 -4.3 -5.1 -8.8 -6.9 -13.4 h -1 c 0.9 4.9 1.3 15.5 1.3 31.8 v 151.5 h -37.1 V 396.4 h 44.7 l 106.7 165.9 c 4.5 6.9 7.4 11.6 8.7 14.2 h 0.6 c -1.1 -6.1 -1.6 -16.5 -1.6 -31.1 v -149 h 36.9 v 231.2 Z m 168.3 0 H 397.7 V 396.4 h 124.6 V 429 H 436 v 65.4 h 79.5 v 32.4 H 436 v 68.4 h 91.4 v 32.4 Z m 270 0 h -43.4 l -44 -161.7 c -1.8 -6.9 -3 -14.4 -3.4 -22.6 h -0.6 c -0.5 7.6 -1.8 15 -3.9 22.2 l -44.2 162 h -44.3 L 549 396.3 h 42.1 L 633 565.6 c 1.7 7.1 2.8 14.5 3.2 22.2 h 0.8 c 0.4 -5.5 1.9 -12.9 4.5 -22.2 L 689 396.4 h 39.5 L 772.4 567 c 1.5 5.8 2.6 12.7 3.4 20.6 h 0.6 c 0.3 -5.4 1.6 -12.5 3.7 -21.3 l 41.1 -169.9 h 39.7 l -63.4 231.2 Z"
                      fill="#d81e06"
                      p-id="2364"
                    />
                  </svg>
                </span>
                <span>{{ item.created }}</span>
              </li>
            </ul>
          </div>
        </div>
        <div class="new-content">
          <div class="new-title">
            <span>{{ unitType }}</span>
            <el-button
              size="mini"
              type="primary"
              @click="unitTypeListDetails"
            >更多</el-button>
          </div>
          <div class="news-list">
            <ul>
              <li
                v-for="(item, index) in unitTypeList"
                :key="index"
                @click="handlePreview(item)"
              >
                <span>
                  {{ item.fileshowname }}
                  <svg
                    v-if="item.isFlag"
                    t="1592373035895"
                    class="icon"
                    viewBox="0 0 1024 1024"
                    version="1.1"
                    xmlns="http://www.w3.org/2000/svg"
                    p-id="2363"
                    xmlns:xlink="http://www.w3.org/1999/xlink"
                    width="20"
                    height="20"
                    style="position: relative;top: 4px;"
                  >
                    <path
                      d="M 860.4 263.1 H 163.6 c -54.8 0 -99.6 44.8 -99.6 99.6 v 298.7 c 0 54.8 44.8 99.6 99.6 99.6 h 696.9 c 54.8 0 99.6 -44.8 99.6 -99.6 V 362.7 c -0.1 -54.8 -44.9 -99.6 -99.7 -99.6 Z M 359.2 627.6 h -41.9 L 206.8 457.7 c -2.8 -4.3 -5.1 -8.8 -6.9 -13.4 h -1 c 0.9 4.9 1.3 15.5 1.3 31.8 v 151.5 h -37.1 V 396.4 h 44.7 l 106.7 165.9 c 4.5 6.9 7.4 11.6 8.7 14.2 h 0.6 c -1.1 -6.1 -1.6 -16.5 -1.6 -31.1 v -149 h 36.9 v 231.2 Z m 168.3 0 H 397.7 V 396.4 h 124.6 V 429 H 436 v 65.4 h 79.5 v 32.4 H 436 v 68.4 h 91.4 v 32.4 Z m 270 0 h -43.4 l -44 -161.7 c -1.8 -6.9 -3 -14.4 -3.4 -22.6 h -0.6 c -0.5 7.6 -1.8 15 -3.9 22.2 l -44.2 162 h -44.3 L 549 396.3 h 42.1 L 633 565.6 c 1.7 7.1 2.8 14.5 3.2 22.2 h 0.8 c 0.4 -5.5 1.9 -12.9 4.5 -22.2 L 689 396.4 h 39.5 L 772.4 567 c 1.5 5.8 2.6 12.7 3.4 20.6 h 0.6 c 0.3 -5.4 1.6 -12.5 3.7 -21.3 l 41.1 -169.9 h 39.7 l -63.4 231.2 Z"
                      fill="#d81e06"
                      p-id="2364"
                    />
                  </svg>
                </span>
                <span>{{ item.created }}</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <!-- 点击更多显示弹框 -->
    <el-dialog
      :title="showProduct==='1'?'最新预警产品':'最新预报产品'"
      :visible.sync="dialogTableVisible"
    >
      <el-table
        :data="gridData"
        @row-click="dataDetailsEvent"
      >
        <el-table-column
          v-for="(item, index) in tableMeta"
          :key="index"
          align="center"
          :prop="item.key"
          :label="item.label"
          :width="item.width"
        />
        <!-- <el-table-column property="fileshowname" label="最新预警产品" />
        <el-table-column property="created" label="时间" width="300" />-->
      </el-table>
      <pagination
        v-show="totalProduct > 0"
        :total="showProduct==='1'?Number(totalProduct):Number(totalUnitType)"
        :page.sync="pageNum"
        :limit.sync="pageSize"
        @pagination="paginationChange"
      />
    </el-dialog>
    <el-dialog
      class="dialog-iframe"
      :visible.sync="dialogIframe"
      width="90vw"
      top="2vh"
    >
      <div class="dialog-iframe-title">
        <div>
          <span>
            作者：<span>{{dataDetail.name}}</span>
          </span>
          <span>
            发布时间：<span>{{dataDetail.created}}</span>
          </span>
          <span>
            查看次数：<span>{{dataDetail.HITS + 1}}次数</span>
          </span>
          <span>
            制作单位：<span>{{dataDetail.fullName}}</span>
          </span>
        </div>
        <div>
          <el-button
            type="success"
            size="small"
            @click="handledownload"
          >下载</el-button>
        </div>
      </div>
      <iframe
        v-if="iframeUrl"
        :src="iframeUrl"
        frameborder="1"
        width="100%"
        height="95%"
      />
      <img
        v-if="iframeimgUrl"
        :src="iframeimgUrl"
        alt
        style="width:100%; height:100%"
      />
    </el-dialog>
  </div>
</template>

<script>

import FileDownload from "@/utils/fileDownload";
import { getNewUnits, statisticsUnits } from "@/api/productLibrary.js";
import pagination from "@/components/Pagination";
import moment from "moment";
export default {
  name: "NewProducts",
  components: { pagination },
  data() {
    return {
      product: "",
      unitType: "",
      productList: [],
      currentPage4: 4,
      unitTypeList: [],
      gridData: [],
      dialogIframe: false,
      iframeUrl: null,
      iframeimgUrl: null,
      dialogTableVisible: false,
      pageNum: 1,
      pageSize: 8,
      totalProduct: "",
      totalUnitType: "",
      showProduct: "",
      tableMeta: [
        { key: "fileshowname", label: "最新预警产品" },
        { key: "created", label: "时间" }
      ],
      productListDetail: [],
      unitTypeListDetail: [],
      dataDetail: {
        name:'未知',
        fullName:'未知',
      },
      download: null
    };
  },

  created() {
    this.getData()
  },
  methods: {
    productListDetails() {
      this.dialogTableVisible = true
      this.showProduct = "1"
      this.pageNum = 1
      this.getDataDetails()
    },
    unitTypeListDetails() {
      this.showProduct = "2"
      this.dialogTableVisible = true
      this.pageNum = 1
      this.getDataDetails()
    },
    paginationChange() {
      this.getDataDetails()
    },
    // 获取数据
    getData() {
      getNewUnits({ pageNum: this.pageNum, pageSize: this.pageSize }).then(
        res => {
          this.product = res.data.product
          this.unitType = res.data.unitType
          this.productList = res.data.productList.list.map(item => {
            const old =
              parseInt(new Date(item.created).getTime()) + 8 * 60 * 60 * 1000
            item.created = moment(old).format("YYYY-MM-DD HH:mm:ss")

            const a = parseInt(
              item.created
                .replace(/-/g, "")
                .replace(/:/g, "")
                .replace(" ", "")
            )
            const b = parseInt(moment().format("YYYYMMDD000000`"))
            // 小于1天，就显示最新
            item["isFlag"] = a - b < 1000000 && a - b > 0
            return item
          })
          this.unitTypeList = res.data.unitTypeList.list.map(item => {
            const old =
              parseInt(new Date(item.created).getTime()) + 8 * 60 * 60 * 1000
            item.created = moment(old).format("YYYY-MM-DD HH:mm:ss")
            const a = parseInt(
              item.created
                .replace(/-/g, "")
                .replace(/:/g, "")
                .replace(" ", "")
            )
            const b = parseInt(moment().format("YYYYMMDD000000`"))
            // 小于1天，就显示最新
            item["isFlag"] = a - b < 1000000 && a - b > 0
            return item
          })
        }
      )
    },
    getDataDetails() {
      this.gridData = []
      getNewUnits({ pageNum: this.pageNum, pageSize: this.pageSize }).then(
        res => {
          this.productListDetail = res.data.productList.list
          this.totalProduct = res.data.productList.total
          this.totalUnitType = res.data.unitTypeList.total
          this.unitTypeListDetail = res.data.unitTypeList.list
          if (this.showProduct === "1") {
            this.productListDetail.map(item => {
              this.gridData.push({
                fileshowname: item.fileshowname,
                created: item.created,
                url: item.url
              })
            })
          } else if (this.showProduct === "2") {
            this.unitTypeListDetail.map(item => {
              this.gridData.push({
                fileshowname: item.fileshowname,
                created: item.created,
                url: item.url
              })
            })
          }
        }
      );
    },
    // 跳转到产品详情页面
    handlePreview(item) {
      this.dataDetail = {...this.dataDetail, ...item}
      statisticsUnits({
        id: item.ID,
        createdby: item.CREATEDBY
      }).then(res => {
        this.dataDetail['name'] = res.data.realName || '未知'
        this.dataDetail['fullName'] = res.data.fullName || '未知'
      }).catch(() => {
        this.dataDetail['name'] = '未知'
        this.dataDetail['fullName'] = '未知'
      })
      let docUrl = item.url
      this.download = item.url
      const wacServer = "172.18.112.96"
      const docName =
        "http://172.18.112.129/productdb/proDir1/MSP2_WF/MO/20200611/0a5c000a-560d-48fa-a8a5-f5f21808ea40.DOCX"
      const docUrls = item.url.split(".");
      const suffix = docUrls[docUrls.length - 1].toLowerCase();
      docUrl =
        "http://172.18.112.103:8080/wopi-api/files/@/" + encodeURI(docUrl)
      if (suffix === "doc" || suffix === "docx") {
        docUrl =
          "http://" +
          wacServer +
          "/wv/wordviewerframe.aspx?WOPISrc=" +
          encodeURI(docUrl)
      }
      if (suffix === "pdf") {
        docUrl =
          "http://" +
          wacServer +
          "/wv/wordviewerframe.aspx?PdfMode=1&WOPISrc=" +
          encodeURI(docUrl)
      }
      if (suffix === "xls" || suffix === "xlsx") {
        docUrl =
          "http://" +
          wacServer +
          "/x/_layouts/xlviewerinternal.aspx?WOPISrc=" +
          encodeURI(docUrl)
      }
      if (suffix === "ppt" || suffix === "pptx") {
        docUrl =
          "http://" +
          wacServer +
          "/p/PowerPointFrame.aspx?PowerPointView=ReadingView&WOPISrc=" +
          encodeURI(docUrl)
      }
      if (
        suffix === "bmp" ||
        suffix === "jpg" ||
        suffix === "jpeg" ||
        suffix === "png" ||
        suffix === "gif"
      ) {
        this.iframeimgUrl = docName;
      } else if (
        suffix === "doc" ||
        suffix === "docx" ||
        suffix === "ppt" ||
        suffix === "pptx" ||
        suffix === "xls" ||
        suffix === "xlsx" ||
        suffix === "pdf"
      ) {
        this.iframeUrl = docUrl
      } else if (suffix === "txt" || suffix === "TXT") {
        this.iframeUrl = docName
      }

      this.dialogIframe = true
      // w.location.href = item.url
    },
    dataDetailsEvent(it) {
      const filterData = this.gridData.filter(
        item => item.fileshowname === it.fileshowname
      );
      const filterUrl = window.open("")
      filterUrl.location.href = filterData[0].url
    },
    // 下载
    handledownload() {
      new FileDownload().fileBlodDownload(this.download)
    }
  }
}
</script>

<style>
.dialog-iframe .el-dialog {
  min-height: 90vh;
}
.dialog-iframe .el-dialog__body {
  height: 90vh;
}
.dialog-iframe-title {
  width: 100%;
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.dialog-iframe-title > div > span {
  margin-right: 30px;
  color: #555;
  font-weight: bold;
}
.dialog-iframe-title > div > span > span {
  color: #999999;
  margin-left: 10px;
  font-weight: normal;
}
</style>
<style scoped  lang="scss">
.new-product {
  width: 100%;
  height: 480px;
  background-color: #f5f6fa;
  .hot-content {
    width: 1300px;
    margin: 0 auto;
    .hot-content-header {
      width: 100%;
      height: 100px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      & > span:first-child {
        color: #333;
        font-size: 24px;
        margin-top: 10px;
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
      // & > span:last-child {
      //   font-size: 20px;
      //   color: #666;
      // }
    }
    .hot-contnet-box {
      width: 100%;
      display: flex;
      justify-content: space-between;
      .new-content {
        border: 1px solid #dadde6;
        border-radius: 4px;
        width: 630px;
        height: 320px;
        padding: 0 20px;
        background-color: white;
        .new-title {
          display: flex;
          width: 100%;
          height: 50px;
          justify-content: space-between;
          align-items: center;
          border-bottom: 1px solid #dadde6;
          span {
            font-size: 16px;
            color: #3d7dea;
            position: relative;
            font-weight: bold;
            &::after {
              position: absolute;
              content: "";
              left: 0;
              top: 32px;
              width: 76px;
              height: 2px;
              background-color: #3d7dea;
            }
          }
        }
        .news-list {
          width: 100%;
          ul {
            width: 100%;
            padding: 10px 0px 5px 0px;
            margin: 0;
            // padding-left: 15px;
            li {
              width: 100%;
              height: 30px;
              line-height: 30px;
              font-size: 14px;
              color: #666;
              display: flex;
              justify-content: space-between;
              align-items: center;
              cursor: pointer;
              & > span:first-child {
                flex: 1;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
                padding-left: 12px;
                position: relative;
                &::after {
                  position: absolute;
                  content: "";
                  left: 0;
                  top: 13px;
                  width: 4px;
                  height: 4px;
                  border-radius: 50%;
                  background-color: #666;
                }
              }
              & > span:last-child {
                width: 140px;
                overflow: hidden;
                text-align: right;
              }
              &:hover {
                color: #24d2f0;
              }
              &:hover > span:first-child {
                &::after {
                  background-color: #24d2f0;
                }
              }
            }
          }
        }
      }
    }
  }
}
</style>
<style>
.new-product .el-button--primary {
  background-color: #3d7dea;
}
</style>

