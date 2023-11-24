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
          <el-form-item label="账户" prop="username" style="width: 380px" label-width="100px">
            <el-input v-model="dataForm.username" placeholder="账户为您的手机号码"></el-input>
          </el-form-item>
<!--          <el-form-item label="手机验证码" prop="vcode" style="width: 380px" label-width="100px">-->
<!--            <el-input v-model="dataForm.vcode"></el-input>-->
<!--          </el-form-item>-->

          <el-form-item label="手机验证码" prop="vcode" style="width: 380px" label-width="95px">
            <el-row :gutter="10" >
              <el-col :span="16">
                <el-input v-model="dataForm.vcode" placeholder="手机验证码"></el-input>
              </el-col>
              <el-col :span="4">
                <el-button type="primary" plain round size="medium" @click="createCodeAndSend()" :disabled="isDisabled">{{ buttonName }}</el-button>
              </el-col>
            </el-row>
          </el-form-item>

          <el-form-item>
            <el-row :gutter="12" style="margin-top:10px;">
              <el-col :span="20" style="text-align:center;">
                <el-button type="primary" size="medium" @click="submit('dataForm')">登录</el-button>
                <el-button @click="login()">账号密码登录</el-button>
              </el-col>
            </el-row>
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
      buttonName: '发送短信',
      isDisabled: false,
      time: 60,
      // 表单信息
      dataForm: {
        // 账户数据
        username: '',
        // 验证码数据
        vcode: '',
        // 真实验证码数据
        truecode: ''
      },
      createPhoneCode: '',

      // 表单验证
      rules: {
        // 设置账户效验规则
        username: [
          {required: true, message: '请输入手机号', trigger: 'blur'},
          {pattern: /^1[3456789]\d{9}$/, message: '手机号码格式不正确', trigger: 'blur'}
        ],
        // 设置验证码效验规则
        vcode: [
          {required: true, message: '请输入验证码', trigger: 'blur'},
          {min: 4, max: 4, message: '请输入正确的4位验证码', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    // 生成六位的数字验证码并发送短信
    createCodeAndSend () {
      if (/^1[3456789]\d{9}$/.test(this.dataForm.username) === true) {
        let me = this
        me.isDisabled = true
        let interval = window.setInterval(function () {
          me.buttonName = '(' + me.time + ')秒'
          --me.time
          if (me.time < 0) {
            me.buttonName = '重新发送'
            me.time = 60
            me.isDisabled = false
            window.clearInterval(interval)
          }
        }, 1000)

        var codeLength = 4
        // foreach循环，六次
        this.createPhoneCode = ''
        for (var i = 0; i < codeLength; i++) {
          var index = Math.floor(Math.random() * (9))
          index = index.toString()
          this.createPhoneCode += index
        }

        console.log(this.createPhoneCode)
        this.axios({
          method: 'post',
          params: {
            username: this.dataForm.username,
            createPhoneCode: this.createPhoneCode
          },
          url: '/code'
        })
          .then(successResponse => {
            if (successResponse.data.code === 200) {
              this.$message.success('短信验证码发送成功')
            } else {
              this.$message.error('短信验证码发送失败')
            }
          })
      } else {
        this.$message.warning('请输入正确手机号')
      }
    },

    // 提交登录
    submit (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.createPhoneCode === this.dataForm.vcode) {
            this.axios({
              method: 'post',
              params: {
                username: this.dataForm.username
              },
              url: '/login2'
            }).then(successResponse => {
              if (successResponse.data.code === 200) {
                this.$router.replace({path: '/main'})
                this.$message.success('登录成功')
              } else {
                this.$message.error('验证码错误')
              }
            })
          } else {
            this.$message.error('短信验证码错误')
          }
        } else {
          this.$message.error('提交出现错误')
          return false
        }
      })
    },
    login () {
      this.$router.replace({path: '/login'})
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
