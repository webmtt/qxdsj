<template>
  <div class="data-down">
    <div class="data-down-content">
      <div class="hot-content-header">
        <span>数据服务</span>
        <span>全品类气象数据，海量气象存储</span>
        <!-- 数据服务 -->
      </div>
      <div class="list-content">
        <div v-for="item in dataList" :key="item.id" class="list-item">
          <img :src="item.imgUrl">
          <div class="item-content">
            <div class="content-header">
              <span class="content-header-title"  @click="toDataDown(item)">{{ item.title }}</span>
              <span class="content-header-sub">共
                <!-- <span>{{ item.cate1 }}</span> 大类 -->
                <span>{{ item.means }}</span> 种资料</span>
            </div>
            <div class="content-list">
              <p style="    color: #999999;line-height: 25px; overflow: hidden;">
                {{item.chndescription}}
              </p>
              <!-- <span v-for="(itemchild, indexchild) in item.children" :key="indexchild" @click="toDataDown(itemchild)">
                {{ itemchild.title }}({{ itemchild.means }})
              </span> -->
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import { getDataSortCount } from '@/api/home.js'

export default {
  name: 'DataDown',
  data() {
    return {
      dataList: [
        {
          id: 1,
          cate1: 7,
          means: 232,
          title: '基础数据',
          imgUrl: require('@/assets/images/homedown1.png'),
          chndescription: null,
          link: ''
        },
        {
          id: 2,
          cate1: 5,
          means: 13,
          title: '融合分析产品',
          chndescription: null,
          imgUrl: require('@/assets/images/homedown2.png'),
          link: ''
        },
        {
          id: 3,
          title: '再分析产品',
          cate1: 4,
          means: 15,
          
          chndescription: null,
          imgUrl: require('@/assets/images/homedown3.png'),
          link: ''
        },
        {
          id: 4,
          title: '均一化产品',
          cate1: 4,
          means: 17,
          chndescription: null,
          imgUrl: require('@/assets/images/homedown4.png'),
          link: ''
        },
        {
          id: 5,
          title: '卫星数据产品',
          cate1: 2,
          means: 273,
          chndescription: null,
          imgUrl: require('@/assets/images/homedown5.png'),
          link: ''
        },
        {
          id: 6,
          title: '其它',
          cate1: 3,
          means: 38,
          chndescription: null,
          imgUrl: require('@/assets/images/homedown6.png'),
          link: ''
        }
      ]
    }
  },
  created() {
    this.getSortCount()
  },
  methods: {
    getSortCount() {
      getDataSortCount().then(res => {
        if (res.data && res.data.length) {
          const data = res.data
          this.dataList.forEach((item, index) => {
            item.id =   data[index] ? data[index].categoryid : 'null',
            item.title = data[index] ? data[index].chnname : '-'
            item.chndescription = data[index] ? data[index].chndescription : '-'
            item.means = data[index] ? data[index].datacount : '-'
          })
        }
      })
    },
    // 数据服务点击跳转
    toDataDown(d) {
      this.$router.push({ path: `/dataDown`, query: { id: d.id }})
    }
  }
}
</script>

<style scoped  lang="scss">
.data-down {
  width: 100%;
  height: 790px;
  background-color: #f5f6fa;
  .data-down-content {
    width: 1300px;
    height: 100%;
    margin: 0 auto;
    .hot-content-header {
      width: 100%;
      height: 140px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      & > span:first-child {
        color: #333;
        font-size: 24px;
        font-weight: bolder;
        padding-bottom: 15px;
        position: relative;
        &::after {
          content: '';
          position: absolute;
          left: -22px;
          top: 2px;
          width: 4px;
          height: 24px;
          background-color: #24d2f0;
        }
      }
      & > span:last-child {
        font-size: 16px;
        color: #666;
      }
    }
    .list-content {
      width: 100%;
      height: auto;
      display: flex;
      justify-content: flex-start;
      flex-wrap: wrap;
      .list-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 600px;
        height: 170px;
        margin-right: 100px;
        background-color: #ffffff;
        margin-bottom: 20px;
        padding: 30px 40px;
        border:1px solid #d7d8d9;
        img {
          width: 65px;
          height: 65px;
          margin-right: 30px;
        }
        .item-content {
          // width: 100%;
          height: 165px;
          flex: 1;
          .content-header {
            width: 100%;
            height: 50px;
            line-height: 50px;
            font-weight: bold;
            .content-header-title {
              font-size: 20px;
              color: #333;
              cursor: pointer;
            }
            .content-header-sub {
              margin-left: 50px;
              font-size: 16px;
              span {
                color: #a02f1c;
              }
            }
          }
          .content-list {
            display: flex;
            justify-content: flex-start;
            flex-wrap: wrap;
            & > span {
              cursor: pointer;
              width: 33%;
              height: 20px;
              margin-bottom: 10px;
              position: relative;
              overflow: hidden;
              font-size: 14px;
              // padding-left: 12px;
              color: #999999;
              // &::after {
              //   position: absolute;
              //   content: '';
              //   width: 6px;
              //   height: 6px;
              //   border-radius: 50%;
              //   background-color: #999;
              //   left: 0;
              //   top: 6px;
              // }
            }
          }
        }
      }
      & > div:nth-child(2n) {
        margin-right: 0px;
      }
    }
  }
}
</style>
