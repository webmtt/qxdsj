// fn（n） 函数编号
const ToolLibrary = {
  // s截取某个字段后面的字符串
  // fn1
  h_spliceStr(str, reg, n) {
    return str.split(reg)[n]
  },
  // 删除对象中值为null的字段 obj 为要检索的对象，arr 去除其他值的列表
  // fn2
  h_clearObjNull(obj, arr = []) {
    const target = JSON.parse(JSON.stringify(obj))
    Object.keys(target).forEach(key => {
      if (this.h_getDataType(target[key]) === 'Object') {
        if (Object.keys(target[key]).length === 0) {
          delete target[key]
        } else {
          target[key] = this.h_clearObjNull(target[key], arr)
        }
      } else {
        if (!target[key] || arr.findIndex(item => item === target[key]) > -1) {
          delete target[key]
        }
      }
    })
    return target
  },
  // fn3
  // 判断文件具体类型 返回首字母大写的类型格式
  h_getDataType(data) {
    return ['Null', 'Undefined', 'String', 'Number', 'Function', 'Date', 'Array', 'RegExp', 'Object'].find(item => Object.prototype.toString.call(data) === '[object ' + item + ']')
  },
  // fn4
  // 判断非空对象
  h_objectIsEmpty(target) {
    return !!Object.keys(target).length
  },
  // fn5
  // 滚动条的高度 返回数值
  h_getScrollTop() {
    let scroll_top = 0
    if (document.documentElement && document.documentElement.scrollTop) {
      scroll_top = document.documentElement.scrollTop
    } else if (document.body) {
      scroll_top = document.body.scrollTop
    }
    return scroll_top
  },
  // 随机生成一个字符串
  h_randomString(length) {
    const defaultstr = 'abcdefghigklmnopqrstuvwxyzABCDEFGHJKMNPQRSTWXYZ0123456789'
    const strLength = defaultstr.length
    let target = ''
    for (var i = 0; i < length; i++) {
      target += '' + defaultstr.charAt(Math.floor(Math.random() * strLength))
    }
    return target
  }
}
ToolLibrary.length = Object.keys(ToolLibrary).length
export default ToolLibrary
