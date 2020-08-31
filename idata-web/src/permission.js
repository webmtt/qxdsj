import router from "./router";
import store from "./store";
import { Message } from "element-ui";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
// import { getName, getPass } from '@/utils/auth' // get token from cookie
import getPageTitle from "@/utils/get-page-title";

import { getUserinfo, setUserinfo } from "@/utils/auth";

import { insertDeskLog } from "@/api/user.js";

NProgress.configure({ showSpinner: false }); // NProgress Configuration

// const whiteList = ['/login', '/home'] // no redirect whitelist
const blackList = ["/api"];

router.beforeEach(async (to, from, next) => {
  insertDeskLog({
    createBy: getUserinfo() ? JSON.parse(getUserinfo()).chName : "游客",
    requestUri: to.fullPath,
    title: to.meta.title
  });
  // start progress bar
  NProgress.start();
  // 是否有匹配
  if (to.matched.length === 0) {
    next(`/home`);
  }
  // 如果用户登录 更新cookies时间
  if (getUserinfo()) {
    setUserinfo(JSON.parse(getUserinfo()));
  } else {
    if (blackList.indexOf(to.path) > -1) {
      Message.error("请您先登录");
      next(`/login`);
    }
    // 删除登录信息
    store.dispatch("user/logout");
  }

  // set page title
  document.title = getPageTitle(to.meta.title);
  next();
});

router.afterEach(() => {
  // finish progress bar
  NProgress.done();
});
