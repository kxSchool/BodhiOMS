module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: [
    'plugin:vue/essential',
    // '@vue/standard'
  ],
  parserOptions: {
    parser: 'babel-eslint'
  },
//   rules: {
//     'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
//     'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
//     "no-unused-vars": 'off'
//   }
rules: {
    "no-console": "off",
    "no-unused-vars":"off", //重要 var 变量为引入
    "no-debugger": process.env.NODE_ENV === "production" ? "error" : "off",
    }
}
