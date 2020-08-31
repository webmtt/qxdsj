<template>
  <div class="nav-sub">
    <div class="nav-carousel-content">
      <div
        v-for="item in dataList"
        :key="item.id"
        class="nav-item"
        @click="handleScroll(item)"
      >
        <img :src="item.imgUrl">
        <div class="nav-item-title">{{ item.title }}</div>
        <div style="font-size:14px">{{ item.subtitle }}</div>
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
          id: 2,
          imgUrl: require('@/assets/images/libraryicon2.png'),
          title: '专题产品',
          subtitle: 'Special products',
          subID: 'specialProduct'
        },
        {
          id: 5,
          imgUrl: require('@/assets/images/libraryicon5.png'),
          title: '最新产品',
          subtitle: 'New Products',
          subID: 'newProducts'
        },
        {
          id: 4,
          imgUrl: require('@/assets/images/libraryicon4.png'),
          title: '产品清单',
          subtitle: 'Product List',
          subID: 'serverProductsList'
        },
        {
          id: 6,
          imgUrl: require('@/assets/images/libraryicon6.png'),
          title: '热门产品',
          subtitle: 'Hot Products',
          subID: 'hotPorducts'
        },
        {
          id: 1,
          imgUrl: require('@/assets/images/libraryicon1.png'),
          title: '命名规范',
          subtitle: 'Naming Conventions',
          subID: 'ServerProductsDefinition'
        }
        // {
        //   id: 2,
        //   imgUrl: require('@/assets/images/libraryicon2.png'),
        //   title: '准退管理',
        //   subtitle: 'Must Refund Management',
        //   subID: 'AccessManagement'
        // }
      ]
    }
  },
  created() {
    // const userInfo = getUserinfo() ? JSON.parse(getUserinfo()) : null
    // if (userInfo && userInfo.iscreate) {
    //   this.dataList.splice(2, 0, {
    //     id: 7,
    //     imgUrl: require('@/assets/images/libraryicon3.png'),
    //     title: '创建产品',
    //     subtitle: 'Latest interface',
    //     subID: null,
    //     link: 'productLibrary/create'
    //   })
    // }
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
  margin-top: -70px;
  // position: relative;
  // background-color: #242a37;
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
        content: "";
        top: 0;
        right: -1px;
        width: 1px;
        height: 100%;
        background-color: #2f3645;
      }
      &::before {
        position: absolute;
        content: "";
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
      // div:last-child {
      //   font-size: 10px;
      // }
    }
  }
}
</style>
