<template>
  <div>
    <el-row class="title">我的评论</el-row>
    <el-row>
      <el-card style="width: 95%;margin-left: 20px">
        <!-- type:index会自动生成序号 -->
        <el-table :data="mycomment.slice((currentPage-1)*pagesize,currentPage*pagesize)" style="width: 100%">
          <el-table-column
            label="序号"
            type="index"
            :index="indexAdd"
            width="50"
          />
          <el-table-column align="center" prop="storename" label="商家名称" width="180">
          </el-table-column>
          <el-table-column align="center" prop="foodname" label="美食名称" width="180">
          </el-table-column>
          <el-table-column align="center" prop="content" label="评论内容" width="400">
          </el-table-column>
          <el-table-column align="center" prop="score" label="我的评分" width="100">
          </el-table-column>
          <el-table-column align="center" label="操作" width="180">
            <template v-slot="scope" class="active">
              <el-button @click="food(scope)"
                         type="warning" icon="el-icon-tableware" circle></el-button>
              <el-button @click="dialog2(scope)"
                         type="primary" icon="el-icon-edit" circle></el-button>
              <el-dialog
                title="编辑评论信息"
                :visible.sync="dialog2form"
                @close="clear">
                <el-form v-model="form" style="text-align: left" ref="dataForm">
                  <el-form-item label="评论内容" :label-width="formLabelWidth" prop="content">
                    <el-input v-model="form.content" autocomplete="off" ></el-input>
                  </el-form-item>
                  <el-form-item label="评分" :label-width="formLabelWidth" prop="score">
                    <el-input v-model="form.score" autocomplete="off"></el-input>
                  </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="dialog2form = false">取 消</el-button>
                  <el-button type="primary" @click="updatecomment()">确 定</el-button>
                </div>
              </el-dialog>
              <el-button @click="deletecomment(scope)"
                         type="danger" icon="el-icon-delete" circle></el-button>
            </template>
          </el-table-column>
        </el-table>
        <span></span>
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pagesize"
          :total="mycomment.length">
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
      mycomment: [],
      currentPage: 1,
      pagesize: 5,
      dialog2form: false,
      form: {
        content: '',
        score: 0
      },
      formLabelWidth: '140px',
      tempidx: 0
    }
  },
  created: function () {
    this.showmycomment()
  },
  methods: {
    showmycomment () {
      var _this = this
      this.$axios.post('/mycomment', {
        username: '123',
        password: '123'
      }, {headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }}).then(resp => {
        if (resp && resp.status === 200) {
          _this.mycomment = resp.data
          for (let i = 0; i < Object.values(_this.mycomment).length; i++) {
            console.log(_this.mycomment[i].commentid)
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
        content: '',
        score: 0
      }
    },
    food (scope) {
      console.log('*************' + this.mycomment[scope.$index].foodid)
      this.axios({
        method: 'post',
        params: {
          foodid: this.mycomment[scope.$index].foodid
        },
        url: '/setfoodid'
      })
      this.$router.push('/foodpage')
    },
    deletecomment (scope) {
      console.log('!!!')
      console.log(this.mycomment[scope.$index].commentid)
      this.$confirm('是否永久删除该评论', '警告', {
        confirmButtonText: '继续',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios(
          {
            method: 'post',
            params: {
              commentid: this.mycomment[scope.$index].commentid
            },
            url: '/deletecomment'
          }).then(resp => {
          if (resp && resp.data.code === 200) {
            this.mycomment.splice(scope.$index, 1)
            this.$message.success(resp.data.message)
          } else {
            this.$message.error(resp.data.message)
          }
        })
      })
    },
    dialog2 (scope) {
      console.log(this.mycomment[scope.$index].commentid)
      this.tempidx = scope.$index
      this.dialog2form = true
      this.form.content = this.mycomment[scope.$index].content
      this.form.score = this.mycomment[scope.$index].score
    },
    updatecomment () {
      console.log('!!!')
      console.log(this.tempidx)
      this.$axios
        .post('/updatecomment', {
          commentid: this.mycomment[this.tempidx].commentid,
          userid: this.mycomment[this.tempidx].userid,
          foodid: this.mycomment[this.tempidx].foodid,
          content: this.form.content,
          score: this.form.score,
          foodname: this.mycomment[this.tempidx].foodname,
          storename: this.mycomment[this.tempidx].storename,
          submittime: this.mycomment[this.tempidx].submittime,
          nickname: this.mycomment[this.tempidx].nickname
        }).then(resp => {
          if (resp && resp.data.code === 200) {
            this.dialog2form = false
            this.showmycomment()
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
