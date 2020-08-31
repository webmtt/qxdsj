<template>
  <div class="compontent-box h-iframe">
    <iframe v-if="iframeUrl" :src="iframeUrl" frameborder="0" width="100%" height="100%" />
  </div>
</template>

<script>
export default {
  name: "HIframe",
  props: {
    valueData: {
      required: false,
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      iframeUrl: null
    };
  },
  watch: {
    valueData: {
      handler(newVal, oldVal) {
        if (this.valueData && this.valueData.length) {
          this.iframeUrl = null;

          setTimeout(() => {
            this.iframeUrl = this.valueData[0].list[0];
          });
        } else {
          this.iframeUrl = null;
        }
      },
      deep: true
    }
  },
  created() {
    if (this.valueData.length && this.valueData[0].list.length) {
      this.iframeUrl = this.valueData[0].list[0];
    }
  }
};
</script>

<style scoped  lang="scss">
.h-iframe {
  width: 100%;
  height: 100%;
}
</style>
