<template>
  <div>
    <NavBar></NavBar>

    <!-- 背景图片 -->
    <div style="height: 1.066667rem">
      <div class="disp-align-center" style="height: 1.066667rem">
        <van-icon
          size="20"
          name="arrow-left"
          style="position: absolute; left: 0.266666rem"
          @click="routerTo"
        />
        <div class="margin-auto font-we-bold title" style="color: #2b2c2f">
          资金密码
        </div>
      </div>
    </div>
    <div class="collection-backimg"></div>
    <div class="from-paddin">
      <div class="userColor" style="margin-top: 0.8rem; text-align: center">
        {{ valuehtmo }}
      </div>
      <van-password-input
        v-if="info"
        :value="value"
        :length="6"
        :gutter="10"
        :focused="showKeyboard"
        @focus="showKeyboard = true"
      />
      <van-password-input
        v-else
        :value="value1"
        :length="6"
        :gutter="10"
        :focused="showKeyboard"
        @focus="showKeyboard = true"
        :error-info="errorInfo"
      />
      <!-- 数字键盘 -->
      <van-number-keyboard
        v-if="info"
        v-model="value"
        :show="showKeyboard"
        :maxlength="6"
      />
      <van-number-keyboard
        v-else
        v-model="value1"
        :show="showKeyboard"
        :maxlength="6"
      />
    </div>
  </div>
</template>

<script>
import { SetFundPassword } from "../api/business";
export default {
  data() {
    return {
      valuehtmo: "请设置输入资金密码",
      passsword: "",
      value: "",
      value1: "",
      errorInfo: "",
      showKeyboard: true,
      isOriginHeight: false, //当前高度是否仍等于默认高度
      // 联系电话
      infoshow: false,
      info: true,
      confirmPwd: "",
    };
  },
  watch: {
    value(value) {
      if (value.length === 6) {
        this.valuehtmo = "请再次填写以确认";
        this.passsword = value;
        this.value = "";
        this.info = false;
      }
    },
    value1(value) {
      if (value.length === 6) {
        this.confirmPwd = value;
        if (this.passsword === this.confirmPwd) {
          localStorage.setItem("pass", this.passsword);
          this.$message({
            message: "设置成功",
            type: "success",
            center: true,
          });
          this.password();
          if (this.$route.params.id == 1) {
            this.$router.push({
              path: "/Merchan",
              name: "Merchan",
              params: {
                word: 1,
              },
            });
          } else {
            this.$router.push("/home");
          }
        } else {
          this.errorInfo = "俩次密码输入不一致 请重新输入";
          this.value1 = "";
        }
      }
    },
  },

  methods: {
    // 跳回首页
    routerTo() {
      this.$router.back();
    },
    hrefGo() {},
    async password() {
      let Godata = {
        type: 1,
        // 新密码
        newPwd: this.passsword,
        confirmPwd: this.confirmPwd,
      };
      await SetFundPassword(Godata).then((res) => {
        console.log(res);
      });
    },
  },
  mounted() {},
};
</script>
<style>
.collection-backimg {
  width: 100%;
  height: 100%;
  background-size: cover !important;
  z-index: -10;
  position: fixed;
  background-image: url("../assets/home/back.png");
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