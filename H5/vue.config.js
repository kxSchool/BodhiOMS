const proxy = require('http-proxy-middleware');

module.exports = {
  lintOnSave: true,
  css: {
    loaderOptions: {
      css: {},
      postcss: {
        plugins: [
          require('postcss-px2rem')({
            remUnit: 75
          })
        ]
      }
    }
  },
  productionSourceMap: false,
  // 配置反向代理
  devServer: {
    //proxy:{'/api':{}},代理器中设置/api,项目中请求路径为/api的替换为target
    proxy: {
      // 重定向的name
      '/api': {
        // target: 'http://106.14.242.182:3014/api',//代理地址，这里设置的地址会代替axios中设置的baseURL  //服务器测试环境ip
        target: 'http://192.168.10.165:8081/api',//代理地址，这里设置的地址会代替axios中设置的baseURL  //服务器测试环境ip
        // target: 'http://192.168.10.204:3014/api',//代理地址，这里设置的地址会代替axios中设置的baseURL  //服务器测试环境ip
        changeOrigin: true,// 如果接口跨域，需要进行这个参数配置
        //pathRewrite方法重写url  /api重定向到http://192.168.10.165:3014/api
        // pathRewrite: {
        //   '^/api': '/'
        pathRewrite: { '^/api': '/' } //重写之后url为 http://192.168.1.16:8085/xxxx
        //   //pathRewrite: {'^/api': '/api'} 重写之后url为 http://192.168.1.16:8085/api/xxxx
        // }
      },
      // 查看凭证的重定向
      // 重定向的name
      '': {
        // target: 'http://106.14.242.182:3014',//代理地址，这里设置的地址会代替voucher页面中设置的baseURL
        target: 'http://192.168.10.165:3014',//代理地址，这里设置的地址会代替axios中设置的baseURL  //服务器测试环境ip
        changeOrigin: true,// 如果接口跨域，需要进行这个参数配置
        //pathRewrite方法重写url  /api重定向到http://192.168.10.165:3014/api
        // pathRewrite: {
        //   '^/voucher': '/'
        pathRewrite: { '^': '/' }
        // target: 'http://192.168.1.30:8085',//代理地址，这里设置的地址会代替axios中设置的baseURL
        //重写之后url为 http://192.168.1.16:8085/xxxx
        //   //pathRewrite: {'^/api': '/api'} 重写之后url为 http://192.168.1.16:8085/api/xxxx
        // }
      },
    }
  },
  //...
}
