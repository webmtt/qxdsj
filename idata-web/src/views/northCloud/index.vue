<template>
  <div class="home-page">
    <div class="home-banner" />
    <nav-sub @handleScroll="handleScroll" />
    <div ref="dataDirectoryExchange">
      <data-directory-exchange />
    </div>
    <div ref="dataResourceExchangeStatus">
      <data-resource-exchange />
    </div>
    <div ref="fileResourceExchangeDetails">
      <file-resource-exchange />
    </div>
    <div ref="libraryTableResourceExchangeDetails">
      <library-table-resource-exchange />
    </div>
    <div ref="interfaceResourceExchangeDetails">
      <interface-resource-exchange-details />
    </div>
  </div>
</template>

<script>

import NavSub from './components/NavSub.vue'

import DataDirectoryExchange from './components/dataDirectoryExchange.vue'
import DataResourceExchange from './components/dataResourceExchange.vue'
import FileResourceExchange from './components/fileResourceExchange.vue'
import LibraryTableResourceExchange from './components/libraryTableResourceExchange.vue'
import InterfaceResourceExchangeDetails from './components/interfaceResourceExchangeDetails.vue'

export default {
  name: 'ProductLibrary',
  components: {
    NavSub,
    DataDirectoryExchange,
    DataResourceExchange,
    FileResourceExchange,
    LibraryTableResourceExchange,
    InterfaceResourceExchangeDetails
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
    background-image: url('../../assets/images/cloudBg.png');
    background-origin: content-box;
    background-position: center;
    background-repeat: no-repeat;
    background-clip: content-box;
    background-size: cover;
  }
  margin-bottom: 20px;
}
</style>
