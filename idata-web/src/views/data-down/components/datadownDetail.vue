<template>
  <div class="datadown-detail">
    <div class="list-content">
      <div v-for="(item, index) in detailList" :key="index" class="list-content-item">
        <img :src="layImgurl" alt="">
        <div>
          <h4>{{ item.chnname?item.chnname:'这里是中文名称' }}</h4>
          <div class="sub-item">
            <h4>1.概况</h4>
            <p v-html="item.chndescription?item.chndescription:'这里是概况内容'" />
          </div>

          <div class="list-cooper">
            <span @click="handleDetail(item)"><img :src="detailImgurl" alt="">详情信息 </span>
          </div>
        </div>
      </div>
    </div>
    <el-dialog class="form-el-dialog form-el-dialog-add" top="5vh" :visible.sync="dialogVisible" width="1000px">
      <dialog-detail v-if="detailData" :detail-data="detailData" />
    </el-dialog>
  </div>
</template>

<script>

import DialogDetail from './DialogDetail.vue'

import { getDetail } from '@/api/dataDown.js'

export default {
  name: 'DatadownDetail',
  components: { DialogDetail },
  props: {
    detailList: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      dialogVisible: false,
      dialogTitle: '',
      detailData: null,
      layImgurl: require('@/assets/images/lay.png'),
      detailImgurl: require('@/assets/images/detail.png'),
      downImgurl: require('@/assets/images/down.png')
    }
  },
  methods: {
    handleDetail(data) {
      this.dialogTitle = data.chnname
      getDetail(data.datacode).then(res => {
        if (res.data && res.data.dataDef) {
          this.detailData = res.data.dataDef || null
          this.dialogVisible = true
        } else {
          this.detailData = null
          this.$message({
            message: '暂无数据',
            type: 'success'
          })
        }
      }).catch(() => {
        this.detailData = null
        this.$message({
          message: '暂无数据',
          type: 'success'
        })
      })
    }
  }
}
</script>

<style scoped  lang="scss">
.datadown-detail {
  width: 100%;
  height: auto;
  .list-content {
    padding: 0 20px;
    .list-content-item {
      padding: 27px 0;
      display: flex;
      justify-content: flex-start;
      border-bottom: 1px solid #dcdcdc;
      img {
        width: 26px;
        height: 26px;
        margin-right: 20px;
      }
      & > div {
        flex: 1;
        h4 {
          padding: 0;
          margin: 0;
          font-size: 20px;
          color: #333;
        }
        p {
          padding: 0;
          margin: 0;
          font-size: 18px;
          color: #666666;
          text-indent: 18px;
          line-height: 30px;
        }
        .sub-item {
          margin-top: 10px;
          h4 {
            margin-bottom: 9px;
          }
        }
        .list-cooper {
          margin-top: 10px;
          display: flex;
          justify-content: flex-start;
          align-items: center;
          height: 20px;
          line-height: 20px;
          span {
            display: flex;
            cursor: pointer;
            align-items: center;
            img {
              width: 20px;
              height: 20px;
              margin-right: 10px;
            }
          }
          & > span:first-child {
            margin-right: 30px;
          }
        }
      }
    }
  }
}
</style>
