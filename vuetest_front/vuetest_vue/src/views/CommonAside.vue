<template>
  <el-row class="tac">
    <el-col>
      <el-menu
        active-text-color="#e89b39"
        background-color="#FAF8F5FF"
        class="el-menu-vertical-demo"
        default-active="$route.path"
        router
        text-color="#090000"
        @close="handleClose"
        @open="handleOpen">

        <el-submenu index="search">
          <template slot="title">
            <i class="el-icon-search"></i>
            <span>首页搜索</span>
          </template>

          <el-menu-item-group>
            <el-menu-item index="searchstore">
              <i class="el-icon-s-shop"></i>
              <span>搜索商家</span>
            </el-menu-item>

            <el-menu-item index="searchfood">
              <i class="el-icon-food"></i>
              <span>搜索美食</span>
            </el-menu-item>
          </el-menu-item-group>

        </el-submenu>

        <el-menu-item index="personal">
          <i class="el-icon-user-solid"></i>
          <span slot="title">个人中心</span>
        </el-menu-item>

        <el-menu-item index="comment">
          <i class="el-icon-s-comment"></i>
          <span slot="title">我的评论</span>
        </el-menu-item>

        <el-menu-item index="store">
          <i class="el-icon-s-management"></i>
          <span slot="title">商家管理</span>
        </el-menu-item>

      </el-menu>
    </el-col>
  </el-row>
</template>

<style>
.iconfont{
  margin-right:10px;
}
</style>

<script>
export default {
  methods: {
    handleOpen (key, keyPath) {
      console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      console.log(key, keyPath)
    }
  }
}
</script>
