<template>
  <div class="side-bar">
    <div class="api-type">
      <div class="api-type-header-bg">
        <div class="api-type-header" @click="showTypeList">
          <div><img :src="iconImg" alt="">资料类别</div>
          <i class="el-icon-arrow-down" :class="{'is-active-type':isTypeShow}" />
        </div>
      </div>
      <div class="type-list" :class="{'is-active-type-list':isTypeShow}">
        <div class="type-list-content">
          <div v-for="item in navTypeData" :key="item.dataClassId" :class="{'is-active':activeTypeId === item.dataClassId}" @click="getTypeDetail(item)">{{ item.dataClassName }}</div>
        </div>
      </div>
    </div>
    <div class="api-search" style="display:none">
      <el-input v-model="search" placeholder="请输入内容" class="input-with-select">
        <el-button slot="append" icon="el-icon-search" />
      </el-input>
    </div>
    <div class="api-list">
      <div class="api-type-header-bg" @click="isShowItem">
        <div class="api-type-header">
          <div><img :src="iconImg" alt="">资料名称</div>
          <i class="el-icon-arrow-down" :class="{'is-active-type':isItemShow}" />
        </div>
      </div>
      <div class="detail-list" :class="{'is-active-item':isItemShow}">
        <div v-for="item in navTypeDetail" :key="item.value" :class="{'is-active':activeTypeDetailId === item.value}" @click="getTypeDetailContent(item)">{{ item.DATA_NAME }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SideBar',
  props: {
    navTypeData: {
      type: Array,
      default: () => []
    },
    activeTypeId: {
      type: String,
      default: () => ''
    },
    navTypeDetail: {
      type: Array,
      default: () => []
    },
    activeTypeDetailId: {
      type: String,
      default: () => ''
    }
  },

  data() {
    return {
      activeNav: '1',
      iconImg: require('@/assets/images/nav-detail.png'),
      isTypeShow: false,
      isItemShow: false,
      search: ''
    }
  },
  created() {
    if (this.activeTypeId) {
      this.showTypeList()
      this.isShowItem()
    }
  },
  methods: {
    getTypeDetail(data) {
      this.$emit('handleTypeList', data.dataClassId)
    },
    getTypeDetailContent(data) {
      this.$emit('handleTypeDetailList', data.value)
    },
    showTypeList() {
      this.isTypeShow = !this.isTypeShow
    },
    isShowItem() {
      this.isItemShow = !this.isItemShow
    }
  }
}
</script>

<style scoped  lang="scss">
.side-bar {
  width: 100%;
  margin-bottom: 80px;
  .api-type {
    width: 100%;
  }
  .api-type-header-bg {
    border-bottom: 1px solid #1a1f32;
    background-image: url('../../../assets/images/sidebarbg.png');
    background-origin: content-box;
    background-position: center;
    background-repeat: no-repeat;
    background-clip: content-box;
    background-size: cover;
    position: relative;
    &::after {
      position: absolute;
      content: '';
      top: 0;
      left: 0;
      width: 4px;
      height: 100%;
      background-color: #28cce9;
    }
    .api-type-header {
      width: 100%;
      height: 60px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 20px;
      color: #ffffff;
      font-size: 16px;
      div {
        img {
          width: 20px;
          height: 20px;
          margin-right: 20px;
        }
        height: 100%;
        display: flex;
        align-items: center;
      }
      i {
        transform: rotate(90deg);
        transition: all 0.3s;
      }
    }
    .is-active-type {
      transform: rotate(0deg) !important;
    }
  }
  .type-list {
    height: 0px;
    overflow: hidden;

    .type-list-content {
      padding: 30px 20px 0;
      margin-bottom: 10px;
      display: flex;
      justify-content: flex-start;
      flex-wrap: wrap;
      color: #ffffff;
      div {
        width: 50%;
        margin-bottom: 15px;
        cursor: pointer;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
  .api-search {
    padding: 20px;
  }
  .api-list {
    .detail-list {
      height: 0;
      overflow: hidden;
      margin-top: 20px;
      width: 100%;
      color: #ffffff;
      padding: 0 20px;
      div {

        cursor: pointer;
        font-size: 16px;
        color: #ffffff;
        height: 30px;
        line-height: 30px;
        width: 100%;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
}
.is-active-type-list,
.is-active-item {
  height: auto !important;
}
.is-active {
  color: #28cce9 !important;
}
</style>
<style>
.side-bar .el-input__inner {
  background-color: transparent;
  color: #ffffff;
  border-right: none;
}
.side-bar .el-input__inner:focus {
  border-color: #ffffff;
}
.side-bar .el-input-group__append {
  background-color: transparent;
  padding: 0 10px;
}
.side-bar .el-input-group__prepend {
  padding: 0 10px;
}
</style>

