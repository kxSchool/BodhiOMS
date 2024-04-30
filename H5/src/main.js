import Vue from 'vue'
import cors from 'cors'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
import './assets/index.css'
import axios from 'axios'
import Recharge from './components/Recharge.vue'
import Paid from './components/Paid.vue'
import store from './store/index'
import 'amfe-flexible'
import { Message } from "element-ui";
import Token from './views/Home.vue'
import { showLoading, hideLoading } from "../loading";
import Vant from 'vant';
import 'vant/lib/index.css';
import moment from 'moment/moment'
import NavBar from "./components/NavBar.vue";
Vue.component('NavBar', NavBar)

Vue.use(Vant);

// 获取到初始值
store.commit('initdata');
// 后俩位
Vue.filter('amount', function (value) {
  // 截取当前数据到小数点后两位
  let transformVal = parseFloat(value).toFixed(3)
  let realVal = transformVal.substring(0, transformVal.length - 1)
  return realVal
});

Vue.filter('moment', function (value, formatString) {
  formatString = formatString || 'YYYY-MM-DD HH:mm:ss';
  return moment(value).format(formatString);
});

// 实在付款 后俩位
Vue.filter('idexasd', function (value) {
  const newVal = parseFloat(value).toFixed(2);
  return newVal;
}),
  Vue.filter('iskes', function (value) {
    // 截取当前数据到小数点后三位
    let tempVal = parseFloat(value).toFixed(5)
    let realVal = tempVal.substring(0, tempVal.length - 1)
    return realVal
  })
// 后俩位
Vue.filter('amounswt', function (value) {
  // 截取当前数据到小数点后两位
  let transformVal = parseFloat(value).toFixed(3)
  let realVal = transformVal.substring(0, transformVal.length - 1)
  return realVal
});
// 后四位
// 小数点后4位
Vue.filter('siwen', function (value) {
  const newVal = parseFloat(value).toFixed(4);
  return newVal;
}),
  // 小数点后4位 不进1
  Vue.filter('amounsas', function (value) {
    // 截取当前数据到小数点后两位
    let transformVal = parseFloat(value).toFixed(6)
    let realVal = transformVal.substring(0, transformVal.length - 2)
    return realVal
  });
// console.log(Token.data().token);
Vue.prototype.$message = Message
// 点击复制
import Clipboard from 'clipboard'
// 配置全局样式css
import './assets/global.css'
// 注册全局组件 第一个值为自定义命名
Vue.component('Rearge', Recharge)
// 设置默认访问路径
axios.defaults.baseURL = '/api'
// 设置请求超时时间
axios.defaults.timeout = 10000
Vue.prototype.axios = axios
// 添加请求拦截器
// axios.interceptors.request.use(function (config) {
//   // 在发送请求之前做些什么
//   showLoading()//显示加载中
//   return config;
// }, function (error) {
//   hideLoading(); //关闭加载
//   // 对响应错误做点什么
//   Message({
//     message: '请求错误',
//     type: 'error',
//     duration: 2000,
//     center: true
//   })
//   return Promise.reject(error);
// });

// 请求拦截器
axios.interceptors.request.use(
  config => {
    if (localStorage.getItem('Authorization')) {
      config.headers.token = localStorage.getItem('Authorization');
      // console.log(config.headers.Authorization)
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  });
// 添加响应拦截器
axios.interceptors.response.use(function (response) {
  hideLoading(); //关闭加载
  // 对响应数据做点什么
  return response;
}, function (error) {
  hideLoading(); //关闭加载
  // 对响应错误做点什么
  Message({
    message: '请求错误',
    type: 'error',
    duration: 2000,
    center: true
  })
  return Promise.reject(error);
});
Vue.config.productionTip = false

export default axios

new Vue({
  router,
  render: h => h(App),
  store,
  cors

}).$mount('#app')
