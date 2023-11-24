<template>
  <div class="parent">
    <div class="left">
      <img src="../assets/logo.png" alt="">
      <span>美食推荐系统</span>
    </div>

    <div class="right">
      <el-button type="info" @click="logout" class="my_icon" style="margin-left:1050px">退出</el-button>
    </div>

<!--    &lt;!&ndash; 退出按钮 &ndash;&gt;-->
<!--    <el-button type="info" @click="logout" class="right">退出</el-button>-->
  </div>
</template>

<script>
export default {
  name: '',
  methods: {
    // 退出操作
    logout () {
      // 清空token
      window.sessionStorage.clear()
      //  将登录页面转入到登录界面
      this.$router.push('/login')
    }
  }
}
</script>

<style>

.parent{
  display:flex;
  flex-direction:row;
  /*justify-content:space-between;*/
  height:60px;
  background: linear-gradient(to right,#e89b39 5%,white);

  .right {
    position: relative;
    top:10px;
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
  }
  .left {
    display: flex;
    align-items: center;//纵向居中
    /*flex-direction: row;*/
    width: 300px;

    > img {
      width: 50px;
      height: 50px;
      position: relative;
      vertical-align: middle;
      top: 5px;
      left: 8px;
    }

    > span {
      margin-left: 20px;
      height: 60px;
      color: white;
      font-size: 22px;
      line-height: 60px;
    / / 控制元素垂直距离 text-align: center;
    }
  }
}
</style>
