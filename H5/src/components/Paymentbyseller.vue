<template>
  <div>
    <!-- 首页背景图 -->
    <div class="back-imageinfo" style="height: auto">
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
            请付款
          </div>
        </div>
      </div>
      <van-pull-refresh
        v-model="isLoading"
        success-text="刷新成功"
        @refresh="onRefresh"
        style="padding: 0.76rem 0.533333rem"
      >
        <div class="disp-space-bet" style="color: #fae8a6">
          <!-- 请付款 -->
          <div class="font-we-bold">
            <div
              style="
                font-size: 20px;
                margin-bottom: 0.28rem;
                -webkit-text-stroke: 0.013333rem #5e3328;
                text-stroke: 0.013333rem #5e3328;
              "
            >
              请付款
            </div>
            <!-- 付款超时时间 -->
            <div
              style="
                font-size: 16px;
                width: 5.72rem;
                -webkit-text-stroke: 0.013333rem #5e3328;
                text-stroke: 0.013333rem #5e3328;
              "
            >
              请在<span></span>
              <span v-show="this.timeDay">{{ this.timeDay }}天</span>
              <span v-show="this.timeHr">{{ this.timeHr }}时</span>
              <span v-show="this.timemin">{{ this.timemin }}分钟</span>
              <span>{{ this.timeSec }}秒</span>内完成付款，超时将自动取消交易
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
            <div
              style="
                font-size: 15px;
                font-weight: bold;
                -webkit-text-stroke: 0.013333rem #5e3328;
                text-stroke: 0.013333rem #5e3328;
              "
            >
              联系对方
            </div>
          </div>
        </div>
        <!-- 付款的详细信息 -->
        <div style="padding-top: 0.493333rem">
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
                <span
                  style="
                    -webkit-text-stroke: 0.013333rem #5e3328;
                    text-stroke: 0.013333rem #5e3328;
                  "
                  >单价:¥{{ this.datainfo.orderUnitPrice | amounswt }}</span
                >
                <span
                  style="
                    margin-left: 0.266667rem;
                    -webkit-text-stroke: 0.013333rem #5e3328;
                    text-stroke: 0.013333rem #5e3328;
                  "
                  >数量:{{ this.datainfo.saroNumber | siwen }}SARO</span
                >
              </div>
            </div>
          </div>
          <!-- 底部的详细信息 -->
          <div class="botton">
            <div
              v-if="hide"
              class="border-bottom font-we-bold list"
              style="color: #d14330; font-size: 14px"
            >
              请使用本人账户向以下账户自行转账
            </div>
            <div
              class="border-bottom font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              付款方式
              <div style="color: #5d3129">银行卡</div>
            </div>
            <div
              class="border-bottom font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              收款人
              <div style="color: #5d3129">
                {{ this.datainfo.bankFavour }}
                <span
                  style="margin-left: 0.066667rem; color: #7abd37"
                  id="copy_text"
                  :data-clipboard-text="this.datainfo.bankFavour"
                  @click="handleCopyFun"
                  >复制</span
                >
              </div>
            </div>
            <div
              class="border-bottom font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              银行卡号
              <div style="color: #5d3129">
                {{
                  this.datainfo.userBankCard == "string"
                    ? null
                    : this.datainfo.userBankCard
                }}
                <span
                  style="margin-left: 0.066667rem; color: #7abd37"
                  id="copy_text"
                  :data-clipboard-text="this.datainfo.userBankCard"
                  @click="handleCopyFun"
                  >复制</span
                >
              </div>
            </div>
            <div
              class="border-bottom font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              开户银行
              <div style="color: #5d3129">
                {{ this.datainfo.userBank }}
              </div>
            </div>
            <div
              class="border-bottom bordr-solde font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              开户支行
              <div style="color: #5d3129">
                {{
                  this.datainfo.userBankBranch == "string"
                    ? null
                    : this.datainfo.userBankBranch
                }}
              </div>
            </div>
            <div
              class="border-bottom font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              卖家姓名
              <div style="color: #5d3129">
                {{ this.datainfo.realName }}
              </div>
            </div>
            <div
              class="border-bottom font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              卖家账号
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
              </div>
            </div>
          </div>
          <!-- 备注信息 -->
          <div v-if="hide">
            <div v-show="bottoninfo">
              <div
                style="color: #ffd800; font-size: 14px; margin-top: 0.386667rem"
                class="font-we-bold zitimiao"
              >
                提现时需要扣除2%手续费和5元银行转账费，增值部分另需扣除20%个税
                <!-- 成功付款图片 -->
                <div class="disp-space-bet" style="margin-top: 0.506667rem">
                  <img
                    @click="close"
                    style="width: 4.213333rem; height: 1.04rem"
                    src="../assets/home/close.png"
                    alt=""
                  />

                  <img
                    @click="isnick"
                    src="../assets/home/wycgfkX2.png"
                    style="width: 4.213333rem; height: 1.04rem; display: block"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 提示遮罩层 -->
        <div v-show="cellphonenumber == true" style="display: none">
          <div
            :class="[cellphonenumber ? 'popContainer' : '']"
            @touchmove.prevent
          >
            <div class="prompt-box-back-imgs" v-show="cellphonenumber == true">
              <!-- 提示框文字 用户收款银行 -->
              <div class="flex-even" style="justify-content: center">
                <div
                  class="prompt-title font-we-bold text-center"
                  style="flex-direction: column"
                >
                  <div
                    class="text-center"
                    style="
                      color: #5d3129;
                      font-weight: bold;
                      font-size: 0.426667rem;
                    "
                  >
                    联系电话
                  </div>

                  <div
                    style="
                      margin: 0.533333rem 0;
                      color: #5d3129;
                      font-weight: bold;
                      font-size: 0.426667rem;
                    "
                  >
                    {{ this.replace }}
                  </div>
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
        <!-- 提示遮罩层 -->
        <div v-show="cellphonenumbersss == true" style="display: none">
          <div
            :class="[cellphonenumbersss ? 'popContainer' : '']"
            @touchmove.prevent
          >
            <div
              class="prompt-box-back-imgs"
              v-show="cellphonenumbersss == true"
            >
              <!-- 提示框文字 用户收款银行 -->
              <div class="flex-even">
                <div
                  class="prompt-title font-we-bold text-center"
                  style="flex-direction: column"
                >
                  <div class="text-center">确认已付款</div>
                </div>
                <div class="disp-ju-center wed-100">
                  <div class="confirmDetermine disp-ju-center">
                    <img
                      @click="confirmpayment"
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
                    @click="remvediaeee"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
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
                关闭订单后SARO将释放退还给对方，请谨慎操作
              </div>
            </div>
            <div class="disp-ju-center wed-100">
              <div class="confirmDetermine disp-ju-center">
                <img
                  @click="closeHide"
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
                @click="remvediaesd"
              />
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
let Base64 = require("js-base64").Base64;
import Clipboard from "clipboard";
import Recharge from "./Recharge.vue";
import {
  BusinessValidateFundassword,
  CloseOrder,
  RecycleOrder,
  OrderDetails,
} from "../api/user";
export default {
  components: { Recharge },
  props: {
    // 付款状态
    OrderStatus: {
      type: "",
      default: () => {
        return [
          {
            texts: "请付款",
            status: "请在1时59分59秒内完成付款，超时将自动取消交易",
          },
        ];
      },
    },
    // 商户电话
    telephone: {
      type: "",
      default: () => {
        return [
          {
            number: "15544466665",
          },
        ];
      },
    },
    // 用户购买金额和单价数量
    UserRecharge: {
      type: "",
      default: () => {
        return [
          {
            total: "1",
            UnitPrice: "1",
            quantity: "1",
          },
        ];
      },
    },
    // 判断是否显示隐藏一些字段
    hide: {
      type: Boolean,
      default: true,
    },
    // 卖家信息
    data: {
      type: "",
      default: () => {
        return [
          {
            itme: "付款方式",
            name: "王五四",
          },
          {
            itme: "收款人",
            name: "2",
            id: 1,
          },
          {
            itme: "银行卡号",
            name: "3",
            id: 1,
          },
          {
            itme: "开户银行",
            name: "4",
          },
          {
            itme: "开户支行",
            name: "5",
            id: 0,
          },
          {
            itme: "卖家姓名",
            name: "6",
          },
          {
            itme: "订单编号",
            name: "7",
            id: 1,
          },
          {
            itme: "下单时间",
            name: "8",
          },
        ];
      },
    },
  },
  data() {
    return {
      cellphonenumbersss: false,
      Collection: false,
      valuehtmo: "请输入资金密码，以验证身份",
      value: "",
      showKeyboard: true,
      errorInfo: "",
      showis: false,
      afetrgg: "",
      isLoading: false,
      title: "请付款",
      // 判断是否显示手机号
      replace: "",
      cellphonenumber: false,
      cellphonenumb: false,
      trede: true,
      // 上传图片的路径
      src: "",
      // 判断图片是否上传成功
      srcinfo: 0,

      // 判断是否显示成功显示
      bottoninfo: true,
      // 用户付款数据
      datainfo: "",
      // 上传服务器凭证
      credentials: "",
      // 剩余分钟
      time: "",
      countTime: 180,
      interval: "",
      toLiveBtn: "30.00",
      timeriw: false,
      plasstime: 0,
      timeDay: 0,
      timeHr: 0,
      timemin: 0,
      timeSec: 0,
      payindex: 0,
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
            message: "验证成功",
            type: "success",
            center: true,
          });
          this.axiospost();
        });
      }
    },
  },
  methods: {
    // 点击关闭订单
    close() {
      this.payindex = 0;
      this.value = "";
      this.showis = true;
    },
    // 确定关闭订单
    async closeHide() {
      let hidedata = {
        orderNo: this.datainfo.orderNoStr,
      };
      await CloseOrder(hidedata).then((res) => {
        console.log("商家关闭订单");
        console.log(res);
        if (res.data.code == 1) {
          this.$message({
            message: "关闭订单成功",
            type: "success",
            center: true,
          });
          this.$router.push({
            paht: "/Merchantsad",
            name: "Merchantsad",
            params: {
              id: this.datainfo.orderId,
            },
          });
          return;
        }
        this.$message({
          message: res.data.message,
          type: "error",
          center: true,
        });
      });
    },
    // 取消关闭订单
    remvediaesd() {
      this.cellphonenumb = false;
    },
    // 取消确定已付款
    remvediaeee() {
      this.value = "";
      this.cellphonenumbersss = false;
    },
    async confirmpayment() {
      let mentdata = {
        orderNo: this.datainfo.orderNoStr,
      };
      await RecycleOrder(mentdata).then((res) => {
        if (res.data.code != 1) {
          this.$message({
            message: res.data.message,
            type: "error",
            center: true,
          });
          return;
        }
        this.$message({
          message: "回收成功",
          type: "success",
          center: true,
        });
        this.$router.push({
          paht: "/Salecomp",
          name: "Salecomp",
          params: {
            id: this.datainfo.orderId,
          },
        });
        console.log(res);
      });
    },
    async axiospost() {
      if (this.payindex == 0) {
        this.cellphonenumb = true;
      } else {
        this.cellphonenumbersss = true;
      }
      this.showis = false;
    },
    onRefresh() {
      if (this.countTime == 0) {
        this.$router.push({
          path: "/Sellcan",
          name: "Sellcan",
          id: this.$route.params.id,
        });
      }
      this.obtain();
      this.timeriw = true;
      this.isLoading = false;
    },
    routerTo() {
      this.$router.push({
        path: "/userSellList",
        name: "userSellList",
        params: {
          index: 1,
        },
      });
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
    // 点击显示手机号弹出框
    number() {
      this.cellphonenumber = true;
    },
    // 隐藏手机号弹出框
    remvedia() {
      this.cellphonenumber = false;
    },
    // 点击显示凭证弹出框
    isnick() {
      this.payindex = 1;
      this.value = "";
      this.showis = true;
    },
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
    // 获取用户详细信息
    async obtain() {
      if (localStorage.getItem("payid")) {
        this.$route.params.id = localStorage.getItem("payid");
      }
      await OrderDetails(this.$route.params.id).then((res) => {
        console.log("商家获取订单详情");
        console.log(res);
        this.plasstime = res.data.data.validTime * 60;
        this.datainfo = res.data.data;
        this.replace = this.datainfo.phone.replace(/[^\d]/g, "");
        // console.log(this.replace);
      });
      if (!this.timeriw) {
        var date1 = new Date();
        date1 = Date.parse(date1);
        date1 = date1 / 1000;
        var date = new Date(this.datainfo.orderTime);
        // 有三种方式获取
        var date = Date.parse(date);
        var date = date / 1000;
        this.time = date1 - date;
        this.time = this.plasstime - this.time;
        this.countTime = this.time;
        if (this.countTime <= 0) {
          this.countTime = 0;
        }
        let day = parseInt(this.countTime / 60 / 60 / 24);
        let hr = parseInt((this.countTime / 60 / 60) % 24);
        let min = parseInt((this.countTime / 60) % 60);
        let sec = parseInt(this.countTime % 60);
        day = day > 9 ? day : "0" + day;
        hr = hr > 9 ? hr : "0" + hr;
        min = min > 9 ? min : "0" + min;
        sec = sec > 9 ? sec : "0" + sec;
        if (day <= 0) {
          this.timeDay = false;
        } else {
          this.timeDay = `${day}`;
        }
        if (hr <= 0) {
          this.timeHr = false;
        } else {
          this.timeHr = `${hr}`;
        }
        if (min <= 0) {
          this.timemin = false;
        } else {
          this.timemin = `${min}`;
        }
        this.timeSec = `${sec}`;
        const that = this;
        that.interval = setInterval(() => {
          if (that.countTime <= 0) {
            // 计时结束，清除缓存
            clearInterval(that.interval);
          } else {
            that.countTime--;
            let day = parseInt(that.countTime / 60 / 60 / 24);
            let hr = parseInt((that.countTime / 60 / 60) % 24);
            let min = parseInt((that.countTime / 60) % 60);
            let sec = parseInt(that.countTime % 60);
            day = day > 9 ? day : "0" + day;

            hr = hr > 9 ? hr : "0" + hr;
            min = min > 9 ? min : "0" + min;
            sec = sec > 9 ? sec : "0" + sec;
            if (day <= 0) {
              this.timeDay = false;
            } else {
              this.timeDay = `${day}`;
            }
            if (hr <= 0) {
              this.timeHr = false;
            } else {
              this.timeHr = `${hr}`;
            }
            if (min <= 0) {
              this.timemin = false;
            } else {
              this.timemin = `${min}`;
            }
            this.timeSec = `${sec}`;
          }
        }, 1000);
      }
    },
  },
  mounted() {
    this.obtain();
    // alert(localStorage.getItem("info"));
  },
};
</script>
<style>
.back-imageses {
  padding: 0.76rem 0.533333rem;
  background-image: url("../assets/home/eback2.png");
  background-repeat: no-repeat;
  background-size: cover;
}

.bott-imges {
  height: 1.986667rem;
  width: 100%;
  background-image: url("../assets/home/bjksX2.png");
  background-repeat: no-repeat;
  background-size: cover;
}

.botton {
  background-color: #f3e2a9;
  border-radius: 0px 0px 26px 26px;
  box-shadow: 1px 4px 0px rgba(0, 0, 0, 0.5);
}

.border-bottom {
  width: 100%;
  border-bottom: 1px dashed #af6f3a;
  padding: 0.333333rem 0;
}

.border-bottom:last-child {
  border-bottom: none;
}

.bordr-sold {
  border-bottom: 5px solid #af6f3a;
}

.confirmDetermine {
  background: #77b936;
  box-shadow: 0px -1px 2px 0px rgba(255, 255, 255, 0.3) inset,
    -1px -2px 0px rgba(249, 251, 115, 0.8);
  width: 1.826667rem;
  height: 0.573333rem;
  color: #ffd800;
  font-size: 15px;
}

.prompt-box-back-imgs {
  background-image: url("../assets/home/e1d.png");
  background-repeat: no-repeat;
  width: 6.213333rem;
  height: 4.16rem;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-size: 100%;
}

.PaidImages {
  background-image: url("../assets/home/edX2.png");
  background-repeat: no-repeat;
  width: 5.733333rem;
  height: auto;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-size: 100%;
  padding: 0.746667rem 0.8rem;
}

.confirmImges {
  width: 100%;
  height: 0.786667rem;
}

.UploadPictures {
  width: 3.253333rem;
  height: 3.52rem;
  background: #ffffff;
  position: relative;
}

.UploadPictures::after {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  width: 200%;
  height: 200%;
  border: 1px solid #af6f3a;
  transform: scale(0.5);
  transform-origin: 0 0;
  box-sizing: border-box;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.imese {
  margin-top: 0.4rem;
  max-width: 3.253333rem;
  max-height: 3.52rem;
}

.list {
  margin: 0 0.426667rem;
  box-sizing: border-box;
  width: auto !important;
}

.bordr-solde {
  margin: 0;
  border-bottom: 2px solid #af6f3a;
  padding: 0.333333rem 0.426667rem;
}

.Pleabordr {
  border: 1px solid #af6f3a;
}

.zitimiao {
  letter-spacing: 1px;
  text-shadow: 1px 1px 0px #5d3129, -1px -1px 0px #5d3129, 1px 1px 0px #5d3119,
    -1px -1px 0px #5d3129, 1px 1px 0px #5d3129, -1px -1px 0px #5d3129;
  color: #5d3129;
}
</style>