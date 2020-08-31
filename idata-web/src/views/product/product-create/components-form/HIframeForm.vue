<template>
  <div class="components-form-data">
      <el-form label-width="100px">
      <el-form-item label="网站链接">
        <el-input
          v-model="iframeUrl"
          placeholder="请输入iframe模块链接"
        ></el-input>
      </el-form-item>
      <el-form-item label="">
        <el-button
          :disabled='!iframeUrl'
          type="success"
          @click="getFormData"
        >保存数据</el-button>
      </el-form-item>
    </el-form>
    <div class="select-box">
      <div v-for="item in listData" :key="item.id" @click="selcetedItem(item)">
        <img :src="item.url" alt />
        <span>{{item.name}}</span>
      </div>
    </div>
    <div>
      <el-checkbox-group v-model="selectUrl" @change="handleCheckChange">
        <el-checkbox v-for="(item, index) in dataList" :key="index" :label="item.id">{{item.title}}</el-checkbox>
      </el-checkbox-group>
    </div>
  </div>
</template>

<script>
import { getProductList } from "@/api/specialProduct.js";

export default {
  name: "HIframeForm",
  props: {
    componentActive: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      iframeUrl: "",
      listData: [
        {
          id: 1,
          url: require("@/assets/images/libraryicon2.png"),
          name: "专题产品",
          link: "http://172.18.112.51:8085/#/self-special-product"
        }
      ],
      baseUrl: null,
      selectUrl: [],
      dataList: []
    };
  },

  mounted() {
    if (
      this.componentActive.compontentData.length &&
      this.componentActive.compontentData[0].list.length
    ) {
      this.iframeUrl = this.componentActive.compontentData[0].list[0];
    }
    if (this.iframeUrl) {
      if (this.iframeUrl.indexOf("self-special-product") > -1) {
        let index = this.iframeUrl.indexOf("?id=")
        const parmas = this.iframeUrl.substring( index+ 4)
        this.selectUrl =  parmas.split(',')
        this.selcetedItem(this.listData[0]);
      }
    }
  },
  methods: {
    getFormData() {
      this.$emit("getCompontentData", {
        title: null,
        list: [this.iframeUrl]
      });
    },
    // 选择内容
    selcetedItem(item) {
      if (item.name === "专题产品") {
        this.iframeUrl = this.baseUrl = `${item.link}`; // item.link + this.pramaUrl;
        if(this.selectUrl.length) {
          this.handleCheckChange()
        }else {
          this.selectUrl = [];
        }
        const obj = {
          pagenum: "9999"
        };
        if (this.$store.state.user.iD) {
          obj["userid"] = this.$store.state.user.iD;
        }
        getProductList({ ...obj }).then(res => {
          if (res.data && res.data && res.data.data.length) {
            this.dataList = res.data.data.map(item => {
              return {
                id: item.id,
                title: item.product_name
              };
            });
          }
        });
      }
    },
    // 选择
    handleCheckChange() {
      this.pramaUrl = this.selectUrl.join(",");
      this.iframeUrl = `${this.baseUrl}?id=${this.pramaUrl}`;
    }
  },
  destroyed() {
    this.iframeUrl = null;
  }
};
</script>

<style scoped lang="scss">
.components-form-data {
  width: 100%;
  padding: 20px;
  min-height: 90vh;
  .select-box {
    width: 100%;
    // border: 1px solid #f2f2f2;
    margin-top: 20px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding: 40px 0;
    & > div {
      width: 80px;
      height: 120px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      border: 1px solid #f2f2f2;
      border-radius: 6px;
      cursor: pointer;
      & > img {
        border: 1px solid #f2f2f2;
        padding: 4px;
        border-radius: 50%;
      }
      & > span {
        margin-top: 10px;
      }
      &:hover {
        border-color: #24d2f0;
        & > img {
          border-color: #24d2f0;
        }
        & > span {
          color: #24d2f0;
        }
      }
    }
  }
}
</style>
