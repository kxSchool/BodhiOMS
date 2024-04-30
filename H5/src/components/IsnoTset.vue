<template>
  <div>
    <NavBar></NavBar>

    <!-- 背景图片 -->
    <div style="height: 1.066667rem">
      <div class="disp-align-center" style="height: 1.066667rem">
        <van-icon @click="clickto" size="20" name="arrow-left" style="position: absolute; left: 0.266666rem" />
        <div class="margin-auto font-we-bold title" style="color: #2b2c2f">
          修改资金密码
        </div>
      </div>
    </div>
    <div class="collection-backimg"></div>

    <div class="from-paddin">
      <div class="userColor" style="margin-top: 0.8rem; text-align: center">
        {{ valuehtmo }}
      </div>
      <van-password-input :value="value" :length="6" :gutter="10" :focused="showKeyboard"
        @focus="showKeyboard = true" />
      <!-- 数字键盘 -->
      <van-number-keyboard v-model="value" :show="showKeyboard" :maxlength="6" />
      <!-- 提示框文字 用户收款银行 -->
      <!-- 提示框 -->
      <div v-if="this.Collection" class="prompt-box-back-img" style="position: absolute">
        <div class="flex-even">
          <div class="prompt-title font-we-bold">资金密码错误，请重试</div>
          <div class="disp-ju-center wed-100">
            <img @click="Setup" class="cancel-img" src="@/assets/home/wangji.png" style="width: auto;height: .693333rem;"/>
            <img class="cancel-img" src="@/assets/home/chonshi.png" alt style="margin-left: 0.306667rem;width: auto;height: .693333rem;"
              @click="remvedia" />
          </div>
        </div>
      </div>
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
    </div>
    <div @click="passswordfor" class="font-we-bold title" style="color: #d14330 !important; font-size: 16px;text-align: end;margin-right: .453333rem;margin-top: .546667rem;">
      忘记密码?
    </div>
  </div>
</template>

<script>
  import { BusinessValidateFundassword } from "../api/user";
  export default {
    data() {
      return {
        Collectionesd: false,
        Collection: false,
        valuehtmo: "请输入资金密码，以验证身份",
        value: "",
        showKeyboard: true,
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
            // console.log(res);
            if (res.data.code != 1) {
              this.Collection = true;
              return;
            }
            this.$message({
              message: "验证成功",
              type: "success",
              center: true,
            });
            if (this.$route.params.id == 1) {
              this.$router.push({
                path: "/tranpass",
                name: "tranpass",
                params: {
                  id: 1,
                },
              });
            } else {
              this.$router.push({
                path: "/tranpass",
                name: "tranpass",
              });
            }
          });
        }
      },
    },

    methods: {
      mehode() {
        this.Collectionesd = false;
      },
      passswordfor() {
        if (localStorage.getItem("mailbox") == 1) {
          this.Collectionesd = true;
          return;
        }

        if (this.$route.params.id == 1) {
          this.$router.push({
            path: "/cellhone",
            name: "cellhone",
            params: {
              id: 1,
            },
          });
        } else {
          this.$router.push({
            path: "/cellhone",
            name: "cellhone",
          });
        }
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
      remvedia() {
        this.Collection = false;
        this.value = "";
        this.showKeyboard = true;
      },
      clickto() {
        this.$router.back();
      },
    },
    mounted() {
      // console.log(this.$route.params.id);
    },
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