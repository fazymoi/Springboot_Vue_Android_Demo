<template>
  <div class="parent">
    <div class="up">
      <el-row>
        <el-col :span="1">
          <el-button @click="back()" icon="el-icon-arrow-left" class="i-back"></el-button>
        </el-col>
        <el-col :span="9">
          <template>
            <img :src="cover" class="store_img" />
          </template>
        </el-col>

        <el-col :span="9" class="up-left">
          <el-row class="information"><span>美食名称：{{ foodname }}</span></el-row>
          <el-row class="information"><span>美食价格：{{ price }}</span></el-row>
          <el-row class="information"><span>美食评分：{{ score }}</span></el-row>
          <el-row class="information"><span>原材料：{{ ingredient }}</span></el-row>
          <el-row class="information"><span>标签：{{ tag }}</span></el-row>
        </el-col>

        <el-col :span="5">
          <el-button @click="store()" type="danger">进店看看</el-button>
        </el-col>

      </el-row>
    </div>

    <el-row>
      <span class="foods" style="color: #ee861f">美食评论</span>
    </el-row>
    <el-row>
      <el-row>
        <el-card style="width: 85%;margin-left: 20px;margin-bottom: 10px">
          <el-form v-model="form" style="text-align: left" ref="dataForm">
            <el-row>
                <el-form-item class="formtype" label="评论内容" :label-width="'100px'"
                              prop="content">
                  <el-input  resize="none"
                             type="textarea"
                             :autosize="{ minRows: 2, maxRows: 2 }"
                             v-model="form.content" autocomplete="off" ></el-input>
                </el-form-item>
              <el-form-item class="formtype" label="评分" :label-width="'100px'" prop="score">
                <el-rate v-model="form.score" autocomplete="off" @change="rateChange" ></el-rate>
              </el-form-item>
              <el-row><el-button type="primary" @click="onSubmit()">确 定</el-button></el-row>
            </el-row>

          </el-form>
        </el-card>
      </el-row>
      <div class="bottom">
        <el-row>
          <el-card style="width: 85%;margin-left: 20px">
            <!-- type:index会自动生成序号 -->
            <el-table :data="comments.slice((currentPage-1)*pagesize,currentPage*pagesize)"
                      style="width: 100%">

              <el-table-column label="序号" type="index" :index="indexAdd" width="80"/>
              <el-table-column prop="nickname" label="用户昵称" width="180"></el-table-column>
              <el-table-column prop="submittime" label="提交时间" width="180"></el-table-column>
              <el-table-column prop="content" label="评论内容" width="300"></el-table-column>
              <el-table-column prop="score" label="评分" width="160"></el-table-column>

            </el-table>

            <span></span>
            <el-pagination
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-size="pagesize"
              :total="comments.length">
            </el-pagination>
          </el-card>
        </el-row>
      </div>
    </el-row>

  </div>
</template>

<!--cover:require(`../assets/logo.png`),-->
<script>
export default {
  name: '',
  data: function () {
    return {
      form: {
        content: '',
        score: ''
      },
      cover: '',
      foodname: '',
      price: '',
      score: '',
      ingredient: '',
      tag: '',
      foodid: 0,
      currentPage: 1,
      pagesize: 5,
      comments: []
    }
  },
  created () {
    this.showfood()
    this.foodcomment()
  },
  methods: {
    rateChange (value) {
      this.form.score = value
      console.log(this.form.score)
    },
    back () {
      this.$router.back()// 后退，原数据保留
      // this.$router.go(-1);
    },
    onSubmit () {
      console.log(this.form.content)
      this.$axios(
        {
          method: 'post',
          params: {
            content: this.form.content,
            score: this.form.score
          },
          url: '/addcomment'
        }).then(resp => {
        if (resp && resp.data.code === 200) {
          this.form.score = 0
          this.form.content = ''
          this.$message.success(resp.data.message)
          this.foodcomment()
          // this.$emit('onSubmit')
        } else {
          this.$message.error(resp.data.message)
        }
      })
    },
    showfood () {
      // console.log("!!!!!!")
      this.axios({
        method: 'post',
        params: {
          storeid: '1234'
        },
        url: '/showfood'
      }).then(resp => {
        this.foodname = resp.data.foodname
        this.price = resp.data.price
        this.ingredient = resp.data.ingredient
        this.tag = resp.data.tag
        this.score = resp.data.score
        this.cover = resp.data.cover
        this.foodid = resp.data.foodid
      })
    },

    foodcomment () {
      this.axios({
        method: 'post',
        params: {
          storeid: '1234'
        },
        url: '/foodcomment'
      }).then(resp => {
        if (resp && resp.status === 200) {
          this.comments = resp.data
          // console.log("!!!!!!!!!!!")
          for (let i = 0; i < Object.values(this.comments).length; i++) {
            console.log(this.comments[i].nickname)
            if (this.comments[i].nickname === '') { this.comments[i].nickname = '匿名' }
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
    store () {
      this.axios({
        method: 'post',
        params: {
          foodid: this.foodid
        },
        url: '/fromfoodtostore'
      }).then(resp => {
        if (resp && resp.status === 200) {
          this.$router.replace({path: '/storepage'})
        }
      })
    }
  }

}
</script>

<style scoped>

.parent{
  background: #ffffff;
  height:100vh;
  display:flex;
  flex-wrap: wrap;
}

.up{
  background: #f1cc99;
  height:42vh;
  width: 100vw;
}

.up-right{
  background: #ecb66d;
}

.i-back{
  border: 0px;
  text-align: left;
  font-size: 20px;
  margin-left: -20px;
  background: #f1cc99;
}

.up-left{
  /*background: #ffffff;*/
  text-align: left;
  height:42vh;
}

.store_img {
  margin-top: 5px;
  width: 360px;
  height: 270px;
}

.information
{
  margin-top: 20px;
  margin-left: 30px;
}

.bottom{
  background: #ffffff;
  height:58vh;
  width: 100vw;
}

.foods{
  margin-left: 20px;
  text-align: left;
  height: 60px;
  color: #6c6c6c;
  font-size: 22px;
  line-height: 60px;
}

.my_img {
  width: 160px;
  height: 120px;
  /*margin: 0 auto;*/
}

.formtype {
  width: 600px;
}
/deep/ .el-rate__icon{
  font-size: 30px;
}
</style>
