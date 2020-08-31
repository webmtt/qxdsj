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
export default {
  name: 'NavSub',
  data() {
    return {

      dataList: [
        {
          id: 1,
          imgUrl: require('@/assets/images/navhotproduct.png'),
          title: '热门产品',
          subtitle: 'Popular  product',
          subID: 'hotProduct'
        },
        {
          id: 2,
          imgUrl: require('@/assets/images/homeAPi4.png'),
          title: '行业应用',
          subtitle: 'Industry  application',
          subID: 'precipitationSurvey'
        },
        {
          id: 3,
          imgUrl: require('@/assets/images/navapi.png'),
          title: '我的服务',
          subtitle: 'API  service',
          subID: 'apiServer'
        },
        {
          id: 4,
          imgUrl: require('@/assets/images/navDateDown.png'),
          title: '数据服务',
          subtitle: 'Data  download',
          subID: 'dataDown'
        },
        {
          id: 5,
          imgUrl: require('@/assets/images/navproduct.png'),
          title: '特色产品',
          subtitle: 'Featured  products',
          subID: 'featuredProducts'
        },
        {
          id: 6,
          imgUrl: require('@/assets/images/homeAPi1.png'),
          title: '服务流程',
          subtitle: 'Service process',
          subID: 'serviceProcessRef'
          // link: '/api'
        }
      ]
    }
  },
  methods: {
    handleScroll(item) {
      this.$emit('handleScroll', item)
    }
  }
}
</script>

<style scoped  lang="scss">
  .nav-sub{
    width:100%;
    height: auto;
        // margin-top: -70px;
    // position: relative;
    background-color: #242a37;
    .nav-carousel-content{
      width: 1300px;
      margin: 0 auto;
      height: 150px;
      background-color: #242a37;
      display: flex;
      justify-content: space-around;
      align-items: center;
      .nav-item{
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
        }div:last-child {
          font-size:10px;
        }
      }
    }
  }
</style>
