<template>
  <div ref="textListRef" class="h-text-list compontent-box product-border">
    <div v-if="title" class="h-carousel-title" :style="headerStyle">
      <i class="el-icon-s-grid" />
      {{ title }}
    </div>
    <ul>
      <li v-for="item in dataList" :key="item.link">
        <a>
          {{ item.text }}
          <span v-if="item.subText">{{ item.subText }}</span>
        </a>
        <a style="margin-left:20px;text-align: right;">{{ item.created }}</a>
        <img v-if="newIconShow === true" :src="newIcon">
      </li>
    </ul>
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
import { getMoreVersion } from '../createApi.js'
import moment from 'moment'

export default {
  name: 'HTextList',
  props: {
    valueData: {
      required: false,
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      title: null,
      headerStyle: null,
      newIcon: require('@/assets/images/new.png'),
      todayTime: '',
      newIconShow: false,
      dataList: [
        {
          link: '1',
          text: '这里的风景很美丽1',
          subText: '内蒙大草原'
        }
      ],
      metaData: {
        pageNum: 1,
        pageSize: 10,
        totalNum: 10
      } // 数据分页ss
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
  created() {},
  mounted() {
    if (this.valueData.length && this.valueData[0].params) {
      this.title = this.valueData[0].formData.title
      this.headerStyle = this.valueData[0].style
      // 计算高度
      const oh = this.$refs['textListRef'].offsetHeight
      const h = parseInt(oh) - 80
      this.metaData.pageSize = parseInt(h / 40)
      this.getList()
    }
  },
  methods: {
    getList() {
      getMoreVersion({ ...this.metaData, ...this.valueData[0].params }).then(
        res => {
          if (res.data && res.data.list && res.data.list.length) {
            this.dataList = res.data.list.map(item => {
              return {
                id: item.id,
                link: item.id,
                url: item.url,
                fileshowname: item.fileshowname,
                text: item.fileshowname,
                created: item.created
              }
            })
            this.metaData.pageNum = res.data.pageNum
            this.metaData.totalNum = res.data.total
            const day1 = new Date()
            day1.setTime(day1.getTime())
            this.todayTime =
              day1.getFullYear() +
              '-' +
              (day1.getMonth() + 1) +
              '-' +
              day1.getDate()
            this.dataList.map(item => {
              if (item.created.indexOf(this.todayTime) !== -1) {
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
      this.metaData.pageNum = d.page
      this.getList()
    }
  }
}
</script>

<style scoped  lang="scss">
@import "../components-header.scss";
.h-text-list {
  width: 100%;
  height: 100%;
  margin: 0;
  overflow: hidden;
  background-color: #ffffff;
  border: 1px solid #f2f2f2;
  display: flex;
  flex-direction: column;
  justify-self: start;
  ul {
    flex: 1;
    width: 100%;
    margin: 0;
    padding: 0 10px;
    li {
      width: 100%;
      overflow: hidden;
      height: 40px;
      line-height: 40px;
      padding: 0 15px;
      border-bottom: 1px solid #f2f2f2;
      display: flex;
      align-items: center;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      position: relative;
      &::after {
        content: "";
        position: absolute;
        top: 18px;
        left: 1px;
        width: 4px;
        height: 4px;
        border-radius: 50%;
        background-color: #cccccc;
      }
      a {
        flex: 1;
        font-size: 14px;
        color: #555;
        height: 40px;
        padding-right: 15px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        span {
          margin-left: 15px;
          font-size: 12px;
          color: #999;
        }
      }
    }
  }
  .text-list-page {
    width: 100%;
    height: 26px;
  }
}
</style>
