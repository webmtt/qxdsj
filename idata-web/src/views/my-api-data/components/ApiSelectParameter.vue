<template>
  <div class="api-select-parameter">
    <div class="select-body">
      <div>
        <span>资料类别</span>
        <el-select
          v-model="typeId"
          placeholder="请选择"
          @change="activeTypeEvent"
        >
          <el-option
            v-for="item in navTypeData"
            :key="item.value"
            :label="item.chName"
            :value="item.value"
          />
        </el-select>
      </div>
      <div>
        <span>资料名称</span>
        <el-select
          v-model="typeNameId"
          placeholder="请选择"
          @change="activeTypeDetailEvent"
        >
          <el-option
            v-for="item in navTypeDetail"
            :key="item.value"
            :label="item.chName"
            :value="item.value"
          />
        </el-select>
      </div>
      <div>
        <span>访问接口</span>
        <el-select
          v-model="apiId"
          placeholder="请选择"
          @change="activeApiListEvent"
        >
          <el-option
            v-for="item in apiListData"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'ApiSelectParameter',
  props: {
    navTypeData: {
      type: Array,
      default: () => []
    },
    activeTypeId: {
      type: String,
      default: () => ''
    },
    navTypeDetail: {
      type: Array,
      default: () => []
    },
    activeTypeDetailId: {
      type: String,
      default: () => ''
    },
    apiListData: {
      type: Array,
      default: () => []
    },
  },
  data() {
    return {
      typeId: this.activeTypeId ? this.activeTypeId : null,
      typeNameId: this.activeTypeDetailId ? this.activeTypeDetailId : null,
      apiId: null,
    }
  },
  methods: {
    // 资料类别
    activeTypeEvent(e) {
      this.typeId = e
      this.$emit('handleTypeList', this.typeId)
    },
    // 资料名称
    activeTypeDetailEvent(e) {
      this.typeNameId = e
      this.$emit('handleTypeDetailList', this.typeNameId)
    },
    // 接口
    activeApiListEvent() {
      this.$emit('handleChangeApi', this.apiId)
    },
  },
  watch: {
    activeTypeId() {
      this.typeId = this.activeTypeId ? this.activeTypeId : null
    },
    activeTypeDetailId() {
      this.typeNameId = this.activeTypeDetailId ? this.activeTypeDetailId : null
    },
    apiListData() {
      this.apiId = null;
      if (!this.apiId) {
        this.apiId = this.apiListData[0].value
        this.activeApiListEvent()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.api-select-parameter {
  width: 100%;
  height: 100%;
  .select-body {
    width: 100%;
    height: 100%;
    & > div {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      border: 1px solid #dbdbdb;
      & > span {
        display: inline-block;
        width: 160px;
        height: 40px;
        line-height: 40px;
        background-color: #eee;
        color: #333;
        font-size: 16px;
        text-align: center;
        margin-right: 40px;
      }
      & > div {
        flex: 1;
      }
    }
    & > div:nth-child(2) {
      margin-top: -1px;
    }
    & > div:last-child() {
      margin-top: -1px;
    }
  }
}
</style>