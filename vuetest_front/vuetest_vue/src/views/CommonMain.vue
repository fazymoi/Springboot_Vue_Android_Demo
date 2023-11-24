<template>
  <div class="main">
    <router-view/>
<!--    <div>-->
<!--      <el-row :gutter="20">-->
<!--        <el-col :span="3" @click.native="menuChange('Login')"  ><div class="grid-content bg-purple">首页</div></el-col>-->
<!--        <el-col :span="3" @click.native="menuChange('index')"><div class="grid-content bg-purple">测试</div></el-col>-->
<!--      </el-row>-->
<!--    </div>-->
<!--    <div :is="currentView"></div>-->
  </div>
</template>

<script>
// import Login from '@/components/Login'
// import AppIndex from '@/components/home/AppIndex'
// export default {
//   name:'main',
//   components:{
//     Login,
//     AppIndex
//   },
//   data(){
//     return {
//       currentView:'Login'
//     }
//   },
//   methods:{
//     menuChange(manu){
//       this.currentView=manu;
//       console.log(this.currentView);
//     }
//   }
// }
</script>

<style>
  .main{
    color:black;
    font-size: 26px;
    text-align: center;
  }
</style>
