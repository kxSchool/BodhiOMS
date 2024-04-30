<template>
  <div class="concent">
    <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-left: 40px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商家管理</el-breadcrumb-item>
      <el-breadcrumb-item>商家列表</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row :gutter="40">
        <el-col :span="80">
          <el-form :model="queryInfo" :inline="true" label-width="58px">
            <el-form-item label="角色:" prop="userRole">
              <el-select v-model="queryInfo.userRole" clearable placeholder="角色" style="width: 150px; margin-right: 10px">
                <el-option label="玩家" :value="1"></el-option>
                <el-option label="商家" :value="2"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="账号：" prop="userAccount">
              <el-input v-model.trim="queryInfo.userAccount" placeholder="账号" clearable style="width: 200px; margin-right: 10px" class="filter-item" />
            </el-form-item>
            <!-- <el-date-picker
            v-model="dateRange"
            clearable
            type="datetimerange"
            value-format="yyyy-MM-dd HH:mm:ss"
            format="yyyy-MM-dd HH:mm:ss"
            :picker-options="pickerOptions"
            style="width: 360px; margin-right: 10px"
            range-separator="至"
            start-placeholder="下单开始时间"
            end-placeholder="下单结束时间"
            @change="changeTime"
            align="right"
          >
          </el-date-picker> -->
            <!-- 成交时间 -->
            <!-- <el-date-picker
            v-model="dateRanges"
            clearable
            type="datetimerange"
            value-format="yyyy-MM-dd HH:mm:ss"
            format="yyyy-MM-dd HH:mm:ss"
            :picker-options="pickerOptions"
            style="width: 360px; margin-right: 10px"
            range-separator="至"
            start-placeholder="成交开始时间"
            end-placeholder="成交结束时间"
            @change="changeTimes"
            align="right"
          >
          </el-date-picker> -->
            <el-button class="filter-item" type="primary" icon="el-icon-search" @click="searchList"> 查询 </el-button>
            <!-- <el-button class="filter-item" type="primary" icon="el-icon-search" @click="getGoodsList"> 新增商家 </el-button> -->
          </el-form>
        </el-col>
      </el-row>
      <el-table :data="goodsList" border stripe :cell-style="tsetStyleOne">
        <el-table-column type="index" label="序号" align="center" width="60"></el-table-column>
        <el-table-column label="昵称" prop="nickname" align="center" :show-overflow-tooltip="true" min-width="90"></el-table-column>
        <el-table-column label="角色" prop="userRole" align="center">
          <template slot-scope="{ row }">
            <span>{{ row.userRole | userRoleFilter }}</span>
          </template>
        </el-table-column>
        <!-- 1.用户、  2商家 -->
        <el-table-column label="游戏角色" prop="rspGameUser.user_type" align="center" :show-overflow-tooltip="true" min-width="100">
          <template slot-scope="scope">
            <div v-if="scope.row.rspGameUser != null">
              <span>{{ scope.row.rspGameUser.user_type == 1 ? '玩家' : '商家' }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="账号" prop="userAccount" align="center" :show-overflow-tooltip="true" min-width="100"></el-table-column>
        <el-table-column label="联系电话" prop="userPhone" align="center" :show-overflow-tooltip="true" min-width="100">
          <!-- <template slot-scope="scope">
            <p>{{ scope.row.userPhone }}</p>
            <p>{{ scope.row.userPhone2 }}</p>
            <p>{{ scope.row.userPhone3 }}</p>
          </template> -->
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top"
              >     
              <div class="popover-one">
                <p>{{ scope.row.userPhone }}</p>
                <p>{{ scope.row.userPhone2 }}</p>
                <p>{{ scope.row.userPhone3 }}</p>
                          
              </div>
                      
              <div slot="reference" class="name-wrapper">
                              
                <div>{{ scope.row.userPhone }}</div>
                           
              </div>
               </el-popover
            >
          </template>
        </el-table-column>
        <el-table-column label="SARO用户充值单价（CNY）" prop="rechargeUnitPrice" align="center" :show-overflow-tooltip="true" min-width="180"></el-table-column>
        <el-table-column label="SARO用户卖出单价（CNY）" prop="recoveryUnitPrice" align="center" :show-overflow-tooltip="true" min-width="180"></el-table-column>

        <el-table-column label="收款信息" align="center" min-width="120">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top"
              >     
              <div class="popover-one">
                <p v-if="scope.row.bankFavour">收款人：{{ scope.row.bankFavour }}</p>
                <p v-if="scope.row.userBankCard">银行卡号：{{ scope.row.userBankCard }}</p>
                <p v-if="scope.row.userBank">开户银行：{{ scope.row.userBank }}</p>
                <p v-if="scope.row.userBankBranch">开户支行：{{ scope.row.userBankBranch }}</p>
                       
              </div>
                      
              <div slot="reference" class="name-wrapper">
                              
                <div v-if="scope.row.bankFavour">{{ scope.row.bankFavour }} <span style="color: blue" class="el-icon-chat-dot-square"></span></div>
                           
              </div>
               </el-popover
            >
          </template>
        </el-table-column>

        <el-table-column label="当前SARO数量" prop="userSaroNumber" align="center" :show-overflow-tooltip="true" min-width="120"></el-table-column>
        <el-table-column label="创建时间" width="200px" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.createTime">{{ scope.row.createTime | formatDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="userStatus" align="center">
          <!-- userRole :1玩家  、 2商家 -->
          <!-- userStatus： 1已启用、 2已禁用 -->
          <template slot-scope="scope">
            <div v-if="scope.row.userRole == 2">
              <div
                v-if="
                  scope.row.userPhone &&
                  scope.row.bankFavour &&
                  scope.row.userAccount &&
                  scope.row.rechargeUnitPrice &&
                  scope.row.recoveryUnitPrice &&
                  scope.row.userBank &&
                  scope.row.userBankBranch &&
                  scope.row.userBankCard
                "
              >
                <span>{{ scope.row.userStatus == 1 ? '已启用' : '已禁用' }}</span>
              </div>
              <div v-else>
                <span>未设置</span>
              </div>
            </div>

            <!-- {{ JSON.stringify(scope.row.userRole) }} -->
            <!-- <span v-if="scope.row.userStatus == 1">已启用</span>
            <span v-if="scope.row.userStatus == 2">已禁用</span>
            <div v-if="scope.row.userPhone ==''&& scope.row.bankFavour==''&& scope.row.userAccount=='' ">
                <span v-if="scope.row.userStatus ==3">未设置</span>
            </div> -->
          </template>
        </el-table-column>

        <el-table-column label="操作" width="240px" align="center" class="edit-class">
          <template slot-scope="scope">
            <div class="edit-class-one">
              <!-- <el-button v-if="scope.row.userRole == 1" type="warning" icon="el-icon-d-caret" round size="mini" @click="upgrade(scope.row)">升级为商家</el-button>
              <el-button type="primary" icon="el-icon-edit" round size="mini" @click="handleAddAndEdit('edit', scope.row)">编辑</el-button>
              <el-button v-if="scope.row.userStatus == 1" type="danger" icon="el-icon-remove" round size="mini" @click="handleOpen(scope.row)">禁用</el-button>
              <el-button v-else type="success" icon="el-icon-circle-check" round size="mini" @click="handleOpen(scope.row)">启用</el-button> -->
              <el-button v-if="scope.row.userRole == 2" type="primary" icon="el-icon-edit" round size="mini" @click="handleAddAndEdit('edit', scope.row)">编辑</el-button>
              <el-button v-if="scope.row.userRole == 1" type="warning" icon="el-icon-d-caret" round size="mini" @click="upgrade(scope.row)">升级为商家</el-button>
              <div
                v-if="
                  scope.row.userRole == 2 &&
                  scope.row.bankFavour &&
                  scope.row.userAccount &&
                  scope.row.rechargeUnitPrice &&
                  scope.row.recoveryUnitPrice &&
                  scope.row.userBank &&
                  scope.row.userBankBranch &&
                  scope.row.userBankCard &&
                  scope.row.userPhone
                "
              >
                <span>
                  <el-button v-if="scope.row.userStatus == 1" type="danger" icon="el-icon-remove" round size="mini" @click="handleOpen(scope.row)">禁用</el-button>
                  <el-button v-else type="success" icon="el-icon-circle-check" round size="mini" @click="handleOpen(scope.row)">启用</el-button>
                </span>
              </div>
              <span v-if="scope.row.userStatus == 2 &&
                  scope.row.bankFavour &&
                  scope.row.userAccount &&
                  scope.row.rechargeUnitPrice &&
                  scope.row.recoveryUnitPrice &&
                  scope.row.userBank &&
                  scope.row.userBankBranch &&
                  scope.row.userBankCard &&
                 scope.row.userPhone">
                <el-button v-if="scope.row.userRole == 2" type="danger" icon="el-icon-delete" size="mini" @click="removeById(scope.row.userId)"></el-button>
              </span>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 编辑 用户or商家 -->
      <el-dialog :title="this.titleUser == 1 ? '编辑玩家' : '编辑商家'" :visible.sync="addDialogVisible" width="900px" @close="closeDialog">
        <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="300px" class="test-el-dialog">
          <el-row>
            <el-col :span="18">
              <el-form-item :label="this.titleUser == 1 ? '玩家账号' : '商家账号'" prop="userAccount">
                <el-input v-model.trim="addForm.userAccount" placeholder="请输入账号" :disabled="true"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item :label="this.titleUser == 1 ? '玩家昵称' : '商家昵称'" prop="nickname">
                <el-input v-model="addForm.nickname" placeholder="请输入昵称" :disabled="true"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label="联系电话1" prop="userPhone">
                <el-input v-model.trim="addForm.userPhone" placeholder="请输入联系电话1"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label="联系电话2" prop="userPhone2">
                <el-input v-model.trim="addForm.userPhone2" placeholder="请输入联系电话2"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label="联系电话3" prop="userPhone3">
                <el-input v-model.trim="addForm.userPhone3" placeholder="请输入联系电话3"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label="SARO充值单价" prop="rechargeUnitPrice">
                <el-input v-model.trim="addForm.rechargeUnitPrice" placeholder="请输入SARO充值单价"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label="SARO提现单价" prop="recoveryUnitPrice">
                <el-input v-model.trim="addForm.recoveryUnitPrice" placeholder="请输入SARO提现单价"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label="收款人姓名" prop="bankFavour">
                <el-input type="phone" v-model.trim="addForm.bankFavour" placeholder="请输收款人姓名"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label="银行卡号" prop="userBankCard">
                <el-input v-model.trim="addForm.userBankCard" placeholder="请输银行卡号"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label="开户银行" prop="userBank">
                <el-input v-model.trim="addForm.userBank" placeholder="请输入开户银行"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label="开户支行" prop="userBankBranch">
                <el-input v-model.trim="addForm.userBankBranch" placeholder="请输入开户支行"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label="状态" prop="userStatus">
                <el-select v-model="addForm.userStatus" placeholder="请选择状态">
                  <el-option label="已启用" :value="1"></el-option>
                  <el-option label="已禁用" :value="2"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>

        <span slot="footer" class="dialog-footer">
          <el-button @click="addDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="_addUser">确 定</el-button>
        </span>
      </el-dialog>

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
import { getUserList, editUserList, checkUserRole, upgradeUser } from '@/api/application'
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
      idTest: '',
      titleUser: '',
      userStatusOne: '',
      idw: '',
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
      UpgradeMerchant: '',
      imgFileList: [],
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      keyword: '',
      page: 1,
      size: 10,
      total: 0,
      orderType: 2,
      goodsList: [],
      dialogText: '添加玩家',
      //弹出框
      addDialogVisible: false,
      dateRange: [],
      dateRanges: [],
      queryInfo: {
        //查询的字段
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
        userRole: '',
      },
      addForm: {
        bankFavour: '',
        nickname: '',
        rechargeUnitPrice: 0,
        recoveryUnitPrice: 0,
        userAccount: '',
        userBank: '',
        userBankBranch: '',
        userBankCard: '',
        userId: '',
        userPhone: '',
        userPhone2: '',
        userPhone3: '',
        userStatus: 0,
      },
      addFormRules: {
        userAccount: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
        rechargeUnitPrice: [{ required: true, message: '请输入SARO充值单价', trigger: 'blur' }],
        recoveryUnitPrice: [{ required: true, message: '请输入SARO提现单价', trigger: 'blur' }],
        bankFavour: [{ required: true, message: '请输入收款人姓名', trigger: 'blur' }],
        userBankCard: [{ required: true, message: '请输入银行卡号', trigger: 'blur' }],
        userBank: [{ required: true, message: '请输入开户银行', trigger: 'blur' }],
        userBankBranch: [{ required: true, message: '请输入开户支行', trigger: 'blur' }],
        userStatus: [{ required: true, message: '请输入选择状态', trigger: 'blur' }],

        userPhone: [
          { required: true, message: '请输入手机号码', trigger: 'blur' },
          { length: 11, message: '手机号码长度11个位', trigger: 'blur' },
          { valiadator: clickPhone, trigger: 'blur' },
        ],
        emailAddress: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          {
            type: 'email',
            message: '请输入正确的邮箱地址',
            trigger: ['blur', 'change'],
          },
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
      if (type == 0) {
        return '已禁用'
      }
      if (type == 1) {
        return '已启用'
      }
      if (type == 2) {
        return '已删除'
      }
      if (type == 3) {
        return '未设置'
      }
    },
    userRoleFilter(type) {
      if (type == 1) {
        return '玩家'
      }
      if (type == 2) {
        return '商家'
      }
    },
  },

  methods: {
    //商家列表收款信息右对齐
    tsetStyleOne({ row, column, rowIndex, columnIndex }) {
      if (columnIndex == 8) {
        return 'display: flex;justify-content:flex-end;align-items: center;'
      }
      if (columnIndex == 12) {
        return 'display: flex;justify-content:flex-start;align-items: center;'
      }
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
        this.queryInfo.orderTimeStr = ''
        this.queryInfo.createTime = ''
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
        userRole: this.queryInfo.userRole,
      }
      const res = await getUserList(data)

      if (res) {
        // console.log(res.data.total)
        this.goodsList = res.data.records
        this.total = parseInt(res.data.total)
      }
    },
    //点击编辑或者添加调用的方法
    async _addUser() {
      if (this.type == 'add') {
        this.addUsers()
      } else if (this.type == 'edit') {
        this.updateUserr()
      }
    },
    //修改商家信息
    handleAddAndEdit(type, row) {
      this.idTest = row.userId
      console.log(row.userRole)
      if (type == 'add') {
        ;(this.dialogText = '添加玩家'), (this.disabled = false), (this.addForm = { username: '', password: '', nickName: '', emailAddress: '' })
      } else {
        this.dialogText = '编辑玩家'
        ;(this.addForm = row), (this.addForm.password = row.telephone)
      }
      this.type = type
      if (row.userRole) {
        this.addDialogVisible = true
        this.titleUser = row.userRole
      }
    },

    // 修改用户信息接口
    async updateUserr() {
      let ruleForm = {
        bankFavour: this.addForm.bankFavour,
        nickname: this.addForm.nickname,
        rechargeUnitPrice: this.addForm.rechargeUnitPrice,
        recoveryUnitPrice: this.addForm.recoveryUnitPrice,
        userAccount: this.addForm.userAccount,
        userBank: this.addForm.userBank,
        userBankBranch: this.addForm.userBankBranch,
        userBankCard: this.addForm.userBankCard,
        userId: this.addForm.userId,
        userPhone: this.addForm.userPhone,
        userPhone2: this.addForm.userPhone2,
        userPhone3: this.addForm.userPhone3,
        userStatus: this.addForm.userStatus,
      }
      console.log(ruleForm)
      this.$refs['addFormRef'].validate((valid) => {
        if (valid) {
          editUserList(ruleForm).then((res) => {
            if (res.code == 1) {
              this.getGoodsList()
              this.$message({
                message: '编辑成功',
                type: 'success',
              })
              this.addDialogVisible = false
            } else {
              this.$message.error(res.message)
            }
          })
        }
      })
    },
    //点击升级按钮确定是否能够升级
    async upgrade(row) {
      let ruleForm = {
        userId: row.userId,
      }
      const res = await checkUserRole(ruleForm)
      console.log(res)
      if (res.code == 1) {
        const conf = await this.$confirm('确定升级为商户吗', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
        if (conf != 'confirm') {
          return this.$message.info('已经取消操作')
        }
        upgradeUser(ruleForm).then((res) => {
          console.log(res)
          if (res.code == 1) {
            this.getGoodsList()
          }
          this.$message.success(res.message)
        })
      } else if (res.code == 0) {
        return this.$message.error(res.message + '不能升级为商家')
      }
      this.$message.error(res.message)
    },
    // 点击按钮商户变用户
    async removeById(userId) {
      this.idw = userId
      const conf = await this.$confirm('是否将此商户修改为玩家, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
      if (conf != 'confirm') {
        return this.$message.info('已经取消操作')
      }
      let ruleForm = {
        userId: this.idw,
        userRole: 1,
      }
      console.log(ruleForm)
      const res = await editUserList(ruleForm)
      if (res.code == 1) {
        this.getGoodsList()
        return this.$message.success(res.message)
      }

      this.$message.error(res.message)
    },
    // 1启用、2禁用
    async handleOpen(row) {
      if (row.userStatus === 1) {
        this.userStatusOne = 2
      }
      if (row.userStatus === 2) {
        this.userStatusOne = 1
      }
      let text = row.userStatus === 1 ? '禁用' : '启用'
      const conf = await this.$confirm('确定' + text + '该账户？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
      if (conf != 'confirm') {
        return this.$message.info('已经取消操作')
      }
      let ruleForm = {
        userId: row.userId,
        userStatus: this.userStatusOne,
      }
      console.log(ruleForm)
      const res = await editUserList(ruleForm)
      if (res.code == 1) {
        this.getGoodsList()
        return this.$message.success(res.message)
      }
      this.$message.error(res.message)
    },
    query() {},
    // 分页
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

.test-el-dialog /deep/ .el-input__inner {
  width: 360px;
}
/deep/ .edit-class-one {
  .el-button {
    margin-right: 10px;
  }
}

// /deep/ .el-table .el-table__cell{
//     display: flex;
//     justify-content:flex-end;
//     align-items: center;
// }
</style>
