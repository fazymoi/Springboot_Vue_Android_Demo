<template>
  <el-container class="home-container">
    <el-header >
      <common-header />
    </el-header>

    <el-container>
      <el-aside width="13%">
        <common-aside />
      </el-aside>
      <el-main>
        <div class="main">
          <router-view/>
        </div>
      </el-main>
    </el-container>

  </el-container>
</template>

<script>
import CommonAside from './CommonAside'
import CommonHeader from './CommonHeader'
import CommonMain from './CommonMain'
export default {
  name: 'index',
  components: {
    CommonAside,
    CommonHeader,
    CommonMain
  }
}
</script>

<style>
  .home-container {
    height: 100vh;
    width: 100vw;
  }

  .el-header {
    padding:0 0px;
    /*background-color: #e89b39;*/
    border-bottom: 2px solid #e89b39;
  }

  .el-aside {
    background-color: #FAF8F5FF;
    border-right: 1px solid black;
    /*border: 2px solid black;*/

  }

  .el-main {
    background-color: white;
    padding: 0px;
  }
</style>
