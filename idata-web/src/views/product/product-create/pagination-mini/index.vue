<template>
  <div :class="{'hidden':hidden}" class="pagination-mine-container">
    <el-pagination small :background="background" :current-page.sync="currentPage" :page-size="20" :total="total" layout="prev, pager, next" @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { scrollTo } from '@/utils/scroll-to'

export default {
  name: 'PaginationMini',
  props: {
    total: {
      required: true,
      type: Number
    },
    page: {
      type: Number,
      default: 1
    },
    limit: {
      type: Number,
      default: 20
    },
    pageSizes: {
      type: Array,
      default() {
        return [10, 20, 30, 50]
      }
    },
    layout: {
      type: String,
      default: 'prev, pager, next'
    },
    background: {
      type: Boolean,
      default: true
    },
    autoScroll: {
      type: Boolean,
      default: true
    },
    hidden: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    currentPage: {
      get() {
        return this.page
      },
      set(val) {
        this.$emit('update:page', val)
      }
    },
    pageSize: {
      get() {
        return this.limit
      },
      set(val) {
        this.$emit('update:limit', val)
      }
    }
  },
  methods: {
    handleCurrentChange(val) {
      this.$emit('pagination', { page: val, limit: this.pageSize })
      if (this.autoScroll) {
        scrollTo(100, 200)
      }
    }
  }
}
</script>

<style>
.pagination-mine-container {
  width: 100%;
  background: #fff;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
.pagination-container .el-pagination {
  text-align: right;
  padding: 0px;
  line-height: 0;
}
.pagination-container.hidden {
  display: none;
}
</style>

