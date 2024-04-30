<template>
  <!-- 头部组件 -->
  <div>
    <NavBar></NavBar>
    <Rearge title="添加收款银行卡"></Rearge>
    <!-- 背景图片 -->
    <div
      class=" Numberimg"
      style="background-size: cover !important"
    ></div>
    <van-pull-refresh
      v-model="isLoading"
      success-text="刷新成功"
      @refresh="onRefresh"
    >
      <!-- 用户姓名 -->
      <form class="from-paddin" style="padding: 0.733333rem !important">
        <div class="userColor">姓名</div>
        <input
          disabled="disabled"
          @blur="userinput(fromins.name)"
          type="text"
          placeholder="请填写姓名"
          class="user-inputs"
          v-model="fromins.name"
          maxlength="10"
          style="
            box-shadow: 0 0.013333rem 0.013333rem #6e716e inset,
              0 -0.013333rem 0 #6e716e inset;
          "
        />
        <div class="userColor">银行卡号</div>
        <input
          type="text"
          placeholder="请输入银行卡号"
          class="user-inputs"
          v-model="fromins.Cardnumber"
          id="number"
          @blur="bankcar(fromins.Cardnumber)"
          @input="Cardnum"
          style="
            box-shadow: 0 0.013333rem 0.013333rem #6e716e inset,
              0 -0.013333rem 0 #6e716e inset;
          "
        />
        <div class="userColor">开户银行</div>
        <input
          type="text"
          placeholder="请填写开户银行"
          class="user-inputs"
          id="branch"
          v-model="fromins.account"
          @blur="userin(fromins.account)"
          @input="userinco(fromins.branch)"
          style="
            box-shadow: 0 0.013333rem 0.013333rem #6e716e inset,
              0 -0.013333rem 0 #6e716e inset;
          "
        />
        <div class="userColor">开户支行</div>
        <input
          type="text"
          placeholder="请填写开户支行"
          class="user-inputs"
          id="bank"
          v-model="fromins.branch"
          @blur="branch(fromins.branch)"
          @input="userinco(fromins.branch)"
          style="
            box-shadow: 0 0.013333rem 0.013333rem #6e716e inset,
              0 -0.013333rem 0 #6e716e inset;
          "
        />
        <div style="color: #d14330; font-size: 0.4rem" class="font-we-bold">
          银行卡名字必须跟实名认证一致，若充值的卡号
          跟提现卡号不一致，会导致提现时无法提取
        </div>
        <img
          v-if="!determine"
          src="@/assets/home/bcanX2.png"
          class="submiteindex"
        />
        <img
          v-else
          src="@/assets/home/baocunx2.png"
          class="submiteindex"
          @click="hrefGo"
        />
      </form>
      <van-popup v-model="showis" position="bottom" :style="{ height: '50%' }">
        <div>
          <div class="collection-backimg"></div>
          <div class="from-paddin">
            <div
              class="userColor"
              style="
                text-align: center;
                color: #5d3129;
                font-size: 17px;
                margin-bottom: 0.52rem;
                padding-top: 0.666667rem;
                margin-bottom: 0.52rem;
              "
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
                    @click="remvedia"
                  />
                </div>
              </div>
            </div>
          </div>
          <router-link to="/cellhone">
            <div
              style="
                color: #d14330;
                font-size: 15px;
                margin-top: 0.4rem;
                text-align: right;
                font-weight: bold;
                padding-right: 0.533333rem;
              "
            >
              忘记密码?
            </div>
          </router-link>
        </div>
      </van-popup>
    </van-pull-refresh>
  </div>
</template>

<script>
import { mapMutations } from "vuex";
import { ListTeLephone } from "../api/business";
import { SetUpPhone, BusinessValidateFundassword } from "../api/user";
export default {
  data() {
    return {
      Collection: false,
      valuehtmo: "请输入资金密码，以验证身份",
      value: "",
      showKeyboard: true,
      showis: false,

      isLoading: false,
      fromins: {
        // 姓名
        name: "",
        // 开户银行卡号
        Cardnumber: "",
        // 开户银行
        account: "",
        // 开户支行
        branch: "",
      },
      // 判断用户是否填写收款信息
      determine: false,
      // 判断保存是否成功
      // 姓名
      nameinfo: false,
      asinsd: false,
    };
  },
  methods: {
    onRefresh() {
      this.bankcard();
      this.isLoading = false;
    },
    // 姓名检测
    userinput(name) {
      this.nameinfo = true;
    },
    // 开户银行卡号检测
    bankcar(name) {
      let num = name.replace(/[^\x00-\xff]/g, "xx").length;
      if (num == 0) {
        this.fromins.Cardnumber = "";
        this.$message({
          center: true,
          message: "请输入银行卡号",
        });
      }
    },
    // 开户支行检测
    branch(name) {
      let num = name.replace(/[^\x00-\xff]/g, "xx").length;
      let reg = /^[\u4E00-\u9FA5]+$/; //正则匹配规则
      if (!reg.test(name) && num != 0) {
        this.fromins.branch = "";
        this.$message({
          center: true,
          message: "请输入中文",
        });
        this.determine = false;
        return;
      }
      if (name.length != 0) {
        this.determine = true;
      }
    },
    // 开户银行检测
    userin(name) {
      let num = name.replace(/[^\x00-\xff]/g, "xx").length;
      let reg = /^[\u4E00-\u9FA5]+$/; //正则匹配规则
      if (!reg.test(name) && num != 0) {
        this.fromins.account = "";
        this.$message({
          center: true,
          message: "请输入中文",
        });
        this.determine = false;
        return;
      }
      if (name.length != 0) {
        this.determine = true;
      }
    },
    // 判断是否显示保存按钮
    userinco() {
      if (
        this.fromins.name &&
        this.fromins.Cardnumber &&
        this.fromins.account &&
        this.fromins.branch
      ) {
        this.determine = true;
      } else {
        this.determine = false;
      }
    },
    // 银行卡号
    Cardnum() {
      var numReg = /^[0-9]+$/;
      var numRe = new RegExp(numReg);
      if (
        !numRe.test(this.fromins.Cardnumber) &&
        this.fromins.Cardnumber.length != 0
      ) {
        this.$message({
          center: true,
          message: "请输入数字",
        });
        this.fromins.Cardnumber = "";
      } else {
        this.asinsd = true;
      }
      if (
        this.fromins.Cardnumber &&
        this.fromins.account &&
        this.fromins.name &&
        this.fromins.branch
      ) {
        this.determine = true;
      } else {
        this.determine = false;
      }
    },
    ...mapMutations["increment"],
    // 点击保存用户输入的银行卡信息
    async hrefGo() {
      this.value = "";
      // 下单弹出密码验证
      this.showis = true;
    },
    // 调用银行卡接口
    async bankcard() {
      await ListTeLephone().then((res) => {
        console.log("获取用户电话和银行卡信息");
        console.log(res);
        this.contactnumber = res.data.data;
        // 姓名
        this.fromins.name = res.data.data.bankFavour;
        // 银行卡号
        this.fromins.Cardnumber = res.data.data.userBankCard;
        // 开户银行
        this.fromins.account = res.data.data.userBank;
        // 开户支行
        this.fromins.branch = res.data.data.userBankBranch;
        // console.log(this.contactnumber);
      });
      if (
        this.fromins.name &&
        this.fromins.Cardnumber &&
        this.fromins.account &&
        this.fromins.branch
      ) {
        this.determine = true;
      } else {
        this.determine = false;
      }
    },
    async axiospost() {
      let postdata = {
        bankFavour: this.fromins.name,
        userBank: this.fromins.account,
        userBankBranch: this.fromins.branch,
        userBankCard: this.fromins.Cardnumber,
      };
      await SetUpPhone(postdata).then((res) => {
        console.log("用户设置电话和银行卡信息");
        console.log(res);
        if (res.data.message == "更新成功") {
          this.$router.push({
            path: "/Home",
          });
          this.$store.commit("increment", 1);
        }
      });
    },
    Setup() {
      this.$router.push("/cellhone");
    },
    remvedia() {
      this.value = "";
      this.Collection = false;
      this.showKeyboard = true;
    },
  },

  mounted() {
    this.bankcard();
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
};
</script>
<style>
* {
  padding: 0%;
  margin: 0%;
}

input::-webkit-input-placeholder {
  font-size: 32px;
  color: #5e3229;
  opacity: 0.5;
  font-weight: bold;
}

input::-moz-placeholder {
  /* Mozilla Firefox 19+ */
  font-size: 32px;
  color: #5e3229;
  opacity: 0.5;
  font-weight: bold;
}

input:-moz-placeholder {
  /* Mozilla Firefox 4 to 18 */
  font-size: 32px;
  color: #5e3229;
  opacity: 0.5;
  font-weight: bold;
}

input:-ms-input-placeholder {
  /* Internet Explorer 10-11 */
  font-size: 32px;
  color: #5e3229;
  opacity: 0.5;
  font-weight: bold;
}

input {
  font-size: 32px;
  padding: 0 0.4rem;
  color: #5e3229;
  font-weight: bold;
  background: #d7c599;
  outline: none;
  border: 0.026667rem solid #fff5d4;
  letter-spacing: 3px;
}

.userColor {
  color: #5e3229;
  font-size: 32px;
  font-weight: bold;
}

.user-inputs {
  color: #5e3229;
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 0.666667rem;
  height: 1.253333rem;
  width: 100%;
  box-sizing: border-box;
  border-radius: 0.106667rem;
}

.submiteindex {
  width: 5.266667rem;
  height: 1.453333rem;
  display: block;
  margin: 1.413333rem auto;
}

.el-message__content {
  font-size: 30px;
}

.van-password-input__security {
  height: 1.2rem !important;
}

.Userimg {
  
  background-image: url("../assets/home/back.png") !important;
}


</style>