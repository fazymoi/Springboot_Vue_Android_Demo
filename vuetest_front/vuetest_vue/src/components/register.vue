<template>
  <body id="paper">
  <el-form :model="loginForm" :rules="rules" ref="ruleform" class="login-container" label-position="left"
           label-width="0px" v-loading="loading">
    <h3 class="login_title">用户注册</h3>
    <el-form-item prop="username">
      <el-input type="text" v-model="loginForm.username"
                auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input type="password" v-model="loginForm.password"
                auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
    <el-form-item prop="password2">
    <el-input type="password" v-model="loginForm.password2"
              auto-complete="off" placeholder="重新输入密码"></el-input>
    </el-form-item>
    <el-form-item style="width: 100%">
      <el-button type="primary" style="width: 40%;background: #505458;border: none" v-on:click="register('ruleform')">注册</el-button>
    </el-form-item>
  </el-form>
  </body>
</template>
<script>
export default{
  name: 'Register',
  data () {
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.loginForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      rules: {
        username: [{required: true, message: '用户名不能为空', trigger: 'blur'}],
        password: [{required: true, message: '密码不能为空', trigger: 'blur'},
          {min: 3, max: 6, message: '长度为3-6位', trigger: 'blur'}],
        password2: [{required: true, validator: validatePass2, message: '密码不一致', trigger: 'blur'}]
      },
      checked: true,
      loginForm: {
        username: '',
        password: '',
        password2: ''
      },
      loading: false
    }
  },
  methods: {
    register (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          var _this = this
          this.$axios
            .post('/register', {
              username: this.loginForm.username,
              password: this.loginForm.password
            })
            .then(resp => {
              if (resp.data.code === 200) {
                this.$alert('注册成功', '提示', {
                  confirmButtonText: '确定'
                })
                _this.$router.replace('/login')
              } else {
                this.$alert(resp.data.message, '提示', {
                  confirmButtonText: '确定'
                })
              }
            })
            .catch(failResponse => {})
        } else {
          this.$alert('注册失败', '提示', {
            confirmButtonText: '确定'
          })
        }
      })
    }
  }
}
</script>
