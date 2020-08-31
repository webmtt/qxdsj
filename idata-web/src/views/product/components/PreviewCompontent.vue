<template>
  <div ref="preview" class="preview-compontent">
    <div v-if="previewList.length" class="preview-content">
      <div
        v-for="item in previewList"
        :key="item.id"
        class="component-item"
        :style="{ height: item.h + 'px', width: item.w + 'px', top: item.y + 'px', left: item.x + 'px' }"
      >
        <components :is="item.compontentName" :value-data="item.compontentData" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PreviewCompontent',
  props: {
    componentsList: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      fullWidth: 1200,
      previewList: []
    }
  },
  created() {
    this.previewList = JSON.parse(JSON.stringify(this.componentsList))
  },
  mounted() {
    // this.setResizeComponents()
    this.fullWidth = this.$refs.preview.offsetWidth
    this.previewList.forEach(item => {
      item.wf = this.setNumLast(item.w) / this.fullWidth
      item.wx = this.setNumLast(item.x) / this.setNumLast(item.w)
      item.wh = this.setNumLast(item.w) / this.setNumLast(item.h)
      item.wy = this.setNumLast(item.y) / this.setNumLast(item.h)
    })
    window.onresize = () => {
      this.setResizeComponents()
    }
  },
  methods: {
    // 规整宽度函数
    setResizeComponents() {
      this.fullWidth = this.$refs.preview.offsetWidth
      this.previewList.forEach(item => {
        item.w = this.setNumLast(item.wf * this.fullWidth)
        item.h = this.setNumLast(item.w / item.wh)
        item.x = this.setNumLast(item.wx * item.w)
        item.y = this.setNumLast(item.wy * item.h)
      })
    },
    // 获取最后一个
    setNumLast(n) {
      return ((n / 10).toFixed()) * 10
    }
  }
}
</script>

<style scoped  lang="scss">
.preview-compontent{
  width: 100%;
  max-width: 1300px;
  min-width: 320px;
  height: auto;
  margin: 0 auto;
  padding:10px;
  position: relative;
  box-sizing: border-box;
  background-color: #ffffff;
  margin-bottom: 60px;
	.preview-content{
    width: 100%;
    max-width: 1300px;
    min-width: 320px;
		margin: 0 auto;
		height: auto;
		position: relative;
		.component-item{
			position: absolute;
		}
	}

}
</style>
