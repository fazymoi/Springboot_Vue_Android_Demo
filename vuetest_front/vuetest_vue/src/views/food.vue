<template>
  <div>
    <!--title-->
    <el-row class="title"><el-button @click="back()" icon="el-icon-arrow-left" class="i-back"></el-button>美食管理</el-row>

    <!--新建美食-->
    <el-row>
      <div>
        <div class="buttondiv">
          <el-button @click="dialogFormVisible = true" class="el-button">
            <i class="el-icon-circle-plus-outline"></i>
            <span class="text">新建美食</span>
          </el-button>
        </div>
        <el-dialog
          title="填写美食信息"
          :visible.sync="dialogFormVisible"
          @close="clear">
          <el-form v-model="form" style="text-align: left" ref="dataForm">
            <el-form-item label="美食名称" :label-width="formLabelWidth" prop="title">
              <el-input v-model="form.foodname" autocomplete="off" ></el-input>
            </el-form-item>
            <el-form-item label="价格" :label-width="formLabelWidth" prop="author">
              <el-input v-model="form.price" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="食材" :label-width="formLabelWidth" prop="date">
              <el-input type="textarea" v-model="form.ingredient" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="美食标签" :label-width="formLabelWidth" prop="press">
              <el-input type="textarea" v-model="form.tag" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="上传美食图片" :label-width="formLabelWidth" prop="cover">
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

    <!--美食表格-->
    <el-row>
      <el-card style="width: 95%;margin-left: 20px">
        <!-- type:index会自动生成序号 -->
        <el-table :data="foods.slice((currentPage-1)*pagesize,currentPage*pagesize)" style="width: 100%">
          <el-table-column
            label="序号"
            type="index"
            :index="indexAdd"
            width="100"
          />
          <el-table-column prop="foodname" label="美食名称" width="120">
          </el-table-column>
          <el-table-column label="美食图片" width="200">
            <template v-slot="scope">
              <img :src="scope.row.cover" />
            </template>
          </el-table-column>
          <el-table-column prop="price" label="价格" width="120">
          </el-table-column>
          <el-table-column prop="ingredient" label="食材">
          </el-table-column>
          <el-table-column prop="tag" label="美食标签">
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template v-slot="scope" class="active">

              <!--编辑美食信息-->
              <el-button @click="dialog2(scope)" type="primary" icon="el-icon-edit" circle></el-button>
              <el-dialog
                title="编辑美食信息"
                :visible.sync="dialog2form"
                @close="clear">
                <el-form v-model="form" style="text-align: left" ref="dataForm">
                  <el-form-item label="美食名称" :label-width="formLabelWidth" prop="title">
                    <el-input v-model="form.foodname" autocomplete="off" ></el-input>
                  </el-form-item>
                  <el-form-item label="价格" :label-width="formLabelWidth" prop="author">
                    <el-input v-model="form.price" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="食材" :label-width="formLabelWidth" prop="date">
                    <el-input type="textarea" v-model="form.ingredient" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="美食标签" :label-width="formLabelWidth" prop="press">
                    <el-input type="textarea" v-model="form.tag" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="上传美食图片" :label-width="formLabelWidth" prop="cover">
                    <img-upload @onUpload="uploadImg" ref="imgUpload"></img-upload>
                  </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                  <el-button @click="dialog2form = false">取 消</el-button>
                  <el-button type="primary" @click="updatefood()">确 定</el-button>
                </div>
              </el-dialog>

              <!--删除美食信息-->
              <el-button @click="deletefood(scope)" type="danger" icon="el-icon-delete" circle></el-button>
            </template>
          </el-table-column>

        </el-table>
        <span></span>
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pagesize"
          :total="foods.length">
        </el-pagination>

      </el-card>
    </el-row>

  </div>

</template>

<script>
// import StoreForm from './storeform'
import ImgUpload from './ImgUpload'
export default {
  name: 'Store',
  components: {ImgUpload},
  data () {
    return {
      foods: [],
      currentPage: 1,
      pagesize: 5,
      dialogFormVisible: false,
      dialog2form: false,
      form: {
        foodname: '',
        price: '',
        ingredient: '',
        tag: '',
        cover: ''
      },
      formLabelWidth: '140px',
      tempidx: 0
    }
  },
  created: function () {
    this.showfoods()
  },
  methods: {
    back () {
      this.$router.back()// 后退，原数据保留
    },

    uploadImg () {
      this.form.cover = this.$refs.imgUpload.url
    },

    showfoods () {
      var _this = this
      this.$axios.post('/foods', {headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }}).then(resp => {
        if (resp && resp.status === 200) {
          _this.foods = resp.data
          for (let i = 0; i < Object.values(_this.foods).length; i++) {
            console.log(_this.foods[i].foodid)
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
        foodname: '',
        price: '',
        ingredient: '',
        tag: '',
        cover: ''
      }
    },

    // 添加
    onSubmit () {
      let obj = {
        foodname: this.form.foodname,
        price: this.form.price,
        ingredient: this.form.ingredient,
        tag: this.form.tag,
        cover: this.form.cover
      }
      console.log(this.form.foodname)

      this.axios({
        method: 'post',
        params: {
          foodname: this.form.foodname,
          price: this.form.price,
          ingredient: this.form.ingredient,
          tag: this.form.tag,
          cover: this.form.cover
        },
        url: '/addfood'
      }).then(resp => {
        if (resp && resp.data.code === 200) {
          this.foods.push(obj)
          this.dialogFormVisible = false
          this.$message.success(resp.data.message)
          this.showfoods()
          // this.$emit('onSubmit')
        } else {
          this.$message.error(resp.data.message)
        }
      })
    },

    // 删除
    deletefood (scope) {
      console.log('!!!!!!')
      console.log(this.foods[scope.$index].foodid)
      this.$confirm('是否永久删除该美食', '警告', {
        confirmButtonText: '继续',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios({
          method: 'post',
          params: {
            foodid: this.foods[scope.$index].foodid
          },
          url: '/deletefood'
        }).then(resp => {
          if (resp && resp.data.code === 200) {
            this.foods.splice(scope.$index, 1)
            this.$message.success(resp.data.message)
          } else {
            this.$message.error(resp.data.message)
          }
        })
      })
    },

    // 编辑显示
    dialog2 (scope) {
      console.log(this.foods[scope.$index].foodid)
      this.tempidx = scope.$index
      this.dialog2form = true
      this.form.foodname = this.foods[scope.$index].foodname
      this.form.price = this.foods[scope.$index].price
      this.form.ingredient = this.foods[scope.$index].ingredient
      this.form.tag = this.foods[scope.$index].tag
      this.form.cover = this.foods[scope.$index].cover
    },

    // 更新
    updatefood () {
      console.log('!!!')
      console.log(this.tempid)
      this.$axios({
        method: 'post',
        params: {
          foodid: this.foods[this.tempidx].foodid,
          foodname: this.form.foodname,
          price: this.form.price,
          ingredient: this.form.ingredient,
          tag: this.form.tag,
          cover: this.form.cover
        },
        url: '/updatefood'
      }).then(resp => {
        if (resp && resp.data.code === 200) {
          this.dialog2form = false
          this.showfoods()
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

  .i-back{
    border: 0px;
    text-align: left;
    font-size: 22px;
    margin-left: -20px;
  }

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
