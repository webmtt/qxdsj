<template>
  <div class="h-img-info">

    <div class="h-img-info-img">
      <img :src="imgUrl">
    </div>
    <div class="h-img-info-text">这里的风景真美丽-这里的风景真美丽-这里的风景真美丽这里的风景真美丽这里的风景真美丽这里的风景真美丽</div>
  </div>
</template>

<script>
export default {
  name: 'HImgInfo',
  data() {
    return {
      imgUrl: require('@/assets/create/4.jpg')
    }
  }
}
</script>

<style  scoped  lang="scss">
  .h-img-info{
      width:100%;
      height: 100%;
      display: flex;
      justify-content: flex-start;
      align-items: center;
      background-color: #ffffff;
      .h-img-info-img{
        flex: 0.382;
        // max-width: 200px;
        padding-right: 15px;
        img {
          width: 100%;
          height: 100%;
        }
      }
      .h-img-info-text{
          flex: 0.618;
          overflow: hidden;
      }
  }
</style>
