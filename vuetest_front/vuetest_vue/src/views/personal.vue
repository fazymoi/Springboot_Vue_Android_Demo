<template>
  <div style="background-color:#FCFCFC;height:100%;">
    <div style="margin-top:25px;margin-left:80px;">
      <el-row :gutter="10">
        <el-col :span="2">
          <div style="background-color:#FFEBCD;width:60px;height:60px;display:inline-block;border-radius:50%;overflow:hidden;">
            <template>
              <img :src="this.cover" @click="dialog2()" class="my_img"/>
            </template>
          </div>

          <el-dialog
            title="编辑头像"
            :visible.sync="dialog2form"
            @close="clear">
            <el-form style="text-align: left" ref="dataForm">
              <el-form-item label="上传投头像" :label-width="formLabelWidth" prop="cover">
                <img-upload @onUpload="uploadImg" ref="imgUpload" v-model="cover"></img-upload>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="dialog2form = false">取 消</el-button>
              <el-button type="primary" @click="updateimg()">确 定</el-button>
            </div>
          </el-dialog>

          <div style="font-size: 24px;color:#6c6c6c;margin-top:5px;margin-left:6px;"><span>{{ mynickname }}</span></div>
          <div style="margin-top:50px;margin-left:1px;"><el-button type="text" style="font-size:18px;color:#4D4D4D;"  @click="infomationClick()">个人信息<span style="color:#6c6c6c;" v-show="infomationShow" class="el-icon-s-promotion"></span></el-button></div>
          <div style="margin-top:5px;margin-left:1px;"><el-button type="text" style="font-size:18px;color:#4D4D4D;" @click="passwordClick()">修改密码<span style="color:#6c6c6c;" v-show="passwordShow" class="el-icon-s-promotion"></span></el-button></div>
        </el-col>

        <el-col :span="16">
          <el-row :gutter="0" style="margin-top:20px;">
            <el-col :span="3">
              <div style="font-size: 20px;color:#4D4D4D;text-align:right;"><span>账号：</span></div>
            </el-col>
            <el-col :span="4" style="font-size: 20px;color:#4D4D4D;">{{ myaccount }}</el-col>
          </el-row>

          <!-- 个人信息 -->
          <el-row v-show="infomationShow" style="margin-top:30px;margin-left:20px;">
            <el-card style="margin-top:20px;color:#6c6c6c;font-size: 20px;background: #F4E4CD;box-shadow: 0 0 20px #6f7985;">
              <information />
            </el-card>
          </el-row>
          <!-- 修改密码 -->
          <el-row v-show="passwordShow" style="margin-top:30px;margin-left:20px;">
            <el-card style="margin-top:20px;color:#6c6c6c;font-size: 20px;background: #F4E4CD;box-shadow: 0 0 20px #6f7985;">
              <el-form :model="personalForm">

                <el-row :gutter="12" style="margin-top:10px;">
                  <el-col :span="10">
                    <div style="text-align:right;"><span>密码：</span></div>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item prop="password1">
                      <el-input type="password" show-password v-model="personalForm.password1" placeholder="请输入新的密码" ></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row :gutter="12" style="margin-top:10px;">
                  <el-col :span="10">
                    <div style="text-align:right;"><span>确认密码：</span></div>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item prop="password2">
                      <el-input type="password" show-password v-model="personalForm.password2" placeholder="请再次输入新的密码" ></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="12" style="margin-top:10px;">
                  <el-col :span="24" style="text-align:center;">
                    <el-button type="info" plain size="medium" @click="submit()">修改</el-button>
                  </el-col>
                </el-row>
              </el-form>
            </el-card>
          </el-row>

        </el-col>
      </el-row>
    </div>

    <div>

    </div>
  </div>
</template>
<script>
import information from './information'
import ImgUpload from './ImgUpload'
export default {
  components: {information, ImgUpload},
  data: function () {
    return {
      infomationShow: true,
      passwordShow: false,
      mynickname: '',
      myaccount: '',
      personalForm: {
        password1: '',
        password2: ''
      },
      dialog2form: false,
      formLabelWidth: '140px',
      cover: ''
    }
  },
  created () {
    this.update()
  },
  methods: {
    infomationClick () { // 个人信息事件
      this.infomationShow = true
      this.passwordShow = false
    },
    passwordClick () { // 密码事件
      this.infomationShow = false
      this.passwordShow = true
    },
    update () {
      this.axios({
        method: 'post',
        url: '/personal'
      }).then(resp => {
        this.mynickname = resp.data.nickname
        this.myaccount = resp.data.username
        this.cover = resp.data.cover
      })
    },
    submit () { // 提交
      if (this.personalForm.password1 === '') {
        this.$message.warning('密码不能为空')
      } else if (this.personalForm.password2 === '') {
        this.$message.warning('密码不能为空')
      } else {
        if (this.personalForm.password1 === this.personalForm.password2) {
          this.$message.success('修改成功')
          this.axios({
            method: 'post',
            params: {
              password1: this.personalForm.password1,
              password2: this.personalForm.password2
            },
            url: '/password'
          })
        } else {
          this.$message.warning('两次输入的密码不一致,请重新输入')
        }
      }
    },

    clear () {
      this.cover = ''
    },
    dialog2 () {
      this.dialog2form = true
    },
    uploadImg () {
      this.cover = this.$refs.imgUpload.url
    },
    updateimg () {
      console.log(this.myaccount)
      console.log(this.cover)
      this.$axios({
        method: 'post',
        params: {
          username: this.myaccount,
          cover: this.cover
        },
        url: '/updateimg'
      }).then(resp => {
        if (resp && resp.data.code === 200) {
          this.$message.success(resp.data.message)
        } else {
          this.$message.error(resp.data.message)
        }
        this.update()
        this.dialog2form = false
      })
    }
  }
}
</script>

<style>
  .my_img{
    width:60px;
    height:60px;
  }
  .name{ text-align: right; }
  .value{ text-align: left; }
</style>
