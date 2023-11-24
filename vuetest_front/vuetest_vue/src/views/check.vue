<template>
  <div>
    <el-row class="title">审核消息列表</el-row>
    <el-row>
      <el-card style="width: 95%;margin-left: 20px">
        <!-- type:index会自动生成序号 -->
        <el-table :data="checkdata.slice((currentPage-1)*pagesize,currentPage*pagesize)" style="width: 100%">
          <el-table-column
            label="序号"
            type="index"
            :index="indexAdd"
            width="50"
          />
          <el-table-column prop="userid" label="用户id" width="180">
          </el-table-column>
          <el-table-column prop="storeid" label="商家id" width="180">
          </el-table-column>
          <el-table-column prop="submittime" label="提交时间" width="180">
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template v-slot="scope" class="active">
              <el-button @click="info(scope)">审核信息</el-button>
            </template>
            <el-dialog
              title="商家信息"
              :visible.sync="dialog2form"
              :append-to-body="true"
            @close="clear">
              <el-form v-model="form" style="text-align: left" ref="dataForm" >
                <el-form-item label="店名" :label-width="formLabelWidth" prop="storename">
                  <el-input v-model="form.storename" autocomplete="off" readonly="true" ></el-input>
                </el-form-item>
                <el-form-item label="统一社会信用代码" :label-width="formLabelWidth" prop="storecode">
                  <el-input v-model="form.storecode" autocomplete="off" readonly="true" ></el-input>
                </el-form-item>
                <el-form-item label="商家地址" :label-width="formLabelWidth" prop="storeaddr">
                  <el-input v-model="form.storeaddr" autocomplete="off" readonly="true"></el-input>
                </el-form-item>
                <el-form-item label="营业时间" :label-width="formLabelWidth" prop="openinghours">
                  <el-input v-model="form.openinghours" autocomplete="off" readonly="true"></el-input>
                </el-form-item>
                <el-form-item label="联系电话" :label-width="formLabelWidth" prop="contact">
                  <el-input v-model="form.contact" autocomplete="off"  readonly="true"></el-input>
                </el-form-item>
                <el-form-item label="店铺简介" :label-width="formLabelWidth" prop="introduction">
                  <el-input type="textarea" v-model="form.introduction" autocomplete="off" readonly="true"></el-input>
                </el-form-item>
                <el-form-item label="商家图片" :label-width="formLabelWidth" prop="cover">
                  <img :src="form.cover" />
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialog2form = false">取 消</el-button>
                <el-button @click= pass()>通过审核</el-button>
                <el-button @click="dialog1form = true">不通过审核</el-button>
              </div>
            </el-dialog>
            <el-dialog
              title="填写审核不通过原因"
              :visible.sync="dialog1form"
              :append-to-body="true"
              @close="clear">
              <el-form v-model="form2" style="text-align: left" ref="dataForm" >
                <el-form-item :label-width="formLabelWidth">
                  <el-input type="textarea" v-model="form2.reason" autocomplete="off"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="keep2alive()">取 消</el-button>
                <el-button @click="unpass()">确 认</el-button>
              </div>
            </el-dialog>
          </el-table-column>
        </el-table>
        <span></span>
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pagesize"
          :total="checkdata.length">
        </el-pagination>

      </el-card>
    </el-row>

  </div>

</template>

<script>
export default {
  name: 'Store',
  data () {
    return {
      checkdata: [],
      storedata: {},
      currentPage: 1,
      pagesize: 5,
      dialog2form: false,
      dialog1form: false,
      form2: {
        reason: ''
      },
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
    this.showcheckdata()
  },
  methods: {
    showcheckdata () {
      var _this = this
      this.$axios.post('/checkdata', {
        username: '123',
        password: '123'
      }, {headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }}).then(resp => {
        if (resp && resp.status === 200) {
          _this.checkdata = resp.data
          for (let i = 0; i < Object.values(_this.checkdata).length; i++) {
            console.log(_this.checkdata[i].storeid)
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
    unpass () {
      this.axios({
        method: 'post',
        params: {
          storeid: this.checkdata[this.tempidx].storeid,
          checkid: this.checkdata[this.tempidx].checkid,
          status: this.form2.reason
        },
        url: '/unpassstore'
      }).then(resp => {
        if (resp && resp.data.code === 200) {
          this.dialog1form = false
          this.dialog2form = false
          this.checkdata.splice(this.tempidx, 1)
          this.$message.success(resp.data.message)
        } else {
          this.$message.error(resp.data.message)
        }
      })
    },
    pass () {
      this.axios({
        method: 'post',
        params: {
          storeid: this.checkdata[this.tempidx].storeid,
          checkid: this.checkdata[this.tempidx].checkid
        },
        url: '/passstore'
      }).then(resp => {
        if (resp && resp.data.code === 200) {
          this.dialog2form = false
          this.checkdata.splice(this.tempidx, 1)
          this.$message.success(resp.data.message)
        } else {
          this.$message.error(resp.data.message)
        }
      })
    },
    keep2alive () {
      this.dialog2form = false
      this.dialog1form = false
    },
    info (scope) {
      var _this = this
      console.log(this.checkdata[scope.$index].storeid)
      this.tempidx = scope.$index
      this.axios({
        method: 'post',
        params: {
          storeid: this.checkdata[scope.$index].storeid
        },
        url: '/getstore'
      }).then(resp => {
        if (resp && resp.status === 200) {
          _this.storedata = resp.data
          this.storedata = resp.data
          this.dialog2form = true
          this.form.storename = this.storedata.storename
          this.form.storecode = this.storedata.storecode
          this.form.storeaddr = this.storedata.storeaddr
          this.form.openinghours = this.storedata.openinghours
          this.form.contact = this.storedata.contact
          this.form.introduction = this.storedata.introduction
          this.form.cover = this.storedata.cover
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
