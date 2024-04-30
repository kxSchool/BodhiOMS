<template>
  <div>
    <NavBar></NavBar>

    <!-- 背景图片 -->
    <Rearge title="联系电话"></Rearge>
    <div class="Numberimg"></div>
    <van-pull-refresh
      v-model="isLoading"
      success-text="刷新成功"
      @refresh="onRefresh"
      style="height: 100vh !important; padding: 0.733333rem !important"
    >
      <div class="from-paddin" style="padding: 0 !important">
        <div class="userColor">联系电话1</div>
        <form action="">
          <input
            v-model.trim="phone"
            type="phone"
            placeholder="请输入手机号"
            maxlength="13"
            class="user-input Merchanimg"
          />
        </form>
      </div>
      <div
        class="from-paddin"
        style="padding: 0 !important; padding-top: 0.733333rem !important"
      >
        <div class="userColor">联系电话2</div>
        <form action="">
          <input
            v-model.trim="contactnumber1"
            type="phone"
            placeholder="设置您的建议联系电话（选填）"
            maxlength="13"
            class="user-input Merchanimg"
          />
        </form>
      </div>
      <div
        class="from-paddin"
        style="padding: 0 !important; padding-top: 0.733333rem !important"
      >
        <div class="userColor">联系电话3</div>
        <input
          v-model.trim="contactnumber2"
          type="phone"
          placeholder="设置您的建议联系电话（选填）"
          maxlength="13"
          class="user-input Merchanimg"
        />
      </div>
      <img
        v-show="showinfo"
        src="../assets/home/baocunx2.png"
        class="submit"
        @click="hrefGo"
        style="position: absolute; bottom: 20%"
      />
    </van-pull-refresh>
  </div>
</template>

<script>
import { mapMutations } from "vuex";
// 点击复制
import Clipboard from "clipboard";
import { SetUpPhone } from "../api/user";
import { ListTeLephone } from "../api/business";
export default {
  data() {
    return {
      isLoading: false,
      phone: "",
      contactnumber1: "",
      contactnumber2: "",
      defaultPhoneHeight: 0, //屏幕默认高度
      nowPhoneHeight: 0, //屏幕现在的高度
      showinfo: true,
    };
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
    contactnumber1(newValue, oldValue) {
      if (this.contactnumber1) {
        // 监听
        this.contactnumber1 =
          newValue.length > oldValue.length
            ? newValue
                .replace(/\s/g, "")
                .replace(/(\d{3})(\d{0,4})(\d{0,4})/, "$1 $2 $3")
            : this.contactnumber1.trim();
      }
    },
    contactnumber2(newValue, oldValue) {
      if (this.contactnumber2) {
        // 监听
        this.contactnumber2 =
          newValue.length > oldValue.length
            ? newValue
                .replace(/\s/g, "")
                .replace(/(\d{3})(\d{0,4})(\d{0,4})/, "$1 $2 $3")
            : this.contactnumber2.trim();
      }
    },
  },
  methods: {
    onRefresh() {
      this.naubm();
      this.isLoading = false;
    },
    ...mapMutations["contact"],
    async hrefGo() {
      if (this.phone.replace(/[^\d]/g, "").length == 11) {
        // 保存的手机号
        let Godata = {
          userPhone: this.phone,
          userPhone2: this.contactnumber1,
          userPhone3: this.contactnumber2,
        };
        await SetUpPhone(Godata).then((res) => {
          console.log("商家设置电话");
          console.log(res);
          if (res.data.message == "更新成功") {
            this.$router.push({
              path: '"/Merchan"',
              name: "Merchan",
              params: {
                dialogVisib: true,
              },
            });
            this.$store.commit("contact", 1);
          }
        });
      } else {
        this.$message({
          message: "请输入完整手机号",
          center: true,
          type: "warning",
        });
      }
    },
    async naubm() {
      // 调用保存手机号接口
      await ListTeLephone().then((res) => {
        console.log("获取用户电话和银行卡信息");
        console.log(res);
        if (res.data.message == "操作成功") {
          this.phone = res.data.data.userPhone;
          if (res.data.data.userPhone2) {
            this.contactnumber1 = res.data.data.userPhone2;
          } else {
            this.contactnumber1 = "";
          }
          if (res.data.data.userPhone3) {
            this.contactnumber2 = res.data.data.userPhone3;
          } else {
            this.contactnumber2 = "";
          }
        }
      });
    },
  },
  mounted() {
    this.defaultPhoneHeight = window.innerHeight;
    window.onresize = () => {
      this.nowPhoneHeight = window.innerHeight;
    };
    this.naubm();
  },
  destroyed() {
    window.onresize = null;
  },
};
</script>
<style scoped>
.Merchanimg {
  font-weight: bold !important;
  color: #5e3229 !important;
  font-size: 0.426667rem !important;
}

.user-input {
  margin-bottom: 0 !important;
}

.submit {
  width: 5.266667rem;
  height: 1.453333rem;
  display: block;
  position: absolute;
  bottom: 2.133333rem;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>