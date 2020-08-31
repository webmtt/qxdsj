<template>
  <div class="side-bar">
    <el-menu
      :default-active="activeNav"
      class="el-menu-vertical-demo"
      background-color="#31394b"
      text-color="#fff"
      active-text-color="#28cce9"
      mode="vertical"
      @select="selected"
    >
      <template v-for="item in navList">
        <el-submenu v-if="item.children.length" :key="item.id" :index="item.id">
          <template slot="title">
            <span>{{ item.title }}</span>
          </template>
          <template v-if="item.children.length">
            <el-submenu
              v-for="itemInner in item.children"
              :key="itemInner.id"
              :index="itemInner.id"
            >
              <template slot="title">{{itemInner.title}}</template>
              <template v-if="itemInner.children && itemInner.children.length">
                <el-menu-item
                  v-for="itemInnerThree in itemInner.children"
                  :key="itemInnerThree.id"
                  :index="itemInnerThree.id"
                >{{itemInnerThree.title}}</el-menu-item>
              </template>
            </el-submenu>
          </template>
        </el-submenu>
      </template>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "SideBar",
  props: {
    navList: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      activeNav: "1",
      navData: []
    };
  },

  created() {
    if (this.navList.length) {
      if (this.navList[0] && this.navList[0].children.length) {
        const d = this.navList[0].children;
        if (d[0].children.length) {
          const f = d[0].children;
          this.activeNav = f[0].id;
        }
      }
      this.$emit("navSelected", this.activeNav);
    }
  },
  methods: {
    selected(id) {
      this.$emit("navSelected", id);
    }
  }
};
</script>

<style scoped  lang="scss">
.side-bar {
  width: 100%;
}
.el-menu-item {
  padding-left: 60px !important;
}
.side-bar .el-submenu {
  margin: -1px;
}
</style>
