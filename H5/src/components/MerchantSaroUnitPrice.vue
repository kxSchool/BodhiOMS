<template>
  <div>
    <!-- 背景图片 -->
    <NavBar></NavBar>

    <Rearge title="SARO单价设置"></Rearge>
    <div class="Numberimg"></div>
    <div class="from-paddin" style="padding: 0.733333rem !important">
      <form action="">
        <div class="userColor">SARO用户充值单价(CNY)</div>
        <input
          type="number"
          class="user-input"
          v-model="FormObj.UnitPrice"
          style="color: #5e3229; font-size: 16px; font-weight: bold"
        />
        <div class="userColor">SARO用户卖出单价(CNY)</div>
        <input
          type="number"
          class="user-input"
          v-model="FormObj.sellout"
          style="color: #5e3229; font-size: 16px; font-weight: bold"
        />
      </form>
    </div>
    <van-popup v-model="showis" position="bottom" :style="{ height: '50%' }">
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
              <div class="prompt-title font-we-bold">资金密码错误，请重试</div>
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
    <img src="../assets/home/bcanXX2.png" class="submit" @click="hrefGo" />
  </div>
</template>

<script>
import { mapMutations } from "vuex";
import { BusinessValidateFundassword, ModifyUnitPrice } from "../api/user";
import { ListTeLephone } from "../api/business";
export default {
  data() {
    return {
      Collectionss: false,
      valuehtmo: "请输入资金密码，以验证身份",
      value: "",
      showKeyboard: true,
      errorInfo: "",
      showis: false,
      FormObj: {
        // 充值单价
        UnitPrice: "",
        // 用户卖出单价
        sellout: "",
      },
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
    // 获取商家自身信息
    async Mycode() {
      await ListTeLephone().then((res) => {
        console.log("获取商家的充值和卖出单价");
        console.log(res);
        if (res.data.code == 1) {
          this.FormObj.UnitPrice = res.data.data.rechargeUnitPrice;
          this.FormObj.sellout = res.data.data.recoveryUnitPrice;
          return;
        }
        this.$message({
          message: res.data.message,
          type: "error",
          center: true,
        });
      });
    },
    remvediass() {
      this.value = "";
      this.Collectionss = false;
      this.showKeyboard = true;
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
    ...mapMutations["contact"],
    async axiospost() {
      let postdata = {
        rechargeUnitPrice: this.FormObj.UnitPrice,
        recoveryUnitPrice: this.FormObj.sellout,
      };
      await ModifyUnitPrice(postdata).then((res) => {
        console.log("商家修改充值和卖出单价");
        console.log(res);
        if (res.data.code == 1) {
          this.showis = false;
          this.$message({
            message: "修改单价成功",
            type: "success",
            center: true,
          });
          this.$router.push("/Merchan");
        }
      });
      return;
    },
    hrefGo() {
      if (this.FormObj.UnitPrice && this.FormObj.sellout) {
        // 下单弹出密码验证
        this.value = "";
        this.showis = true;
        return;
      }
      this.$message({
        message: "请补全充值和卖出单价",
        type: "error",
        center: true,
      });
    },
  },
  mounted() {
    this.Mycode();
  },
};
</script>
<style scoped>

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
  margin-bottom: 0.626667rem;
  height: 1.146667rem;
  width: 100%;
  box-sizing: border-box;
  border-radius: 0.106666rem;
}

.submit {
  width: 5.266667rem;
  height: 1.4rem;
  display: block;
  position: absolute;
  bottom: 2.133333rem;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>