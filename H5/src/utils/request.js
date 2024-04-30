import axios from 'axios'

// 创建axios实例
const service = axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: "/api",
    // 超时
    timeout: 10000
})


service.interceptors.request.use(config => {
    if (localStorage.getItem('Authorization')) {
        config.headers.token = localStorage.getItem('Authorization');
    }
    return config
}, error => {
    console.log(error)
    Promise.reject(error)
})


export default service