<template>
  <div>
    <!-- 首页背景图 -->
    <div class="back-imageinfo">
      <NavBar></NavBar>

      <div style="height: 1.066667rem">
        <div class="disp-align-center" style="height: 1.066667rem">
          <van-icon size="20" name="arrow-left" style="position: absolute; left: 0.266666rem" @click="routerTo" />
          <div class="margin-auto font-we-bold title" style="color: #2b2c2f">
            {{ title }}
          </div>
        </div>
      </div>
      <van-pull-refresh v-model="isLoading" success-text="刷新成功" @refresh="onRefresh"
        style="height: 100vh; padding: 0.76rem 0.533333rem">
        <div class="disp-space-bet" style="color: #fae8a6">
          <!-- 请付款 -->
          <div class="font-we-bold">
            <div style="
                font-size: 20px;
                margin-bottom: 0.28rem;
                  -webkit-text-stroke: .013333rem #5E3328;
text-stroke: .013333rem #5E3328;
              ">
              已完成
            </div>
            <!-- 付款超时时间 -->
            <div style="
                font-size: 16px;
                width: 5.72rem;
                  -webkit-text-stroke: .013333rem #5E3328;
text-stroke: .013333rem #5E3328;
              ">
              用户充值已完成
            </div>
          </div>
          <div class="disp-dir-cen" @click="number">
            <img src="../assets/home/lxdhX2.png" alt="" style="
                width: 1.226667rem;
                height: 1.186667rem;
                margin-bottom: 0.28rem;
              " />
            <div style="
                font-size: 15px;
                  font-weight: bold;
                  -webkit-text-stroke: .013333rem #5E3328;
text-stroke: .013333rem #5E3328;
              ">
              联系对方
            </div>
          </div>
        </div>
        <!-- 付款的详细信息 -->
        <div style="margin-top: 0.493333rem">
          <div class="bott-imges disp-space-bet" style="padding: 0.333333rem; box-sizing: border-box">
            <img src="../assets/home/SARO@2x.png" style="width: 1.333333rem; height: 1.32rem" />
            <div class="font-we-bold" style="font-size: 15px">
              <div style="
                  display: flex;
                  justify-content: end;
                  color: #ffbe32;
                  font-size: 21px;
                  text-shadow: 2px 1px 0px #5d3128;
                ">
                ¥{{ this.datainfo.payAmount | amounswt }}
              </div>
              <div style="color: #fbe7a6; font-size: 14px">
                <span style="
                     -webkit-text-stroke: .013333rem #5E3328;
text-stroke: .013333rem #5E3328;
                  ">单价:¥{{ this.datainfo.orderUnitPrice | amounswt }}</span>
                <span style="
                    margin-left: 0.266667rem;
                        -webkit-text-stroke: .013333rem #5E3328;
text-stroke: .013333rem #5E3328;
                  ">数量:{{ this.datainfo.saroNumber | siwen }}SARO</span>
              </div>
            </div>
          </div>
        </div>
        <!-- 底部的详细信息 -->
        <div class="botton">
          <div class="border-bottom font-we-bold disp-space-bet list" style="color: #b06f39; font-size: 15px">
            买家姓名
            <div style="color: #5d3129">
              {{ this.datainfo.realName }}
            </div>
          </div>
          <div class="border-bottom font-we-bold disp-space-bet list" style="color: #b06f39; font-size: 15px">
            买家账号
            <div style="color: #5d3129">
              {{ this.datainfo.userName }}
              <span style="margin-left: 0.066667rem; color: #7abd37" id="copy_text"
                :data-clipboard-text="this.datainfo.userName" @click="handleCopyFun">复制</span>
            </div>
          </div>
          <div class="border-bottom font-we-bold disp-space-bet list" style="color: #b06f39; font-size: 15px">
            订单编号
            <div style="color: #5d3129">
              {{ this.datainfo.orderNoStr }}
              <span style="margin-left: 0.066667rem; color: #7abd37" id="copy_text"
                :data-clipboard-text="this.datainfo.orderNoStr" @click="handleCopyFun">复制</span>
            </div>
          </div>
          <div class="border-bottom font-we-bold disp-space-bet list" style="color: #b06f39; font-size: 15px">
            下单时间
            <div style="color: #5d3129">
              {{ this.datainfo.orderTime | moment }}
              <span style="margin-left: 0.066667rem; color: #7abd37" id="copy_text"
                :data-clipboard-text="this.datainfo.orderTime | moment" @click="handleCopyFun">复制</span>
            </div>
          </div>
        </div>
        <!-- 提示遮罩层 -->
        <div v-show="cellphonenumber == true" style="display: none">
          <div :class="[cellphonenumber ? 'popContainer' : '']" @touchmove.prevent>
            <div class="prompt-box-back-imgs" v-show="cellphonenumber == true">
              <!-- 提示框文字 用户收款银行 -->
              <div class="flex-even" style="    justify-content: center;">
                <div class="prompt-title font-we-bold text-center" style="flex-direction: column"></div>
                <div class="text-center" style="color: #5D3129;font-weight: bold;font-size: .426667rem;">联系电话</div>
                <div style="margin:.533333rem 0;color: #5D3129;font-weight: bold;font-size: .426667rem;">{{ this.replace
                  }}</div>
                <div style="margin:.533333rem 0;color: #5D3129;font-weight: bold;font-size: .426667rem;"
                  v-if="this.replace1 != null">
                  {{ this.replace1 }}
                </div>
                <div style="margin:.533333rem 0;color: #5D3129;font-weight: bold;font-size: .426667rem;"
                  v-if="this.replace2 != null">
                  {{ this.replace2 }}
                </div>
                <div class="disp-ju-center wed-100">
                  <div class="confirmDetermine disp-ju-center">
                    <a :href="'tel:' + this.replace">
                      <img class="cancel-img" src="../assets/home/queren.png" alt="" />
                    </a>
                  </div>
                  <img class="cancel-img" src="../assets/home/quxiaoX2.png" alt style="margin-left: 0.306667rem"
                    @click="remvedia" />
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
  import ConfirmThePayment from "./confirmThePayment.vue";
  import Recharge from "./Recharge.vue";
  // 点击复制
  import Clipboard from "clipboard";
  import { OrderDetails } from "../api/user";
  export default {
    components: { Recharge, ConfirmThePayment },
    data() {
      return {
        isLoading: false,
        replace: "",
        replace1: null,
        replace2: null,
        title: "已完成",
        cellphonenumber: false,
        //   用户购买信息
        OrderStatus: [
          {
            texts: "已完成",
            status: "用户充值已完成",
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
      // 获取用户详细信息
      async obine() {
        if (!this.$route.params.id) {
          this.$route.params.id = localStorage.getItem("info");
        }
        await OrderDetails(this.$route.params.id).then((res) => {
          console.log("商家获取订单信息");
          console.log(res);
          this.datainfo = res.data.data;
          this.replace = this.datainfo.phone.replace(/[^\d]/g, "");
          if (this.datainfo.userPhone2 != null) {
            this.replace1 = this.datainfo.userPhone2.replace(/[^\d]/g, "");
          }
          if (this.datainfo.userPhone3 != null) {
            this.replace2 = this.datainfo.userPhone3.replace(/[^\d]/g, "");
          }
          // console.log(this.datainfo);
        });
      },
      // 点击显示手机号弹出框
      number() {
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
          path: "/MerH5",
          name: "MerH5",
          params: {
            index: 3,
          },
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