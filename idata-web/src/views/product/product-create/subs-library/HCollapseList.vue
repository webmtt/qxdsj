<template>
  <div class="h-cllapse-list">
    <el-collapse v-model="activeNames" @change="handleChange">
      <el-collapse-item title="一致性 Consistency" name="1">
        <div>与现实生活一致；</div>
      </el-collapse-item>
      <el-collapse-item title="反馈 Feedback" name="2">
        <div>控制反馈</div>
      </el-collapse-item>
      <el-collapse-item title="效率 Efficiency" name="3">
        <div>简化流程：设计简洁直观的操作流程；</div>
      </el-collapse-item>
      <el-collapse-item title="可控 Controllability" name="4">
        <div>用户决策：根据场景可给</div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script>
export default {
  name: 'HCollapseList',
  data() {
    return {
      activeNames: []
    }
  },
  methods: {
    handleChange(val) {
    }
  }
}
</script>

<style scoped>
  .h-cllapse-list{
    width:100%;
    height: 100%;
    padding: 0 20px;
    overflow: hidden;
  }
</style>
