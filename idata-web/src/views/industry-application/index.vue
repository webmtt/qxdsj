<template>
  <div class="industry-application">
    <div class="industry-application-content">
      <div class="product-list-header">
        <div>
          <span class="header-title-line" />
          <span class="header-title-text">行业方案</span>
          <span class="header-sub-title">更便捷，更精准，更个性化</span>
        </div>
      </div>
      <div class="industry-body">
        <div
          v-for="item in dataList"
          :key="item.id"
          class="list-item"
          @click="handleLinkTo(item)"
        >
          <span>
            <img
              :src="item.imgUrl"
              alt=""
            >
          </span>
          <div class="item-title">{{ item.title }}</div>
          <div class="item-title-e">{{ item.engName }}</div>
          <div
            class="item-title-content"
            v-html="item.content"
          />
        </div>
      </div>
      <pagination
        v-show="metaData.total > 0"
        style="margin-top: 20px;"
        :total="metaData.total"
        :page.sync="metaData.page"
      />

      <!-- @pagination="paginationChange" -->

    </div>
  </div>
</template>
<script>

import { getList } from '@/api/industryApplication.js'

export default {
  name: 'IndustryApplication',
  data() {
    return {
      dataList: [],
      metaData: {
        total: 0,
        page: 1
      }
    }
  },
  created() {
    this.getDataList()
  },
  methods: {
    getDataList() {
      getList().then(res => {
        if (res.data && res.data.list) {
          this.metaData.total = res.data.total
          this.metaData.page = res.data.pageNum
          if (res.data.list.length) {
            this.dataList = res.data.list.map((item, index) => {
              return {
                id: item.id,
                imgUrl: item.imageurl,
                title: item.title,
                engName: item.entitle,
                content: item.content
              }
            })
          }
        } else {
          this.metaData.total = 0
          this.metaData.page = 0
        }
      })
    },
    handleLinkTo(d) {
      this.$router.push({ path: `/industryApplication/detail/${d.id}` })
    }
  }
}
</script>

<style scoped  lang="scss">
.industry-application {
  width: 100%;
  min-height: 400px;
  .industry-application-content {
    width: 1300px;
    margin: 0 auto;
    .product-list-header {
      width: 100%;
      height: 90px;
      line-height: 90px;
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
    .industry-body {
      width: 100%;
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      flex-wrap: wrap;
      align-items: center;
      .list-item {
        border: 2px solid transparent;
        border-radius: 10px;
        width: 320px;
        height: 430px;
        margin-right: 140px;
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom: 12px;
        cursor: pointer;
        // justify-content: center;
        & > span > img {
          width: 250px;
          height: 250px;
          border-radius: 50%;
        }
        & > span {
          display: block;
          width: 250px;
          height: 250px;
          margin-top: 24px;
          display: inline-block;
          position: relative;
          &::after {
            content: "";
            position: absolute;
            width: 270px;
            height: 270px;
            top: -10px;
            left: -10px;
            border-radius: 50%;
            border: 2px solid #333;
          }
        }
        .item-title {
          margin-top: 24px;
          font-size: 18px;
          color: #333;
        }
        .item-title-e {
          margin-top: 5px;
          font-size: 13px;
          color: #666666;
          position: relative;
        }
        .item-title-content {
          margin-top: 18px;
          padding: 0 50px;
          font-size: 14px;
          color: #808080;
        }
        &:hover {
          border-color: #196bd5;
          span {
            &::after {
              border: 2px solid #196bd5;
            }
          }
          .item-title,
          .item-title-content {
            color: #196bd5;
          }
          .item-title-e {
            color: #196bd5;
            display: flex;
            &::after {
              content: "";
              position: absolute;
              top: 20px;
              left: 50%;
              transform: translateX(-50%);
              width: 30px;
              height: 3px;
              background-color: #ffa523;
            }
          }
        }
      }
      & > .list-item:nth-child(3n) {
        margin-right: 0;
      }
    }
  }
}
</style>
