<template>
  <div>
    <NavBar></NavBar>

    <!-- 背景图片 -->
    <div style="height: 1.066667rem">
      <div class="disp-align-center" style="height: 1.066667rem">
        <van-icon @click="clickto" size="20" name="arrow-left" style="position: absolute; left: 0.266666rem" />
        <div class="margin-auto font-we-bold title" style="color: #2b2c2f">
          忘记资金密码
        </div>
      </div>
    </div>
    <div class="collection-backimg"></div>
    <div class="from-paddin">
      <div class="userColor" style="
        color: #5E3229;
        font-size: 19px;
          margin-top: 0.8rem;
          text-align: center;
          margin-bottom: 0.666667rem;
        ">
        手机验证码
      </div>
      <div style="position: relative;
            text-align: center;
      ">
        <input onkeyup="this.value=this.value.replace(/[, ]/g,'')" type="text" placeholder="输入手机验证码" maxlength="15"
          v-model="books" style="
            height: 1.36rem;
            font: icon;
            width:92%;
            padding: 0 0 0 0.266667rem;
            box-sizing: border-box;
            background: #d7c596 !important;
border: 1px solid #fff4d4 !important;
          " />

        <div class="disp-flex" style="
            position: absolute;
            right: 5%;
            top: 50%;
            transform: translate(0, -50%);
            align-items: center;
            color: #5e3229;
            font-size: 15.999975px;
            font-weight: bold;
          ">
          <div style="
              margin-right: 0.133333rem;
              border-right: 1.5px solid #5d3129;
              height: 0.533333rem;
            "></div>
          <van-grid clickable v-show="times">
            <van-grid-item text="获取验证码" @click="acquire" style="margin-right: .213333rem;" />
          </van-grid>
          <van-count-down v-show="!times" style="
              color: #5e3229;
              font-size: 15.999975px;
              font-weight: bold;
              margin-top: 0.066667rem;
            " ref="countDown" :time="time" :auto-start="false" format="ss秒" @finish="finish" />
        </div>
      </div>

      <div @click="inlanbolck" class="disp-ju-center">
        <img src="../assets/home/xiayibu.png" style="
        margin-top: 1.813333rem;
        width: 5.266667rem;
        height: 1.453333rem;
      " alt="">
      </div>

      <!-- 数字键盘 -->
    </div>
  </div>
</template>

<script>
  import { Dialog } from "vant";
  import { GetVerificationCode, ListTeLephone } from "../api/business";
  import { ValidationCode } from "../api/user";
  export default {
    data() {
      return {
        times: true,
        time: 60 * 1000,
        Collection: false,
        value: "",
        showKeyboard: true,
        books: "",
        contactnumber: "",
      };
    },
    methods: {
      Setup() {
        this.$router.push("/cellhone");
      },
      remvedia() {
        this.Collection = false;
        this.value = "";
        this.showKeyboard = true;
      },
      clickto() {
        this.$router.back();
      },
      fonri() { },
      // 点击获取验证码 这个地方暂时有问题
      async acquire() {
        let str = this.contactnumber.replace(/\s*/g, "");
        this.contactnumber = str.trim(this.contactnumber);
        let acquiredata = {
          userPhone: localStorage.getItem("onPhone"),
        };
        await GetVerificationCode(acquiredata).then((res) => {
          console.log(res);
          if (res.data.code != 1) {
            this.$message({
              message: res.data.message,
              type: "error",
              center: true,
            });
            return;
          }
          this.$message({
            message: "发送成功",
            type: "success",
            center: true,
          });
          this.$refs.countDown.start();
          this.times = false;
        });
      },
      finish() {
        this.times = true;
        this.$refs.countDown.reset();
      },
      // 调用手机号接口
      async number() {
        await ListTeLephone().then((res) => {
          this.contactnumber = res.data.data.userPhone;
        });
      },
      async inlanbolck() {
        if (this.books.length == 6) {
          let bolckdata = {
            code: this.books,
            userPhone: localStorage.getItem("onPhone"),
          };
          await ValidationCode(bolckdata).then((res) => {
            console.log("效验验证码");
            console.log(res);
            if (res.data.code != 1) {
              this.$message({
                message: res.data.message,
                type: "error",
                center: true,
              });
              this.books = "";
              return;
            }
            this.$message({
              message: "验证成功",
              type: "success",
              center: true,
            });
            //   });
            //   成功后跳转
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
        } else {
          this.$message({
            message: "请输入6位数验证码",
            type: "error",
            center: true,
          });
          this.books = "";
        }
      },
    },
    mounted() {
      this.number();
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
    font-size: 0.426667rem !important;
    color: #5e3229;
    font-weight: bold;
  }

  input::-moz-placeholder {
    font-size: 32.0.426667rem !important;
    color: #5e3229;
    font-weight: bold;

  }

  input:-moz-placeholder {
    font-size: 32.0.426667rem !important;
    color: #5e3229;
    font-weight: bold;

  }

  input:-ms-input-placeholder {
    font-size: 0.426667rem !important;
    color: #5e3229;
    font-weight: bold;

  }

  .userColor {
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

  .van-grid-item__content--center {
    display: block !important;
    padding: 0 !important;
    background-color: transparent !important;
  }

  .van-grid-item__text {
    color: #5e3229 !important;
    font-weight: bold;
    font-size: 32px !important;
  }

  .van-grid-item {
    flex-basis: auto !important;
  }

  [class*="van-hairline"]::after {
    content: none !important;
  }
</style>