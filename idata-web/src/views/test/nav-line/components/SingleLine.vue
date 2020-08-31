
<template>
  <div class="single-line">
    <div class="single-content">
      <div class="single-item" :style="styleObj">
        <span
          v-for="(item, index) in singleList"
          :key="index"
          :class="{'is-selected':activeList.findIndex(selectedItem => selectedItem.id === item.id) > -1}"
          @click="selectedData(item)"
        >{{ item.title }}</span>
      </div>
      <i v-if="showIcon" class=" show-all el-icon-arrow-left" :style="styleIconObj" @click="changeHeight = changeHeight ==='50px'?'auto':'50px'" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'SingleLine',
  props: {
    singleList: {
      type: Array,
      default: () => []
    },
    activeList: {
      type: Array,
      default: () => []
    },
    singleSelect: {
      type: Boolean,
      default: false
    },
    showIcon: {
      type: Boolean,
      default: false
    },
    singleId: {
      type: String,
      default: '0'
    }
  },
  data() {
    return {
      changeHeight: '50px'
    }
  },
  computed: {
    styleObj() {
      return {
        height: this.changeHeight
      }
    },
    styleIconObj() {
      return {
        Transform: this.changeHeight === '50px' ? 'rotate(0deg)' : 'rotate(-90deg)'
      }
    }
  },
  methods: {
    selectedData(data) {
      this.$emit('selectedData', {
        id: this.singleId,
        data: data
      })
    }
  }
}
</script>

<style scoped  lang="scss">
.single-line {
  width: 100%;
  // padding: 0 20px;
  margin-bottom: 10px;
  border-radius: 6px;
  .single-content {
    width: 100%;
    display: flex;
    justify-content: space-between;
      position: relative;
    .single-item {
      flex: 1;
      display: flex;
      justify-content: flex-start;
      flex-wrap: wrap;
      transition: all 0.3s;
      border-bottom: 1px solid #f2f2f2;
      padding-top: 10px;
      height: 50px;
      overflow: hidden;
    }
    .show-all {
      position: relative;
      display: inline-block;
      top: 12px;
      right: 10px;
      height: 20px;
      width: 20px;
      line-height: 20px;
      background-color: turquoise;
      color: #fff;
      border-radius: 50%;
      font-size: 20px;
      cursor: pointer;
      transition: all 0.3s;
    }
    span {
      font-size: 14px;
      margin: 0 6px 8px 6px;
      padding: 6px;
      text-align: center;
      color: #999;
      background-color: #ffffff;
      border: 1px solid transparent;
      cursor: pointer;
      transition: all 0.3s;
      border-radius: 4px;
      // &:hover {
      //   color: #fff;
      //   background-color: salmon;
      //   border: 1px solid #f2f2f2;
      // }
    }
    .is-selected {
      color: #fff;
      background-color: salmon;
      border: 1px solid #f2f2f2;
    }
    &:hover {
      box-shadow: 0px 0px 4px 4px#f2f2f2;
    }
  }
}
</style>
