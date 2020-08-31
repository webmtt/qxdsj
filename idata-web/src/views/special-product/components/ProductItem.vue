<template>
  <div class="product-page">
    <div class="product-item-body">
      <div
        v-for="(item, index) in dataList"
        :key="index"
        class="product-item"
        @click="handlePreview(item)"
      >
        <img :src="item.imgUrl" />
        <div class="list-info">
          <h4>{{ item.title }}</h4>
          <div class="list-info-box">{{ item.key }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProductItem",
  props:{
    dataList: {
      type: Array,
      default: []
    }
  },
  methods: {
    handlePreview(item) {
      this.$emit('handlePreview', item)
    }
  }
};
</script>

<style scoped  lang="scss">
.product-page {
  width: 100%;
  .special-product {
    width: 1300px;
    margin: 0 auto;
    border: 1px solid red;
  }
  .product-item-body {
    display: flex;
    justify-content: flex-start;
    width: 100%;
    height: auto;
    flex-wrap: wrap;
    & > .product-item {
      width: 310px;
      height: 300px;
      overflow: hidden;
      border-radius: 4px;
      border: 1px solid #ebebeb;
      margin-bottom: 30px;
      display: flex;
      margin-right: 20px;
      justify-content: flex-start;
      flex-direction: column;
      img {
        width: 100%;
        height: 220px;
        margin-bottom: 10px;
      }
      .list-info {
        width: 100%;
        height: 58px;
        padding: 0 10px;
        overflow: hidden;
        h4 {
          height: 32px;
          line-height: 36px;
          padding: 0;
          margin: 0;
          font-size: 16px;
          color: #333;
        }
        .list-info-box {
          width: 100%;
          font-size: 14px;
          color: #888;
          height: 26px;
          line-height: 20px;
          overflow: hidden;
          span {
            padding: 0 26px 0 0;
            position: relative;
            &::before {
              position: absolute;
              content: "|";
              right: 10px;
              top: -3px;
            }
            &:last-child::before {
              position: absolute;
              content: "";
            }
          }
        }
      }
    }
    & > .product-item:nth-child(4n) {
      margin-right: 0;
    }
  }
}
</style>
