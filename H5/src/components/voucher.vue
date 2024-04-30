<template>
  <div>
    <NavBar></NavBar>

    <recharge title="请付款"></recharge>
    <!-- 提示遮罩层 -->
    <div v-show="cellphonenumber">
      <div
        :class="[cellphonenumber ? 'popContainer' : '']"
        @touchmove.prevent
        style="overflow: auto"
      >
        <div class="vouimaes" v-show="cellphonenumber">
          <img class="backvou" :src="baseUrl" alt="" />
          <img
            @click="getOrderProof"
            class="guanbi"
            src="../assets/home/guanbi.png"
            alt=""
          />
        </div>
        <div style="height: 2.666667rem"></div>
      </div>
    </div>
  </div>
</template>

<script>
// 点击复制
import Clipboard from "clipboard";
import Recharge from "./Recharge.vue";
import { GetVoucher } from "../api/user";
export default {
  components: { Recharge },
  data() {
    return {
      baseUrl: "",
      cellphonenumber: true,
    };
  },
  methods: {
    // 获取订单交易凭证
    async voucher() {
      if (!this.$route.params.id) {
        this.$route.params.id = localStorage.getItem("info");
      }
      await GetVoucher(this.$route.params.id).then((res) => {
        console.log("获取订单交易凭证");
        console.log(res);
        if (res.data.message == "获取成功") {
          this.baseUrl += res.data.data;
          // console.log(this.baseUrl);
        } else if (res.data.message == "订单未上传凭证") {
          this.$message({
            message: "订单未上传凭证",
            type: "warning",
            center: true,
          });
        }
      });
    },
    // 返回上一页
    getOrderProof() {
      this.$router.push({
        path: "/UserRech",
        name: "UserRech",
        params: {
          id: this.$route.params.id,
        },
      });
    },
  },
  mounted() {
    this.voucher();
  },
};
</script>
<style>
.backimes {
  background-color: #7f7f7f;
  opacity: 0.5;
}

.vouimaes {
  width: fit-content;
  width: -webkit-min-content;
  width: -moz-min-content;
  width: min-content;
  max-width: 7.226667rem;
  max-height: 12.253333rem;
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.backvou {
  border-radius: 0.266667rem;
  max-width: 7.226667rem;
  max-height: 12.253333rem;
}

.guanbi {
  position: absolute;
  bottom: -12%;
  right: 50%;
  transform: translate(50%, 0);
  width: 1.04rem;
  height: 1.04rem;
}
</style>