<template>
  <el-container class="home-container">
    <el-header >
      <common-header />
    </el-header>

    <el-container>
      <el-aside width="13%">
        <admin-aside />
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
import adminAside from './adminAside'
import CommonHeader from './CommonHeader'

export default {
  name: 'index',
  components: {
    adminAside,
    CommonHeader
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
  background-color: #e89b39;
  border-bottom: 2px solid #e89b39;
}

.el-aside {
  background-color: #545c64;
  /*border: 2px solid black;*/

}

.el-main {
  background-color: white;
}
</style>
