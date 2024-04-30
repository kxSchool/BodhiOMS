//开发阶段
const prodplugins =[]

if(process.env.NODE_ENV === "production"){
  //发布模式
  prodplugins.push("transform-remove-console")
}

module.exports = {
  "presets": [
    "@vue/cli-plugin-babel/preset"
  ],
  "plugins": [

    "transform-remove-strict-mode",
    // [
    //   "component",
    //   {
    //     "libraryName": "element-ui",
    //     "styleLibraryName": "theme-chalk"
    //   }
    // ]
    ...prodplugins
  ],
  "ignore": [
    "public/bower_components/ueditor/utf8-asp/*.js"
  ],

}
