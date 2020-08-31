<template>
  <div class="special-product">
    <div class="product-list-content">
      <div class="hot-content-header">
        <span>专题产品</span>
        <i v-if="userId" @click="goTomore">更多</i>
      </div>

      <div class="product-list-body">
        <product-item :data-list="dataList" @handlePreview="handlePreview"></product-item>
      </div>
    </div>
  </div>
</template>

<script>
import ProductItem from "./components/ProductItem.vue";
import { getServerUrl } from "@/api/user.js";
import { getProductList, getPermissions } from "@/api/specialProduct.js";
export default {
  name: "SpecialProduct",
  components: {
    ProductItem
  },
  data() {
    return {
      userId: null,
      baseUrl: "",
      dataList: [
        {
          id: 1,
          imgUrl: require("@/assets/create/2.jpg"),
          title: "这里是产品库名称",
          key: "",
          content:
            "的发噶的范德萨发生的分公司法的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀"
        }
      ]
    };
  },
  created() {
    this.userId = this.$store.state.user.iD || null
    getServerUrl().then(res => {
      if (res.code === "200") {
        this.baseUrl = res.data;
      }
    });
    getProductList({}).then(res => {
      if (res.data && res.data && res.data.data.length) {
        this.dataList = res.data.data.map(item => {
          return {
            id: item.id,
            title: item.product_name,
            key: item.keyword,
            imgUrl: this.baseUrl + item.smallPng,
            type: item.type
          };
        });
        // this.metaData.pageNum = 1;
        // this.metaData.total = res.data.dataCount;
      }
    });
  },
  methods: {
    // 更多
    goTomore() {
      this.$router.push({ path: `/specialProduct/list` })
    },
    // 跳转到产品详情页面
    handlePreview(item) {
      this.$router.push({
        path: `/specialProduct/detail`,
        query: {
          id: item.id,
          type: item.type
        }
      });
    },
    // 添加列表
    handleNewPage() {
      this.$router.push({ path: `/productLibrary/create` })
    },
    // 获取产品列表
    getList() {}
  }
};
</script>

<style scoped lang="scss">
.special-product {
  width: 100%;
  height: 100%;
  .product-list-content {
    width: 1300px;
    height: auto;
    padding-bottom: 50px;
    margin: 0 auto;
    background-color: #ffffff;
    .hot-content-header {
      width: 100%;
      height: 100px;
      display: flex;
      position: relative;
      // flex-direction: column;
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
      i {
        position: absolute;
        right: 0;
        color: #999;
        font-size: 14px;
        cursor: pointer;
        &:hover {
          color: #24d2f0;
        }
      }
      // & > span:last-child {
      //   font-size: 20px;
      //   color: #666;
      // }
    }
    .product-list-body {
      width: 100%;
    }
  }
}
.page-box {
  width: 1300px;
  margin: 0 auto;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  // border:1px solid #f2f2f2;
  .pagination-container {
    padding: 0;
    margin-top: 0 !important;
  }
}
</style>
