<template>
  <div class="components-form-data">
    <el-form label-width="100px">
      <el-form-item label="请输入站点id">
        <el-input
          v-model="stationNum"
          placeholder="请输入站点id（例如：53463）"
        ></el-input>
      </el-form-item>
      <el-form-item label="">
        <el-button
          :disabled='!stationNum'
          type="success"
          @click="getFormData"
        >保存数据</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getProductList } from "@/api/specialProduct.js";

export default {
  name: "HWeatherForm",
  props: {
    componentActive: {
      type: Object,
      default: () => { }
    }
  },
  data() {
    return {
      stationNum: "",
    };
  },

  mounted() {
  },
  methods: {
    getFormData() {
      if (!this.stationNum) {
        this.$message({
          message: '暂无数据',
          type: 'warning'
        })
        return
      }
      this.$emit("getCompontentData", {
        title: null,
        list: [this.stationNum]
      });
    }
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
