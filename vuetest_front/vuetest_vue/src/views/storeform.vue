<template>
  <div>
    <el-dialog
      title="填写商家信息"
      :visible.sync="dialogFormVisible"
      @close="clear">
      <el-form v-model="form" style="text-align: left" ref="dataForm">
        <el-form-item label="店名" :label-width="formLabelWidth" prop="title">
          <el-input v-model="form.storename" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item label="统一社会信用代码" :label-width="formLabelWidth" prop="author">
          <el-input v-model="form.storecode" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="商家地址" :label-width="formLabelWidth" prop="date">
          <el-input v-model="form.storeaddr" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="营业时间" :label-width="formLabelWidth" prop="press">
          <el-input v-model="form.openinghours" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" :label-width="formLabelWidth" prop="cover">
          <el-input v-model="form.contact" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item label="店铺简介" :label-width="formLabelWidth" prop="abs">
          <el-input type="textarea" v-model="form.instruction" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'StoreForm',
  data () {
    return {
      dialogFormVisible: false,
      form: {
        storename: '',
        storecode: '',
        storeaddr: '',
        openinghours: '',
        contact: '',
        instruction: '',
        status: 0
      },
      formLabelWidth: '140px'
    }
  },
  methods: {
    clear () {
      this.form = {
        storename: '',
        storecode: '',
        storeaddr: '',
        openinghours: '',
        contact: '',
        instruction: ''
      }
    },
    onSubmit () {
      this.$axios
        .post('/addstore', {
          storename: this.form.storename,
          storecode: this.form.storecode,
          storeaddr: this.form.storeaddr,
          openinghours: this.form.openinghours,
          contact: this.form.contact,
          instruction: this.form.instruction,
          status: this.status
        }).then(resp => {
          if (resp && resp.status === 200) {
            this.dialogFormVisible = false
            this.$emit('onSubmit')
          }
        })
    }
  }
}
</script>

<style scoped>
.el-icon-circle-plus-outline {
  font-size: 20px;
}
.el-button{
  cursor: pointer;
}
.buttondiv{
  padding-left: 20px;
  margin-bottom: 20px;
  text-align: left;
}

</style>
