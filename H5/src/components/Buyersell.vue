<template>
  <!--这里是会被缓存的组件-->
  <div>
    <!-- 背景图片 -->
    <div class="back-img">
      <NavBar></NavBar>
      <Rearge></Rearge>
      <van-pull-refresh v-model="isLoading" success-text="刷新成功" @refresh="onRefresh" style="height: 100vh">
        <div class="SARO font-we-bold text-shadow">
          <div>卖出SARO</div>
          <div>单价¥{{ UnitPrice | amounswt }}</div>
        </div>
        <!-- 中间背景图片 -->
        <div class="center-img">
          <!-- 商户名称和银行卡图标 -->
          <div class="disp-space-bet center-top">
            <span class="text-shadow">{{ datainfo.nickname }}</span>
            <img class="yh_img" src="../assets/home/eyhkX2.png" alt="" />
          </div>
          <!-- 充值金额输入框 -->
          <div class="botton-cen">
            <div class="disp-ju-center font-sizetw">
              <div v-for="(item, index) in data" :key="index">
                <img class="botton-img" :src="index == isindex ? item.ima : item.imas" @click="cloak(index, item)" />
              </div>
            </div>
            <!-- 充值输入框 -->
            <div class="post-rela" style="text-align: center">
              <input :placeholder="placehol" type="number" class="quota-input input-queo" style="padding: 0"
                v-model="amount" @blur="lose" @input="inputamount" />
              <div v-if="isindex == 0" class="ReCNY" style="color: #AF6F3A;">CNY</div>
              <div v-else class="ReCNY" style="font-size: 0;">
                <span
                  style="padding-right: .466667rem;color: #AF6F3A;font-size: 16px;border-right:1px dashed;">SARO</span><span
                  @click="Sellall                 "
                  style="font-size:16px;color: #5D3129;padding-left: .16rem;font-weight: bold;">全部卖出</span>
              </div>
            </div>
            <!-- 限额提示 -->
            <!-- <div class="quota">
              限额:¥{{ datainfo.minRecharge }}-¥{{ datainfo.maxRecharge }}
            </div> -->
            <!-- 数量限额 -->
            <!-- <div v-else class="quota">
              数量限制:{{ Minimumquantity | iskes }}～{{
                Maximumquantity | amounsas
              }}个SARO
            </div> -->
            <!-- 充值和付款信息 -->
            <div class="information">
              <div class="disp-space-bet">
                <span class="Recharg">卖出数量</span>
                <span v-if="isindex == 0" class="payment">{{ quantity | iskes }} SARO</span>
                <span v-else class="payment">{{ quantityes | iskes }} SARO</span>
              </div>
              <div class="disp-space-bet" style="margin-top: 0.133333rem">
                <span class="Recharg">实收款</span>
                <span v-if="isindex == 0" class="Price">¥{{ payment | amount }}</span>
                <span v-else class="Price">¥{{ amou | amount }}</span>
              </div>
              <!-- 下单按钮 -->
              <img v-if="!Rechargein" class="order" src="../assets/home/xdhX2.png" alt="" />
              <img v-else @click="placeanorder" class="order" src="../assets/home/xdlvX2.png" alt="" />
            </div>
          </div>
        </div>

        <van-popup v-model="showis" position="bottom" :style="{ height: '50%' }">
          <div>
            <div class="collection-backimg"></div>
            <div class="from-paddin">
              <div class="userColor" style="text-align: center;padding-top: .666667rem;margin-bottom: .52rem;">
                {{ valuehtmo }}
              </div>
              <van-password-input :value="value" :length="6" :gutter="10" :focused="showKeyboard"
                @focus="showKeyboard = true" />
              <!-- 数字键盘 -->
              <van-number-keyboard v-model="value" :show="showKeyboard" :maxlength="6" />

              <!-- 提示框文字 用户收款银行 -->
              <!-- 提示框 -->
              <div v-if="this.Collection" class="prompt-box-back-img" style="position: fixed">
                <div class="flex-even">
                  <div class="prompt-title font-we-bold">
                    资金密码错误，请重试
                  </div>
                  <div class="disp-ju-center wed-100">
                    <img @click="Setup" class="cancel-img" src="@/assets/home/wangji.png"
                      style="width: auto;height: .693333rem;" />
                    <img class="cancel-img" src="@/assets/home/chonshi.png" alt
                      style="margin-left: 0.306667rem;width: auto;height: .693333rem;" @click="remvedia" />
                  </div>
                </div>
              </div>
            </div>
            <div @click="Forget" style="
                color: #d14330;
                font-size: 15px;
                margin-top: .4rem;
                text-align: right;
                padding-right: 0.533333rem;
                font-weight: bold;
              ">
              忘记密码?
            </div>
          </div>
        </van-popup>
        <div v-if="this.Collectionesd" class="prompt-box-back-img" style="position: absolute">
          <div class="flex-even">
            <div class="prompt-title font-we-bold">
              邮箱注册的用户需联系客服<br />协助找回密码
            </div>
            <div class="disp-ju-center wed-100">
              <img @click="mehode" class="cancel-img" style="width: 3.426667rem" src="../assets/home/me.png" />
            </div>
          </div>
        </div>
      </van-pull-refresh>
    </div>
  </div>
</template>

<script>
  import Recharge from "./Recharge.vue";
  import { Popup } from "vant";
  import { ValidationUnitPrice, RechargeOrder } from '../api/business.js'
  import { BusinessValidateFundassword } from '../api/user.js'
  export default {
    components: { Recharge, Popup },
    data() {
      return {
        Collectionesd: false,
        Collection: false,
        valuehtmo: "请输入资金密码，以验证身份",
        value: "",
        showKeyboard: true,
        showis: false,
        Maximumquantity: 0,
        isLoading: false,
        Minimumquantity: 0,
        placehol: "请输入提现金额",
        // 充值金额
        amount: "",
        // 切换限额
        switched: true,
        // 实际付款
        payment: 0,
        // 是否显示下单按钮
        Rechargein: false,
        // 最大充值限额
        quota: "",
        // 把最大充值转为数字类型
        transformation: 0,
        // 最小充值限额
        minimum: 742,
        // 单价
        UnitPrice: "",
        isindex: 0,
        // 充值数量
        quantityes: 0,
        amout: "",
        mounumb: "",
        // 充值商户信息
        datainfo: "",
        // 充值索引
        index: "",
        indexindd: 0,
        amou: 0,
        // 判断按数量充值
        quantityesw: false,
        placeanorderse: false,
        codeinfo: 0,
        timer: null,
        // 充值数量
        data: [
          {
            ima: require("../assets/home/anjine.png"),
            imas: require("../assets/home/anjinewe.png"),
          },
          {
            ima: require("../assets/home/anshul.png"),
            imas: require("../assets/home/anshuliang.png"),
          },
        ],
      };
    },
    methods: {
      // 点击全部卖出
      Sellall() {
        this.amount = localStorage.getItem('SARO')
        if (this.amount > 0) {
          this.Rechargein = true
          this.quantityes = this.amount
          this.amou = this.UnitPrice * this.quantityes;
          if (this.amou > this.quota) {
            this.$message({
              message: "单次卖出最大限额10000元",
              type: "warning",
              center: true,
            });
            this.amount = ''
            this.quantityes = 0
            this.amou = 0
            this.Rechargein = false
            return
          }
          if (this.amou < this.minimum) {
            this.$message({
              message: "单次卖出最小限额742元",
              type: "warning",
              center: true,
            });
            this.amount = ''
            this.quantityes = 0
            this.amou = 0
            this.Rechargein = false

          }
        }
      },
      async Getprice() {
        let indexSt = this.$route.params.data.filter(
          (item) => item.userId == this.$route.params.id
        );
        let data = {
          orderType: 2,
          orderTransactorId: this.$route.params.id,
          orderUnitPrice: indexSt[0].recoveryUnitPrice,
        }
        await ValidationUnitPrice(data)
          .then((res) => {
            console.log("检查商户单价是否有变动");
            console.log(res);
            if (res.data.code != 1) {
              this.$message({
                message: res.data.message,
                type: "error",
                center: true,
              });
              this.$router.push("/home");
            }
          });
      },
      Forget() {
        if (localStorage.getItem("mailbox") == 1) {
          this.Collectionesd = true;
          this.showis = false;
          return;
        }
        this.$router.push("/cellhone");
      },
      mehode() {
        this.Collectionesd = false;
      },
      onRefresh() {
        this.obine();
        this.Getprice();
        this.isLoading = false;
      },
      async obine() {
        if (!this.$route.params.index) {
          this.$route.params.index = localStorage.getItem("index");
        }
        if (!this.$route.params.data) {
          this.$route.params.data = JSON.parse(localStorage.getItem("data"));
        }
        // 最大充值金额
        this.UnitPrice =
          this.$route.params.data[this.$route.params.index].recoveryUnitPrice;
        this.quota = 10000;
        // 最小充值金额
        this.minimum = 742;
        this.datainfo = this.$route.params.data[this.$route.params.index];
      },
      // 监听用户输入的值
      inputamount() {
        if (!/^\d*(?:\.\d{0,4})?$/.test(this.amount)) {
          this.amount = "";
          this.amou = 0;
          this.quantityes = 0;
          this.$message({
            message: "最多输入四位小数",
            type: "warning",
            center: true,
          });
        } else {
          // 最大限额
          // 最小限额
          this.payment = Number(this.amount);
          // 如果是按金额充值
          if (this.isindex == 0) {
            if (this.amount > this.quota) {
              this.amount = "";
              this.payment = 0;
              this.$message({
                message: "单次卖出最大限额10000元",
                type: "warning",
                center: true,
              });
            } else if (this.amount.length == 0) {
              // 是否显示下单按钮
              this.Rechargein = false;
            } else {
              // 显示下单按钮
              this.Rechargein = true;
            }
          } else {
            //充值数量等于用户输入的数量
            this.quantityes = this.amount;
            if (this.amount.length == 0) {
              // 是否显示下单按钮
              this.Rechargein = false;
            } else {
              // 显示下单按钮
              this.Rechargein = true;
            }
          }
          // 按数量充值的金额
          this.amou = this.UnitPrice * this.quantityes;
          var ger = this.amou * 100;
          var kk = Math.ceil(ger);
          this.amou = kk / 100;

          if (Number(this.amou > this.quota)) {
            this.amount = "";
            this.quantityes = 0;
            this.Rechargein = false;
            this.amou = 0;
            // input判断出错一直不断显示这个message 弹窗
            this.$message({
              message: "单次卖出最大限额10000元",
              type: "warning",
              center: true,
            });
          } else if (this.amount.length == 0) {
            this.quantityes = 0;
            this.Rechargein = false;
          }
        }
      },
      lose() {
        let mins = Number(this.minimum);
        if (this.isindex == 1) {
          var minseexs = mins / this.UnitPrice;
          // 截取当前数据到小数点后两位
          let transformVal = parseFloat(minseexs).toFixed(5);
          var realVal = transformVal.substring(0, transformVal.length - 1);

          if (this.amount.length != 0) {
            if (this.amount < Number(realVal)) {
              this.amount = "";
              this.amou = 0;
              this.quantityes = 0;
              this.$message({
                message: "卖出最小限额742元",
                type: "warning",
                center: true,
              });
            } else {
              this.placeanorderse = true;
            }
          }
        }
        if (this.isindex == 0 && this.amount < mins) {
          if (this.amount.length != 0) {
            if (this.amount < realVal) {
              this.amount = "";
              this.amou = 0;
              this.quantityes = 0;
              this.$message({
                message: "超出充值限额",
                type: "warning",
                center: true,
              });
            } else {
              this.placeanorderse = true;
            }
          }
        }
        if (this.amount.length == 0) {
          // 是否显示下单按钮
          this.Rechargein = false;
        } else {
          // 显示下单按钮
          this.Rechargein = true;
        }
      },
      passswordfor() {
        this.$router.push("/cellhone");
      },
      Setup() {
        this.$router.push("/cellhone");
      },
      remvedia() {
        this.value = "";
        this.Collection = false;
        this.showKeyboard = true;
      },
      clickto() {
        this.$router.push("/home");
      },
      // 点击切换图片
      cloak(index, item) {
        this.isindex = index;
        if (this.isindex == 1) {
          // 按数量充值最小额度
          this.Maximumquantity = this.transformation / this.UnitPrice;
          this.Minimumquantity = this.minimum / this.UnitPrice;
          var gerr = this.Minimumquantity * 10000;
          var kkk = Math.ceil(gerr);
          this.Minimumquantity = kkk / 10000;
          this.switched = false;
          this.indexindd = 1;
          this.amount = "";
          this.Rechargein = false;
          this.payment = 0;
          this.payAmount = "";
          this.placehol = "请输入提现数量";
        } else {
          this.switched = true;
          this.placehol = "请输入提现金额";
          this.amount = "";
          this.payment = 0;
          this.amou = 0;
          this.indexindd = 0;
          this.quantityes = 0;
          this.Rechargein = false;
        }
      },
      // 获取充值商户个人信息
      async placeanorder() {
        if (Number(this.quantityes) > Number(localStorage.getItem("SARO"))) {
          this.$message({
            message: "您没有足够的SARO",
            type: "error",
            center: true,
          });
          return;
        }
        if (Number(this.quantity) > Number(localStorage.getItem("SARO"))) {
          this.$message({
            message: "您没有足够的SARO",
            type: "error",
            center: true,
          });
          return;
        }
        this.value = "";
        let placedata = {
          orderType: 2,
          orderPriceCny: Number(this.amount),
          orderPriceSaro: this.quantityes,
          // 下单id
          orderTransactorId:
            this.$route.params.data[this.$route.params.index].userId,
          // 单价
          orderUnitPrice:
            this.$route.params.data[this.$route.params.index].recoveryUnitPrice, // 实付金额
          payAmount: this.payment,
          payAmou: this.amou,
        }
        await ValidationUnitPrice(placedata)
          .then((res) => {
            if (res.data.code != 1) {
              this.$message({
                message: "当前单价有变动，请重新下单",
                type: "error",
                center: true,
              });
              this.codeinfo = 1;
              this.$router.push("/Home");
            }
          });
        if (this.codeinfo == 1) {
          return;
        }
        let minse = Number(this.minimum);
        if (this.indexindd == 1) {
          // this.amount = "";
          this.payment = 0;
        }
        if (this.isindex == 0 && this.amount < minse) {
          this.amount = "";
          this.payment = 0;
          this.$message({
            message: "卖出最小限额742元",
            type: "warning",
            center: true,
          });
          return;
        }
        // 下单弹出密码验证
        this.showis = true;
        if (this.showis == true) {
          // 开启定时器
          this.timers();
        }
      },
      // 定时器
      timers() {
        this.timer = setTimeout(() => {
          this.$message({
            message: "您在当前页面停留时间过长，请重新下单",
            type: "error",
            center: true,
          });
          this.showis = false;
          this.obine();
          this.amount = "";
          this.payment = 0;
          this.quantityes = 0
          this.amou = 0
          this.Rechargein = false;
        }, 30000);
      },
      async axiospost() {
        if (this.isindex == 1) {
          this.amount = 0;
        } else {
          this.quantityes = 0;
        }
        let postdata = {
          orderType: 2,
          orderPriceCny: Number(this.amount),
          orderPriceSaro: Number(this.quantityes),
          // 下单id
          orderTransactorId:
            this.$route.params.data[this.$route.params.index].userId,
          // 单价
          orderUnitPrice:
            this.$route.params.data[this.$route.params.index].recoveryUnitPrice, // 实付金额
          payAmount: this.payment,
          payAmou: this.amou,
          token: this.$store.state.token,
        }
        RechargeOrder(postdata)
          .then((res) => {
            console.log("用户充值下单");
            console.log(res);
            if (res.data.code == 1) {
              localStorage.setItem("key", 0);
              localStorage.setItem("info", res.data.data);
              this.$message({
                message: "验证成功",
                type: "success",
                center: true,
              });
              this.$router.push({
                path: "/Buyerss",
                name: "Buyerss",
                params: {
                  id: this.data.message,
                  index: 1,
                },
              });
            } else {
              this.quantityes = 0;
              this.amou = 0;
              this.$message({
                message: res.data.message,
                type: "warning",
                center: true,
              });
              this.showis = false;
            }
          });
      },
    },
    watch: {
      async value(value) {
        // 清除定时器
        if (value.length >= 1) {
          clearTimeout(this.timer);
        }
        if (value.length === 6) {
          let valuedata = {
            id: Number(localStorage.getItem("userid")),
            password: this.value,
          }
          await BusinessValidateFundassword(valuedata)
            .then((res) => {
              if (res.data.code != 1) {
                this.Collection = true;
                this.value = "";
                return;
              }
              this.value = "";
              this.axiospost();
            });
        }
      },
    },
    mounted() {
      this.obine();
    },
    destroyed() {
      clearTimeout(this.timer);
    },
    computed: {
      // 按金额充值的SARO数量
      quantity: {
        get: function () {
          return this.amount / this.UnitPrice;
        },
        set: function (newValue) {
          return newValue;
        },
      },
      // 按数量充值最大额度
      // Maximumquantity: {
      //   get: function () {
      //     return this.transformation / this.UnitPrice;
      //   },
      //   set: function (newValue) {
      //     return newValue;
      //   },
      // },
      // amou: {
      //   get: function () {
      //     return this.UnitPrice * this.quantityes;
      //   },
      //   set: function (newValue) {
      //     return newValue;
      //   },
      // },
    },
  };
</script>
<style scoped>
  .back-img {
    overflow: scroll;
    position: fixed;
    width: 100%;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    margin: 0 auto;
    z-index: -1;
    background-image: url("../assets/home/eback2.png");
    background-repeat: no-repeat;
    background-size: cover;
  }

  .SARO {
    width: 90%;
    margin: auto;
    padding: 0.533333rem 0;
    line-height: 0.933333rem;
    color: #fae9a6;
    font-size: 40px;
  }

  .center-img {
    width: 90%;
    margin: auto;
    height: 13.173333rem;
    background-image: url("../assets/home/eback.png");
    background-repeat: no-repeat;
    background-size: cover;
    overflow: auto;
    position: relative;
    padding: 0 0.56rem;
    box-sizing: border-box;
  }

  .yh_img {
    width: 0.693334rem;
    height: 0.56rem;
  }

  .center-top {
    position: inherit;
    top: 2%;
    color: #fae9a6;
    font-size: 32px;
    font-weight: bold;
  }

  .botton-cen {
    position: relative;
    top: 8%;
  }

  .botton-img {
    width: 3.093333rem;
    height: 0.92rem;
    padding: 0 0.173334rem;
  }

  .quota {
    color: #af6f3a;
    font-size: 28px;
    font-weight: bold;
    margin-top: 0.333333rem;
  }

  .quota-input {
    font-weight: bold;
    color: #5d3129;
    font-size: 32px;
    width: 90%;
    height: 85%;
    border: none;
    background-color: #dfcea3;
    position: absolute;
    top: 47%;
    left: 50%;
    transform: translate(-50%, -50%);
  }

  .information {
    margin-top: 1.333333rem;
  }

  .Recharg {
    color: #af6f3a;
    font-weight: bold;
    font-size: 32px;
  }

  .payment {
    font-weight: bold;
    color: #5d3129;
    font-size: 32px;
  }

  .Price {
    color: #d14330;
    font-size: 40px;
    font-weight: bold;
    margin-top: 5px;
  }

  .order {
    width: 3.013333rem;
    height: 1.04rem;
    margin: 2.133333rem auto;
    display: block;
  }

  .post-rela {
    background-image: url("../assets/home/jukb.png");
    background-repeat: no-repeat;
    background-size: 100%;
    width: 100%;
    height: 1.213334rem;
    margin-top: 0.4rem;
  }

  .ReCNY {
    font-weight: bold;
    position: absolute;
    top: 47%;
    right: 5%;
    transform: translate(0%, -50%);
    color: #5d3129;
    font-size: 32px;
  }

  input::-webkit-input-placeholder {
    font-size: 32px;
    color: #5e3229;
  }

  input::-moz-placeholder {
    font-size: 32px;
    color: #5e3229;
  }

  input:-moz-placeholder {
    font-size: 32px;
    color: #5e3229;
  }

  input:-ms-input-placeholder {
    font-size: 32px;
    color: #5e3229;
  }

  .userColor {
    color: #5e3229;
    font-size: 32px;
    font-weight: bold;
    margin-bottom: 0.4rem;
  }

  .user-input {
    margin-bottom: 0.666667rem;
    height: 1.253333rem;
    width: 100%;
    box-sizing: border-box;
    border-radius: 0.106666rem;
  }



  .numberimg {
    position: absolute;
    bottom: 20%;
    left: 50%;
    transform: translate(-50%, 0%);
    width: 5.266667rem;
    height: 1.4rem;
    display: block;
  }
</style>