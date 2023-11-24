<template>
  <div :xl="6" :lg="7" class="bg-login">

<!--    &lt;!&ndash;logo&ndash;&gt;-->
<!--    <el-image :src="require('@/assets/logo.png')" class="logo"/>-->

    <!--标题-->
    <el-row type="flex" class="row-bg row-two" justify="center" align="middle">
      <ebackgroud.jpgl-col :span="6"></ebackgroud.jpgl-col>
      <el-col :span="6" offset="6">
        <!--标题-->
        <h1 class="title" style="font-family: 黑体;">美食推荐系统</h1>
      </el-col>
      <el-col :span="6"></el-col>
    </el-row>

    <!--form表单-->
    <el-row type="flex" class="row-bg card" justify="center" align="bottom">
      <el-col :span="7" class="login-card">

        <!--dataForm-->
        <el-form :model="dataForm" :rules="rules" ref="ruleform" label-width="21%" class="login-container">
          <h3 class="login_title" style="margin-top:-10px">用户注册</h3>
          <el-form-item label="账户" prop="username" style="width: 380px">
            <el-input type="text" v-model="dataForm.username"
                      auto-complete="off" placeholder="账户为您的手机号码"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password" style="width: 380px">
            <el-input type="password" v-model="dataForm.password"
                      auto-complete="off" placeholder="密码"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password2" style="width: 380px">
            <el-input type="password" v-model="dataForm.password2"
                      auto-complete="off" placeholder="重新输入密码"></el-input>
          </el-form-item>

          <el-form-item class="btn-ground" style="margin-left:-50px">
            <el-button type="primary" @click="register('ruleform')">注册</el-button>
            <el-button @click="login()">返回登录</el-button>
          </el-form-item>
        </el-form>

      </el-col>
    </el-row>
  </div>
</template>

<script>
export default{
  name: 'Register',
  data () {
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.dataForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      rules: {
        username: [
          {required: true, message: '请输入手机号', trigger: 'blur'},
          {pattern: /^1[3456789]\d{9}$/, message: '手机号码格式不正确', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 16, message: '长度为3-16位', trigger: 'blur'}],
        password2: [{required: true, validator: validatePass2, message: '密码不一致', trigger: 'blur'}]
      },
      checked: true,
      dataForm: {
        username: '',
        password: '',
        password2: ''
      },
      loading: false
    }
  },
  methods: {
    // 重置表单
    login () {
      this.$router.replace({path: '/login'})
    },

    // 注册
    register (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          var _this = this
          this.$axios
            .post('/register', {
              username: this.dataForm.username,
              password: this.dataForm.password
            })
            .then(resp => {
              if (resp.data.code === 200) {
                this.$message({
                  message: '注册成功',
                  type: 'success'
                })
                _this.$router.replace('/login')
              } else {
                this.$message({
                  message: resp.data.message,
                  type: 'error'
                })
              }
            })
            .catch(failResponse => {})
        } else {
          this.$message({
            message: '注册失败',
            type: 'error'
          })
        }
      })
    }
  }
}
</script>

<style scoped>
  .codeImg {
    /*让验证码图片飘在右边*/
    float: right;
    /*设置一些圆角边框*/
    border-radius: 3px;
    /*设置宽度*/
    width: 26%;
  }

  .bg-login {
    height: 100%;
    background-image: url("../assets/background.png");
    background-size: 100%;

  }

  .btn-ground {
    text-align: center;
  }

  .logo {
    margin: 30px;
    height: 70px;
    width: 70px;
    top: -5px;
    left: -5px;
    position: fixed;
  }

  .title {
    /*text-shadow: -2px 2px 1px #5f565e;*/
    text-align: center;
    margin-top: 30%;
    color: #e89b39;
    font-size: 40px;
  }

  .login-card {
    background-color: #ffffff;
    opacity: 0.9;
    box-shadow: 0 0 20px #c1c3c5;
    border-radius: 10px;
    padding: 40px 40px 30px 15px;
    width: auto;

  }
</style>
