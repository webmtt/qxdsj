<template>
  <div class="h-header-type">
    <div class="header-content">
      <img :src="imgUrl">
      <span>这里是标题</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HHeaderType',
  data() {
    return {
      imgUrl: require('@/assets/create/1.jpg')
    }
  }
}
</script>

<style scoped lang="scss">
  .h-header-type{
    width:100%;
    .header-content {
      width: 100%;
      height: 50px;
      background-color: sandybrown;
      display: flex;
      justify-content: flex-start;
      align-items: center;
      img{
        width: 30px;
        height: 30px;
        margin-left:20px;
      }
      span {
        margin-left:15px;
        font-size: 16px;
        color: #333;
      }
    }
  }
</style>
