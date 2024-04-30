<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户列表</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card class="box-card">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            placeholder="请输入内容"
            class="input-with-select"
            v-model="queryInfo.query"
            clearable
            @clear="getUserList"
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="getUserList"
            ></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addDialogVisible = true"
            >添加用户</el-button
          >
        </el-col>
      </el-row>

      <el-table :data="userList" border stripe>
        <el-table-column type="index" label="序号" width="60px">
        </el-table-column>
        <el-table-column prop="username" label="姓名"> </el-table-column>
        <el-table-column prop="email" label="邮箱"> </el-table-column>
        <el-table-column prop="mobile" label="电话"> </el-table-column>
        <el-table-column prop="role_name" label="角色"> </el-table-column>
        <el-table-column label="状态">
          <template slot-scope="scope">
            <!-- {{ scope.row }} -->
            <el-switch
              v-model="scope.row.mg_state"
              @change="userStaTeChanged(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="180px">
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="mini"
              @click="updateUser(scope.row.id)"
            ></el-button>
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              @click="DelUser(scope.row.id)"
            ></el-button>

            <el-tooltip
              effect="dark"
              content="分配角色"
              placement="top"
              :enterable="false"
            >
              <el-button
                type="warning"
                icon="el-icon-setting"
                size="mini"
                @click="setRole(scope.row)"
              ></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-sizes="[2, 5, 10, 20]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>

    <!-- 添加用户 -->
    <el-dialog
      title="添加用户"
      :visible.sync="addDialogVisible"
      width="50%"
      @close="closeDialog"
    >
      <!--  -->
      <el-form
        :model="addForm"
        :rules="addFormRules"
        ref="addFormRef"
        label-width="70px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="addForm.username"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="addForm.password"
            type="password"
            autocomplete="off"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input
            type="phone"
            v-model="addForm.mobile"
            placeholder="请输入手机号码"
          ></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
      </el-form>
      <!--  -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addUser">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 修改用户 -->
    <el-dialog
      title="修改用户"
      :visible.sync="updateDialogVisible"
      width="50%"
      @close="closeUpdateDialog"
    >
      <!-- 修改内容 -->
      <el-form
        :model="updateForm"
        :rules="updateFormRules"
        ref="updateFormRef"
        label-width="70px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            :disabled="true"
            v-model="updateForm.username"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机" prop="mobile">
          <el-input
            type="phone"
            v-model="updateForm.mobile"
            placeholder="请输入手机号码"
          ></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model="updateForm.email"
            placeholder="请输入邮箱"
          ></el-input>
        </el-form-item>
      </el-form>
      <!-- end 修改内容 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="updateDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="updataData">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 分配角色 -->
    <el-dialog
      title="分配角色"
      :visible.sync="setRolesDialogVisible"
      width="50%"
      @close="setRolesClose"
    >
      <div>
        <p>当前的用户:{{ userInfo.username }}</p>
        <p>当前的角色:{{ userInfo.role_name }}</p>
        <p>
          分配新角色:
          <el-select v-model="selectedRolesId" placeholder="请选择">
            <el-option
              v-for="item in rolesList"
              :key="item.id"
              :label="item.roleName"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="setRolesDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="sevaRoleInfo">确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Api from '../../netword/Api'
export default {
  data() {
    // 手机号码 规则验证
    var clickPhone = (rule, value, cb) => {
      //验证手机号的正则表达式
      const regMobile = /^0{0,1}(13[0-9]|15[0-9])[0-9]{8}$/
      if (regMobile.text(value)) {
        return cb()
      }
      cb(new Error(请输入正确的手机号码))
    }
    return {
      // 角色列表
      rolesList: [],
      selectedRolesId:'',
      setRolesDialogVisible: false,
      userInfo: {},
      queryInfo: {
        query: '',
        pagenum: 1,
        pagesize: 2
      },
      userList: [],
      total: 0,
      addDialogVisible: false,
      updateDialogVisible: false,
      addForm: {
        username: '',
        password: '',
        mobile: '',
        email: ''
      },
      updateForm: {},
      addFormRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          {
            min: 3,
            max: 10,
            message: '用户名长度3~10个字符之间',
            trigger: 'blur'
          }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          {
            min: 5,
            max: 15,
            message: '用户名长度6~15个字符之间',
            trigger: 'blur'
          }
        ],
        mobile: [
          { required: true, message: '请输入手机号码', trigger: 'blur' },
          { length: 11, message: '手机号码长度11个位', trigger: 'blur' },
          { valiadator: clickPhone, trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          {
            type: 'email',
            message: '请输入正确的邮箱地址',
            trigger: ['blur', 'change']
          }
        ]
      },
      updateFormRules: {
        mobile: [
          { required: true, message: '请输入手机号码', trigger: 'blur' },
          { length: 11, message: '手机号码长度11个位', trigger: 'blur' },
          { valiadator: clickPhone, trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          {
            type: 'email',
            message: '请输入正确的邮箱地址',
            trigger: ['blur', 'change']
          }
        ]
      }
    }
  },
  created() {
    this.getUserList()
  },
  methods: {
    setRolesClose(){
      this.selectedRolesId =''
      this.userInfo={}
    },
    //分配角色确定
    async sevaRoleInfo(){
      if(!this.selectedRolesId){
        return this.$message.info("请选择分配新角色")
      }
      // const {data:res} = await this.$http.put(`users/${this.userInfo.id}/role`,{
      //   rid:this.selectedRolesId
      // })
      const { data: res } = await this.$http.put(`users/${this.userInfo.id}/role`,
       { rid: this.selectedRolesId })
      if (res.meta.status !== 200) {
        return this.$message.error(res.meta.message)
      }
      this.$message.success(res.meta.message)
      this.getUserList();
      this.setRolesDialogVisible = false
    },
    //角色
    async setRole(userInfo) {
      this.userInfo = userInfo
      const { data: res } = await this.$http.get('roles')
      if (res.meta.status !== 200) {
        return this.$message.error(res.meta.message)
      }
      this.rolesList = res.data
    
      this.setRolesDialogVisible = true
    },
    // getUserList(){
    //     Api.userInfoApi(this.queryInfo)
    //     .then(res =>{
    //         console.log("res:"+JSON.stringify(res));
    //     })
    //     .catch(err=>{
    //         console.log(err);
    //     })
    // }
    async getUserList() {
      const { data: res } = await this.$http.get('users', {
        params: this.queryInfo
      })
      console.log('data:' + JSON.stringify(res))
      if (res.meta.status != 200) {
        return this.$message.error(res.meta.message)
      }
      this.userList = res.data.users
      this.total = res.data.total
    },
    handleSizeChange(newSize) {
      // console.log("newsize:"+newSize);
      this.queryInfo.pagesize = newSize
      this.getUserList()
    },
    //页码
    handleCurrentChange(page) {
      // console.log("page:"+page);
      this.queryInfo.pagenum = page
      this.getUserList()
    },
    //监听 switch 开关
    async userStaTeChanged(userinfo) {
      console.log('message:' + userinfo.mg_state)
      console.log('userInfo:' + JSON.stringify(userinfo.id))
      const { data: res } = await this.$http.put(
        `users/${userinfo.id}/state/${userinfo.mg_state}`
      )
      if (res.meta.status !== 200) {
        userinfo.mg_state = !userinfo.mg_state
        return this.$message.error(res.meta.message)
      }
      this.$message.success(res.meta.msmessageg)
    },
    //关闭 dialog
    closeDialog() {
      this.$refs.addFormRef.resetFields()
    },
    addUser() {
      this.$refs.addFormRef.validate(async valid => {
        console.log('添加用户:' + valid)
        if (!valid) return
        const { data: res } = await this.$http.post('users', this.addForm)

        if (res.meta.status !== 201) {
          return this.$message.error(res.meta.message)
        }
        this.$message.success(res.meta.message)
        this.addDialogVisible = false
        this.getUserList()
      })
    },
    async updateUser(id) {
      // this.updateForm = userInfo
      // console.log('updateForm:' + JSON.stringify(this.updateForm))
      const { data: res } = await this.$http.get('users/' + id)
      if (res.meta.status !== 200) {
        return this.$message.error(res.meta.message)
      }
      this.updateForm = res.data
      this.updateDialogVisible = true
    },
    closeUpdateDialog() {
      this.$refs.updateFormRef.resetFields()
    },
    updataData() {
      this.$refs.updateFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.put(
          'users/' + this.updateForm.id,
          {
            email: this.updateForm.email,
            mobile: this.updateForm.mobile
          }
        )

        // const {data:res} = await this.$http.put(
        //   'users/'+ this.updateForm.id,
        //   {
        //   email:this.updateUser.email,
        //   mobile:this.updateUser.mobile
        //   }
        // )
        if (res.meta.status !== 200) {
          return this.$message.error(res.meta.message)
        }
        this.$message.success(res.meta.message)
        this.updateDialogVisible = false
        this.getUserList()
      })
    },
    //删除用户
    async DelUser(id) {
      console.log('delid:' + id)

      const confirmResult = await this.$confirm(
        '此操作将永久删除该用户, 是否继续?',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).catch(err => err)

      // 点击确定 返回值为：confirm
      // 点击取消 返回值为： cancel

      if (confirmResult !== 'confirm') {
        return this.$message.info('已取消删除')
      }
      const { data: res } = await this.$http.delete('users/' + id)
      if (res.meta.status !== 200) return this.$message.error(res.meta.message)
      this.$message.success(res.meta.message)
      this.getUserList()
    }
  }
}
</script>

<style lang="less" scoped></style>
