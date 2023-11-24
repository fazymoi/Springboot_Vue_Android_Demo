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

        <el-col :span="14" class="up-left">
          <el-row class="information"><span>商店名称：{{ storename }}</span></el-row>
          <el-row class="information"><span>营业时间：{{ openinghours }}</span></el-row>
          <el-row class="information"><span>商店地址：{{ storeaddr }}</span></el-row>
          <el-row class="information"><span>联系电话：{{ contact }}</span></el-row>
          <el-row class="information"><span>商店简介：{{ introduction }}</span></el-row>
        </el-col>

      </el-row>
    </div>

    <el-row>
      <span class="foods" style="color: #ee861f">商店美食</span>
    </el-row>

    <el-row>
      <div class="bottom">
        <el-row>
          <el-card style="width: 85%;margin-left: 20px">
            <!-- type:index会自动生成序号 -->
            <el-table :data="foods.slice((currentPage-1)*pagesize,currentPage*pagesize)"
                      @row-click="handleRowClick"
                      style="width: 100%">

              <el-table-column label="序号" type="index" :index="indexAdd" width="80"/>
              <el-table-column label="美食图片" width="200">
                <template v-slot="scope">
                  <img :src="scope.row.cover" class="my_img"/>
                </template>
              </el-table-column>
              <el-table-column prop="foodname" label="美食名称" width="160"></el-table-column>
              <el-table-column prop="price" label="价格" width="160"></el-table-column>
              <el-table-column prop="ingredient" label="食材" width="160"></el-table-column>
              <el-table-column prop="tag" label="美食标签" width="160"></el-table-column>
              <el-table-column prop="score" label="评分" width="160"></el-table-column>

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
    </el-row>

  </div>
</template>

<!--cover:require(`../assets/logo.png`),-->
<script>
export default {
  name: '',
  data: function () {
    return {
      cover: '',
      storename: '',
      storeaddr: '',
      openinghours: '',
      contact: '',
      introduction: '',

      currentPage: 1,
      pagesize: 5,
      foods: []
    }
  },
  created () {
    this.showstore()
    this.store_foods()
  },
  methods: {
    back () {
      this.$router.replace('/searchstore')// 后退，原数据保留
      // this.$router.go(-1);
    },

    showstore () {
      // console.log("!!!!!!")
      this.axios({
        method: 'post',
        params: {
          storeid: '1234'
        },
        url: '/showstore'
      }).then(resp => {
        this.storename = resp.data.storename
        this.storeaddr = resp.data.storeaddr
        this.openinghours = resp.data.openinghours
        this.contact = resp.data.contact
        this.introduction = resp.data.introduction
        this.cover = resp.data.cover
      })
    },

    store_foods () {
      this.axios({
        method: 'post',
        params: {
          storeid: '1234'
        },
        url: '/store_foods'
      }).then(resp => {
        if (resp && resp.status === 200) {
          this.foods = resp.data
          // console.log("!!!!!!!!!!!")
          for (let i = 0; i < Object.values(this.foods).length; i++) {
            // console.log(this.foods[i].foodname)
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
      return page === 1 ? index + 1 : (page - 1) * pagesize + index + 1
      // return index + 1 + (page - 1) * pagesize
    },

    // 点击行触发，选中或不选中复选框
    handleRowClick (row) {
      // console.log(row.foodname)
      this.axios({
        method: 'post',
        params: {
          foodid: row.foodid
        },
        url: '/setfoodid'
      }).then(resp => {
        if (resp && resp.status === 200) {
          this.$router.replace({path: '/foodpage'})
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

</style>
