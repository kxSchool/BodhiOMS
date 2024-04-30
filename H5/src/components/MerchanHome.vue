<template>
  <div class="Merhome">
    <!-- 头部标题组件 -->
    <div class="back-imageinfoim">
      <!-- 头部标题组件 -->
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
            SARO
          </div>
        </div>
      </div>
      <van-pull-refresh
        v-model="isLoading"
        success-text="刷新成功"
        @refresh="onRefresh"
        style="height: 100vh; padding: 0.76rem 0.533333rem"
      >
        <div class="count font-we-bold">
          <div class="mony disp-space-bet">
            <span>账户余额</span>
            <span v-if="this.data.saro_quantity"
              >{{ this.data.saro_quantity | siwen }}SARO</span
            >
            <span v-else-if="this.data.saro_quantity == undefined"
              >{{ "--" }}SARO</span
            >
            <span v-else-if="this.data.saro_quantity == 0">{{ 0 }}SARO</span>
          </div>
          <img
            @click="Clicknumber"
            class="MerchImage"
            src="../assets/home/gmanX2.png"
          />
        </div>
        <div class="user font-we-bold">
          <!-- <router-link to="" style="font-size: unset"> -->
          <div
            class="one disp-ju-center"
            style="font-size: 0.426667rem"
            @click="UserRechargeList"
          >
            用户充值订单
          </div>
          <!-- </router-link> -->
          <!-- <router-link to="" style="font-size: unset"> -->
          <div
            class="one disp-ju-center"
            style="font-size: 0.426667rem"
            @click="UserSellList(2)"
          >
            用户卖出订单
          </div>
          <!-- </router-link> -->
          <div @click="SAROUnitPrice">
            <div class="one disp-ju-center" style="font-size: 0.426667rem">
              SARO单价
            </div>
          </div>
          <router-link to="/Bank" style="font-size: unset">
            <div class="one disp-ju-center" style="font-size: 0.426667rem">
              我的收款方式
            </div>
          </router-link>
          <router-link to="/Teleph" style="font-size: unset">
            <div class="one disp-ju-center" style="font-size: 0.426667rem">
              我的联系方式
            </div>
          </router-link>
          <div
            @click="postuser"
            class="one disp-ju-center"
            style="font-size: 0.426667rem"
          >
            资金密码
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
              <div class="flex-even" style="    justify-content: center;">
                <div
                  class="prompt-title font-we-bold text-center"
                  style="flex-direction: column"
                >
                  <div class="text-center" style="color: #5D3129;font-weight: bold;font-size: .426667rem;">购买SARO联系电话</div>
                  <div style="margin:.533333rem 0;color: #5D3129;font-weight: bold;font-size: .426667rem;">13325957311</div>
                </div>
                <div class="disp-ju-center wed-100">
                  <div class="confirmDetermine disp-ju-center">
                    <a :href="'tel:' + 13325957311">
                      <img
                        class="cancel-img"
                        src="../assets/home/queren.png"
                        alt
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
        <van-popup
          v-model="showis"
          position="bottom"
          :style="{ height: '50%' }"
        >
          <div>
            <div class="collection-backimg"></div>
            <div class="from-paddin">
              <div
                class="userColor"
                style="text-align: center; padding-top: 0.666667rem;margin-bottom: .52rem;"
              >
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
                v-if="this.Collectionss"
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
                      style="width: auto; height: 0.693333rem"
                    />
                    <img
                      class="cancel-img"
                      src="@/assets/home/chonshi.png"
                      alt
                      style="
                        margin-left: 0.306667rem;
                        width: auto;
                        height: 0.693333rem;
                      "
                      @click="remvediass"
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
      <div
        v-if="this.Collection == 1"
        class="prompt-box-back-img"
        style="position: absolute"
      >
        <!-- 提示框文字 用户交易密码 -->
        <div class="flex-even">
          <div class="prompt-title font-we-bold">
            您暂未设置资金密码，
            <br />请先设置
          </div>
          <div class="disp-ju-center wed-100">
            <!-- 未设置资金密码 -->
            <img
              @click="Merchan"
              class="cancel-img"
              src="@/assets/home/qushezhiX2.png"
            />
            <img
              class="cancel-img"
              src="@/assets/home/quxiaoX2.png"
              alt
              style="margin-left: 0.306667rem"
              @click="remvediaeee"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Recharge from "./Recharge.vue";
import { mapMutations, mapState } from "vuex";
import {
  BusinessGettoKeen,
  BusinessValidatePassword,
  BusinessInformation,
  BusinessValidityValidity,
  BusinessValidateFundassword,
} from "../api/user";
// 点击复制
export default {
  components: { Recharge },
  data() {
    return {
      Collectionss: false,
      valuehtmo: "请输入资金密码，以验证身份",
      value: "",
      showKeyboard: true,
      errorInfo: "",
      showis: false,
      Collection: 0,
      // 判断是否有交易密码   true:有   false:没有
      isLoading: false,
      // 商户电话
      number: "15544466665",
      // 判断是否显示手机号
      cellphonenumber: false,
      // 商家数据
      data: "",
      indexof: 0,
      Transactionpassword: false,
      hometoken: "",
      // firstPlayFlag: true, // 第一次播放标记
    };
  },
  computed: {
    ...mapState(["token"]),
  },
  watch: {
    async value(value) {
      if (value.length === 6) {
        let data = {
          id: Number(localStorage.getItem("userid")),
          password: this.value,
        };
        await BusinessValidateFundassword(data).then((res) => {
          // console.log(res);
          if (res.data.code != 1) {
            this.Collectionss = true;
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
    Setup() {
      this.$router.push({
        path: "/cellhone",
        name: "cellhone",
        params: {
          id: 1,
        },
      });
    },
    remvediass() {
      this.value = "";
      this.Collectionss = false;
      this.showKeyboard = true;
    },
    // 用户充值订单列表
    UserRechargeList() {
      if (this.Transactionpassword) {
        this.value = "";
        // 下单弹出密码验证
        this.showis = true;
        this.indexof = 0;
      } else {
        this.Collection = 1;
      }
    },
    // 进入saro单价页面
    SAROUnitPrice() {
      if (this.Transactionpassword) {
        // 下单弹出密码验证
        this.showis = true;
        this.indexof = 2;
        this.value = "";
      } else {
        this.Collection = 1;
      }
    },
    axiospost() {
      if (this.indexof == 0) {
        this.$router.push({
          path: "/MerH5",
          name: "MerH5",
        });
      } else if (this.indexof == 1) {
        this.$router.push({
          path: "/userSellList",
          name: "userSellList",
        });
      } else if (this.indexof == 2) {
        this.$router.push("/SaroUni");
      }
    },
    // 用户卖出订单列表
    UserSellList() {
      if (this.Transactionpassword) {
        // 下单弹出密码验证
        this.showis = true;
        this.indexof = 1;
        this.value = "";
      } else {
        this.Collection = 1;
      }
    },
    // 校验用户是否有交易密码
    async transPwd() {
      await BusinessValidatePassword().then((res) => {
        console.log("获取商家是否有交易信息");
        console.log(res);
        if (res.data.code != 1) {
          this.Transactionpassword = false;
          return;
        }

        // 效验成功
        this.Collection = 0;
        this.Transactionpassword = true;
        localStorage.setItem("userid", this.data.user_id);
      });
    },
    onRefresh() {
      this.isdns();
      this.isLoading = false;
    },
    ...mapMutations(["assignment"]),
    // 点击显示手机号弹出框
    Clicknumber() {
      this.cellphonenumber = true;
    },
    // 隐藏手机号弹出框
    remvedia() {
      this.cellphonenumber = false;
    },
    // 获取新的token
    getToken() {
      BusinessGettoKeen(this.$route.query.t).then((res) => {
        if (res.data.data != null) {
          console.log("获取新token-------------------------", res.data.data);
          localStorage.setItem("Authorization", res.data.data);
          this.hometoken = {
            headers: {
              token: localStorage.getItem("Authorization"),
            },
          };
          if (this.hometoken) {
            localStorage.setItem("judge", 1);
            this.$nextTick(() => {
              this.transPwd();
              this.isdns();
              this.upindex();
            });
          }
        }
      });
    },
    // // 获取商家信息 页面一加载就调接口
    async Getmerchant() {
      // 你这句代码用来做什么的
      // 获取到vuex里面的值又去赋值吗
      var index = this.$store.state.token;
      if (index) {
        // this.$store.commit("assignment", 0);
      } else {
        this.$store.commit("assignment", this.$route.query.t);
      }
    },
    async upindex() {
      await BusinessValidityValidity(this.hometoken).then((res) => {
        console.log("效验游戏端用户合法性");
        console.log(res);
        if (res.data.code != 1) {
          this.$message({
            message: "登陆失败",
            type: "error",
            center: true,
          });
          setTimeout(() => {
            //  失败后跳转回游戏
            window.location.href = 'uniwebview://game?action="close"';
          }, 1000);
          return;
        }
        localStorage.setItem("onPhone", res.data.data.user_name);
      });
    },
    async isdns() {
      await BusinessInformation(this.hometoken).then((res) => {
        console.log("获取商家信息");
        console.log(res);
        this.data = res.data.data;
        if (res.data.message == "该账号被禁用") {
          this.$message({
            message: "您的商家账户已被禁用",
            type: "error",
            center: true,
          });
        }
      });
    },

    // 跳回游戏
    routerTo() {
      window.location.href = 'uniwebview://game?action="close"';
    },
    // 关闭遮罩层
    remvediaeee() {
      this.Collection = 0;
    },
    Merchan() {
      this.$router.push({
        path: "/tranpass",
        name: "tranpass",
        params: {
          id: 1,
        },
      });
    },

    postuser() {
      if (this.Transactionpassword) {
        this.$router.push({
          path: "/lson",
          name: "lson",
          params: {
            id: 1,
          },
        });
      } else {
        this.Collection = 1;
      }
    },
  },
  created() {
    this.getToken();
    if (this.$route.params.word == 1) {
      this.$router.go(0);
    }
  },
  mounted() {
    localStorage.setItem("sellerIndex", 0);
    if (localStorage.getItem("judge") == 1) {
      this.$nextTick(() => {
        this.transPwd();
        this.isdns();
        this.upindex();
      });
    }
  },
};
</script>
<style>
/* 背景图片 */

.count {
  display: flex;
  align-items: center;
  color: #5d3129;
  font-size: 32px;
}

.mony {
  justify-content: space-evenly !important;
  background-image: url("../assets/home/ezhye.png");
  background-repeat: no-repeat;
  background-size: cover;
  width: 100%;
  height: 1.44rem;
  margin-right: 0.133333rem;
}

.MerchImage {
  width: 1.706667rem;
  height: 1.293333rem;
}

.user {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  flex-direction: row;
  color: #5d3129;
  font-size: 32px;
}

.one {
  margin-top: 0.533333rem;
  width: 4.32rem;
  height: 1.44rem;
  background-image: url("../assets/home/ezzkx.png");
  background-repeat: no-repeat;
  background-size: cover;
  color: #5d3129;
}

.back-imageinfoim {
  background-image: url("../assets/home/back.png");
  background-repeat: no-repeat;
  box-sizing: border-box;
  background-size: cover;
}

body {
  background-color: #fff !important;
  height: 100%;
}
</style>