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
            <div style="font-size: 20px; margin-bottom: 0.28rem;                  -webkit-text-stroke: .013333rem #5E3328;
text-stroke: .013333rem #5E3328;
">待放行</div>
            <!-- 付款超时时间 -->
            <div style="font-size: 16px; width: 5.72rem;                -webkit-text-stroke: .013333rem #5E3328;
text-stroke: .013333rem #5E3328;
">
              请在确认收款后，放行SARO给对方
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
            <div style="font-size: 15px;     font-weight: bold;              -webkit-text-stroke: .013333rem #5E3328;
text-stroke: .013333rem #5E3328;
">联系对方</div>
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
                                    text-shadow: 2px 1px 0px #5d3128;

                "
              >
                ¥{{ this.datainfo.payAmount | amounswt }}
              </div>
              <div style="color: #fbe7a6; font-size: 14px">
                <span style="                -webkit-text-stroke: .013333rem #5E3328;
text-stroke: .013333rem #5E3328;
">单价:¥{{ this.datainfo.orderUnitPrice | amounswt }}</span>
                <span style="margin-left: 0.266667rem;                -webkit-text-stroke: .013333rem #5E3328;
text-stroke: .013333rem #5E3328;
"
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
            买家姓名
            <div style="color: #5d3129">
              {{ this.datainfo.realName }}
            </div>
          </div>
          <div
            class="border-bottom font-we-bold disp-space-bet list"
            style="color: #b06f39; font-size: 15px"
          >
            买家账号
            <div style="color: #5d3129">
              {{ this.datainfo.userName }}
              <span
                style="margin-left: 0.066667rem; color: #7abd37"
                id="copy_text"
                :data-clipboard-text="this.datainfo.userName"
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
              <span
                style="margin-left: 0.066667rem; color: #7abd37"
                id="copy_text"
                :data-clipboard-text="this.datainfo.orderTime | moment"
                @click="handleCopyFun"
                >复制</span
              >
            </div>
          </div>
        </div>
        <div class="disp-space-bet">
          <img
            @click="Closeorder"
            class="User-images"
            src="../assets/home/guanbi@2x.png"
            alt=""
          />
          <img
            @click="voucher"
            class="User-images"
            src="../assets/home/chakan@2x.png"
            alt=""
          />
          <img
            @click="Release"
            class="User-images"
            src="../assets/home/fangxing@2x.png"
            alt=""
          />
        </div>
        <!-- 提示遮罩层 -->
        <div v-show="cellphonenumber == true" style="display: none">
          <div
            :class="[cellphonenumber ? 'popContainer' : '']"
            @touchmove.prevent
          >
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
                      <img
                        class="cancel-img"
                        src="../assets/home/queren.png"
                        alt=""
                      />
                    </a>
                  </div>
                  <img
                    class="cancel-img"
                    src="../assets/home/quxiaoX2.png"
                    alt
                    style="margin-left: 0.306667rem"
                    @click="remvedia"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 确认收款并放行 -->
        <div v-show="Releasese">
          <div v-show="cellphonenumb == true" style="display: none">
            <div
              :class="[cellphonenumb ? 'popContainer' : '']"
              @touchmove.prevent
            >
              <div class="prompt-box-back-imgs" v-show="cellphonenumb == true">
                <!-- 提示框文字 用户收款银行 -->
                <div class="flex-even">
                  <div
                    class="prompt-title font-we-bold text-center"
                    style="flex-direction: column"
                  >
                    <div class="text-center"></div>
                    <div style="margin-top: 0.266667rem">确认已收款并放行</div>
                  </div>
                  <div class="disp-ju-center wed-100">
                    <div class="confirmDetermine disp-ju-center">
                      <img
                        @click="confirm"
                        class="cancel-img"
                        src="../assets/home/queren.png"
                        alt=""
                      />
                    </div>
                    <img
                      class="cancel-img"
                      src="../assets/home/quxiaoX2.png"
                      alt
                      style="margin-left: 0.306667rem"
                      @click="remvedia"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 确认关闭订单 -->
        <div v-show="Closeorderin">
          <div v-show="cellphonenumb == true" style="display: none">
            <div
              :class="[cellphonenumb ? 'popContainer' : '']"
              @touchmove.prevent
            >
              <div class="prompt-box-back-imgs" v-show="cellphonenumb == true">
                <!-- 提示框文字 用户收款银行 -->
                <div class="flex-even">
                  <div
                    class="prompt-title font-we-bold text-center"
                    style="flex-direction: column; padding: 0 0.773333rem"
                  >
                    <div class="text-center">确认关闭订单</div>
                    <div
                      style="
                        margin-top: 0.266667rem;
                        color: #58381c;
                        font-size: 0.373333rem;
                      "
                    >
                      关闭订单后SARO将不会放行给对方，请谨慎操作。
                    </div>
                  </div>
                  <div class="disp-ju-center wed-100">
                    <div class="confirmDetermine disp-ju-center">
                      <img
                        @click="close"
                        class="cancel-img"
                        src="../assets/home/queren.png"
                        alt=""
                      />
                    </div>
                    <img
                      class="cancel-img"
                      src="../assets/home/quxiaoX2.png"
                      alt
                      style="margin-left: 0.306667rem"
                      @click="remvedia"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <van-popup
          v-model="showis"
          position="bottom"
          :style="{ height: '50%' }"
        >
          <div>
            <div class="collection-backimg"></div>
            <div class="from-paddin">
              <div class="userColor" style="text-align: center;padding-top: 0.666667rem;margin-bottom: .52rem;">
                {{ valuehtmo }}
              </div>
              <van-password-input
                :value="value"
                :length="6"
                :gutter="10"
                :focused="showKeyboard"
                @focus="showKeyboard = true"
              />
              <!-- 数字键盘 -->
              <van-number-keyboard
                v-model="value"
                :show="showKeyboard"
                :maxlength="6"
              />

              <!-- 提示框文字 用户收款银行 -->
              <!-- 提示框 -->
              <div
                v-if="this.Collection"
                class="prompt-box-back-img"
                style="position: fixed"
              >
                <div class="flex-even">
                  <div class="prompt-title font-we-bold">
                    资金密码错误，请重试
                  </div>
                  <div class="disp-ju-center wed-100">
                    <img
                      @click="Setup"
                      class="cancel-img"
                      src="@/assets/home/wangji.png"
                      style="width: auto;height: .693333rem;"
                    />
                    <img
                      class="cancel-img"
                      src="@/assets/home/chonshi.png"
                      alt
                      style="margin-left: 0.306667rem;width: auto;height: .693333rem;"
                      @click="remvediase"
                    />
                  </div>
                </div>
              </div>
            </div>
            <div
              @click="Setup"
              style="
                 color: #d14330;
            font-size: 15px;
            margin-top: 0.4rem;
            text-align: right;
            padding-right: 0.533333rem;
            font-weight: bold;
              "
            >
             忘记密码?
            </div>
          </div>
        </van-popup>
      </van-pull-refresh>
    </div>
  </div>
</template>

<script>
import ConfirmThePayment from "./confirmThePayment.vue";
import Recharge from "./Recharge.vue";
// 点击复制
import Clipboard from "clipboard";
import {
  CloseOrder,
  ReleaseOrder,
  BusinessValidateFundassword,
  OrderDetails,
} from "../api/user";
export default {
  components: { Recharge, ConfirmThePayment },
  data() {
    return {
      Collection: false,
      valuehtmo: "请输入资金密码，以验证身份",
      value: "",
      showKeyboard: true,
      errorInfo: "",
      showis: false,
      isLoading: false,
      replace: "",
      replace1: null,
      replace2: null,
      Telephone: 15544466665,
      cellphonenumb: false,
      Closeorderin: false,
      Releasese: false,
      trede: true,
      indexof: 0,
      // 上传图片的路径
      src: "",
      // 判断图片是否上传成功
      srcinfo: 0,
      // 判断是否填写凭证
      isinfo: false,
      // 判断是否显示成功显示
      bottoninfo: true,
      title: "待放行",
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
  watch: {
    async value(value) {
      if (value.length === 6) {
        let valuedata = {
          id: Number(localStorage.getItem("userid")),
          password: this.value,
        };
        await BusinessValidateFundassword(valuedata).then((res) => {
          if (res.data.code != 1) {
            this.Collection = true;
            this.value = "";
            return;
          }
          this.$message({
            message: "验证成功1",
            type: "success",
            center: true,
          });
          this.axiospost();
        });
      }
    },
  },
  methods: {
    Setup() {
      this.$router.push({
        path: "/cellhone",
        name: "cellhone",
        params: {
          id: 1,
        },
      });
    },
    remvediase() {
      this.value = "";
      this.Collection = false;
      this.showKeyboard = true;
    },
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
        this.replace = this.datainfo.phone;
        if (this.datainfo.userPhone2 != null) {
          this.replace1 = this.datainfo;
        }
        if (this.datainfo.userPhone3 != null) {
          this.replace2 = this.datainfo;
        }
      });
    },
    // 点击显示手机号弹出框
    number() {
      this.Releasese = false;
      this.Closeorderin = false;
      this.cellphonenumber = true;
    },
    // 隐藏手机号弹出框
    remvedia() {
      this.cellphonenumber = false;
      this.cellphonenumb = false;
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
          index: 2,
        },
      });
    },
    // 点击显示凭证弹出框
    isnick() {
      this.isinfo = true;
    },
    // 点击关闭遮罩层 点击div之外的 隐藏
    hibe(e) {
      if (!this.$refs.msk.contains(e.target)) {
        this.isinfo = false;
      }
    },
    // 点击放行显示弹窗
    Release() {
      this.value = "";
      this.indexof = 1;
      this.showis = true;
    },
    // 点击关闭订单
    Closeorder() {
      this.value = "";
      this.indexof = 0;
      this.showis = true;
    },
    async axiospost() {
      this.value = "";
      if (this.indexof == 0) {
        this.showis = false;
        this.Closeorderin = true;
        this.cellphonenumb = true;
      } else if (this.indexof == 1) {
        this.showis = false;
        this.Releasese = true;
        this.cellphonenumb = true;
      }
    },
    // 点击跳转到凭证页面
    voucher() {
      this.$router.push({
        path: "/Voucher",
        name: "Voucher",
        params: {
          id: this.datainfo.orderId,
        },
      });
    },
    // 确定关闭订单
    async close() {
      // 下单弹出密码验证
      this.Closeorderin = false;
      let closedata = {
        orderNo: this.datainfo.orderNoStr,
      };
      await CloseOrder(closedata).then((res) => {
        console.log("商家关闭订单");
        console.log(res);
        this.Closeorderin = false;
        if (res.data.message == "关闭成功") {
          this.$router.push({
            path: "/Closur",
            name: "Closur",
            params: {
              id: this.datainfo.orderId,
            },
          });
        } else if (res.data.message == "关闭失败") {
          this.$message({
            message: "订单已经关闭",
            type: "warning",
            center: true,
          });
        }
      });
    },
    // 确定收款并放行
    async confirm() {
      this.Releasese = false;
      let confirmdata = {
        orderNo: this.datainfo.orderNoStr,
      };
      await ReleaseOrder(confirmdata).then((res) => {
        console.log("商家放行订单");
        console.log(res);
        this.Closeorderin = false;
        if (res.data.message == "放行成功") {
          this.$router.push({
            path: "/Complete",
            name: "Complete",
            params: {
              id: this.datainfo.orderId,
            },
          });
        } else if (res.data.message == "订单已关闭") {
          this.$message({
            message: "放行失败",
            type: "warning",
            center: true,
          });
        }
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