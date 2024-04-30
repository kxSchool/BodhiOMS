<template>
  <div id="app">
    <!-- 首页背景图 -->
    <div class="back-imagees">
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

      <!-- <p>刷新次数: {{ count }}</p> -->
      <!-- 充值和卖出按钮 -->
      <router-link to=""></router-link>
      <div class="disp-space-bet" style="padding: 0.493333rem 0.413333rem">
        <div class="disp-flex">
          <img
            v-if="switchimg"
            src="@/assets/home/czX2.png"
            style="width: 2.44rem; height: 0.853333rem"
            @click="Recharge"
          />
          <img
            v-else
            src="@/assets/home/czwxzX2.png"
            style="width: 2.44rem; height: 0.773333rem"
            @click="Recharge"
          />
          <img
            v-if="!switchimg"
            src="@/assets/home/mcxz.png"
            style="width: 2.44rem; height: 0.853333rem; margin-left: 0.32rem"
            @click="sellout"
          />
          <img
            v-else
            src="@/assets/home/mcwxzX2.png"
            style="width: 2.44rem; height: 0.773333rem; margin-left: 0.32rem"
            @click="sellout"
          />
        </div>
        <!-- 订单和更多 -->
        <div class="disp-flex">
          <div class="disp-ju-center">
            <div
              @click="Myrechclick"
              class="order-color disp-colu-cen font-we-bold"
              style="margin-right: 0.233334rem"
            >
              <img src="@/assets/home/eddX2.png" class="order-img" />
              <div class="text-show">订单</div>
            </div>
          </div>
          <div class="disp-ju-center">
            <div class="post-relasd">
              <div
                @click.stop="Popup"
                class="order-color disp-colu-cen font-we-bold"
              >
                <img src="@/assets/home/egdX2.png" class="order-img" />
                <div class="text-show">更多</div>
              </div>
              <div v-if="opup" class="index">
                <div class="backgr-img disp-flex">
                  <div class="disp-flex space-even">
                    <router-link
                      to="/Number"
                      class="text-center aback-color font-we-bold"
                      >联系方式</router-link
                    >

                    <span class="bord"></span>
                    <router-link
                      class="text-center aback-color font-we-bold"
                      to="/User"
                      >收款方式</router-link
                    >
                    <span class="bord"></span>
                    <router-link
                      v-if="password"
                      class="text-center aback-color font-we-bold"
                      to="/tranpass"
                      >交易密码
                    </router-link>
                    <router-link
                      v-else
                      class="text-center aback-color font-we-bold"
                      to="/lson"
                      style="padding-bottom: 0.066667rem"
                      >交易密码</router-link
                    >
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 充值商户列表 -->

      <div
        style="
          padding: 0 0.546667rem 0.48rem 0.546667rem;
          box-sizing: border-box;
        "
        class="backimages"
      >
        <div
          class="back-color"
          style="
            padding-top: 0.066667rem;
            height: auto !important;
            padding-bottom: 0.266667rem !important;
            overflow: hidden;
          "
        >
          <div class="home-top disp-ju-center">
            SARO黄金通证可用于EChain商城提取等值商品
          </div>
          <van-pull-refresh
            v-model="isLoading"
            success-text="刷新成功"
            @refresh="onRefresh"
            style="overflow: unset !important; height: 100%"
          >
            <!-- 商户背景 -->

            <div style="margin: 0.373333rem 0.306666rem">
              <div
                v-for="(item, index) in data"
                :key="index"
                v-show="Hideshow == true"
                class="Merchant-back-img"
                @click="dialogVisibl(number, data, item.userId, index)"
                style="
                  padding: 0.36rem 0.44rem;
                  box-sizing: border-box;
                  margin-bottom: 0.2rem;
                "
              >
                <!-- 小图标 -->
                <img src="@/assets/home/sdX2.png" class="icon-image" />
                <!-- 商户信息 -->
                <div @click="handleClose">
                  <!-- 商户名称和单价 -->
                  <div class="disp-space-bet">
                    <!-- 商户名称 -->
                    <div class="Merchant-title font-we-bold">
                      {{ item.nickname }}
                    </div>
                    <!-- 商户单价 -->
                    <div class="Merchant-title font-we-bold">单价</div>
                  </div>
                  <!-- 商户剩余数量和价格 -->
                  <div
                    class="disp-space-bet"
                    style="margin-bottom: 0.066667rem"
                  >
                    <!-- 商户剩余数量 -->
                    <div class="Merchant-quantity font-we-bold disp-flex">
                      数量:
                      <div
                        class="Merchant-number"
                        style="
                          display: flex;
                          align-items: center;
                          justify-content: center;
                        "
                      >
                        {{ item.saroNumber | siwen }}SARO
                      </div>
                    </div>
                    <!-- 商户价格 -->
                    <div class="Merchant-Price font-we-bold">
                      ¥{{ item.rechargeUnitPrice }}
                    </div>
                  </div>
                  <!-- 商户商品单价和充值按钮 -->
                  <div class="disp-space-bet">
                    <!-- 商户商品单价 -->
                    <div class="Merchant-quantity font-we-bold disp-flex">
                      单价:
                      <div
                        class="Merchant-number"
                        style="
                          display: flex;
                          align-items: center;
                          justify-content: center;
                        "
                      >
                        ¥{{ item.minRecharge }}-¥{{ item.maxRecharge }}
                      </div>
                    </div>

                    <!-- 充值按钮 -->
                  </div>
                  <div class="disp-space-bet">
                    <div
                      style="color: #b06f39; font-weight: bold; font-size: 13px"
                    >
                      在线时间:9:00-12:00&13:00-18:00
                    </div>
                    <img
                      src="../assets/home/chonzhi.png"
                      style="width: 1.44rem; height: 0.6rem"
                    />
                  </div>
                </div>
              </div>
            </div>

            <!-- 卖出 -->
            <div style="margin: 0.373333rem 0.306666rem">
              <div
                v-for="(item, index) in sale"
                :key="index"
                v-show="Hideshow == false"
                class="Merchant-back-img"
                @click="dialogVisibls(sale, item.userId, index)"
                style="
                  padding: 0.36rem 0.44rem;
                  box-sizing: border-box;
                  margin-bottom: 0.2rem;
                "
              >
                <!-- 小图标 -->
                <img src="@/assets/home/sdX2.png" class="icon-image" />
                <!-- 商户信息 -->
                <div @click="handleClose">
                  <!-- 商户名称和单价 -->
                  <div class="disp-space-bet">
                    <!-- 商户名称 -->
                    <div class="Merchant-title font-we-bold">
                      {{ item.nickname }}
                    </div>
                    <!-- 商户单价 -->
                    <div class="Merchant-title font-we-bold">单价</div>
                  </div>
                  <!-- 商户剩余数量和价格 -->
                  <div
                    class="disp-space-bet"
                    style="
                      margin-bottom: 0.066667rem;
                      display: flex;
                      justify-content: right;
                    "
                  >
                    <!-- 商户剩余数量 -->
                    <!-- <div class="Merchant-quantity font-we-bold disp-flex">
                      数量:
                      <div
                        class="Merchant-number"
                        style="
                          display: flex;
                          align-items: center;
                          justify-content: center;
                        "
                      >
                        {{ item.saroNumber | siwen }}SARO
                      </div>
                    </div> -->
                    <!-- 商户价格 -->
                    <div class="Merchant-Price font-we-bold">
                      ¥{{ item.recoveryUnitPrice }}
                    </div>
                  </div>
                  <!-- 商户商品单价和充值按钮 -->
                  <div class="disp-space-bet">
                    <!-- 商户商品单价 -->
                    <!-- <div class="Merchant-quantity font-we-bold disp-flex">
                      限额:
                      <div
                        class="Merchant-number"
                        style="
                          display: flex;
                          align-items: center;
                          justify-content: center;
                        "
                      >
                        ¥{{ item.minRecharge }}-¥{{ item.maxRecharge }}
                      </div>
                    </div> -->
                    <!-- 充值按钮 -->
                    <div
                      style="color: #b06f39; font-weight: bold; font-size: 13px"
                    >
                      在线时间:9:00-12:00&13:00-18:00
                    </div>
                     
                    <img
                      src="../assets/home/chushou.png"
                      style="width: 1.44rem; height: 0.6rem"
                    />
                  </div>
                </div>
              </div>
            </div>
          </van-pull-refresh>
        </div>
      </div>
      <!-- 提示遮罩层 -->
      <div v-if="dialogVisib">
        <div
          v-show="!infois"
          :class="[dialogVisib ? 'popContainer' : '']"
          @touchmove.prevent
        >
          <!-- 提示框 -->
          <div class="prompt-box-back-img">
            <!-- 提示框文字 用户联系方式 -->
            <div v-if="this.Collection == 0" class="flex-even">
              <div class="prompt-title font-we-bold">
                您暂未设置联系电话,
                <br />请先设置
              </div>
              <div class="disp-ju-center wed-100">
                <img
                  @click="Numbr"
                  class="cancel-img"
                  src="@/assets/home/qushezhiX2.png"
                />
                <img
                  class="cancel-img"
                  src="@/assets/home/quxiaoX2.png"
                  alt
                  style="margin-left: 0.306667rem"
                  @click="remvedia"
                />
              </div>
            </div>
            <!-- 提示框文字 用户交易密码 -->
            <div
              v-if="this.Collection == 1 && this.number == 1"
              class="flex-even"
            >
              <div class="prompt-title font-we-bold">
                您暂未设置资金密码，
                <br />请先设置
              </div>
              <div class="disp-ju-center wed-100">
                <!-- 未设置资金密码 -->
                <router-link to="/tranpass">
                  <img class="cancel-img" src="@/assets/home/qushezhiX2.png" />
                </router-link>
                <img
                  class="cancel-img"
                  src="@/assets/home/quxiaoX2.png"
                  alt
                  style="margin-left: 0.306667rem"
                  @click="remvedia"
                />
              </div>
            </div>
            <!-- 提示框文字 用户收款银行 -->
            <div
              v-if="this.Collection == 2 && this.number == 2"
              class="flex-even"
            >
              <div class="prompt-title font-we-bold">
                您暂未设置收款方式,
                <br />请先设置
              </div>
              <div class="disp-ju-center wed-100">
                <img
                  @click="Setup"
                  class="cancel-img"
                  src="@/assets/home/qushezhiX2.png"
                />
                <img
                  class="cancel-img"
                  src="@/assets/home/quxiaoX2.png"
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
  </div>
</template>

<script>
import { mapMutations } from "vuex";
import {
  GettoKeen,
  MerchantList,
  ListofSellers,
  ListTeLephone,
  ValidationTelephone,
  ValidityValidity,
} from "../api/business";
export default {
  name: "app",
  data() {
    return {
      istoken: "",
      // 用户token
      token: "",
      commission: 10040044,
      // 判断充值和賣出不同的显示内容
      Hideshow: true,
      // 判断遮罩层是否显示
      dialogVisib: false,
      infois: false,
      // 充值 卖出 图片切换
      switchimg: true,
      // 判断用户是否把收款信息填写完毕
      info: false,
      // 判断是否显示更多
      opup: false,
      // 充值商户数据
      data: "",
      // 判断用户是否输入手机号
      Collection: 0,
      // 判断用户是否输入银行卡
      number: 0,
      count: 0,
      isLoading: false,
      // 用户卖出的数据
      sale: "",
      // 判断用户是否设置了资金密码
      password: true,
      hometoken: "",
      showtoken: "",
    };
  },

  methods: {
    onRefresh() {
      this.udaer();
      this.updatasale();
      this.isLoading = false;
    },
    ...mapMutations(["assignment"]),
    toThousandsNoZero: function (num) {
      return num
        ? (isNaN(parseFloat(num.toString().replace(/,/g, "")))
            ? 1
            : parseFloat(num.toString().replace(/,/g, ""))
          )
            .toFixed(2)
            .toString()
            .replace(/(\d)(?=(\d{3})+\.)/g, function ($0, $1) {
              return $1 + ",";
            })
        : "";
    },
    // 点击卖出显示卖出的内容
    sellout() {
      // 切换显示隐藏
      this.Hideshow = false;
      this.switchimg = false;
    },
    Recharge() {
      // 切换显示隐藏
      this.Hideshow = true;
      this.switchimg = true;
    },
    // 点击显示遮罩层
    handleClose() {
      if (this.infois) {
        this.dialogVisib = false;
      } else {
        this.dialogVisib = true;
      }
      if (this.Collection == 0) {
        this.dialogVisib = false;
      } else if (this.Collection == 1 && this.number == 1) {
        this.dialogVisib = true;
      }
    },
    // 关闭遮罩层
    remvedia() {
      this.dialogVisib = false;
    },
    // 去设置跳转
    Setup() {
      this.$router.push("/User");
    },
    // 跳转到用户手机号页面
    Numbr() {
      this.$router.push("/Number");
    },
    // 点击更多弹出电话和收款保存页面
    Popup() {
      this.opup = !this.opup;
    },
    // 点击跳转到充值金额页面
    async dialogVisibl(number, data, id, index) {
      localStorage.setItem("index", index);
      localStorage.setItem("data", JSON.stringify(data));
      localStorage.setItem("id", id);
      this.dialogVisib = true;
      if (this.infois) {
        this.$router.push({
          path: "/Recharge",
          name: "Recharge",
          params: {
            data,
            index,
            id,
          },
        });
      }
    },
    // 点击跳转到出售金额页面
    async dialogVisibls(data, id, index) {
      localStorage.setItem("index", index);
      localStorage.setItem("data", JSON.stringify(data));
      localStorage.setItem("id", id);
      this.dialogVisib = true;
      if (this.infois) {
        this.$router.push({
          path: "/Buyersell",
          name: "Buyersell",
          params: {
            data,
            index,
            id,
          },
        });
      }
    },
    getuptoken() {
      // 获取新的token
      GettoKeen(this.$route.query.t).then((res) => {
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
              this.udaer();
              this.updatasale();
              this.updata();
              this.postuser();
              setTimeout(() => {
                this.upindex();
              }, 100);
            });
          }
        }
      });
    },
    async udaer() {
      var index = this.$store.state.token;
      if (index) {
        // this.$store.commit("assignment", 0);
      } else {
        this.$store.commit("assignment", this.$route.query.t);
      }
      console.log("用户的token值");
      console.log(this.$store.state.token);
      await MerchantList(this.hometoken).then((res) => {
        console.log("获取商家列表");
        console.log(res);
        this.data = res.data.data;
      });
    },
    // 获取卖出商家列表
    async updatasale() {
      await ListofSellers().then((res) => {
        console.log("获取卖出商家列表");
        console.log(res);
        this.sale = res.data.data;
      });
    },
    async upindex() {
      await ValidityValidity(this.hometoken).then((res) => {
        console.log("检验游戏端用户合法性");
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
        localStorage.setItem("SARO", res.data.data.saro_quantity / 10000);
        if (res.data.data.user_name.indexOf("@") != -1) {
          localStorage.setItem("mailbox", 1);
        } else {
          localStorage.setItem("mailbox", 0);
        }
      });
    },
    async postuser() {
      await ValidationTelephone(this.hometoken).then((res) => {
        console.log("效验用户电话银行卡信息");
        console.log(res);
        if (res.data.message == "用户未填写手机信息") {
          this.Collection = 0;
        } else if (res.data.message == "用户未设置交易密码") {
          this.Collection = 1;
          this.number = 1;
        } else if (res.data.message == "用户未填写银行卡信息") {
          this.Collection = 2;
          this.number = 2;
        }
        // 效验成功
        if (res.data.code == 1) {
          this.infois = true;
          this.dialogVisib = false;
          this.password = false;
        }
      });
    },
    async updata() {
      await ListTeLephone(this.hometoken).then((res) => {
        console.log("获取用户电话和银行卡信息");
        console.log(res);
        localStorage.setItem("phone", res.data.data.userPhone);
        localStorage.setItem("userid", res.data.data.userId);
      });
    },
    Myrechclick() {
      // this.$store.commit("SET_KEEP_ALIVE", ["aboutList"]); // 将缓存的路由添加
      this.$router.push({
        path: "/Myrech",
        name: "Myrech",
      });
    },
    // 跳回游戏
    routerTo() {
      window.location.href = 'uniwebview://game?action="close"';
    },
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      if (from.path.indexOf("aboutList") > -1) {
        // vm.$store.commit("SET_KEEP_ALIVE", []);
      }
    });
  },
  created() {
    this.getuptoken();
  },
  mounted() {
    localStorage.setItem("first", 0);
    if (localStorage.getItem("judge") == 1) {
      this.$nextTick(() => {
        this.udaer();
        this.updatasale();
        this.updata();
        this.postuser();
        setTimeout(() => {
          this.upindex();
        }, 100);
      });
    }
  },
};
</script>
<style>
* {
  padding: 0;
  margin: 0;
}

.active {
  color: red;
}

.back-imagees {
  overflow: scroll;
  position: fixed;
  width: 100%;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  margin: 0 auto;
  z-index: -1;
  background-image: url("../assets/home/back1.png");
  background-repeat: no-repeat;
  background-size: cover;
}

.order-color {
  padding: 0.2rem;
  margin: -0.2rem;
  color: #fae9a6;
  font-size: 26px;
}

.order-img {
  width: 0.693333rem;
  height: 0.56rem;
}

.back-color {
  background-color: #b0703b;
  height: 100vh;
  border-radius: 0.133333rem;
  opacity: 1;
  box-shadow: 0px 0px 3px 5px rgba(141, 83, 43, 0.45) inset;
  /* box-shadow: 0px 0px 0.133333rem #3d3d3d inset; */
  margin: auto;
}

.Merchant-back-img {
  background-image: url("../assets/home/ezzkX2.png");
  background-repeat: no-repeat;
  position: relative;
  background-size: 100% 100%;
}

.icon-image {
  width: 1.253333rem;
  height: 1.12rem;
  position: absolute;
  left: -0.893333rem;
  top: -0.733333rem;
}

.Merchant-title {
  font-size: 30px;
  color: #5d3129;
}

.Merchant-quantity {
  font-size: 26px;
  color: #5d3129;
}

.Merchant-number {
  font-size: 24px;
  border-radius: 0.093333rem;
  background-color: #d9cb95;
  padding: 0 0.133333rem;
  margin-left: 0.093333rem;
  box-sizing: border-box !important;
}

.Merchant-Price {
  font-size: 32px;
  color: #d14330;
}

.Recharge {
  line-height: 0.493333rem;
  text-align: center;
  font-weight: bold;
  width: 1.36rem;
  height: 0.493333rem;
  color: #feda09;
  font-size: 28px;
  background: #7abd37;
  box-shadow: 0px -1px 2px 0px rgba(255, 255, 255, 0.3) inset,
    -1px -2px 0px rgba(250, 255, 116, 0.8);
}

.popContainer {
  z-index: 100;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
}

.prompt-box-back-img {
  background-image: url("../assets/home/e1d.png");
  background-repeat: no-repeat;
  width: 6.213333rem;
  height: 4.16rem;
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-size: 100%;
}

.prompt-title {
  display: flex;
  justify-content: center;
  color: #5d3129;
  font-size: 32px;
}

.flex-even {
  justify-content: space-evenly;
  flex-direction: column;
  align-items: center;
  display: flex;
  height: 100%;
}

.cancel-img {
  width: 1.92rem;
  height: 0.666667rem;
}

.backgr-img {
  background-image: url("../assets/home/edi.png");
  background-repeat: no-repeat;
  background-size: contain;
  width: 3.573333rem;
  height: 3.093333rem;
  position: absolute;
  right: -10%;
  z-index: 10;
  top: 60%;
}

.bord {
  width: 80%;
  border-top: 1px dashed #cb8f55;
  height: 1px;
  overflow: hidden;
}

.space-even {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  width: inherit;
  align-items: center;
}

.aback-color {
  color: #5e3229;
  font-size: 34px;
}

.post-relasd {
  position: relative;
}

.text-show {
  text-shadow: 1px 1px 1px #5e3229, 1px -1px 1px #5e3229, 1px -1px 1px #5e3229,
    1px -1px 1px #5e3229;
}

.home-top {
  width: 98.5%;
  margin-left: 0.066667rem;
  background-color: #d19754;
  height: 0.88rem;
  font-size: 0.386667rem;
  color: #5d3129;
  font-weight: bold;
}
</style>