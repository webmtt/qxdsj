<template>
  <div class="nav-line">
    <div v-for="(item, index) in navList" :key="index">
      <single-line v-if="item.children && item.children.length" :single-list="item.children" :single-id="item.singleId" :show-icon="item.showIcon" :active-list="item.activeList" :single-select="item.singleSelect" @selectedData="selectedData" />
    </div>
  </div>
</template>

<script>
import SingleLine from './components/SingleLine.vue'
export default {
  name: 'NavLine',
  components: { SingleLine },
  props: {
    dataList: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      initId: 0,
      navData: [],
      navList: [
        {
          singleId: '1',
          showIcon: false, // 显示更多按钮
          singleSelect: true,
          activeList: [],
          children: []
        },
        {
          singleId: '2',
          showIcon: true,
          singleSelect: false,
          activeList: [],
          children: []
        },
        {
          singleId: '3',
          showIcon: true,
          singleSelect: false,
          activeList: [],
          children: []
        }
      ]
    }
  },
  mounted() {
    this.navList[0].children = this.dataList
    this.navList[0].activeList.push(this.navList[0].children[0])
    if (this.navList[0].children[0].children && this.navList[0].children[0].children.length) {
      this.setLineTwo(this.navList[0].children[0].children)
    }
  },
  methods: {
    setLineTwo(target) {
      this.navList[1].children = target
      if (target && target.length) {
        this.setLineThree(target[0].children)
      }
    },
    setLineThree(target) {
      this.navList[2].children = target
    },
    // 选中的数据
    selectedData(data) {
      if (data.id === '1' && data.data.type === 'product') {
        this.navList[1].activeList = []
        this.navList[2].activeList = []
      }
      if (data.id === '1') {
        if (data.data.children && data.data.children.length) {
          this.setLineTwo(data.data.children)
        }
      }
      if (data.id === '2') {
        if (data.data.children && data.data.children.length) {
          this.setLineThree(data.data.children)
        }
      }
      this.navList.forEach((item, i) => {
        if (item.singleId === data.id) {
          const index = item.activeList.findIndex(activeItem => activeItem.title === data.data.title)
          if (index > -1) {
            let hasChild = false
            if (this.navList[i + 1]) {
              this.navList[i + 1].activeList.forEach(nextItem => {
                if (data.data.children.length) {
                  hasChild = data.data.children.findIndex(d => d.title === nextItem.title) > -1
                }
              })
            }
            if (!hasChild) {
              item.activeList.splice(index, 1)
            }
          } else {
            if (item.singleSelect) {
              item.activeList = []
            }
            item.activeList.push(data.data)
          }
        }
      })
      this.$emit('navSelected', this.navList)
    }
  }
}
</script>

<style scoped  lang="scss">
.nav-line {
  width: 100%;
  margin-top: 10px;
  // margin-bottom: 20px;
}
</style>
