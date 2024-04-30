import axios from 'axios'
import Vue from 'vue'
import { TIMEOUT, REQUEST_URL } from '@/utils/settings'
import router from '../router'

// 创建axios实例
const service = axios.create({
  baseURL: '/api', // api 的 base_url
  timeout: TIMEOUT // 请求超时时间
})
// request拦截器  token
service.interceptors.request.use(
  config => {
    const Authorization = localStorage.getItem("token") || ""
    config.headers['content-type'] = 'application/json'
    if (config.url !== '/login') {
        config.headers = {
          token: Authorization
        }

    }
    return config
  },
  error => {
    Promise.reject(error)
  }
)
// response 拦截器
service.interceptors.response.use(
  response => {
    return new Promise((resolve, reject) => {
      const { status } = response
      // console.log(111,response)
      if (status !== 200) {
        Vue.prototype.$message.error(response.data.message)
        reject(response.data)
      } else {
        if (response.data.code == 402) {
            Vue.prototype.$message.error(response.data.message)
            localStorage.setItem('token', '0')
            router.replace({
                path: '/login',
            })
        }
        // Vue.prototype.$message.error(response.data.message)
        resolve(response.data)
      }
    })
  },
  error => {
    Vue.prototype.$message.error(error.response.message)
    return Promise.reject(error)
  }
)
export default service
