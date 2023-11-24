<template>
  <div style="font-size: 18px;">
    <el-form :model="dataForm" :rules="rules" ref="dataForm" label-width="21%" class="dataForm">
      <el-form-item class="item" label="用户昵称" prop="nickname">
        <el-input style="font-size: 18px;" v-model="dataForm.nickname"></el-input>
      </el-form-item>

      <el-form-item class="item" label="手机号码" prop="phone">
        <el-input style="font-size: 18px;" maxlength="18" type="text" readonly="true" v-model="dataForm.phone"></el-input>
      </el-form-item>

      <el-form-item class="item" label="用户身份" prop="identity">
        <el-input  style="font-size: 18px;" maxlength="18" type="text" readonly="true" value="普通用户"></el-input>
      </el-form-item>

      <el-form-item class="item" label="电子邮箱" prop="eaddress">
        <el-input style="font-size: 18px;" v-model="dataForm.eaddress"></el-input>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" plain round size="medium" @click="updata('dataForm')">更新用户</el-button>
      <el-button type="danger" plain round size="medium" @click="deleteuser()">注销用户</el-button>
    </div>
  </div>

</template>

<script>
export default {
  data () {
    return {
      dataForm: {
        nickname: '',
        phone: '',
        eaddress: ''
      },
      rules: {
        // // 设置用户昵称效验规则
        nickname: [
          {required: true, message: '请输入用户昵称', trigger: 'blur'},
          {min: 1, max: 100, message: '昵称长度不能为空', trigger: 'blur'}
        ],
        // 设置邮箱地址效验规则
        eaddress: [
          {required: true, message: '请输入邮箱地址', trigger: 'blur'},
          {pattern: /^([a-zA-Z0-9]+[-_]?)+@[a-zA-Z0-9]+\.[a-z]+$/, message: '请输入正确邮箱地址', trigger: 'blur'}
        ]
      }
    }
  },
  created () {
    this.init()
  },
  methods: {
    // 渲染用户信息
    init () {
      this.axios({
        method: 'post',
        url: '/information'
      }).then(resp => {
        this.dataForm.nickname = resp.data.nickname
        this.dataForm.phone = resp.data.username
        this.dataForm.eaddress = resp.data.eaddress
      })
    },

    // 更新用户信息
    updata (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.axios({
            method: 'post',
            params: {
              nickname: this.dataForm.nickname,
              eaddress: this.dataForm.eaddress
            },
            url: '/updateinfo'
          })
            .then(resp => {
              this.dataForm.nickname = resp.data.nickname
              this.dataForm.eaddress = resp.data.eaddress
              this.$message.success('更新成功')
            })
        } else {
          this.$message.error('修改出现错误')
        }
      })
    },

    // 注销用户
    deleteuser () {
      this.axios({
        method: 'post',
        url: '/deleteinfo'
      })
        .then(resp => {
          this.$message.success(resp.data.message)
          this.$router.replace({path: '/login'})
        })
    }
  }
}
</script>

<style>
  .item .el-form-item__label{
    font-size: 18px;
  }
</style>
