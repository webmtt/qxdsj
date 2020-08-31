<template>
  <div class="app-container">
    <div class="special-product">
      <div class="product-list-body">
        <product-item :data-list="dataList" @handlePreview="handlePreview"></product-item>
      </div>

      <el-pagination
        v-if="metaObj.total"
        style="text-align: right;"
        :current-page.sync="metaObj.pageNum"
        :page-size="20"
        layout="prev, pager, next, jumper"
        :total="metaObj.total"
      ></el-pagination>

      <!-- <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page.sync="currentPage3"
      :page-size="100"
      layout="prev, pager, next, jumper"
      :total="1000">
      </el-pagination>-->

      <!-- <pagination :total="metaData.total" :page.sync="metaData.pageNum" /> -->
    </div>
  </div>
</template>

<script>
import { getServerUrl } from "@/api/user.js";

import ProductItem from "./components/ProductItem.vue";
import Pagination from "@/components-library/Pagination/index.vue";
import { getProductList } from "@/api/specialProduct.js";
export default {
  name: "SpecialProductList",
  components: {
    ProductItem,
    Pagination
  },
  data() {
    return {
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
      ],
      metaObj: {
        pagenum: 1,
        total: 0
      }
    };
  },
  created() {
    getServerUrl().then(res => {
      if (res.code === "200") {
        this.baseUrl = res.data;
      }
    });

    const obj = {};
    if (this.$store.state.user.iD) {
      obj["userid"] = this.$store.state.user.iD;
    }
    obj['pagenum'] = this.metaObj.pagenum
    getProductList({ ...obj }).then(res => {
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
        this.metaObj.pagenum = parseInt(res.data.currentpage);
        this.metaObj.total = res.data.dataCount;
      }
    });
  },
  methods: {
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
    // 分页
    paginationChange() {
      // this.getList();
    }
  }
};
</script>

<style scoped  lang="scss">
.app-container {
  width: 100%;
  .special-product {
    width: 1300px;
    margin: 0 auto;
    // border: 1px solid red;
  }
}
</style>
