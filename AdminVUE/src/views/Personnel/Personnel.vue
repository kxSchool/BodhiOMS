/* eslint-disable vue/require-v-for-key */
<template>
  <div class="personnel">
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-left:40px">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>数据统计</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="centen_box" v-if="goodsList">
      <div class="box">
          <p class="title_suj">今日成交量（SARO）</p>
          <p class="title_prt">{{ list.todaySaro }}</p>
      </div>
      <div class="box">
          <p class="title_suj">今日成交额（元）</p>
          <p class="title_prt">{{ list.todayCny }}</p>
      </div>

      <div class="box">
          <p class="title_suj">昨日成交量（SARO）</p>
          <p class="title_prt">{{ list.yesterdaySaro }}</p>
      </div>
      <div class="box">
          <p class="title_suj">昨日成交额（元）</p>
          <p class="title_prt">{{ list.yesterdayCny }}</p>
      </div>
        <div class="box">
          <p class="title_suj">平台总成交量（SARO）</p>
          <p class="title_prt">{{ list.totalSaro }}</p>
      </div>

      <div class="box">
          <p class="title_suj">平台总成交额（元）</p>
          <p class="title_prt">{{ list.totalCny }}</p>
      </div>
    
    </div>
  </div>
</template>

<script>
import { transReport } from '@/api/personnel'
export default {
  data() {
    return {
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        },
      },
      input: '',
      searchlist: [],
      addForm: [],
      editForm: [],
      userIndex: 0,
      allocDialogVisible: false,
      addDialogVisible: false,
      isAddMembers: false,
      currentPage4: 1,
      goodsList: '',
    }
  },
  async created() {
    this.getGoodsList()
  },
  methods: {
    async getGoodsList() {
      let data = {
        //查询
        page: this.page,
        size: this.size,
      }
      const res = await transReport(data)
      console.log()
      if (res) {
        this.list = res.data
        console.log(res)
        this.goodsList = res.data
        this.total = parseInt(res.data.total)
      }
    },
  },
}
</script>

<style scoped>

.centen_box {
   
  width:95%;
  margin: 80px 50px 0 50px;
  display: flex;
  flex-wrap: wrap;
}
.box {
  width: 20%;
  height: 200px;
  margin-left:4%;
  border: 2px solid #e6e6e6;
  margin-bottom: 80px;
}
.title_suj {
  text-align: center;
  margin-top: 30px;
  font-size: 18px;
}
.title_prt {
  text-align: center;
  margin-top: 70px;
  font-size: 30px;
  font-weight: bold;
}
</style>
