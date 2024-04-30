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
          联系电话
        </div>
      </div>
    </div>

    <div class="Numberimg"></div>
    <van-pull-refresh
      v-model="isLoading"
      success-text="刷新成功"
      @refresh="onRefresh"
      style="height: 100vh"
    >
      <div class="from-paddin" style="padding: 0.733333rem !important">
        <div class="userColor">联系电话</div>
        <input
          type="phone"
          placeholder="请填写您的联系电话"
          class="user-input"
          id="name"
          v-model.trim="contactnumber"
          style="font-weight: bold; color: #5d3129; font-size: 0.426667rem"
          oninput="if(value.length > 11)value = value.slice(0, 11)"
        />
        <img
          v-show="showinfo"
          src="../assets/home/bcanXX2.png"
          class="numberimg"
          @click="hrefGo"
        />
      </div>
    </van-pull-refresh>
  </div>
</template>

<script>
import { mapMutations } from "vuex";
import { SetUpPhone } from "../api/user";
import { ListTeLephone } from "../api/business";
export default {
  data() {
    return {
      defaultPhoneHeight: 0, //屏幕默认高度
      nowPhoneHeight: 0, //屏幕现在的高度
      showinfo: true,
      isOriginHeight: true, //当前高度是否仍等于默认高度
      // 联系电话
      contactnumber: "",
      isLoading: false,
    };
  },

  methods: {
    onRefresh() {
      this.number();
      this.isLoading = false;
    },
    ...mapMutations["contact"],
    // 保存用户输入的手机号
    async hrefGo() {
      if (Object.keys(this.contactnumber).length !== 11) {
        this.$message({
          center: true,
          message: "请输入正确手机号",
        });
        this.contactnumber = "";
      } else {
        let Godata = {
          userPhone: this.contactnumber,
          token: this.$store.state.token,
        };
        await SetUpPhone(Godata).then((res) => {
          console.log("用户设置电话和银行卡信息");
          console.log(res);
          if (res.data.message == "更新成功") {
            this.$router.push({
              path: '"/home"',
              name: "Describe",
              params: {
                dialogVisib: true,
              },
            });
            localStorage.setItem("phonese", this.contactnumber);

            this.$store.commit("contact", 1);
          }
        });
      }
    },
    // 跳回游戏
    routerTo() {
      this.$router.push("/home");
    },
    // 调用手机号接口
    async number() {
      await ListTeLephone().then((res) => {
        console.log("获取用户电话和银行卡信息");
        console.log(res);
        this.contactnumber = res.data.data.userPhone;
      });
    },
  },
  watch: {
    nowPhoneHeight: function () {
      if (this.defaultPhoneHeight != this.nowPhoneHeight) {
        //手机键盘被唤起了。
        this.showinfo = false;
      } else {
        //手机键盘被关闭了。
        this.showinfo = true;
      }
    },
    phone(newValue, oldValue) {
      // 监听
      this.phone =
        newValue.length > oldValue.length
          ? newValue
              .replace(/\s/g, "")
              .replace(/(\d{3})(\d{0,4})(\d{0,4})/, "$1 $2 $3")
          : this.phone.trim();
    },
  },
  created() {
    this.number();
  },
  mounted() {
    this.defaultPhoneHeight = window.innerHeight;
    window.onresize = () => {
      this.nowPhoneHeight = window.innerHeight;
    };
    this.number();
  },
  destroyed() {
    window.onresize = null;
  },
};
</script>
<style>


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