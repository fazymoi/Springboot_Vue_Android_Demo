// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import VueAxios from 'vue-axios'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(VueAxios, axios)
Vue.use(ElementUI)
// 设置反向代理，前端请求默认发送到 http://localhost:8443/api

axios.defaults.baseURL = 'http://localhost:8443/api'
// 全局注册，之后可在其他组件中通过 this.$axios 发送数据
axios.defaults.withCredentials = true
Vue.prototype.$axios = axios
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  render: h => h(App),
  router,
  components: { App },
  template: '<App/>'
})
