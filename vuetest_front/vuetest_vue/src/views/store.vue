<template>
  <div>
    <el-row class="title">我的关联商家</el-row>
<!--    <div class="button">-->
<!--      <el-button type="primary" style="width: 20%;background: #505458;-->
<!--      border: none" v-on:click="addmystore">-->
<!--        新建商家-->
<!--      </el-button>-->
<!--    </div>-->
    <el-row>
      <div>
        <div class="buttondiv">
          <el-button @click="dialogFormVisible = true" class="el-button">
            <i class="el-icon-circle-plus-outline"></i>
            <span class="text">新建商家</span>
          </el-button>
        </div>
        <el-dialog
          title="填写商家信息"
          :visible.sync="dialogFormVisible"
          @close="clear">
          <el-form v-model="form" style="text-align: left" ref="dataForm">
            <el-form-item label="店名" :label-width="formLabelWidth" prop="storename">
              <el-input v-model="form.storename" autocomplete="off" ></el-input>
            </el-form-item>
            <el-form-item label="统一社会信用代码" :label-width="formLabelWidth" prop="storecode">
              <el-input v-model="form.storecode" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="商家地址" :label-width="formLabelWidth" prop="storeaddr">
              <el-input v-model="form.storeaddr" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="营业时间" :label-width="formLabelWidth" prop="openinghours">
              <el-input v-model="form.openinghours" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="联系电话" :label-width="formLabelWidth" prop="contact">
              <el-input v-model="form.contact" autocomplete="off" ></el-input>
            </el-form-item>
            <el-form-item label="店铺简介" :label-width="formLabelWidth" prop="introduction">
              <el-input type="textarea" v-model="form.introduction" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="上传商家图片" :label-width="formLabelWidth" prop="cover">
              <img-upload @onUpload="uploadImg" ref="imgUpload"></img-upload>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="onSubmit">确 定</el-button>
          </div>
        </el-dialog>
      </div>
    </el-row>
    <el-row>
      <el-card style="width: 95%;margin-left: 20px">
        <!-- type:index会自动生成序号 -->
        <el-table :data="mystore.slice((currentPage-1)*pagesize,currentPage*pagesize)" style="width: 100%">
          <el-table-column
            label="序号"
            type="index"
            :index="indexAdd"
            width="50"
          />
          <el-table-column prop="storename" label="店名" width="180">
          </el-table-column>
          <el-table-column prop="storeaddr" label="地址" width="180">
          </el-table-column>
          <el-table-column label="商家图片" width="180">
            <template v-slot="scope">
              <img :src="scope.row.cover" />
            </template>
          </el-table-column>
          <el-table-column prop="contact" label="联系电话" width="100">
          </el-table-column>
          <el-table-column prop="status" label="审核状态" width="100">
          </el-table-column>
          <el-table-column label="操作" align="center">
          <template v-slot="scope" class="active">
            <el-button @click="food(scope)"
                       type="warning" icon="el-icon-tableware" circle></el-button>
            <el-button @click="dialog2(scope)"
                       type="primary" icon="el-icon-edit" circle></el-button>
            <el-dialog
              title="编辑商家信息"
              :visible.sync="dialog2form"
              @close="clear">
              <el-form v-model="form" style="text-align: left" ref="dataForm">
                <el-form-item label="店名" :label-width="formLabelWidth" prop="storename">
                  <el-input v-model="form.storename" autocomplete="off" ></el-input>
                </el-form-item>
                <el-form-item label="统一社会信用代码" :label-width="formLabelWidth" prop="storecode">
                  <el-input v-model="form.storecode" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="商家地址" :label-width="formLabelWidth" prop="storeaddr">
                  <el-input v-model="form.storeaddr" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="营业时间" :label-width="formLabelWidth" prop="openinghours">
                  <el-input v-model="form.openinghours" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="联系电话" :label-width="formLabelWidth" prop="contact">
                  <el-input v-model="form.contact" autocomplete="off" ></el-input>
                </el-form-item>
                <el-form-item label="店铺简介" :label-width="formLabelWidth" prop="introduction">
                  <el-input type="textarea" v-model="form.introduction" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="上传商家图片" :label-width="formLabelWidth" prop="cover">
                  <img-upload @onUpload="uploadImg" ref="imgUpload"></img-upload>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialog2form = false">取 消</el-button>
                <el-button type="primary" @click="updatestore()">确 定</el-button>
              </div>
            </el-dialog>
            <el-button @click="deletestore(scope)"
            type="danger" icon="el-icon-delete" circle></el-button>
          </template>
          </el-table-column>
        </el-table>
        <span></span>
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pagesize"
          :total="mystore.length">
        </el-pagination>

      </el-card>
    </el-row>

  </div>

</template>

<script>
import ImgUpload from './ImgUpload'
export default {
  name: 'Store',
  components: {ImgUpload},
  data () {
    return {
      mystore: [],
      currentPage: 1,
      pagesize: 5,
      dialogFormVisible: false,
      dialog2form: false,
      form: {
        storename: '',
        storecode: '',
        storeaddr: '',
        openinghours: '',
        contact: '',
        introduction: '',
        status: '',
        cover: ''
      },
      formLabelWidth: '140px',
      tempidx: 0
    }
  },
  created: function () {
    this.showmystore()
  },
  methods: {
    uploadImg () {
      this.form.cover = this.$refs.imgUpload.url
    },
    showmystore () {
      var _this = this
      this.$axios.post('/mystore', {
        username: '123',
        password: '123'
      }, {headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }}).then(resp => {
        if (resp && resp.status === 200) {
          _this.mystore = resp.data
          for (let i = 0; i < Object.values(_this.mystore).length; i++) {
            console.log(_this.mystore[i].storeid)
          }
        }
      })
    },
    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage
      console.log(this.currentPage)
    },
    indexAdd (index) {
      // console.log(index, 222222)
      const page = this.currentPage // 当前页码
      const pagesize = this.pagesize // 每页条数
      return index + 1 + (page - 1) * pagesize
    },
    clear () {
      this.form = {
        storename: '',
        storecode: '',
        storeaddr: '',
        openinghours: '',
        contact: '',
        introduction: '',
        cover: ''
      }
    },
    onSubmit () {
      let obj = {
        storename: this.form.storename,
        storecode: this.form.storecode,
        storeaddr: this.form.storeaddr,
        openinghours: this.form.openinghours,
        contact: this.form.contact,
        introduction: this.form.introduction,
        status: '未审核',
        cover: this.form.cover
      }
      console.log(this.form.storename)
      this.$axios
        .post('/addstore', {
          storename: this.form.storename,
          storecode: this.form.storecode,
          storeaddr: this.form.storeaddr,
          openinghours: this.form.openinghours,
          contact: this.form.contact,
          introduction: this.form.introduction,
          status: '未审核',
          cover: this.form.cover
        }).then(resp => {
          if (resp && resp.data.code === 200) {
            obj.status = '未审核'
            this.dialogFormVisible = false
            this.$message.success(resp.data.message)
            this.showmystore()
            // this.$emit('onSubmit')
          } else {
            this.$message.error(resp.data.message)
          }
        })
    },
    food (scope) {
      console.log('*************' + this.mystore[scope.$index].storeid)
      this.axios({
        method: 'post',
        params: {
          storeid: this.mystore[scope.$index].storeid
        },
        url: '/storeid'
      })
      this.$router.push('/food')
    },
    deletestore (scope) {
      console.log('!!!')
      console.log(this.mystore[scope.$index].storeid)
      this.$confirm('是否永久删除该商家', '警告', {
        confirmButtonText: '继续',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios
          .post('/deletestore', {
            storeid: this.mystore[scope.$index].storeid,
            storename: this.mystore[scope.$index].storename,
            storecode: this.mystore[scope.$index].storecode,
            storeaddr: this.mystore[scope.$index].storeaddr,
            openinghours: this.mystore[scope.$index].openinghours,
            contact: this.mystore[scope.$index].contact,
            introduction: this.mystore[scope.$index].introduction,
            status: this.mystore[scope.$index].status
          }).then(resp => {
            if (resp && resp.data.code === 200) {
              this.mystore.splice(scope.$index, 1)
              this.$message.success(resp.data.message)
            } else {
              this.$message.error(resp.data.message)
            }
          })
      })
    },
    dialog2 (scope) {
      console.log(this.mystore[scope.$index].storeid)
      this.tempidx = scope.$index
      this.dialog2form = true
      this.form.storename = this.mystore[scope.$index].storename
      this.form.storecode = this.mystore[scope.$index].storecode
      this.form.storeaddr = this.mystore[scope.$index].storeaddr
      this.form.openinghours = this.mystore[scope.$index].openinghours
      this.form.contact = this.mystore[scope.$index].contact
      this.form.introduction = this.mystore[scope.$index].introduction
      this.form.cover = this.mystore[scope.$index].cover
    },
    updatestore () {
      console.log('!!!')
      console.log(this.tempidx)
      var _status = '未审核'
      this.$axios
        .post('/updatestore', {
          storeid: this.mystore[this.tempidx].storeid,
          storename: this.form.storename,
          storecode: this.form.storecode,
          storeaddr: this.form.storeaddr,
          openinghours: this.form.openinghours,
          contact: this.form.contact,
          introduction: this.form.introduction,
          status: _status,
          cover: this.form.cover
        }).then(resp => {
          if (resp && resp.data.code === 200) {
            this.dialog2form = false
            this.showmystore()
            this.$message.success(resp.data.message)
          } else {
            this.dialog2form = false
            this.$message.error(resp.data.message)
          }
        })
    }
  }
}
</script>
<style scoped>

img {
  width: 160px;
  height: 120px;
  /*margin: 0 auto;*/
}

a {
  text-decoration: none;
}

a:link, a:visited, a:focus {
  color: #3377aa;
}
.title {
  margin-left: 20px;
  text-align: left;
  height: 60px;
  color: #6c6c6c;
  font-size: 22px;
  line-height: 60px;
}
.button {
  margin-left: 20px;
  text-align: left;
  height: 60px;
  color: #6c6c6c;
  font-size: 22px;
  line-height: 60px;
  text-align: left;
}

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
