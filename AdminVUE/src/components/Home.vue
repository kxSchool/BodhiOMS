<template>
  <el-container>
    <el-header>
      <div>
        <img src="../assets/logo.png" width="32" height="32" />
        <span>电商后台管理系统</span>
      </div>
      <el-button type="info" @click="logout">退出</el-button>
    </el-header>
    <el-container>
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <div class="toggle-button" @click="toggleCollapse">|||</div>
        <el-menu
          background-color="#333744"
          text-color="#fff"
          active-text-color="#409fff"
          unique-opened
          :collapse="isCollapse"
          :collapse-transition="false"
          router
          :default-active="activePath"
        >
      
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
                    <el-submenu
            :index="' ' + item.order"
            v-for="item in menuData"
            :key="item.path"
          >
            <template slot="title" @click="clickTitle">
               <i :class="item.icon" ></i>
              <span>{{ item.name }}</span>
            </template>
            <el-menu-item
              class="menuItem"
              @click="clickMenuItem"
              v-for="(item1,index) in item.children"
              :key="item1.path"
              :index="index"
            >
              <span>{{ item1.name }}</span>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
export default {
  data() {
    return {
      menusList: [],
      iconList: {
        "125": "iconfont icon-user",
        "103": "iconfont icon-user1",
        "101": "iconfont icon-shangpinliebiao",
        "102": "iconfont icon-icon",
        "145": "iconfont icon-iconfontpaixingbang"
      },

            menuData: [
        {
          path: "personnel",
          name: "数据管理",
          icon:'el-icon-user-solid',
          order: "2",
          children: [
            {
              path: "personnel",
              name: "交易数据",
            },
          ],
        },
        {
          path: "user",
          name: "订单管理",
          icon:'el-icon-s-custom',
          order: "4",
          children: [
            {
              path: "user",
              name: "充值订单",
            },
            {
              path: "sellOut",
              name: "卖出订单",
            },
          ],
        },
      ],
      isCollapse: false,
      activePath: ""
    };
  },
  created() {
    this.getMenuList();
    this.activePath = window.localstorage.getItem("activePath", "/users");
  },
  methods: {
    //退出
    logout() {
      window.localstorage.clear();
      this.$router.push("/login");
    },
    //一级菜单
    getMenuList() {
      this.$api.menusApi()
        .then(res => {
          // console.log(JSON.stringify(res.data));
          if (res.meta.status != 200) {
            this.$message.error(res.meta.message);
          } else {
            this.menusList = res.data;
            console.log("一级:" + JSON.stringify(this.menusList));
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    // 收起 张开
    toggleCollapse() {
      this.isCollapse = !this.isCollapse;
    },
    //点击 亮度
    saveActiveClick(active) {
      window.localstorage.setItem("activePath", active);
      this.activePath = active;
    }
  }
};
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
  background-color: #dadef1;
}
.iconfont {
  margin-right: 10px;
}
.el-menu {
  border: none;
}
.toggle-button {
  height: 24px;
  color: #fff;
  font-size: 10px;
  background-color: #4a5064;
  text-align: center;
  letter-spacing: 0.2em;
  cursor: pointer;
}
</style>
