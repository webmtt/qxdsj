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
  name: 'NavDown',
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
          singleId: 'id1',
          showIcon: false, // 显示更多按钮
          singleSelect: false,
          activeList: [],
          children: []
        },
        {
          singleId: 'id2',
          showIcon: true,
          singleSelect: true,
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
      this.navList[1].activeList.push(this.navList[0].children[0].children[0])
    }
  },
  methods: {
    setLineTwo(target) {
      this.navList[1].children = target
    },
    // // 选中的数据
    selectedData(data) {
      if (data.id === 'id1') {
        // const index = this.navList[0].activeList.findIndex(activeItem => activeItem.id === data.data.id)
        // if (index > -1) {
        //   const a = this.navList[1].activeList.findIndex(aa => aa.type === data.data.type)
        //   if (a <= -1) {
        //     this.navList[0].activeList.splice(index, 1)
        //   }
        // } else {
        //   this.navList[0].activeList.push(data.data)
        // }
        this.navList[0].activeList = []
        this.navList[1].activeList = []
        this.navList[0].activeList.push(data.data)
        this.setLineTwo(data.data.children)
      }
      if (data.id === 'id2') {
        const c = this.navList[1].activeList.findIndex(activeItem => activeItem.id === data.data.id)
        if (c > -1) {
          this.navList[1].activeList.splice(c, 1)
          return
        }
        // const b = this.navList[1].activeList.findIndex(aa => aa.type === data.data.type)
        // if (b > -1) {
        //   this.navList[1].activeList.splice(b, 1)
        // }
        this.navList[1].activeList.push(data.data)
      }
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
