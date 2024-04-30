<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      <el-breadcrumb-item>角色列表</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row>
        <el-button type="primary">添加角色</el-button>
      </el-row>
      <el-table border stripe :data="rolesList">
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-row
              v-for="(item1, index1) in scope.row.children"
              :key="item1.id"
              :class="['bdbutton', index1 === 0 ? 'bdtop' : '', 'vcenter']"
            >
              <el-col :span="5">
                <el-tag type="primary" closable>{{ item1.authName }}</el-tag>
                <i class="el-icon-caret-right"></i>
              </el-col>
              <el-col :span="19">
                <el-row
                  v-for="(item2, index2) in item1.children"
                  :key="item2.key"
                  :class="[index2 === 0 ? '' : 'bdtop', 'vcenter']"
                >
                  <el-col :span="6">
                    <el-tag type="sucess" closable>{{ item2.authName }}</el-tag>
                    <i class="el-icon-caret-right"></i>
                  </el-col>
                  <el-col :span="18">
                    <el-tag
                      type="warning"
                      closable
                      v-for="item3 in item2.children"
                      :key="item3.id"
                      @close="removeRolesId(scope.row, item3.id)"
                    >
                      {{ item3.authName }}
                    </el-tag>
                  </el-col>
                </el-row>
              </el-col>
            </el-row>
          </template>
        </el-table-column>
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column label="角色名称" prop="roleName"></el-table-column>
        <el-table-column label="角色描述" prop="roleDesc"></el-table-column>
        <el-table-column label="操作" width="300px">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini"
              >编辑</el-button
            >
            <el-button type="danger" icon="el-icon-delete" size="mini"
              >删除</el-button
            >
            <el-button
              type="warning"
              icon="el-icon-setting"
              size="mini"
              @click="showSetRolesDialog(scope.row)"
              >分配权限</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 分配权限 -->
    <el-dialog
      title="分配权限"
      :visible.sync="setRightDialogVisible"
      width="50%"
      @close="setRightsDialogClose"
    >
      <!-- 树形 status -->
      <el-tree
        :data="reghtsList"
        :props="treeProps"
        show-checkbox
        width="50%"
        node-key="id"
        default-expand-all
        :default-checked-keys="defkeys"
        ref="treeRef"
      ></el-tree>

      <!-- end status -->

      <span slot="footer" class="dialog-footer">
        <el-button @click="setRightDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="allotRights">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      rolesList: [],
      setRightDialogVisible: false,
      reghtsList: [],
      treeProps: {
        children: 'children',
        label: 'authName'
      },
      defkeys:[],
      //角色id
      roleId:''

    }
  },
  methods: {
    async getRolesList() {
      const { data: res } = await this.$http.get('roles')
      if (res.meta.status !== 200) {
        return this.$message.error(res.meta.message)
      }
      this.rolesList = res.data
      console.log('rolesList', JSON.stringify(this.rolesList))
    },
    async removeRolesId(scope, rightId) {

      const confirmResult = await this.$confirm(
        '此操作将永久删除该文件, 是否继续?',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).catch(err => err)
      if (confirmResult !== 'confirm') {
        return this.$message.info('取消删除')
      }
      // this.$message.success("删除成功");
      const { data: res } = await this.$http.delete(
        `roles/${scope.id}/rights/${rightId}`
      )
      if (res.meta.status !== 200) {
        return this.$message.error(res.meta.message)
      }
      this.$message.success(res.meta.message)

      this.getRolesList()
    },
    //分配权限
    async showSetRolesDialog(role) {
      this.roleId = role.id;
      const { data: res } = await this.$http.get(`rights/tree`)
      if (res.meta.status != 200) {
        return this.$message.error(res.meta.message)
      }
      // console.log("分配权限",JSON.stringify(res.data));
      this.reghtsList = res.data
      //获取三级节点
      this.getLeafKeys(role,this.defkeys)
      this.setRightDialogVisible = true
    },
    //获取三级
     getLeafKeys(node,arr){
       if(!node.children){
         return arr.push(node.id)
       }
       node.children.forEach(item =>
          this.getLeafKeys(item,arr)
       );
     },
     setRightsDialogClose(){
       this.defkeys=[]
     },
     //点击角色分配权限
      async allotRights(){
        console.log(this.$refs.treeRef.getCheckedKeys());
        console.log(this.$refs.treeRef.getHalfCheckedKeys());
        const keys = [
          ...this.$refs.treeRef.getCheckedKeys(),
          ...this.$refs.treeRef.getHalfCheckedKeys()
        ];
        const idStr = keys.join(",");
        const {data:res} = await this.$http.post(
          `roles/${this.roleId}/rights`,
          {rids:idStr}
        );
        if(res.meta.status != 200){
          return this.$message.error(res.meta.message)
        }
        this.$message.success(res.meta.message)
        this.getRolesList();
        this.setRightDialogVisible = false


     }
  },
  created() {
    this.getRolesList()
  }
}
</script>
<style lang="less" scoped>
.el-tag {
  margin: 7px;
}
.bdtop {
  border-top: 1px solid #eee;
}
.bdbutton {
  border-bottom: 1px solid #eee;
}
.vcenter {
  display: flex;
  align-items: center;
}
</style>
