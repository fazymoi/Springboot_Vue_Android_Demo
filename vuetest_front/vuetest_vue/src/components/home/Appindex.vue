<template>
    <div>
      {{username}}
    </div>
</template>

<script>
export default {
  name: 'AppIndex',
  data () {
    return {
      username: '',
      responseResult: []
    }
  },
  created () {
    // 页面渲染之前执行，调用定义的方法
    // 3.调用定义的方法
    this.getUserList()
  },
  methods: {// 编写具体的方法
    getUserList () {
      // 2.使用axios发送ajax请求
      // axios.提交方式(“请求s接口路径”).then(箭头函数).catch(箭头函数|)
      this.$axios.post('/index', {
        username: '123',
        password: '123'
      }, {headers: {
        'Content-Type': 'application/json'
      }})
        .then(response => {
          // response就是请求后返回的数据，response可以任意取名
          console.log(response.data)
          // 通过response获取具体数据，赋值给定义空数组
          this.username = response.data.result
        })// 请求成功执行then方法
        .catch(failResponse => {
        })// 请求失败执行catch方法
    }
  }
}
</script>
