import axios from 'axios'
//生产环境
// const RUL = 'https://www.liulongbin.top:8888/api/private/v1/'
// const RUL = 'http://106.14.242.182:3014/'
const RUL= "http://192.168.10.153:3015/"
// 测试环境
// const TestRUL = 'http://121.36.159.141:8080/'
const isfalg = true

export function request(config) {
  //配置通用的
  const instance = axios.create({
    baseURL: isfalg ? RUL : TestRUL,
    timeout: 5000
  })
  //拦截器 Interceptor
  instance.interceptors.request.use(
    config => {
      console.log('请求成功', config)
      config.headers.Authorization = window.localStorage.getItem('token')

      // config.transformRequest = [function (data) {
      //     let src = ''
      //     for (let item in data) {
      //       src += encodeURIComponent(item) + '=' + encodeURIComponent(data[item]) + '&'
      //     }
      //     return src
      //   }]
      return config
    },
    err => {
      console.log('请求失败', err)
    }
  )
  instance.interceptors.response.use(
    res => {
      console.log('响应成功', res)
      return res.data
    },
    err => {
      console.log('响应失败', err)
    }
  )
  //请求网咯
  return instance(config)
}
