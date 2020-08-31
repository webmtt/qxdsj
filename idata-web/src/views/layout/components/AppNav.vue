<template>
  <div class="app-nav">
    <div class="app-nav-content">
      <div class="nav-content">
        <el-menu
          height="50"
          :router="true"
          :default-active="active_top_route"
          mode="horizontal"
          background-color="#242a37"
          text-color="#fff"
          active-text-color="#0d95ff"
        >
          <el-menu-item
            v-for="(item, index) in list"
            :key="index"
            :index="item.path"
          >{{ item.title }}</el-menu-item>
        </el-menu>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  title: 'AppNav',
  data() {
    return {
      active_top_route: '/home',
      locationImgUrl: require('@/assets/images/location.png'),
      weatherImgUrl: require('@/assets/images/weather.png'),
      defauleList: [
        {
          id: 8,
          path: '/api',
          title: '我的数据',
          show: false
        }
      ],
      navData: [
        {
          id: '1',
          path: '/home',
          title: '首页',
          show: true
        },
        // {
        //   id: 2,
        //   path: '/specialProduct',
        //   title: '专题产品',
        //   show: true
        // },
        {
          id: 3,
          path: '/productLibrary',
          title: '产品服务',
          show: true
        },

        {
          id: 5,
          path: '/dataDown',
          title: '数据服务',
          show: true
        },
        {
          id: 6,
          path: '/industryApplication',
          title: '行业服务',
          show: true
        },
        {
          id: 7,
          path: '/aboutUs',
          title: '关于我们',
          show: true
        },
        // {
        //   id: 17,
        //   path: '/myApi',
        //   title: 'myData',
        //   show: true
        // }
        // {
        //   id: 8,
        //   path: '/northCloud',
        //   title: '云上北疆',
        //   show: true
        // }
      ]
    }
  },
  computed: {
    list() {
      return this.$store.state.user.name ? this.setList() : this.navData
    }
  },
  watch: {
    $route: {
      handler(val, oldval) {
        if (val.name === '/industryApplication') {
          this.active_top_route = '/industryApplication'
        }
      },
      // 深度观察监听
      deep: true
    }
  },
  created() {
    const id = this.$store.state.user.pId || null
    const name = this.$store.state.user.pName || null
    if (id && name) {
      this.defauleList.push({
        id: 10,
        path: `/productLibrary/detail/${id}`,
        title: '我的产品',
        show: true
      })
    }
  },
  mounted() {
    this.active_top_route = this.$route.path
  },
  methods: {
    setList() {
      const arr = this.navData.concat(this.defauleList)
      arr.sort((a, b) => {
        return a.id - b.id
      })
      return arr
    }
  }
}
</script>
<style>
.el-menu-item {
  height: 50px !important;
}
.el-menu--horizontal > .el-menu-item {
  line-height: 50px;
}
.el-menu--horizontal > .el-menu-item.is-active {
  border-bottom: none !important;
}
.el-menu--horizontal > .el-menu-item.is-active::after {
  position: absolute;
  top: 48px;
  width: 30px;
  height: 2px;
  left: 50%;
  transform: translateX(-50%);
  content: "";
  background-color: #0d95ff;
}
.el-menu.el-menu--horizontal {
    border-bottom: none;
}
</style>

<style scoped lang='scss'>
.app-nav {
  width: 100%;
  height: 50px;
  background-color: #242a37;
  box-sizing: border-box;
  .app-nav-content {
    box-sizing: border-box;
    height: 48px;
    width: 1300px;
    min-width: 1200px;
    margin: 1px auto 1px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .nav-content {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      color: #ffffff;
    }
    .app-weather {
      height: 100%;
      width: 210px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      & > div {
        height: 100%;
        color: #ffffff;
        font-size: 14px;
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      .header-location {
        flex: 1;
        margin-right: 30px;
        img {
          width: 18px;
          height: 18px;
        }
      }
      .header-weather {
        width: 80px;
        img {
          width: 36px;
          height: 24px;
        }
      }
    }
  }
}
</style>
