<template>
  <div class="dialog-components-list">
    <div
      v-for="(item, index) in compontentsList"
      :key="index"
      class="compontent-item"
      :data-content="item.chName"
      @click="handleComponent(item)"
    >
      <components :is="item.compontentName" />
    </div>
  </div>
</template>

<script>

// import ToolLibrary from '@/utils/toolLibrary.js'
import CompontentsInfo from '../product-create/compontentsInfo.js'
export default {
  name: 'ComponentsList',
  data() {
    return {
      compontentsList: []
    }
  },
  mounted() {
    this.compontentsList = CompontentsInfo
  },
  methods: {
    handleComponent(item) {
      this.$emit('selectedComponent', item)
    }
  }

}
</script>

<style  scoped  lang="scss">
.dialog-components-list {
  width: 100%;
  min-width: 800px;
  height: auto;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  // background-color: #f2f2f2;
  .compontent-item {
    width: 300px;
    height: 200px;
    border: 2px dashed #f2f2f2;
    margin-bottom: 8px;
    position: relative;
    overflow: hidden;
    padding: 6px;
    border-radius: 6px;
    transition: all 0.3s;
    cursor: pointer;
    background-color: #ffffff;
    &:hover {
      border: 2px dashed sandybrown;
      padding: 6px;
      .compontent-name {
        display: block;
      }
    }
    &::before{
      display: none;
      position: absolute;
      content:attr(data-content);
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      font-size: 20px;
      padding-top: 20px;
      z-index: 3000;
      text-align: center;
      padding-top: 10px;
      color: #f2f2f2;

      background-color: rgba(0, 0, 0, 0.4);
    }
    &:hover::before {
      display: block;
    }
    &:hover::after {
      content: '+';
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      font-size: 60px;
      background-color: rgba(0, 0, 0, 0.1);
      text-align: center;
      line-height: 200px;
      z-index: 3000;
      color: #f2f2f2;
    }
    & > div {
      border-radius: 6px;
      overflow: hidden;
    }

  }
}
</style>
