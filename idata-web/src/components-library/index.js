import Vue from 'vue'
import Pagination from './Pagination/index.vue' // 分页

const ComponentsList = [
  Pagination
]

ComponentsList.forEach((item) => {
  Vue.component(item.name, item)
})
