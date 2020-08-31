<template>
  <div class="nav-sub">
    <div class="nav-carousel-content">
      <div v-for="item in dataList" :key="item.id" class="nav-item" @click="handleScroll(item)">
        <img :src="item.imgUrl">
        <div class="nav-item-title">{{ item.title }}</div>
        <div>{{ item.subtitle }}</div>
      </div>
    </div>
  </div>
</template>

<script>

// import { getUserinfo } from '@/utils/auth'

export default {
  name: 'NavSub',
  data() {
    return {
      dataList: [
        {
          id: 6,
          imgUrl: require('@/assets/images/northCloudIcon1.png'),
          title: '数据目录交换情况',
          subtitle: 'Data directory exchange',
          subID: 'dataDirectoryExchange'
        },
        {
          id: 5,
          imgUrl: require('@/assets/images/northCloudIcon2.png'),
          title: '数据资源交换情况',
          subtitle: 'Data resource exchange',
          subID: 'dataResourceExchangeStatus'
        },
        {
          id: 4,
          imgUrl: require('@/assets/images/northCloudIcon3.png'),
          title: '文件资源交换详情',
          subtitle: 'Files exchange details',
          subID: 'fileResourceExchangeDetails'
        },
        {
          id: 1,
          imgUrl: require('@/assets/images/northCloudIcon4.png'),
          title: '库表资源交换详情',
          subtitle: 'DataBase tables exchange details',
          subID: 'libraryTableResourceExchangeDetails'
        },
        {
          id: 2,
          imgUrl: require('@/assets/images/northCloudIcon5.png'),
          title: '接口资源交换详情',
          subtitle: 'Api exchange details',
          subID: 'interfaceResourceExchangeDetails'
        }
      ]
    }
  },
  created() {
  },
  methods: {
    handleScroll(item) {
      this.$emit('handleScroll', item)
    }
  }
}
</script>

<style scoped  lang="scss">
.nav-sub {
  width: 100%;
  height: auto;
  margin-top: -50px;
  .nav-carousel-content {
    width: 1300px;
    margin: 0 auto;
    height: 150px;
    background-color: #242a37;
    display: flex;
    justify-content: space-around;
    align-items: center;
    .nav-item {
      flex: 1;
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      position: relative;
      color: #d9dadc;
      cursor: pointer;
      &:hover {
        background-color: #374054;
      }
      &::after {
        position: absolute;
        content: '';
        top: 0;
        right: -1px;
        width: 1px;
        height: 100%;
        background-color: #2f3645;
      }
      &::before {
        position: absolute;
        content: '';
        top: 0;
        left: 0;
        width: 1px;
        height: 100%;
        background-color: #2f3645;
      }
      img {
        width: 44px;
        height: 44px;
        margin-top: -16px;
      }
      .nav-item-title {
        margin-top: 16px;
        margin-bottom: 6px;
        font-size: 18px;
      }
      div:last-child {
        font-size: 10px;
      }
    }
  }
}
</style>
