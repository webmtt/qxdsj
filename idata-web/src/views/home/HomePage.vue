<template>
  <div class="home-page">
    <div class="home-banner" />

    <nav-sub @handleScroll="handleScroll" />
    <div>
      <hot-products />
    </div>
    HomePage
  </div>
</template>

<script>

import NavSub from './components/NavSub.vue'

import HotProducts from './components/HotProducts.vue'
export default {
  name: 'HomePage',
  components: {
    NavSub,
    HotProducts
    // WeatherCarousel,
    // DataServeList,
    // HotProduct,
    // PrecipitationSurvey,
    // ApiServer,
    // FeaturedProducts,
    // DataDown
  },
  methods: {
    handleScroll(item) {
      if (item.subID) {
        document.getElementById(`${item.subID}`).scrollIntoView()
      } else {
        this.$router.push({ path: `${item.link}` })
      }
    }
  }
}
</script>

<style scoped  lang="scss">
.home-page {
  width: 100%;
  .home-banner {
    max-width: 1920px;
    height: 400px;
    min-width: 1300px;
    background-image: url('../../assets/images/bg.png');
    background-origin: content-box;
    background-position: center;
    background-repeat: no-repeat;
    background-clip: content-box;
    background-size: cover;
  }
}
</style>
