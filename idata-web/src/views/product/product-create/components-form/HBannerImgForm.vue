<template>
  <div class="components-form-data">
    <div class="data-content">
      <div class="data-page">
        <div class="data-list">
          <div v-for="item in dataList" :key="item.id" class="data-list-item-type-1" @click="getFormData(item)">
            <img :src="item.url" alt="">
          </div>
        </div>
      </div>
    </div>
    <div style="padding: 0 0 20px 0;">
      <pagination v-show="metaData.totalNum > 0" style="margin-top: 20px;" :total="metaData.totalNum" :page.sync="metaData.pageNum" :limit.sync="metaData.pageSize" @pagination="paginationChange" />
    </div>
  </div>
</template>

<script>
import { getProductsPhoto } from '../createApi.js'
export default {
  name: 'HBannerImgForm',
  props: {
    componentActive: {
      type: Object,
      default: () => { }
    }
  },
  data() {
    return {
      dataList: [],
      metaData: {
        pageNum: 1,
        pageSize: 20,
        totalNum: 0
      }
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    getData() {
      getProductsPhoto(this.metaData).then(res => {
        if (res.data && res.data.list && res.data.list.length) {
          this.metaData.page = parseInt(res.data.pageNum)
          this.metaData.totalNum = parseInt(res.data.total)
          this.dataList = res.data.list.map(item => {
            return {
              ...item // , url: require('@/assets/create/3.jpg')
            }
          })
        }
      })
    },
    // 获取点击数据
    getFormData(item) {
      this.$emit('getCompontentData', {
        id: item.id,
        url: item.url,
        link: item.link
      })
    },
    // 分页
    paginationChange(d) {
      this.metaData.page = d.page
      this.getData()
    }
  }
}
</script>

<style scoped lang="scss">
.components-form-data {
  width: 100%;
  padding: 15px;
  min-height: 90vh;
  .data-content {
    width: 100%;
    display: flex;
    justify-content: flex-start;
    .data-page {
      flex: 1;
      .data-list {
        width: 100%;
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
        .data-list-item-type-1 {
          width: 300px;
          height: 200px;
          margin: 0 23px 20px 0;
          cursor: pointer;
          border: 2px dashed transparent;
          transition: all 0.3s;
          position: relative;
          &:hover::after {
            content: '+';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            font-size: 60px;
            background-color: rgba(0, 0, 0, 0.1);
            text-align: center;
            line-height: 200px;
            // z-index: 3000;
            color: #f2f2f2;
          }
          img {
            width: 100%;
            height: 200px;
            transition: all 0.3s;
          }
          &:hover {
            border-color: salmon;
            border-radius: 4px;
            img {
              transform: scale(0.9, 0.9);
            }
          }
        }
        & > .data-list-item-type-1:nth-child(4n) {
          margin-right: 0;
        }
      }
    }
  }
}
</style>
