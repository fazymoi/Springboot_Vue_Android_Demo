const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    overlay: {
      warnings: false,
      errors: false
    },
  },
  lintOnSave: false, // 关闭语法检测
})
