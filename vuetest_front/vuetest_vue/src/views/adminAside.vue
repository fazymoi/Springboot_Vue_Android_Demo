<template>
  <el-row class="tac">
    <el-col>
      <el-menu
        active-text-color="#ffd04b"
        background-color="#545c64"
        class="el-menu-vertical-demo"
        default-active="$route.path"
        router
        text-color="#fff">

        <el-menu-item index="check">
          <i class="el-icon-search"></i>
          <span slot="title">审核消息</span>
        </el-menu-item>

      </el-menu>
    </el-col>
  </el-row>
</template>

<style>

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
