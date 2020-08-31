<template>
  <div class="side-bar">
    <div class="side-bar-content">
      <div v-for="item in navData" :key="item.id">
        <div class="bar-header" :class="{'active-bar-header':activeheader === item.id}" @click="selectHeader(item)">
          <!-- <img src="" alt=""> -->
          <span>{{ item.title }}</span>
          <i class="el-icon-arrow-down" />
        </div>
        <div v-if="item.children && item.children.length" class="bar-children" :style="{ height: isOpen && activeheader === item.id ? item.height: 0}">
          <div v-for="innerItem in item.children" :ref="item.id+'ref'" :key="innerItem.id" class="bar-children-item" @click="selectChildren(innerItem)">
            <div :style="{color: activeChild === innerItem.id ? '#28cce9':'#ffffff'}">{{ innerItem.title }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'SideBar',
  props: {
    navList: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      activeheader: '',
      activeChild: '',
      navData: [],
      isOpen: false
    }
  },
  created() {
    if (this.navList.length) {
      this.navList.forEach((item, index) => {
        this.navData.push({
          id: index + '',
          title: item.title,
          height: item.list.length ? item.list.length * 58 + 'px' : 0,
          children: item.list.length ? item.list.map((d, i) => {
            return {
              id: i + '',
              title: d
            }
          }) : []
        })
      })
    }

    // getNavList().then(res => {
    //   if (res.data && res.data.length) {
    //     this.navData = []
    //     this.activeheader = res.data[0].categoryid + ''
    //     this.handleEmit({
    //       type: 'nav',
    //       data: {
    //         id: this.activeheader
    //       }
    //     })
    //     res.data.forEach(item => {
    //       this.navData.push({
    //         id: item.categoryid + '',
    //         iconurl: item.iconurl,
    //         title: item.chnname,
    //         height: item.list.length ? item.list.length * 58 + 'px' : 0,
    //         children: item.list.length ? item.list.map(d => {
    //           return {
    //             id: d.categoryid + '',
    //             title: d.chnname
    //           }
    //         }) : []
    //       })
    //     })
    //   }
    // })
  },
  // mounted
  methods: {
    selectHeader(data) {
      this.isOpen = this.activeheader === data.id ? !this.isOpen : true
      this.activeheader = data.id
      this.handleEmit({
        type: 'nav',
        data: data
      })
    },
    selectChildren(data) {
      this.activeChild = data.id
      this.handleEmit({
        type: 'navChildren',
        data: data
      })
    },
    handleEmit(obj) {
      this.$emit('select', obj)
    }
  }
}
</script>

<style scoped  lang="scss">
.side-bar {
  width: 100%;
  min-height: 88vh;
  // margin-bottom: 80px;
  background-color: #31394b;
  .side-bar-content {
    width: 100%;
    .bar-header {
      width: 100%;
      height: 58px;
      display: flex;
      align-items: center;
      color: #ffffff;
      border-bottom: 1px solid #1a1f32;
      position: relative;
      cursor: pointer;

      // img {
      //   width: 20px;
      //   height: 20px;
      //   margin-left: 10px;
      // }
      span {
        margin-left: 15px;
      }
      i {
        color: #ffffff;
        position: absolute;
        right: 10px;
      }
      &:hover {
        background-image: url('../../../assets/images/sidebarbg.png');
        background-origin: content-box;
        background-position: center;
        background-repeat: no-repeat;
        background-clip: content-box;
        background-size: cover;
        &::after {
          position: absolute;
          content: '';
          top: 0;
          left: 0;
          width: 4px;
          height: 100%;
          background-color: #28cce9;
        }
      }
    }
    .active-bar-header {
      background-image: url('../../../assets/images/sidebarbg.png');
      background-origin: content-box;
      background-position: center;
      background-repeat: no-repeat;
      background-clip: content-box;
      background-size: cover;
      &::after {
        position: absolute;
        content: '';
        top: 0;
        left: 0;
        width: 4px;
        height: 100%;
        background-color: #28cce9;
      }
    }
    .bar-children {
      width: 100%;
      transition: all 0.3s;
      height: 0;
      overflow: hidden;
      .bar-children-item {
        // width: 100%;
        height: 58px;
        line-height: 58px;
        color: #ffffff;
        padding-left: 46px;
        font-size: 14px;
        border-bottom: 1px solid #1a1f32;
        cursor: pointer;
      }
    }
  }
}
</style>

