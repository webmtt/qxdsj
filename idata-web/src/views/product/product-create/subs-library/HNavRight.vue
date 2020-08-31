<template>
  <div class="h-nav-right">
    <div v-for="item in dataList" :key="item.id" class="nav-item" :index="item.id">
      <a class="nav-main">
        <img>
        <span>{{ item.text }}</span>
      </a>
      <div v-if="item.children.length" class="nav-sub">
        <div v-for="child in item.children" :key="child.id">{{ child.text }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HNavRight',
  data() {
    return {
      activeId: '1',
      dataList: [
        {
          id: '1',
          text: '这里欢迎您',
          icon: '',
          children: [
            {
              id: '11',
              text: 'chart-1',
              icon: ''
            },
            {
              id: '12',
              text: 'chart-2',
              icon: ''
            },
            {
              id: '13',
              text: 'chart-3',
              icon: ''
            }
          ]
        },
        {
          id: '2',
          text: '这里欢迎您',
          icon: '',
          children: [
            {
              id: '21',
              text: 'chart-2-1',
              icon: ''
            },
            {
              id: '22',
              text: 'chart-2-2',
              icon: ''
            },
            {
              id: '23',
              text: 'chart-2-3',
              icon: ''
            }
          ]
        }
      ]
    }
  },
  methods: {
    handleOpen(key, keyPath) {
    },
    handleClose(key, keyPath) {
    }
  }
}
</script>

<style scoped  lang="scss">
.h-nav-right {
  width: 100%;
  height: 100%;
  // overflow: hidden;
  // padding:0 15px;
  border: 1px solid #f2f2f2;

  background-color: #ffffff;
  border: 1px solid #f2f2f2;
  .nav-item {
    position: relative;
    left: 0;
    width: 100%;
    .nav-main {
      width: 100%;
      padding: 0 15px;
      height: 50px;
      background-color: #ffffff;
      color: #999;
      display: flex;
      justify-content: flex-start;
      align-items: center;
      border-bottom: 1px solid #f2f2f2;
      img {
        width: 20px;
        height: 20px;
        background-color: salmon;
        border: none;
        margin-right: 15px;
      }
    }
    .nav-sub {
      display: none;
      position: absolute;
      top: -1px;
      left: 100%;
      width: 100%;
      height: auto;
      background-color: #ffffff;
      color: #999;
      border: 1px solid #f2f2f2;
      & > div {
        width: 100%;
        height: 50px;
        line-height: 50px;
        padding: 0 15px;
        border-bottom: 1px solid #f2f2f2;
        background-color: #ffffff;
        color: #999;
        &:hover {
          background-color: #f2f2f2;
          color: #ffffff;
        }
      }
    }
    &:hover {
      background-color: #f2f2f2;
      color: #ffffff;
      .nav-sub {
        display: block;
      }
    }
  }
}
</style>
