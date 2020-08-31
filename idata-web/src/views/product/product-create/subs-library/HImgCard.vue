<template>
  <div class="h-img-card">
    <div class="card-img">
      <img :src="imgUrl">
    </div>
    <div class="card-text">
      <div class="card-name">名称</div>
      <div class="card-sbuInfo">hafhafeoaijoidnfaidnfaio</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HImgCard',
  data() {
    return {
      imgUrl: require('@/assets/create/4.jpg')
    }
  }
}
</script>

<style scoped lang="scss">
  .h-img-card{
    width:100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    border: 1px solid #f2f2f2;
    background-color: #ffffff;
    .card-img{
      width: 100%;
      height: 70%;
      background-color: darkcyan;
      img {
        width: 100%;
        height: 100%;
      }
    }
    .card-text{
      width: 100%;
      height: 30%;
      padding: 0 15px;
      .card-name{
        height: 60%;
        width: 100%;
        line-height: 40px;
        color: #555;
      }
      .card-sbuInfo{
        height: 60%;
        width: 100%;
        color: #999;
      }
    }
  }
</style>
