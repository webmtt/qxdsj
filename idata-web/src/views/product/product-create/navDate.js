import { getUserNavData, getTree } from './createApi.js'
var productsList = []
var productsTreeList = []

class CrearteNavData {
  constructor(option) {
    this.option = option
    this.initId = 1
    this.dataListNav = []
  }
  async getProductsFileType() {
    if (productsList.length) {
      return productsList
    } else {
      return await this.getProductsFileData()
    }
  }
  async getProductsFileData() {
    const arrList = [
      {
        id: 'product',
        title: '产品子类',
        type: 'product',
        selectMore: true,
        children: []
      },
      {
        id: 'typs',
        title: '业务门类',
        type: 'typs',
        selectMore: true,
        children: []
      },
      {
        id: 'unit',
        title: '制作单位',
        type: 'unit',
        selectMore: true,
        children: []
      }
    ]
    const res = await getUserNavData()
    if (res.data && res.data.productMap) {
      const productMap = []
      Object.keys(res.data.productMap).forEach(key => {
        productMap.push({
          id: this.initId++,
          title: key,
          type: 'product',
          pId: [],
          children: res.data.productMap[key]
        })
      })
      productMap.forEach(item => {
        if (item.children && item.children.length) {
          item.children = item.children.map(cItem => {
            return {
              id: this.initId++,
              title: cItem.PRODUCTNAME,
              type: 'product',
              pId: [item.id],
              children: []
            }
          })
        }
      })
      arrList[0].children = productMap
    }
    if (res.data && res.data.typeList) {
      arrList[1].children = res.data.typeList.map(item => {
        return {
          id: this.initId++,
          title: item.TYPENAME,
          pId: [],
          type: 'typs'
        }
      })
    }
    if (res.data && res.data.unitList) {
      arrList[2].children = res.data.unitList.map(item => {
        return {
          id: this.initId++,
          title: item.UNITNAME,
          pId: [],
          type: 'unit'
        }
      })
    }
    productsList = this.dataListNav = arrList
    return productsList
  }
  async getProductsTree() {
    if (productsTreeList.length) {
      return productsTreeList
    } else {
      return await this.getProductsTreeList()
    }
  }
  async getProductsTreeList() {
    const arrList = [
      {
        id: 'product',
        title: '产品子类',
        type: 'product',
        selectMore: true,
        children: []
      },
      {
        id: 'typs',
        title: '业务门类',
        type: 'typs',
        selectMore: true,
        children: []
      },
      {
        id: 'unitt',
        title: '单位业务门类',
        type: 'unitt',
        selectMore: true,
        children: []
      },
      {
        id: 'unitp',
        title: '单位产品子类',
        type: 'unitp',
        selectMore: true,
        children: []
      }
    ]
    const res = await getTree()
    if (res.data && res.data.productList) {
      arrList[0].children = res.data.productList.map(item => {
        return {
          id: this.initId++,
          title: item.PRODUCTNAME,
          pId: [],
          type: 'product'
        }
      })
    }
    if (res.data && res.data.typeList) {
      arrList[1].children = res.data.typeList.map(item => {
        return {
          id: this.initId++,
          title: item.TYPENAME,
          pId: [],
          type: 'typs'
        }
      })
    }
    if (res.data && res.data.unitList) {
      arrList[2].children = res.data.unitList.map(item => {
        return {
          id: this.initId++,
          title: item.UNITNAME,
          pId: [],
          type: 'unitt'
        }
      })
      arrList[3].children = arrList[2].children.map(item => {
        return { ...item, type: 'unitp', id: this.initId++ }
      })
    }
    productsTreeList = arrList
    return productsTreeList
  }
}
export default CrearteNavData
