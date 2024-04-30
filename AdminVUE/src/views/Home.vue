/* eslint-disable vue/no-unused-vars */
<template>
  <el-container>
    <el-header style="height: 85px">
      <div>
        <img class="logo" src="../assets/logo1.png" width="32" height="32" />
        <span>企鹅农场SARO交易管理平台</span>
      </div>
      <div>
        <div class="testName">{{ userName }}</div>
        <el-button class="el-icon-remove" type="primary" @click="logout"> 退出</el-button>
      </div>
    </el-header>
    <el-container>
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <el-menu background-color="#333744" text-color="#fff" active-text-color="#409fff" unique-opened :collapse="isCollapse" :collapse-transition="false" router :default-active="activePath">
          <!-- <el-submenu
            :index="item.id + ''"
            v-for="item in menusList"
            :key="item.id"
          >
            <template slot="title">
              <i :class="iconList[item.id]"></i>
              <span>{{ item.authName }}</span>
            </template>
 
            <el-menu-item
              :index="'/' + items.path"
              v-for="items in item.children"
              :key="items.id"
              @click="saveActiveClick('/' + items.path)"
            >
              <template slot="title">
                <i class="el-icon-menu"></i>
                <span>{{ items.authName }}</span>
              </template>
            </el-menu-item>
          </el-submenu> -->
          <el-submenu :index="' ' + item.order" v-for="item in menuData" :key="item.path">
            <template slot="title" @click="clickTitle">
              <i :class="item.icon"></i>
              <span>{{ item.name }}</span>
            </template>
            <el-menu-item class="menuItem" @click="clickMenuItem" v-for="(item1, index) in item.children" :key="index" :index="item1.path">
              <span>{{ item1.name }}</span>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main class="main">
        <div class="toggle-button" :class="icon" @click="toggleCollapse"></div>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
export default {
  data() {
    return {
      userName: '',
      icon: 'el-icon-s-fold',
      menusList: [],
      iconList: {
        125: 'iconfont icon-user',
        103: 'iconfont icon-user1',
        101: 'iconfont icon-shangpinliebiao',
        102: 'iconfont icon-icon',
        145: 'iconfont icon-iconfontpaixingbang',
      },

      menuData: [
        // {
        //   name: "权限管理",
        //   order: "1",
        //   path: "jurisdiction",
        //   icon:'el-icon-lock',
        //   children: [
        //     {
        //       path: "jurisdiction",
        //       name: "权限信息",
        //     },
        //   ],
        // },
        {
          path: 'personnel',
          name: '数据管理',
          icon: 'el-icon-s-data',
          order: '2',
          children: [
            {
              path: 'personnel',
              name: '交易数据',
            },
          ],
        },
        {
          path: 'user',
          name: '订单管理',
          icon: 'el-icon-s-order',
          order: '4',
          children: [
            {
              path: 'user',
              name: '充值订单',
            },
            {
              path: 'sellOut',
              name: '卖出订单',
            },
          ],
        },
        {
          path: 'application',
          name: '商家管理',
          icon: 'el-icon-setting',
          order: '3',
          children: [
            {
              path: 'application',
              name: '商家列表',
            },
          ],
        },
      ],
      isCollapse: false,
      activePath: '',
    }
  },
  created() {
    this.userName = localStorage.getItem('userName')
    // this.getMenuList()
    // this.activePath = window.sessionStorage.getItem('activePath', '/users')
  },
  methods: {
    //退出
    logout() {
      this.$confirm('退出登录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        this.$router.push({ path: '/login' })
      })
    },
    //一级菜单
    // getMenuList() {
    //   this.$api
    //     .menusApi()
    //     .then(res => {
    //       // console.log(JSON.stringify(res.data));
    //       if (res.meta.status != 200) {
    //         this.$message.error(res.meta.message)
    //       } else {
    //         this.menusList = res.data
    //         console.log('一级:' + JSON.stringify(this.menusList))
    //       }
    //     })
    //     .catch(err => {
    //       console.log(err)
    //     })
    // },
    // 收起 张开
    toggleCollapse() {
      this.isCollapse = !this.isCollapse
      if (this.icon === 'el-icon-s-fold') {
        this.icon = 'el-icon-s-unfold'
      } else {
        this.icon = 'el-icon-s-fold'
      }
    },
    //点击 亮度
    saveActiveClick(active) {
      window.localstorage.setItem('activePath', active)
      this.activePath = active
    },
    clickTitle() {
      console.log(1111)
      //   this.style.display = 'block'
    },
    clickMenuItem() {
      console.log(1111)
      //   this.style.display = 'none'
    },
  },
}
</script>
<style lang="less" scoped>
.el-container {
  height: 100%;
}
.el-header {
  background-color: #373d41;
  display: flex;
  justify-content: space-between;
  padding-left: 0px;
  align-items: center;
  color: #fff;
  font-size: 20px;
  > div {
    display: flex;
    align-items: center;
    span {
      margin-left: 15px;
    }
  }
}
.el-aside {
  background-color: #333744;
}
.el-main {
  background-color: #fff;
}
.iconfont {
  margin-right: 10px;
}
.el-menu {
  background-color: #fff;
  border: none;
}
.toggle-button {
  float: left;
  margin-top: -3px;
  height: 30px;
  width: 30px;
  font-size: 25px;
  text-align: center;
  letter-spacing: 0.2em;
  cursor: pointer;
}
.logo {
  margin-left: 20px;
}
.testName {
  text-align: center;
  color: wilte;
  margin-right: 20px;
}
</style>
