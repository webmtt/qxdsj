<template>
  <div class="product-create">
    <add-components :components-list="componentsList" @handleGetComponent="handleGetComponent" />
    <div ref="createPage" class="create-page" :style="{ height: parentHeight + 'px' }">
      <vdr
        v-for="item in componentsList"
        :key="item.id"
        :is-conflict-check="true"
        :snap="true"
        :w="item.w"
        :h="item.h"
        :x="item.x"
        :y="item.y"
        :min-height="item.minHeight"
        :max-height="item.maxHeight"
        :parent="true"
        :snap-tolerance="0"
        @refLineParams="getRefLineParams"
        @resizing="onResize"
        @dragging="onDrag"
      >
        <div
          class="vdr-item-content"
          @click="handleMousedown(item)"
          @mousedown="handleMousedown(item)"
        >
          <components :is="item.compontentName" :value-data="item.compontentData" />
          <div class="vdr-contemt-cooper" :data-content="item.chName">
            <el-button type="warning" size="mini" @click.stop="handleEditData(item)">编辑数据</el-button>
            <el-button type="success" size="mini" @click.stop="handleDetail(item)">删除组件</el-button>
          </div>
        </div>
      </vdr>
      <span
        v-for="item in vLine"
        v-show="item.display"
        :key="item.id"
        class="ref-line v-line"
        :style="{ left: item.position, top: item.origin, height: item.lineLength}"
      />
      <span
        v-for="item in hLine"
        v-show="item.display"
        :key="item.id"
        class="ref-line h-line"
        :style="{ top: item.position, left: item.origin, width: item.lineLength}"
      />
      <el-dialog
        v-if="dialogVisible"
        class="form-el-dialog form-el-dialog-add"
        :title="componentActive.chName"
        :visible.sync="dialogVisible"
        width="1320px"
        top="1vh"
      >
        <components-form-data
          :component-active="componentActive"
          @getCompontentData="getCompontentData"
        />
      </el-dialog>
    </div>
  </div>
</template>
<script>
import { getProducts } from "@/api/productLibrary.js";
import AddComponents from "./components/AddComponents.vue";
// import AppMask from './components/AppMask.vue'
import ComponentsFormData from "./components/ComponentsFormData.vue";
import vdr from "vue-draggable-resizable-gorkys";
import "vue-draggable-resizable-gorkys/dist/VueDraggableResizable.css";
export default {
  name: "ProductCreate",
  provide() {
    return {
      componentsList: this.componentsList
    };
  },
  components: {
    AddComponents,
    vdr,
    // AppMask,
    ComponentsFormData
  },
  data() {
    return {
      productId: null, // 产品id
      username: null, // 用户名
      dialogVisible: false, // 增加数据的弹出框
      parentHeight: 1000,
      vLine: [], // 参考线
      hLine: [], // 参考线
      w: 300, // 元素的宽
      h: 0, // 元素的高度
      x: 0, // 元素的x点位置
      y: 0, // 元素的y点位置
      InitId: 1, // 组件初始化id
      componentsID: 1, // 组件id
      defaultOptions: [
        // 配置特殊属性
        {
          name: "HHeaderType",
          height: 50,
          maxHeight: 50
        }
      ],
      componentActive: {},
      componentsList: []
    };
  },
  created() {
    // 做安全验证使用
    this.username = this.$route.query.username || null;
  },
  mounted() {
    this.productId = this.$route.query.id || null;
    if (this.productId) {
      setTimeout(() => {
        getProducts({ id: this.productId }).then(res => {
          if (res.data && res.data.product) {
            this.componentsList = [];
            this.componentsList = JSON.parse(res.data.product);
            this.componentsID = this.componentsList[
              this.componentsList.length - 1
            ].id;
            const arr = [];
            this.componentsList.forEach(item => {
              arr.push(item.h + item.y);
            });
            arr.sort((a, b) => {
              return b - a;
            });
            this.parentHeight = arr[0] ? arr[0] + 50 : 400;
          }
        });
      }, 0);
    }
  },
  methods: {
    handleMousedown(d) {
      this.componentActive = d;
    },
    // 辅助线回调事件
    getRefLineParams(params) {
      const { vLine, hLine } = params;
      this.vLine = { ...vLine, id: new Date().getTime() };
      this.hLine = { ...hLine, id: new Date().getTime() };
    },
    // 重置组件尺寸大小
    onResize: function(x, y, width, height) {
      const obj = this.componentsList.find(
        item => item.id === this.componentActive.id
      );
      const h = height - obj.h;
      this.parentHeight = parseInt(this.parentHeight) + parseInt(h);
      obj.x = x;
      obj.y = y;
      obj.w = width;
      obj.h = height;
      // this.setResizeComponents()
    },
    // 拖动组件位置
    onDrag: function(x, y) {
      const obj = this.componentsList.find(
        item => item.id === this.componentActive.id
      );
      this.parentHeight = this.parentHeight + y - obj.y;
      obj.x = x;
      obj.y = y;
      // this.setResizeComponents()
    },
    // 添加组件
    handleGetComponent(data) {
      const arr = [];
      this.componentsList.forEach(item => {
        arr.push(item.y + item.h);
      });
      arr.sort((a, b) => {
        return b - a;
      });
      this.parentHeight = arr[0] ? arr[0] + 2000 : this.parentHeight;
      const obj = this.defaultOptions.find(
        item => item.name === data.compontentName
      );
      // 组件属性列表
      this.componentsList.push({
        id: this.componentsID++,
        w: 300, // 元素的宽
        h: obj ? obj.height : 200, // 元素的高度
        x: 0, // 元素的x点位置
        y: arr[0] ? arr[0] : 0, // 元素的y点位置
        maxWidth: 1320,
        minHeight: 50,
        maxHeight: obj ? obj.maxHeight : 1000,
        compontentData: [],
        ...data
      });
    },
    // 删除组件
    handleDetail(data) {
      this.$confirm("此操作将删除该组件, 是否继续删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          const index = this.componentsList.findIndex(
            item => item.id === data.id
          );
          if (index > -1) {
            this.componentsList.splice(index, 1);
          }
          this.$message({
            showClose: true,
            type: "success",
            message: "删除成功!"
          });
        })
        .catch(() => {
          this.$message({
            showClose: true,
            type: "info",
            message: "已取消删除"
          });
        });
    },
    // 编辑组件数据
    handleEditData(data) {
      this.dialogVisible = true;
    },
    // 获取组件数据
    getCompontentData(data) {
      this.componentsList.forEach(item => {
        if (item.id === data.activeData.id) {
          item.compontentData = [{ ...data.selectData }];
        }
      });
      this.dialogVisible = false;
    }
  }
};
</script>
<style>
.test1 {
  background-color: rgb(239, 154, 154);
}
.test2 {
  background-color: rgb(129, 212, 250);
}
.test3 {
  background-color: rgb(174, 213, 129);
}
.vdr-item {
  position: relative;
}
.form-el-dialog-preview .el-dialog__body {
  padding: 0;
  background-color: #ffffff;
}
#app {
  min-width: 320px;
}
</style>

<style scoped  lang="scss">
.product-create {
  width: 100%;
  max-width: 1300px;
  min-width: 320px;
  height: auto;
  margin: 0 auto;
  flex: 1;
  position: relative;
  padding: 0 10px;
  .product-create-header {
    width: 100%;
    height: 120px;
    background-color: #f2f2f2;
  }
  .create-page {
    position: relative;
    box-sizing: content-box;
    border: 1px dashed #ccc;
    // min-height:100vh;
    margin-bottom: 80px;
    padding-bottom: 80px;
    transition: all 0.1s;
    // padding: 10px 10px 80px 10px;
    .vdr-item-content {
      width: 100%;
      height: calc(100% - 1px);
      position: relative;
      .vdr-contemt-cooper {
        display: none;
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.4);
        font-size: 60px;
        text-align: center;
        z-index: 1000;
        color: #f2f2f2;
        justify-content: center;
        align-items: center;
        button {
          z-index: 1000;
        }
        &::before {
          display: none;
          position: absolute;
          content: attr(data-content);
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
          font-size: 20px;
          padding-top: 20px;
          text-align: center;
          padding-top: 10px;
          color: #f2f2f2;

          background-color: rgba(0, 0, 0, 0.2);
        }
        &:hover::before {
          display: block;
        }
      }
      &:hover {
        .vdr-contemt-cooper {
          display: flex;
        }
      }
    }
  }
}
</style>
<style>
.form-el-dialog-add .el-dialog__wrapper {
  z-index: 3000;
}
.form-el-dialog-add .el-dialog__body {
  padding: 0;
}
.form-el-dialog-add .el-dialog__header {
  border-bottom: 1px solid #ccc;
}
</style>
