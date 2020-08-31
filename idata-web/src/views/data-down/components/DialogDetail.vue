<template>
  <div class="dialog-detail">
    <div class="detail-content">
      <div class="detail-box">
        <img
          :src="imgList[0]"
          alt
        />
        <div class="detail-info">
          <div class="title">{{ chnname }}</div>
          <div class="detail-gaikuang">
            <div class="sub-title">1. 概况</div>
            <p v-html="dataInfo" />
            <div class="sub-title">2. 数据源</div>
            <p>{{ datasourcecode || '空'}}</p>
          </div>
        </div>
      </div>
      <!-- <div style="text-align: center;">
        <el-button type="success">
          <i class="el-icon-set-up" /> MUSIC接口</el-button>
      </div>-->
      <div class="detail-box">
        <img
          :src="imgList[1]"
          alt
        />
        <div class="detail-info">
          <div class="title">元数据基本信息</div>
          <div class="info-list">
            <div
              v-for="item in originalData"
              :key="item.id"
            >
              <span>{{ item.title }}：</span>
              <span>{{ item.val }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="detail-box">
        <img
          :src="imgList[2]"
          alt
        />
        <div class="detail-info">
          <div class="title">地理覆盖范围</div>
          <div class="info-list">
            <div
              v-for="item in geographicData"
              :key="item.id"
            >
              <span>{{ item.title }}：</span>
              <span>{{ item.val }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="detail-box">
        <img
          :src="imgList[3]"
          alt
        />
        <div class="detail-info">
          <div class="title">时间覆盖范围</div>
          <div class="info-list">
            <div
              v-for="item in timerData"
              :key="item.id"
            >
              <span>{{ item.title }}：</span>
              <span>{{ item.val }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="detail-box">
        <img
          :src="imgList[4]"
          alt
        />
        <div class="detail-info">
          <div class="title">联系方法</div>
          <div class="info-list">
            <div
              v-for="item in contentData"
              :key="item.id"
            >
              <span>{{ item.title }}：</span>
              <span>{{ item.val }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="detail-box">
        <img
          :src="imgList[5]"
          alt
        />
        <div class="detail-info">
          <div class="title">引用文献</div>
          <div class="info-list">
            (无)
            <div
              v-for="item in referenceData"
              :key="item.id"
            >
              <span>{{ item.title }}：</span>
              <span>{{ item.val }}</span>
            </div>
          </div>
        </div>
      </div>
      <!-- <p v-html="detailData.dataDef?detailData.dataDef.chndescription:''" /> -->
    </div>
  </div>
</template>

<script>
export default {
  name: "DialogDetail",
  props: {
    detailData: {
      type: Object,
      default: () => { }
    }
  },
  data() {
    return {
      chnname: "",
      dataInfo: "",
      imgList: [
        require("@/assets/images/datadownDetial 1.png"),
        require("@/assets/images/datadownDetial 2.png"),
        require("@/assets/images/datadownDetial 3.png"),
        require("@/assets/images/datadownDetial 4.png"),
        require("@/assets/images/datadownDetial 5.png"),
        require("@/assets/images/datadownDetial 6.png")
      ],
      datasourcecode: '',
      referenceData: [],
      contentData: [
        {
          id: 1,
          title: '数据集责任人',
          key: "producer",
          val: "无"
        },
        {
          id: 2,
          title: "数据负责单位",
          key: "productionunit",
          val: "无"
        },
        {
          id: 3,
          title: "联系信息",
          key: "contactinfo",
          val: "无"
        }
      ],
      timerData: [
        {
          id: 1,
          title: "起始时间",
          key: "databegintime",
          val: "无"
        },
        {
          id: 2,
          title: "终止时间",
          key: "dataendtime",
          val: "无"
        },
        {
          id: 3,
          title: "观测共计频次",
          key: "obsfreq",
          val: "无"
        }
      ],
      geographicData: [
        {
          id: 1,
          title: "地理范围描述",
          key: "areascope",
          val: "无"
        },
        {
          id: 2,
          title: "最东经度",
          key: "eastlon",
          val: "无"
        },
        {
          id: 3,
          title: "最西经度",
          key: "westlon",
          val: "无"
        },
        {
          id: 4,
          title: "最北纬度",
          key: "northlat",
          val: "无"
        },
        {
          id: 5,
          title: "最南纬度",
          key: "southlat",
          val: "无"
        }
      ],
      originalData: [
        {
          id: 1,
          title: "数据集名称",
          key: "chnname",
          val: "无"
        },
        {
          id: 2,
          title: "数据集代码",
          key: "udatacode",
          val: "无"
        },
        {
          id: 3,
          title: "更新频率",
          key: "updatefreq",
          val: "无"
        },
        {
          id: 4,
          title: "空间分辨率",
          key: "spatialresolution",
          val: "无"
        },
        {
          id: 5,
          title: "制作时间",
          key: "producetime",
          val: "无"
        },
        {
          id: 6,
          title: "参考系",
          key: "refersystem",
          val: "无"
        },
        {
          id: 7,
          title: "关键字",
          key: "keywords",
          val: "无"
        },
        {
          id: 8,
          title: "发布时间",
          key: "publishtime",
          val: "无"
        }
      ]
    };
  },
  watch: {
    detailData: {
      handler(newVal, oldVal) {
        this.setData();
      },
      deep: true
    }
  },
  created() {
    this.setData();
  },
  methods: {
    setData() {
      this.chnname = this.detailData["chnname"];
      this.datasourcecode = this.detailData["datasourcecode"];
      this.dataInfo = this.detailData["chndescription"];
      this.originalData.forEach(item => {
        item["val"] = this.detailData[item.key] || "无";
      });
      this.geographicData.forEach(item => {
        item["val"] = this.detailData[item.key] || "无";
      });
      this.timerData.forEach(item => {
        item["val"] = this.detailData[item.key] || "无";
      });
      this.contentData.forEach(item => {
        item["val"] = this.detailData[item.key] || "无";
      });
    }
  }
};
</script>

<style>
.el-dialog__header {
  display: none;
}
.el-dialog__body {
  background-color: #f8faff;
  padding: 20px;
}
</style>

<style scoped  lang="scss">
.dialog-detail {
  width: 100%;
  .detail-content {
    width: 100%;
    background-color: #ffffff;
    .detail-box {
      display: flex;
      justify-content: flex-start;
      padding: 20px;
      img {
        width: 72px;
        height: 72px;
        margin-right: 20px;
      }
      .detail-info {
        flex: 1;
        padding-top: 25px;
        border-bottom: 1px dashed #f2f2f2;
        .title {
          font-size: 20px;
          color: #196bd5;
          font-weight: 600;
          margin-bottom: 20px;
        }
        .detail-gaikuang {
          p {
            font-size: 18px;
            color: #666;
            line-height: 30px;
          }
        }
        .sub-title {
          font-size: 18px;
          color: #333;
        }
        .info-list {
          display: flex;
          justify-content: flex-start;
          align-items: center;
          flex-wrap: wrap;
          margin: 20px 0 30px;
          div {
            width: 48%;
            height: 40px;
            display: flex;
            align-items: center;
            font-size: 16px;
            margin-right: 2%;
            & > span:first-child {
              display: inline-block;
              columns: #333;
              width: 128px;
              // overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              font-weight: bold;
              position: relative;
              &::after {
                content: "";
                position: absolute;
                top: 7px;
                left: -10px;
                width: 4px;
                height: 4px;
                border-radius: 50%;
                background-color: #333;
              }
            }
            & > span:last-child {
              display: inline-block;
              columns: #999;
              flex: 1;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
          }
          & > div:nth-child(2n) {
            margin-right: 0;
          }
        }
      }
    }
  }
}
</style>
