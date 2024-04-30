<template>
  <div class="concent">
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-left:40px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>订单管理</el-breadcrumb-item>
      <el-breadcrumb-item>卖出订单</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row :gutter="40">
        <el-col :span="80">
          <el-form :model="queryInfo" :rules="addFormRules" :inline="true" label-width="88px">
            <el-form-item label="订单编号：" prop="orderNoStr">
              <el-input
                v-model.trim="queryInfo.orderNoStr"
                placeholder="订单编号"
                clearable
                style="width: 200px; margin-right: 10px"
                class="filter-item"
                onkeypress="javascript:if(event.keyCode == 32)event.returnValue = false;"
              />
            </el-form-item>
            <el-form-item label="卖家昵称：" prop="nickname">
              <el-input
                v-model.trim="queryInfo.nickname"
                placeholder="卖家昵称"
                clearable
                style="width: 200px; margin-right: 10px"
                class="filter-item"
                onkeypress="javascript:if(event.keyCode == 32)event.returnValue = false;"
              />
            </el-form-item>
            <el-form-item label="卖家账号：" prop="userAccount">
              <el-input
                v-model.trim="queryInfo.userAccount"
                placeholder="卖家账号"
                clearable
                style="width: 200px; margin-right: 10px"
                class="filter-item"
                onkeypress="javascript:if(event.keyCode == 32)event.returnValue = false;"
              />
            </el-form-item>
            <el-form-item label="商家账号：" prop="merchantAccount">
              <el-input
                v-model.trim="queryInfo.merchantAccount"
                placeholder="商家账号"
                clearable
                style="width: 200px; margin-right: 10px"
                class="filter-item"
                onkeypress="javascript:if(event.keyCode == 32)event.returnValue = false;"
              />
            </el-form-item>
            <el-form-item label="状态：" prop="orderStatus">
              <el-select v-model.trim="queryInfo.orderStatus" clearable placeholder="全部" style="width: 150px; margin-right: 10px">
                <el-option label="全部" :value="''"></el-option>
                <el-option label="待付款" :value="1"></el-option>
                <el-option label="已关闭" :value="5"></el-option>
                <el-option label="已完成" :value="3"></el-option>
                <el-option label="已取消" :value="4"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="下单时间：">
              <el-date-picker
                v-model="dateRange"
                clearable
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                format="yyyy-MM-dd HH:mm:ss"
                :picker-options="pickerOptions"
                style="width: 360px; margin-right: 10px"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @change="changeTime"
                align="right"
              >
              </el-date-picker>
            </el-form-item>
            <!-- 成交时间 -->
            <el-form-item label="成交时间：">
              <el-date-picker
                v-model="dateRanges"
                clearable
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                format="yyyy-MM-dd HH:mm:ss"
                :picker-options="pickerOptions"
                style="width: 360px; margin-right: 10px"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @change="changeTimes"
                align="right"
              >
              </el-date-picker>
            </el-form-item>
              <el-button class="filter-item" type="primary" icon="el-icon-search" @click="searchList"> 查询 </el-button>
          </el-form>
        
        </el-col>
      </el-row>
      <el-table :data="goodsList" border stripe :cell-style="tsetStyleOne">
        <el-table-column type="index" label="序号" align="center"  width="60"></el-table-column>
        <el-table-column label="订单编号" prop="orderNoStr" align="center" :show-overflow-tooltip="true" min-width="100"></el-table-column>
        <el-table-column label="订单金额（元）" prop="payAmount" align="center" :show-overflow-tooltip="true" min-width="120"></el-table-column>
        <el-table-column label="单价（元）" prop="orderUnitPrice" align="center" :show-overflow-tooltip="true" min-width="100"></el-table-column>
        <el-table-column label="数量（SARO）" prop="saroNumber" align="center" :show-overflow-tooltip="true" min-width="140"></el-table-column>
        <el-table-column label="卖家昵称" prop="nickname" align="center" :show-overflow-tooltip="true" min-width="100"></el-table-column>
        <el-table-column label="卖家账号" prop="userAccount" align="center" :show-overflow-tooltip="true" min-width="100"></el-table-column>
        <el-table-column label="卖家联系电话" prop="phone" align="center" :show-overflow-tooltip="true" min-width="120"></el-table-column>     
        <el-table-column label="卖家收款方式" align="center" min-width="140">
          <template slot-scope="scope" >
            <el-popover trigger="hover" placement="top">     
              <div class="popover-one">
                <p v-if="scope.row.bankFavour">收款人：{{scope.row.bankFavour}}</p>
                <p v-if="scope.row.userBankCard">银行卡号：{{scope.row.userBankCard}}</p>
                <p v-if="scope.row.userBank">开户银行：{{scope.row.userBank}}</p>   
                <p v-if="scope.row.userBankBranch">开户支行：{{scope.row.userBankBranch}}</p>                          
              </div>                   
              <div slot="reference" class="name-wrapper">                
              <div v-if="scope.row.bankFavour">{{scope.row.bankFavour}} <span style="color:blue" class="el-icon-chat-dot-square"></span></div>                          
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="商家昵称" prop="merchantNickname" align="center" :show-overflow-tooltip="true" min-width="100"></el-table-column>
        <el-table-column label="商家账号" prop="merchantAccount" align="center" :show-overflow-tooltip="true" min-width="100"></el-table-column>
                <el-table-column label="商家联系电话" align="center" :show-overflow-tooltip="true" min-width="120">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">     
              <div class="popover-one">
                <p>{{ scope.row.merchantPhone }}</p>
                <p>{{ scope.row.merchantPhone2 }}</p>
                <p>{{ scope.row.merchantPhone3 }}</p>                     
              </div>   
              <div slot="reference" class="name-wrapper">                           
                <div v-if="scope.row.merchantPhone">{{scope.row.merchantPhone}} <span style="color:blue" class="el-icon-chat-dot-square"></span></div>
                           
              </div>
               </el-popover
            >
          </template>
        </el-table-column>
        <el-table-column label="下单时间" width="200px" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.orderTimeStr | formatDate }}</span>
          </template>
        </el-table-column>

        <el-table-column label="成交时间" width="200px" align="center" :show-overflow-tooltip="true" min-width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.confirmTimeStr">{{ scope.row.confirmTimeStr | formatDate }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column label="付款凭证" prop="transactorProof" align="center"></el-table-column> -->

    <!--    <el-table-column label="付款凭证" prop="transactorProof" align="center" :show-overflow-tooltip="true">
          <template slot-scope="scope">
           <div class="demo-image__preview">
              <el-image 
                style="width: 75px; height: 50px"
                :src="imgFileList + scope.row.transactorProof" 
                :preview-src-list="[scope.row.transactorProof]">
              </el-image>
            </div> 
          </template>
        </el-table-column>
        -->

        <el-table-column label="状态" prop="orderStatus" align="center">
          <template slot-scope="{ row }">
            <span>{{ row.orderStatus | genderFilter }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="220px" align="center">
          <template slot-scope="scope" v-if="!(scope.row.orderStatus !== 1)">
            <div class="edit-class-one">
              <el-button type="danger"  round icon="el-icon-remove"  size="mini" @click="handleOpenOrNo(scope.row,1)">关闭订单</el-button>
              <el-button type="success" round icon="el-icon-success" size="mini" @click="handleOpenOrNo(scope.row,2)">确认付款</el-button>
              </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 添加用户 -->
      <!-- <el-dialog :title="dialogText" :visible.sync="addDialogVisible" width="50%" @close="closeDialog">
        <span slot="footer" class="dialog-footer">
          <el-button @click="addDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="_addUser">确 定</el-button>
        </span>
      </el-dialog> -->
      <el-pagination
       background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="this.page"
        :page-sizes="[5, 10, 20, 40]"
        :page-size="this.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
import { getOrderList, editOrder } from '@/api/sellOut'
import { formatDate } from '@/components/js/data'
export default {
  data() {
    var clickPhone = (rule, value, cb) => {
      //验证手机号的正则表达式
      const regMobile = /^0{0,1}(13[0-9]|15[0-9])[0-9]{8}$/
      if (regMobile.text(value)) {
        return cb()
      }
      cb(new Error(请输入正确的手机号码))
    }
    return {
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
              picker.$emit('pick', [start, end])
            },
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
              picker.$emit('pick', [start, end])
            },
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
              picker.$emit('pick', [start, end])
            },
          },
        ],
      },
      uploadParams: {
        file: null,
      },
      imgFileList: '',
      dialogImageUrl: '',
      dialogVisible: false,
      keyword: '',
      page: 1,
      size: 10,
      total: 0,
      orderType: 2,
      goodsList: [],
      dialogText: '添加用户',
      addDialogVisible: false,
      dateRange: [],
      dateRanges: [],
      queryInfo: {
        nickname: '',
        orderNoStr: '',
        orderType: 2,
        endOrderTime: '',
        startOrderTime: '',
        userAccount: '',
        merchantAccount: '',
        orderStatus: '',
        startConfirmTime: '',
        endConfirmTime: '',
        merchantNickname: '',
      },
      addForm: {
        bankFavour: '',
        phone: '',
        province: '',
        city: '',
        district: '',
        detail: '',
        uid: '',
        id: '',
        postCode: '',
      },
    addFormRules: {
        orderNoStr: [
          { pattern:/^[0-9]*$/, message: '订单编号请输入数字！' }
        ],
         nickname: [
          { pattern:/^[\u4E00-\u9FA5A-Za-z0-9_]+$/, message: '请输入正确格式昵称！' }
        ],
          userAccount: [
          { pattern:/^[0-9]*$/, message: '卖家账号请输入数字!' }
        ],
           merchantAccount: [
          { pattern:/^[0-9]*$/, message: '商家账号请输入数字!' }
        ],
      },
      allRecharge: 0,
      dayAllPay: 0,
    }
  },
  async created() {
    this.getGoodsList()
  },
  filters: {
    formatDate(time) {
      var data = new Date(time)
      return formatDate(data, 'yyyy-MM-dd hh:mm:ss')
    },
    genderFilter(type) {
      if (type == 1) {
        return '待付款'
      }
      if (type == 3) {
        return '已完成'
      }
      if (type == 4) {
        return '已取消'
      }
      if (type == 5) {
        return '已关闭'
      }
    },
  },
  methods: {
      tsetStyleOne({ row, column, rowIndex, columnIndex }) {
	           if ( columnIndex == 8) {
		        return "display: flex;justify-content:flex-end;align-items: center; padding-right:10px;";
		      }
	   	 },
    //关闭或者确认订单
    async handleOpenOrNo(row,num) {
      console.log(row.orderId)
      let test = num===1?'确定关闭订单?':'确定已付款成功?'
      const conf = await this.$confirm(test, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
      if (conf != 'confirm') {
        return this.$message.info('已经取消对商品的操作')
      }
      let ruleForm = {
        orderId: row.orderId,
        orderType: 2,
        operateType: num,
      }
      console.log(ruleForm)
      const res = await editOrder(ruleForm)
      if (res.code == 1) {
        this.getGoodsList()
        return this.$message.success(res.message)
      }
      this.$message.error(res.message)
    },

    searchList() {
      this.page = 1
      this.getGoodsList()
    },
    //   下单时间
    changeTime(a) {
      if (a) {
        this.queryInfo.startOrderTime = a[0]
        this.queryInfo.endOrderTime = a[1]
      } else {
        this.queryInfo.startOrderTime = ''
        this.queryInfo.endOrderTime = ''
      }
    },
    // 成交时间
    changeTimes(a) {
      if (a) {
        this.queryInfo.startConfirmTime = a[0]
        this.queryInfo.endConfirmTime = a[1]
      } else {
        this.queryInfo.startConfirmTime = ''
        this.queryInfo.endConfirmTime = ''
      }
    },
    // 账单列表
    async getGoodsList() {
      let data = {
        //查询
        page: this.page,
        size: this.size,
        startOrderTime: this.queryInfo.startOrderTime,
        endOrderTime: this.queryInfo.endOrderTime,
        orderNoStr: this.queryInfo.orderNoStr,
        nickname: this.queryInfo.nickname,
        orderType: this.queryInfo.orderType,
        userAccount: this.queryInfo.userAccount,
        merchantAccount: this.queryInfo.merchantAccount,
        orderStatus: this.queryInfo.orderStatus,
        startConfirmTime: this.queryInfo.startConfirmTime,
        endConfirmTime: this.queryInfo.endConfirmTime,
        merchantNickname: this.queryInfo.merchantNickname,
      }
      const res = await getOrderList(data)
      if (res) {
        console.log(res.data.total)
        this.goodsList = res.data.records
        this.total = parseInt(res.data.total)
      }
    },

    query() {},
    handleSizeChange(size) {
      //   this.page = 1
      this.size = size
      this.getGoodsList()
    },
    handleCurrentChange(current) {
      console.log(current)
      this.page = current
      this.getGoodsList()
      //   console.log(this.page);
    },
    // goAddPage() {
    //   this.addDialogVisible = true
    // },
    closeDialog() {
      this.$refs.addFormRef.resetFields()
    },
  },
}
</script>
<style lang="less" scoped>
.popover-one {
  font-size: 18px;
}
</style>
