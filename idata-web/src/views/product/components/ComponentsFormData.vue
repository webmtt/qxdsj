<template>
  <div class="components-form">
    <components
      :is="componentActive.compontentForm"
      :component-active="componentActive"
      @getCompontentData="getCompontentData"
    />
  </div>
</template>

<script>

export default {
  name: 'ComponentsFormData',
  components: {
  },
  props: {
    componentActive: {
      type: Object,
      default: () => { }
    }
  },
  methods: {
    getCompontentData(d) {
      this.$emit('getCompontentData', {
        selectData: { ...d },
        activeData: this.componentActive
      })
    }
  }
}
</script>

<style scoped lang="scss">
.components-form {
  width: 100%;
  padding: 20px 0;
  min-height: 90vh;
  .compontent-header {
    width: 100%;
    height: 60px;
    padding: 20px 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .compontent-title {
      font-size: 20px;
      color: #333;
    }
  }
  .compontent-header-search {
    // background-color: #f2f2f2;
    // height: 100px;
    border-radius: 6px;
    padding: 0 20px 0px;
    // justify-content: flex-start;
    // margin-bottom: 20px;
    & > div {
      display: flex;
      width: 500px;
      & > div {
        margin-right: 10px;
      }
      // padding: 0 20px;
      .search-label {
        padding: 0 0 15px 0;
        font-size: 14px;
        color: #333;
        font-weight: bold;
      }
    }
  }
  .search-content {
    transition: 0.3s;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.3);
  }
  .data-content {
    width: 100%;
    display: flex;
    justify-content: flex-start;
    .side-bar-form {
      width: 200px;
    }
    .data-page {
      flex: 1;
      .data-list {
        width: 100%;
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
        .data-list-item-type-1 {
          width: 300px;
          height: 200px;
          margin: 0 20px 20px 0;
          cursor: pointer;
          border: 2px dashed transparent;
          transition: all 0.3s;
          position: relative;
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
            // z-index: 3000;
            color: #f2f2f2;
          }
          img {
            width: 100%;
            height: 200px;
            transition: all 0.3s;
          }
          &:hover {
            border-color: salmon;
            border-radius: 4px;
            img {
              transform: scale(0.9, 0.9);
            }
          }
        }
        & > .data-list-item-type-1:nth-child(4n) {
          margin-right: 0;
        }
      }
    }
  }
}
</style>
