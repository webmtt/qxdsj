<template>
  <div
    ref="HImgTextList"
    class="compontent-box h-img-text-list product-border"
  >
    <div
      v-if="title"
      class="h-carousel-title"
      :style="headerStyle"
    >
      <i class="el-icon-s-grid" />
      {{ title }}
    </div>

    <div class="list-box">
      <div
        v-for="item in dataList"
        :key="item.id"
        class="h-img-text-list-item"
      >
        <div class="iten-inner text-list">
          <img :src="item.url">
          <span>{{ item.fileshowname }}</span>
          <span style="margin-left:20px;">{{ item.created }}</span>
          <img
            v-if="newIconShow === true"
            :src="newIcon"
          >
        </div>
      </div>
    </div>
    <div class="text-list-page">
      <pagination-mini
        v-show="metaData.totalNum > 0"
        :total="metaData.totalNum"
        :page.sync="metaData.pageNum"
        :limit.sync="metaData.pageSize"
        @pagination="paginationChange"
      />
    </div>
  </div>
</template>

<script>
import { getMorePhoto, getMenuPhoto } from '../createApi.js'
import moment from 'moment'

export default {
  name: 'HImgTextList',
  props: {
    valueData: {
      required: false,
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      title: '',
      headerStyle: null,
      newIcon: require('@/assets/images/new.png'),
      dataList: [
        {
          id: 1,
          url: require('@/assets/create/1.jpg'),
          fileshowname: '这里的风景真美丽这里的风景真美丽这里的风景真美丽'
        }
      ],
      metaData: {
        pageNum: 1,
        pageSize: 10,
        totalNum: 0
      },
      todayTime: '',
      newIconShow: false
    }
  },
  watch: {
    valueData: {
      handler(newVal, oldVal) {
        if (this.valueData.length && this.valueData[0].params) {
          this.title = this.valueData[0].formData.title
          this.headerStyle = this.valueData[0].style
          this.getList()
        }
      },
      deep: true
    }
  },
  mounted() {
    if (this.valueData.length && this.valueData[0].params) {
      this.title = this.valueData[0].formData.title
      this.headerStyle = this.valueData[0].style
      // 计算高度
      const oh = this.$refs['HImgTextList'].offsetHeight
      const h = parseInt(oh) - 22 - 40
      this.metaData.pageSize = parseInt(h / 40)
      this.getList()
    }
  },
  methods: {
    getList() {
      getMenuPhoto({ ...this.metaData, ...this.valueData[0].params }).then(
        res => {
          this.metaData.totalNum = res.data.total
          if (res.data.list && res.data.list.length) {
            this.dataList = res.data.list.map((item, index) => {
              return {
                filename: item.filename,
                fileshowname: item.fileshowname,
                url: item.url,
                id: item.id,
                created: item.created
              }
            })
            const day1 = new Date()
            day1.setTime(day1.getTime())
            this.todayTime =
              day1.getFullYear() +
              '-' +
              (day1.getMonth() + 1) +
              '-' +
              day1.getDate()
            this.dataList.map(item => {
              if (item.created.indexOf(this.todayTime) != -1) {
                this.newIconShow = true
              } else {
                this.newIconShow = false
              }
              item.created = moment(item.created, 'YYYYMMDDhhmmss').format(
                'YYYY-MM-DD HH:mm:ss'
              )
            })
          }
        }
      )
    },
    paginationChange(d) {
      // this.metaData.pageNum = d.page
      this.valueData[0].params.pageNum = d.page
      this.getList()
    }
  }
}
</script>

<style  scoped  lang="scss">
@import "../components-header.scss";
.h-img-text-list {
  width: 100%;
  height: 100%;
  overflow: hidden;
  background-color: #ffffff;
  display: flex;
  flex-direction: column;
  .list-box {
    flex: 1;
    overflow: hidden;
  }
  .text-list-page {
    height: 22px;
  }
  .h-img-text-list-item {
    width: auto;
    height: 40px;
    border-bottom: 1px solid #f2f2f2;
    .iten-inner {
      display: flex;
      width: 100%;
      height: 40px;
      align-items: center;
      justify-content: flex-start;
      padding: 0px 10px;
      img {
        width: 40px;
        height: 36px;
        background-color: #99a9bf;
      }
      span {
        margin-left: 15px;
        flex: 1;
        padding-right: 15px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
}
</style>
