'use strict'
const path = require('path')
const defaultSettings = require('./src/settings.js')
function resolve(dir) {
  return path.join(__dirname, dir)
}
const name = defaultSettings.title || '企鹅' // 标题
const port = 8088 // 端口
module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? './' : '/', //打包之后生成的文件名
  outputDir: 'dist',
  assetsDir: 'static', // 是否开启eslint保存检测，有效值：ture | false | 'error'
  lintOnSave: process.env.NODE_ENV === 'development', // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
  productionSourceMap: false,
  devServer: {
    host: '0.0.0.0',
    port: 8082,
    open: true,
    proxy: {
      // 重定向的name
      '': {
        // target: 'http://192.168.10.204:3015',//代理地址，这里设置的地址会代替axios中设置的baseURL
        // target: 'http://192.168.10.165:3015/api',//代理地址，这里设置的地址会代替axios中设置的baseURL   //测试环境
        target: 'http://192.168.10.165:8081', //代理地址，这里设置的地址会代替axios中设置的baseURL  //后台本地
        changeOrigin: true, // 如果接口跨域，需要进行这个参数配置
        //pathRewrite方法重写url  /api重定向到http://192.168.10.165:3014/api
        // pathRewrite: {
        //   '^/api': '/'
        pathRewrite: { '^/': '/' }, //重写之后url为 http://192.168.1.16:8085/xxxx
        //   //pathRewrite: {'^/api': '/api'} 重写之后url为 http://192.168.1.16:8085/api/xxxx
        // }
      },
    },
    disableHostCheck: true,
  },

  configureWebpack: (config) => {
    config.optimization.minimizer[0].options.terserOptions.compress.drop_console = process.env.NODE_ENV === 'production'
    config.externals = {
      resolve: {
        alias: {
          '@': resolve('src'),
        },
      },
    }
  },
}
