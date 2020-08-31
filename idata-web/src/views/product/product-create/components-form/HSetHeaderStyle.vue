<template>
  <div class="set-header-data">
    <el-collapse>
      <el-collapse-item title="设置标题样式" name="1">
        <div>
          <el-form class="demo-form-inline input-with-select" label-width="120px">
            <el-form-item label="字体颜色">
              <el-input v-model="setColor" placeholder="请输入字体颜色" @change="handleChange" />
              <el-color-picker v-model="setColor" style="margin-right:30px" @change="handleChange" />
            </el-form-item>
            <el-form-item label="标题的背景色">
              <el-input v-model="setBackgroundColor" placeholder="请输入标题的背景色" @change="handleChange" />
              <el-color-picker v-model="setBackgroundColor" style="margin-right:30px" @change="handleChange" />
            </el-form-item>
          </el-form>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>
<script>

export default {
  name: 'HSetHeaderStyle',
  data() {
    return {
      setColor: '#008B8B',
      setBackgroundColor: '#f1f1f1'
    }
  },
  methods: {
    handleChange() {
      const obj = {
        color: this.setColor,
        backgroundColor: this.setBackgroundColor
      }
      this.$emit('handleChangeStyle', obj)
    }
  }
}
</script>
<style>
.set-header-data .el-collapse-item__header {
  font-weight: bold;
  color: #333;
  font-size: 14px;
}
.demo-form-inline .el-form-item__content {
  display: flex;
}
</style>
