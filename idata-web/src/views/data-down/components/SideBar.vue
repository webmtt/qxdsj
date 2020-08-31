<template>
  <div class="side-bar">
    <div class="side-bar-content">
      <div style="padding: 6px;" class="side-bar-search">
        <el-input placeholder="请输入内容" v-model="inputdata" class="input-with-select" @input="handleSearchData">
          <!-- <el-button slot="append" icon="el-icon-search" @click="handleSearchData"></el-button> -->
        </el-input>
      </div>
      <div class="side-body">
        <div v-for="item in navData" :key="item.id">
          <div
            class="bar-header"
            :class="{'active-bar-header':activeheader === item.id}"
            @click="selectHeader(item)"
          >
            <img src alt />
            <span>{{ item.title }}</span>
            <i class="el-icon-arrow-down" />
          </div>
          <div
            v-if="item.children && item.children.length"
            class="bar-children"
            :style="{ height: isOpen && activeheader === item.id ? item.height: 0}"
          >
            <div
              v-for="innerItem in item.children"
              :ref="item.id+'ref'"
              :key="innerItem.id"
              class="bar-children-item"
              @click="selectChildren(innerItem)"
            >
              <div
                :style="{color: activeChild === innerItem.id ? '#28cce9':'#ffffff'}"
              >{{ innerItem.title }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getNavList } from "@/api/dataDown.js";

export default {
  name: "SideBar",
  data() {
    return {
      inputdata: "",
      activeheader: null,
      activeChild: "",
      navData: [],
      isOpen: false,
      dataList: [] // 元数据
    };
  },
  created() {
    this.getData({})
  },
  methods: {
    // 获取导航树
    getData(params) {
      getNavList(params).then(res => {
        this.navData = []
        this.dataList = []
        if (res.data && res.data.length) {
          res.data.forEach(item => {
            if (item.pid === 0) {
              this.dataList.push({ ...item, list: [] });
            } else {
              const obj =
                this.dataList.find(childItem => childItem.id === item.pid) ||
                null;
              if (obj) {
                obj["list"].push(item);
              }
            }
          });
        }
        if (this.$route.query.id) {
          this.activeheader = this.$route.query.id + "";
          this.handleEmit({
            type: "nav",
            data: {
              id: this.activeheader
            }
          });
          this.isOpen = true;
        }
        if (this.dataList.length) {
          this.navData = [];
          this.activeheader = this.activeheader
            ? this.activeheader
            : this.dataList[0].id + "";
          this.handleEmit({
            type: "nav",
            data: {
              id: this.activeheader
            }
          });
          this.dataList.forEach(item => {
            this.navData.push({
              id: item.id + "",
              title: item.name,
              height: item.list.length ? item.list.length * 58 + "px" : 0,
              children: item.list.length
                ? item.list.map(d => {
                    return {
                      id: d.id + "",
                      title: d.name
                    };
                  })
                : []
            });
          });
        }
      });
    },
    selectHeader(data) {
      this.isOpen = this.activeheader === data.id ? !this.isOpen : true;
      this.activeheader = data.id;
      this.handleEmit({
        type: "nav",
        data: data
      });
    },
    selectChildren(data) {
      this.activeChild = data.id;
      this.handleEmit({
        type: "navChildren",
        data: data
      });
    },
    handleEmit(obj) {
      this.$emit("select", obj);
    },
    // 搜索
    handleSearchData() {
      this.getData({
        keyword:this.inputdata
      })
    }
  }
};
</script>
<style>
.side-bar-search .el-input-group__append {
  background-color: rgb(59, 57, 75);
  color: white;
  /* border-radius: 6px; */
}
</style>

<style scoped  lang="scss">
.side-bar {
  width: 100%;
  .side-bar-content {
    width: 100%;
    .side-body {
      height: 100vh;
      overflow-y: auto;
      &::-webkit-scrollbar-track-piece {
        background: #d3dce6;
      }

      &::-webkit-scrollbar {
        width: 6px;
      }

      &::-webkit-scrollbar-thumb {
        background: #99a9bf;
        border-radius: 20px;
      }
    }
    .bar-header {
      width: 100%;
      height: 58px;
      display: flex;
      align-items: center;
      color: #ffffff;
      border-bottom: 1px solid #1a1f32;
      position: relative;
      cursor: pointer;
      font-size: 14px;
      span {
        margin-left: 15px;
      }
      i {
        color: #ffffff;
        position: absolute;
        right: 10px;
      }
      &:hover {
        background-image: url("../../../assets/images/sidebarbg.png");
        background-origin: content-box;
        background-position: center;
        background-repeat: no-repeat;
        background-clip: content-box;
        background-size: cover;
        &::after {
          position: absolute;
          content: "";
          top: 0;
          left: 0;
          width: 4px;
          height: 100%;
          background-color: #28cce9;
        }
      }
    }
    .active-bar-header {
      background-image: url("../../../assets/images/sidebarbg.png");
      background-origin: content-box;
      background-position: center;
      background-repeat: no-repeat;
      background-clip: content-box;
      background-size: cover;
      &::after {
        position: absolute;
        content: "";
        top: 0;
        left: 0;
        width: 4px;
        height: 100%;
        background-color: #28cce9;
      }
    }
    .bar-children {
      width: 100%;
      transition: all 0.3s;
      height: 0;
      overflow: hidden;
      .bar-children-item {
        // width: 100%;
        height: 58px;
        line-height: 58px;
        color: #ffffff;
        padding-left: 20px;
        font-size: 14px;
        border-bottom: 1px solid #1a1f32;
        cursor: pointer;
        overflow: hidden;
      }
    }
  }
}
</style>

