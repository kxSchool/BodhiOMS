import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
// 引入全局样式
import './assets/css/global.css'
import './assets/fonts/iconfont.css'
import Api from "./netword/Api"
import axios from "axios"
import TreeTable from "vue-table-with-tree-grid"
// 富文本编辑器
import VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css' // import styles
import 'quill/dist/quill.snow.css' // for snow theme
import 'quill/dist/quill.bubble.css' // for bubble theme

//加载页面 进度条 Nprogress
import Nprogress from "nprogress"
import "nprogress/nprogress.css"

import './assets/styles/elementUI.scss'
import 'animate.css';   //动画
axios.defaults.baseURL= '';
// axios.defaults.baseURL= "http://106.14.242.182:3014";

axios.interceptors.request.use(config => {
  Nprogress.start()
  config.headers.Authorization = window.localstorage.getItem("token");
  return config;
});
axios.interceptors.response.use( config =>{
  Nprogress.done()
  return config
})
Vue.prototype.$http = axios;
axios.defaults.withCredentials = false;
Vue.config.productionTip = false
Vue.prototype.$api=Api
Vue.component("tree-table", TreeTable)
Vue.use(VueQuillEditor)

//过滤器
Vue.filter("dataFormat",function(time){
  var dt= new Date(time);
  const y = dt.getFullYear()
  const m = (dt.getMonth()+1+"").padStart(2,"0")
  const d = (dt.getDate()+"").padStart(2,"0")

  const hh = (dt.getHours()+'').padStart(2,0)
  const mm = (dt.getMinutes()+'').padStart(2,0)
  const ss = (dt.getSeconds()+'').padStart(2,0)

  return `${y}-${m}-${d}  ${hh}:${mm}:${ss}`
})


new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
