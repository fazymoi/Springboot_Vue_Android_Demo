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
        <el-form :model="dataForm" :rules="rules" ref="dataForm" label-width="21%" class="dataForm">
          <h3 class="login_title" style="margin-top:-10px">用户登录</h3>
          <el-form-item label="账户" prop="username" style="width: 380px">
            <el-input v-model="dataForm.username" placeholder="账户为您的手机号码"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password" style="width: 380px">
            <el-input type="password" v-model="dataForm.password" placeholder="密码"></el-input>
          </el-form-item>
          <el-form-item label="验证码" prop="code" style="width: 380px">
            <el-input v-model="dataForm.code" class="code-input" style="width: 60%;float: left" placeholder="验证码"></el-input>
            <!--验证码组件-->
            <div @click="refreshCode">
              <s-identify :identifyCode="identifyCode"></s-identify>
            </div>
          </el-form-item>
<!--          <el-form-item label="记住密码" prop="remember">-->
<!--            <el-switch v-model="dataForm.remember"></el-switch>-->
<!--          </el-form-item>-->

          <el-form-item label="身份" prop="identity" style="width: 380px">
            <el-select v-model="dataForm.identity" placeholder="请选择用户身份" style="width: 300px">
              <el-option v-for="item in dataForm.options" :key="item.label" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item class="btn-ground" style="width: 480px;margin-left:-90px">
            <el-button type="info" @click="register('dataForm')" >注册</el-button>
            <el-button type="primary" @click="submitForm('dataForm')">登录</el-button>
            <el-button @click="login2()">普通用户手机验证码登录</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import SIdentify from '../views/SIdentify'
export default {
  name: 'Login',
  components: {SIdentify},
  data () {
    return {
      // 表单信息
      dataForm: {
        // 账户数据
        username: '',
        // 密码数据
        password: '',
        // 验证码数据
        code: '',
        // 用户身份选择
        options: [{label: '普通用户', value: '普通用户'}, {label: '管理员', value: '管理员'}],
        // 用户身份数据
        identity: ''
      },
      identifyCodes: '1234567890abcdefjhijklinopqrsduvwxyz', // 随机串内容
      identifyCode: '',
      // 表单验证
      rules: {
        // 设置账户效验规则
        username: [
          {required: true, message: '请输入手机号', trigger: 'blur'},
          {pattern: /^1[3456789]\d{9}$/, message: '手机号码格式不正确', trigger: 'blur'}
        ],
        // 设置密码效验规则
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 16, message: '长度为3-16位', trigger: 'blur'}
        ],
        // 设置验证码效验规则
        code: [
          {required: true, message: '请输入验证码', trigger: 'blur'},
          {min: 4, max: 4, message: '长度为 4 个字符', trigger: 'blur'}
        ],
        identity: [
          { required: true, message: '请选择身份', trigger: 'change' }
        ]
      },
      // 绑定验证码图片
      codeImg: null
    }
  },
  created () {
    this.dataForm.identity = this.dataForm.options[0].value
  },
  mounted () {
    // 初始化验证码
    this.identifyCode = ''
    this.makeCode(this.identifyCodes, 4)
  },
  methods: {
    refreshCode () {
      this.identifyCode = ''
      this.makeCode(this.identifyCodes, 4)
    },
    makeCode (o, l) {
      for (let i = 0; i < l; i++) {
        this.identifyCode += this.identifyCodes[this.randomNum(0, this.identifyCodes.length)]
      }
    },
    randomNum (min, max) {
      return Math.floor(Math.random() * (max - min) + min)
    },
    // 提交表单
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.dataForm.code.toLowerCase() !== this.identifyCode.toLowerCase()) {
            this.$message.error('请填写正确验证码')
            this.refreshCode()
          } else {
            // 表单验证成功
            // alert('submit')
            // this.$axios
            //   .post('/login', {
            //     username: this.dataForm.username,
            //     password: this.dataForm.password,
            //     identity: this.dataForm.identity
            //   })

            this.axios({
              method: 'post',
              params: {
                username: this.dataForm.username,
                password: this.dataForm.password,
                identity: this.dataForm.identity
              },
              url: '/login'
            })

              .then(successResponse => {
                if (successResponse.data.code === 200) {
                  if (this.dataForm.identity === '普通用户') {
                    this.$router.replace({path: '/main'})
                  } else {
                    this.$router.replace({path: '/adminmain'})
                  }
                  // this.$router.replace({path: '/main'})
                  this.$message.success('提交成功')
                } else {
                  this.$message.error('账号或密码错误')
                }
              })
              .catch(failResponse => {
                this.$message.error('账号或密码错误')
              })
          }
        } else {
          this.$message.error('提交出现错误')
        }
      })
    },
    // 重置表单
    login2 () {
      this.$router.replace({path: '/login2'})
    },
    // 注册
    register (formName) {
      this.$refs[formName].resetFields()
      this.$router.push('/register')
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
