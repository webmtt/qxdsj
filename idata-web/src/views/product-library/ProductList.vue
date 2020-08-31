<template>
  <div class="product-list">
    <div class="product-list-content">
      <div class="product-list-header">
        <div>
          <span class="header-title-line" />
          <span class="header-title-text">产品库</span>
          <span class="header-sub-title">精准服务，品类齐全</span>
        </div>
        <el-button style="float: right" type="success" size="mini" @click="handleNewPage">+</el-button>
      </div>
      <div class="product-list-body">
        <div v-for="item in dataList" :key="item.id" class="product-item" @click="handlePreview(item)">
          <div class="product-img">
            <img :src="item.imgUrl">
          </div>
          <div class="product-info">
            <div class="product-info-title">{{ item.title }}</div>
            <div class="product-info-content">{{ item.content }}</div>
          </div>
        </div>
      </div>
      <div class="page-box">
        <pagination
          v-show="metaData.total > 0"
          style="margin-top: 20px;"
          :total="metaData.total"
          :page.sync="metaData.page"
          :limit.sync="metaData.limit"
          @pagination="paginationChange"
        />
      </div>

    </div>
  </div>
</template>

<script>

export default {
  name: 'ProductList',
  data() {
    return {
      dataList: [
        {
          id: 1,
          imgUrl: require('@/assets/create/2.jpg'),
          title: '这里是产品库名称',
          content: '的发噶的范德萨发生的分公司法的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀'
        },
        {
          id: 2,
          imgUrl: require('@/assets/create/2.jpg'),
          title: '这里是产品库名称',
          content: '的发噶的范德萨发生的分公司法的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀'
        },
        {
          id: 3,
          imgUrl: require('@/assets/create/2.jpg'),
          title: '这里是产品库名称',
          content: '的发噶的范德萨发生的分公司法的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀'
        },
        {
          id: 4,
          imgUrl: require('@/assets/create/2.jpg'),
          title: '这里是产品库名称',
          content: '的发噶的范德萨发生的分公司法的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀'
        },
        {
          id: 5,
          imgUrl: require('@/assets/create/2.jpg'),
          title: '这里是产品库名称',
          content: '的发噶的范德萨发生的分公司法的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀'
        },
        {
          id: 6,
          imgUrl: require('@/assets/create/2.jpg'),
          title: '这里是产品库名称',
          content: '的发噶的范德萨发生的分公司法的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀'
        },
        {
          id: 7,
          imgUrl: require('@/assets/create/2.jpg'),
          title: '这里是产品库名称',
          content: '的发噶的范德萨发生的分公司法的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀分公司法规人生如歌散热管施工热缩管生日歌的发噶的范德萨发生的分公司法规三国杀'
        }
      ],
      queryData: {
        page: 1
      },
      metaData: {
        page: 1,
        total: 100,
        limit: 20
      }
    }
  },
  methods: {
    // 跳转到产品详情页面
    handlePreview(item) {
      this.$router.push({ path: `/productLibrary/detail/${item.id}` })
    },
    // 添加列表
    handleNewPage() {
      this.$router.push({ path: `/productLibrary/create` })
    },
    // 获取产品列表
    getList() {
    },
    // 分页
    paginationChange(p) {
      this.queryData.page = p
      this.getList()
    }
  }
}
</script>

<style scoped lang="scss">
  .product-list{
    width:100%;
    height: 100%;
    .product-list-content{
      width: 1200px;
      height: auto;
      padding-bottom:50px;
      margin: 0 auto;
      .product-list-header{
        width: 100%;
        height: 90px;
        line-height: 90px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .header-title-line{
          display: inline-block;
          position: relative;
          top:3px;
          width: 4px;
          height: 20px;
          background-color: #24d2f0;
          margin-right: 20px;
        }
        .header-title-text{
          color: #333;
          font-size: 20px;
          font-weight: bolder;
          letter-spacing: 2px;
        }
        .header-sub-title{
          margin-left: 40px;
          margin-top: 4px;
          color: #999;
          font-size: 16px;
          letter-spacing: 2px;
        }
      }
      .product-list-body{
        display: flex;
        justify-content: flex-start;
        width: 100%;
        height: auto;
        flex-wrap: wrap;
        &>.product-item {
          width: 400px;
          height: 190px;
          border:1px solid #ebebeb;
          margin-bottom:  30px;
          display: flex;
          justify-content: flex-start;
          padding: 20px;
          cursor: pointer;
          .product-img{
            width:80px;
            height:80px;
            border:1px dashed #f2f2f2;
            img {
              width: 100%;
              height: 100%;
            }
          }
          .product-info{
            flex: 1;
            height: 150px;
            padding-left: 20px;
            .product-info-title{
              width: 100%;
              height: 50px;
              line-height: 50px;
              color: #333;
              font-weight: bold;
              font-size: 16px;
            }
            .product-info-content{
              width: 100%;
              height: 100px;
              line-height: 26px;
              overflow: hidden;
              color: #999;
              font-size: 14px;
            }
          }
          &:hover {
            border:1px solid darkorange;
          }
        }
      }

    }
  }
  .page-box{
    width: 1200px;
    margin: 0 auto;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    border:1px solid #f2f2f2;
    .pagination-container{
      padding: 0;
      margin-top: 0 !important;
    }
  }
</style>
