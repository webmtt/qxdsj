<template>
  <div class="h-dropdown-folding">
    <el-menu :default-active="activeId" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose">
      <el-submenu v-for="item in dataList" :key="item.id" :index="item.id">
        <template slot="title">
          <i class="el-icon-location" />
          <span>{{ item.text }}</span>
        </template>
        <el-menu-item-group v-if="item.children.length">
          <el-menu-item v-for="child in item.children" :key="child.id" :index="child.id">
            {{ child.text }}
          </el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: 'HDropdownFolding',
  data() {
    return {
      activeId: '1',
      dataList: [
        {
          id: '1',
          text: '这里欢迎您11',
          icon: '',
          children: [
            {
              id: '11',
              text: 'chart-1',
              icon: ''
            },
            {
              id: '12',
              text: 'chart-2',
              icon: ''
            },
            {
              id: '13',
              text: 'chart-3',
              icon: ''
            }
          ]
        },
        {
          id: '2',
          text: '这里欢迎您',
          icon: '',
          children: [
            {
              id: '21',
              text: 'chart-2-1',
              icon: ''
            },
            {
              id: '22',
              text: 'chart-2-2',
              icon: ''
            },
            {
              id: '23',
              text: 'chart-2-3',
              icon: ''
            }
          ]
        }
      ]
    }
  },
  methods: {
    handleOpen(key, keyPath) {
    },
    handleClose(key, keyPath) {
    }
  }
}
</script>

<style scoped  lang="scss">
.h-dropdown-folding {
  width: 100%;
  height: 100%;
  overflow: hidden;
  background-color: #ffffff;
  border: 1px solid #f2f2f2;
}
</style>
