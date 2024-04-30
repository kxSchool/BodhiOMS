<template>
  <div>
    <!-- 首页背景图 -->
    <div class="back-imageinfo">
      <NavBar></NavBar>
      <div style="height: 1.066667rem">
        <div class="disp-align-center" style="height: 1.066667rem">
          <van-icon
            size="20"
            name="arrow-left"
            style="position: absolute; left: 0.266666rem"
            @click="routerTo"
          />
          <div class="margin-auto font-we-bold title" style="color: #2b2c2f">
            {{ title }}
          </div>
        </div>
      </div>
      <van-pull-refresh
        v-model="isLoading"
        success-text="刷新成功"
        @refresh="onRefresh"
        style="height: 100vh; padding: 0.76rem 0.533333rem"
      >
        <div class="disp-space-bet" style="color: #fae8a6">
          <!-- 请付款 -->
          <div class="font-we-bold">
            <div style="font-size: 20px; margin-bottom: 0.28rem">交易关闭</div>
            <!-- 付款超时时间 -->
            <div style="font-size: 16px; width: 5.72rem">
              您的订单已被买家关闭，若有疑问，请及时联系买家
            </div>
          </div>
          <div class="disp-dir-cen" @click="number">
            <img
              src="../assets/home/lxdhX2.png"
              alt=""
              style="
                width: 1.226667rem;
                height: 1.186667rem;
                margin-bottom: 0.28rem;
              "
            />
            <div style="font-size: 15px">联系对方</div>
          </div>
        </div>
        <!-- 付款的详细信息 -->
        <div style="margin-top: 0.493333rem">
          <div
            class="bott-imges disp-space-bet"
            style="padding: 0.333333rem; box-sizing: border-box"
          >
            <img
              src="../assets/home/SARO@2x.png"
              style="width: 1.333333rem; height: 1.32rem"
            />
            <div class="font-we-bold" style="font-size: 15px">
              <div
                style="
                  display: flex;
                  justify-content: end;
                  color: #ffbe32;
                  font-size: 21px;
                "
              >
                ¥{{ this.datainfo.payAmount | amounswt }}
              </div>
              <div style="color: #fbe7a6; font-size: 14px">
                <span>单价:¥{{ this.datainfo.orderUnitPrice | amounswt }}</span>
                <span style="margin-left: 0.266667rem"
                  >数量:{{ this.datainfo.saroNumber | siwen }}SARO</span
                >
              </div>
            </div>
          </div>
        </div>
        <!-- 底部的详细信息 -->
        <div class="botton">
          <div
            class="border-bottom font-we-bold disp-space-bet list"
            style="color: #b06f39; font-size: 15px"
          >
            买家昵称
            <div style="color: #5d3129">
              {{ this.datainfo.nickname }}
              <span
                style="margin-left: 0.066667rem; color: #7abd37"
                id="copy_text"
                :data-clipboard-text="this.datainfo.nickname"
                @click="handleCopyFun"
                >复制</span
              >
            </div>
          </div>
          <div
            class="border-bottom font-we-bold disp-space-bet list"
            style="color: #b06f39; font-size: 15px"
          >
            订单编号
            <div style="color: #5d3129">
              {{ this.datainfo.orderNoStr }}
              <span
                style="margin-left: 0.066667rem; color: #7abd37"
                id="copy_text"
                :data-clipboard-text="this.datainfo.orderNoStr"
                @click="handleCopyFun"
                >复制</span
              >
            </div>
          </div>
          <div
            class="border-bottom font-we-bold disp-space-bet list"
            style="color: #b06f39; font-size: 15px"
          >
            下单时间
            <div style="color: #5d3129">
              {{ this.datainfo.orderTime | moment }}
            </div>
          </div>
        </div>
        <!-- 提示遮罩层 -->
        <div v-show="cellphonenumber == true" style="display: none">
          <div
            :class="[cellphonenumber ? 'popContainer' : '']"
            @touchmove.prevent
          >
            <div
              class="prompt-box-back-imgse"
              v-show="cellphonenumber == true"
              style=""
            >
              <!-- 提示框文字 用户收款银行 -->
              <div class="flex-even">
                <div
                  class="prompt-title font-we-bold text-center"
                  style="flex-direction: column"
                >
                  <div class="text-center">联系电话</div>
                  <div style="margin-top: 0.333333rem">
                    {{ replace }}
                    <a
                      :href="'tel:' + replace"
                      style="
                        color: #71c738;
                        font-size: 16px;
                        font-weight: bold;
                        margin-left: 0.133333rem;
                      "
                      >拨打</a
                    >
                  </div>
                  <div style="margin-top: 0.333333rem" v-if="this.replace1">
                    {{ this.replace1 }}
                    <a
                      :href="'tel:' + replace1"
                      style="
                        color: #71c738;
                        font-size: 16px;
                        font-weight: bold;
                        margin-left: 0.133333rem;
                      "
                      >拨打</a
                    >
                  </div>
                  <div style="margin-top: 0.333333rem" v-if="this.replace2">
                    {{ this.replace2 }}
                    <a
                      :href="'tel:' + replace2"
                      style="
                        color: #71c738;
                        font-size: 16px;
                        font-weight: bold;
                        margin-left: 0.133333rem;
                      "
                      >拨打</a
                    >
                  </div>
                </div>
                <div class="disp-ju-center wed-100">
                  <img
                    class="cancel-img"
                    src="../assets/home/removbe.png"
                    style="width: 3.52rem"
                    @click="remvedia"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </van-pull-refresh>
    </div>
  </div>
</template>

<script>
// 点击复制
import Clipboard from "clipboard";
import ConfirmThePayment from "./confirmThePayment.vue";
import Recharge from "./Recharge.vue";
import { GetMerchantPhone } from "../api/business";
export default {
  components: { Recharge, ConfirmThePayment },
  data() {
    return {
      isLoading: false,
      replace: "",
      replace1: null,
      replace2: null,
      title: "交易关闭",
      cellphonenumber: false,
      //   用户购买信息
      OrderStatus: [
        {
          texts: "已付款",
          status: "请耐心等待卖家放行，若长时间未放行可联系卖家",
        },
      ],
      // 用户购买金额和单价数量
      UserRecharge: [
        {
          total: "1000",
          UnitPrice: "400",
          quantity: "50.25",
        },
      ],
      // 用户付款信息
      datainfo: "",
    };
  },
  methods: {
    onRefresh() {
      this.obine();
      this.isLoading = false;
    },
    // 只更新商家电话
    async UpdataPhone() {
      await GetMerchantPhone(this.$route.params.id).then((res) => {
        console.log("用户获取最新商家电话");
        console.log(res);
        this.replace = res.data.data.userPhone.replace(/[^\d]/g, "");
        if (res.data.data.userPhone2) {
          this.replace1 = res.data.data.userPhone2.replace(/[^\d]/g, "");
        } else {
          this.replace1 = "";
        }
        if (res.data.data.userPhone3) {
          this.replace2 = res.data.data.userPhone3.replace(/[^\d]/g, "");
        } else {
          this.replace2 = "";
        }
      });
    },
    // 获取用户详细信息
    async obine() {
      if (!this.$route.params.id) {
        this.$route.params.id = localStorage.getItem("info");
      }
      await GetMerchantPhone(this.$route.params.id).then((res) => {
        console.log("用户获取订单详情");
        console.log(res);
        this.datainfo = res.data.data;
        this.replace = this.datainfo.userPhone.replace(/[^\d]/g, "");
        if (this.datainfo.userPhone2) {
          this.replace1 = this.datainfo.userPhone2.replace(/[^\d]/g, "");
        } else {
          this.replace1 = "";
        }
        if (this.datainfo.userPhone3) {
          this.replace2 = this.datainfo.userPhone3.replace(/[^\d]/g, "");
        } else {
          this.replace2 = "";
        }
      });
    },
    // 点击显示手机号弹出框
    number() {
      this.UpdataPhone();
      this.cellphonenumber = true;
    },
    // 隐藏手机号弹出框
    remvedia() {
      this.cellphonenumber = false;
    },
    // 操作：点击了复制按钮
    handleCopyFun() {
      let clipboard = new Clipboard("#copy_text");
      clipboard.on("success", (e) => {
        this.$message({
          message: "复制成功",
          type: "success",
          center: true,
        });
        clipboard.destroy(); // 释放内存
      });
      clipboard.on("error", (e) => {
        // 不支持复制
        alert("该浏览器不支持自动复制");
        clipboard.destroy(); // 释放内存
      });
    },
    // 返回
    routerTo() {
      this.$router.push({
        path: "/BuyesList",
        name: "BuyesList",
      });
    },
  },
  mounted() {
    this.obine();
  },
};
</script>
<style>
</style>