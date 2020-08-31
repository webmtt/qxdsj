<template>
  <div class="home-page">
    <div class="home-banner" />
    <nav-sub @handleScroll="handleScroll" />
    <div ref="specialProduct">
      <special-product />
    </div>
    <div ref="newProducts">
      <new-products />
    </div>

    <div ref="serverProductsList">
      <server-products-list />
    </div>
    <div ref="hotPorducts">
      <hot-products />
    </div>
    <div ref="ServerProductsDefinition">
      <server-products-definition />
    </div>
    <div ref="AccessManagement">
      <!-- <access-management /> -->
    </div>
  </div>
</template>

<script>

import NavSub from './components/NavSub.vue'

import HotProducts from './components/HotProducts.vue'
import NewProducts from './components/NewProducts.vue'
import ServerProductsList from './components/ServerProductsList.vue'
import ServerProductsDefinition from './components/ServerProductsDefinition.vue'
// import AccessManagement from './components/AccessManagement.vue'
import SpecialProduct from '../special-product/index.vue'
export default {
  name: 'ProductLibrary',
  components: {
    NavSub,
    HotProducts,
    NewProducts,
    ServerProductsList,
    ServerProductsDefinition,
    // AccessManagement,
    SpecialProduct
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
        this.$refs[item.subID].scrollIntoView()
        // document.getElementById(`${item.subID}`).scrollIntoView()
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
    background-image: url("../../assets/images/bg.png");
    background-origin: content-box;
    background-position: center;
    background-repeat: no-repeat;
    background-clip: content-box;
    background-size: cover;
  }
}
</style>
