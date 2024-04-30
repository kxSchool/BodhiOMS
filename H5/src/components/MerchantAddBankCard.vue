<template>
  <!-- 头部组件 -->
  <div>
    <!-- 头部标题组件 -->
    <NavBar></NavBar>
    <Rearge title="添加收款银行卡"></Rearge>
    <!-- 背景图片 -->
    <div class="Numberimg"></div>
    <van-pull-refresh
      v-model="isLoading"
      success-text="刷新成功"
      @refresh="onRefresh"
      style="height:100vh"
    >
      <!-- 用户姓名 -->
      <form class="from-paddin" style="padding: 0.733333rem !important">
        <div class="userColor">姓名</div>
        <input
          disabled
          type="text"
          placeholder="请填写姓名"
          class="user-inputs"
          v-model="this.data.bankFavour"
        />
        <div class="userColor">银行卡号</div>
        <input
          disabled
          type="number"
          placeholder="请输入银行卡号"
          class="user-inputs"
          id="number"
          v-model="this.data.userBankCard"
        />
        <div class="userColor">开户银行</div>
        <input
          disabled
          type="text"
          placeholder="请填写开户银行"
          class="user-inputs"
          id="branch"
          v-model="this.data.userBank"
        />
        <div class="userColor">开户支行</div>
        <input
          disabled
          type="text"
          placeholder="请填写开户支行"
          class="user-inputs"
          id="bank"
          v-model="this.data.userBankBranch"
        />
        <div class="BankCard font-we-bold">收款银行卡仅支持联系客服修改</div>
      </form>
    </van-pull-refresh>
  </div>
</template>

<script>
import { mapMutations } from "vuex";
import { ListTeLephone } from "../api/business";
// 点击复制
export default {
  data() {
    return {
      isLoading: false,
      name: "",
      data: "",
      // 判断用户是否填写收款信息
    };
  },
  methods: {
    // 获取商家自身信息
    async obtain() {
      await ListTeLephone().then((res) => {
        console.log("商家获取收款信息");
        console.log(res);
        if (res.data.code == 1) {
          this.data = res.data.data;
          return;
        }
        this.$message({
          message: "获取收款信息失败",
          type: "error",
          center: true,
        });
      });
    },
    onRefresh() {
      this.$router.go(0);
      this.isLoading = false;
    },
    ...mapMutations["increment"],
    hrefGo() {
      this.$router.push({
        path: "/Home",
      });
      this.$store.commit("increment", 1);
    },
  },
  mounted() {
    this.obtain();
  },
};
</script>
<style>
.BankCard {
  color: #d14330;
  font-size: 0.426667rem;
}
</style>