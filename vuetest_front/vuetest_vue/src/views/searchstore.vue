<template>
  <el-container>
    <el-header >
      <el-row :gutter="10" type="flex" justify="center" align="middle" style="margin-top: 10px;">
        <el-col :span="2" offset="0" style="color: #ee861f;font-size: 20px">
          <span>商家搜索</span>
        </el-col>
        <el-col :span="10">
          <el-input v-model="searchstore" placeholder="按关键词搜索商家" class="search" prop="searchstore"></el-input>
        </el-col>
        <el-col :span="1">
          <el-button type="info" icon="el-icon-search" round class="my_icon" @click="search()">搜索</el-button>
        </el-col>
      </el-row>
    </el-header>

    <!--商家搜索结果-->
    <el-main>
      <div class="main">
        <el-row>
          <el-card style="width: 95%;margin-left: 20px">
            <!-- type:index会自动生成序号 -->
            <el-table :data="stores.slice((currentPage-1)*pagesize,currentPage*pagesize)"
                      @row-click="handleRowClick"
                      style="width: 100%">

              <el-table-column label="序号" type="index" :index="indexAdd" width="100"/>
              <el-table-column label="商家图片" width="200">
                <template v-slot="scope">
                  <img :src="scope.row.cover" class="my_img"/>
                </template>
              </el-table-column>

              <el-table-column prop="storename" label="店名" width="200">
              </el-table-column>
              <el-table-column prop="storeaddr" label="地址" width="200">
              </el-table-column>
              <el-table-column prop="contact" label="联系电话" width="200">
              </el-table-column>

            </el-table>

            <span></span>
            <el-pagination
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-size="pagesize"
              :total="stores.length">
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
      searchstore: '',
      currentPage: 1,
      pagesize: 5,
      stores: []
    }
  },

  methods: {
    search () {
      console.log(this.searchstore)
      this.axios({
        method: 'post',
        params: {
          searchstore: this.searchstore
        },
        url: '/searchstores'
      }).then(resp => {
        if (resp && resp.status === 200) {
          this.stores = resp.data
          // console.log("!!!!!!!!!!!")
          for (let i = 0; i < Object.values(this.stores).length; i++) {
            // console.log(this.stores[i].storename)
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
      // console.log(row.storename)
      this.axios({
        method: 'post',
        params: {
          storeid: row.storeid
        },
        url: '/setstoreid'
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
