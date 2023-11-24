<template>
  <el-container>

    <el-header >
      <el-row :gutter="10" type="flex" justify="center" align="middle" style="margin-top: 10px;">
        <el-col :span="2" style="color: #ee861f;font-size: 20px">
          <span>美食搜索</span>
        </el-col>
        <el-col :span="10">
          <el-input v-model="searchfood" placeholder="按关键词搜索美食" class="search" prop="searchfood"></el-input>
        </el-col>
        <el-col :span="1">
          <el-button type="info" icon="el-icon-search" round class="my_icon" @click="search()">搜索</el-button>
        </el-col>
      </el-row>
    </el-header>

    <!--美食搜索结果-->
    <el-main>
      <div class="main">
        <el-row>
          <el-card style="width: 95%;margin-left: 20px">
            <!-- type:index会自动生成序号 -->
            <el-table :data="foods.slice((currentPage-1)*pagesize,currentPage*pagesize)"
                      @row-click="handleRowClick"
                      style="width: 100%">

              <el-table-column label="序号" type="index" :index="indexAdd" width="100"/>
              <el-table-column label="美食图片" width="180">
                <template v-slot="scope">
                  <img :src="scope.row.cover" class="my_img"/>
                </template>
              </el-table-column>
              <el-table-column prop="foodname" label="美食名称" width="180"></el-table-column>
              <el-table-column prop="price" label="价格" width="180"></el-table-column>
              <el-table-column prop="ingredient" label="食材" width="180"></el-table-column>
              <el-table-column prop="tag" label="美食标签" width="180"></el-table-column>
              <el-table-column prop="score" label="评分" width="180"></el-table-column>

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
    </el-main>

  </el-container>
</template>

<script>
export default {
  name: '',
  data () {
    return {
      searchfood: '',
      currentPage: 1,
      pagesize: 5,
      foods: []
    }
  },

  methods: {
    search () {
      // console.log(this.searchfood)
      this.axios({
        method: 'post',
        params: {
          searchfood: this.searchfood
        },
        url: '/searchfoods'
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
      return index + 1 + (page - 1) * pagesize
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
  .el-header {
    padding:0 0px;
    /*background-color: #e89b39;*/
    border-bottom: 1px solid #e89b39;
  }

  .el-main {
    background-color: white;
    padding: 0px;
  }

  .search {
    border: 2px solid #e89b39;
    border-radius: 5px;
    height: 35px;

    .el-input__inner {
      height: 35px;
    }

    .el-input__inner:hover {
      background-color: white;
    }
  }

  .my_img {
    width: 160px;
    height: 120px;
    /*margin: 0 auto;*/
  }

  .my_icon{
    background: #ecb66d;
    border-color: #e89b39;
    border:3px;
    color: #090000;
  }
  .my_icon:hover {
    background: #e89b39;
    border-color: #e89b39;
    color: #090000;
    box-shadow: 0 0 5px 1px #999
  }

</style>
