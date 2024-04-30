<template>
  <div>
    <!-- 首页背景图 -->
    <div class="back-imageinfo" style="height: auto">
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
            请付款
          </div>
        </div>
      </div>
      <van-pull-refresh
        v-model="isLoading"
        success-text="刷新成功"
        @refresh="onRefresh"
        style="padding: 0.76rem 0.533333rem"
      >
        <div class="disp-space-bet" style="color: #fae8a6">
          <!-- 请付款 -->
          <div class="font-we-bold">
            <div style="font-size: 20px; margin-bottom: 0.28rem">请付款</div>
            <!-- 付款超时时间 -->
            <div style="font-size: 16px; width: 5.72rem">
              请在<span></span>
              <span v-show="this.timeDay">{{ this.timeDay }}天</span>
              <span v-show="this.timeHr">{{ this.timeHr }}时</span>
              <span v-show="this.timemin">{{ this.timemin }}分钟</span>
              <span>{{ this.timeSec }}秒</span>内完成付款，超时将自动取消交易
            </div>
          </div>
          <div class="disp-dir-cen" @click="number">
            <img
              src="../assets/home/lxdhX2.png"
              alt=""
              style="
                width: 1.226667rem;
                height: 1.186667rem;
                margin-bottom: 0.28rem;
              "
            />
            <div style="font-size: 15px">联系对方</div>
          </div>
        </div>
        <!-- 付款的详细信息 -->
        <div style="padding-top: 0.493333rem">
          <div
            class="bott-imges disp-space-bet"
            style="padding: 0.333333rem; box-sizing: border-box"
          >
            <img
              src="../assets/home/SARO@2x.png"
              style="width: 1.333333rem; height: 1.32rem"
            />
            <div class="font-we-bold" style="font-size: 15px">
              <div
                style="
                  display: flex;
                  justify-content: end;
                  color: #ffbe32;
                  font-size: 21px;
                "
              >
                ¥{{ this.datainfo.payAmount | amounswt }}
              </div>
              <div style="color: #fbe7a6; font-size: 14px">
                <span>单价:¥{{ this.datainfo.orderUnitPrice | amounswt }}</span>
                <span style="margin-left: 0.266667rem"
                  >数量:{{ this.datainfo.saroNumber | siwen }}SARO</span
                >
              </div>
            </div>
          </div>
          <!-- 底部的详细信息 -->
          <div class="botton">
            <div
              v-if="hide"
              class="border-bottom font-we-bold list"
              style="color: #d14330; font-size: 14px"
            >
              请使用本人账户向以下账户自行转账
            </div>
            <div
              class="border-bottom font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              付款方式
              <div style="color: #5d3129">银行卡</div>
            </div>
            <div
              class="border-bottom font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              收款人
              <div style="color: #5d3129">
                {{ this.datainfo.bankFavour }}
                <span
                  style="margin-left: 0.066667rem; color: #7abd37"
                  id="copy_text"
                  :data-clipboard-text="this.datainfo.bankFavour"
                  @click="handleCopyFun"
                  >复制</span
                >
              </div>
            </div>
            <div
              class="border-bottom font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              银行卡号
              <div style="color: #5d3129">
                {{
                  this.datainfo.userBankCard == "string"
                    ? null
                    : this.datainfo.userBankCard
                }}
                <span
                  style="margin-left: 0.066667rem; color: #7abd37"
                  id="copy_text"
                  :data-clipboard-text="this.datainfo.userBankCard"
                  @click="handleCopyFun"
                  >复制</span
                >
              </div>
            </div>
            <div
              class="border-bottom font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              开户银行
              <div style="color: #5d3129">
                {{ this.datainfo.userBank }}
              </div>
            </div>
            <div
              class="border-bottom bordr-solde font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              开户支行
              <div style="color: #5d3129">
                {{
                  this.datainfo.userBankBranch == "string"
                    ? null
                    : this.datainfo.userBankBranch
                }}
              </div>
            </div>
            <div
              class="border-bottom font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              卖家昵称
              <div style="color: #5d3129">
                {{ this.datainfo.nickname }}
              </div>
            </div>
            <div
              class="border-bottom font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              订单编号
              <div style="color: #5d3129">
                {{ this.datainfo.orderNoStr }}
                <span
                  style="margin-left: 0.066667rem; color: #7abd37"
                  id="copy_text"
                  :data-clipboard-text="this.datainfo.orderNoStr"
                  @click="handleCopyFun"
                  >复制</span
                >
              </div>
            </div>
            <div
              class="border-bottom font-we-bold disp-space-bet list"
              style="color: #b06f39; font-size: 15px"
            >
              下单时间
              <div style="color: #5d3129">
                {{ this.datainfo.orderTime | moment }}
              </div>
            </div>
          </div>
          <!-- 备注信息 -->
          <div v-if="hide">
            <div v-show="bottoninfo">
              <div
                style="color: #f3e2a9; font-size: 14px; margin-top: 0.386667rem"
                class="font-we-bold"
              >
                在转账过程中请勿备注充值SARO等信息，防止汇款被拦截，银行卡被冻结等问题
                <!-- 成功付款图片 -->
                <img
                  @click="isnick"
                  src="../assets/home/wycgfkX2.png"
                  style="
                    width: 4.213333rem;
                    height: 1.04rem;
                    margin: 0.506667rem auto;
                    display: block;
                  "
                />
              </div>
            </div>
          </div>
        </div>
        <!-- 提示遮罩层 -->
        <div v-show="cellphonenumber == true" style="display: none">
          <div
            :class="[cellphonenumber ? 'popContainer' : '']"
            @touchmove.prevent
          >
            <div
              class="prompt-box-back-imgse"
              v-show="cellphonenumber == true"
              style=""
            >
              <!-- 提示框文字 用户收款银行 -->
              <div class="flex-even">
                <div
                  class="prompt-title font-we-bold text-center"
                  style="flex-direction: column"
                >
                  <div class="text-center">联系电话</div>
                  <div style="margin-top: 0.333333rem">
                    {{ replace }}
                    <a
                      :href="'tel:' + replace"
                      style="
                        color: #71c738;
                        font-size: 16px;
                        font-weight: bold;
                        margin-left: 0.133333rem;
                      "
                      >拨打</a
                    >
                  </div>
                  <div style="margin-top: 0.333333rem" v-if="this.replace1">
                    {{ this.replace1 }}
                    <a
                      :href="'tel:' + replace1"
                      style="
                        color: #71c738;
                        font-size: 16px;
                        font-weight: bold;
                        margin-left: 0.133333rem;
                      "
                      >拨打</a
                    >
                  </div>
                  <div style="margin-top: 0.333333rem" v-if="this.replace2">
                    {{ this.replace2 }}
                    <a
                      :href="'tel:' + replace2"
                      style="
                        color: #71c738;
                        font-size: 16px;
                        font-weight: bold;
                        margin-left: 0.133333rem;
                      "
                      >拨打</a
                    >
                  </div>
                </div>
                <div class="disp-ju-center wed-100">
                  <img
                    class="cancel-img"
                    src="../assets/home/removbe.png"
                    style="width: 3.52rem"
                    @click="remvedia"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
        <div>
          <!-- 提示遮罩层 -->
          <div v-show="isinfo" @click="hibe($event)">
            <div :class="[trede ? 'popContainer' : '']">
              <!-- 确认付款图片凭证 -->
              <div class="PaidImages" ref="msk">
                <div class="disp-colu-cen font-we-bold">
                  <div style="color: #5d3129; font-size: 16px">确认已付款</div>
                  <div
                    style="color: #d14330; font-size: 15px; margin-top: 0.4rem"
                  >
                    若未付款点击确认已付款，平台核实后将有可能对您的账号进行禁用或封号处理
                  </div>
                  <div class="disp-colu-cen">
                    <div
                      style="
                        color: #5d3129;
                        font-size: 15px;
                        margin-top: 0.4rem;
                      "
                    >
                      请上传您的付款凭证
                    </div>
                    <label for="upload" class="uplades">
                      <input
                        style="display: none"
                        type="file"
                        class="fonts"
                        name=""
                        @change="afterRead"
                        ref="updata"
                        accept="image/*"
                        id="upload"
                      />

                      <img
                        v-show="src"
                        class="imese Pleabordr"
                        :src="src"
                        alt=""
                        srcset=""
                      />
                      <div
                        v-show="!src"
                        class="UploadPictures disp-ju-center"
                        style="margin-top: 0.4rem"
                      >
                        <img
                          style="height: 1.213333rem"
                          src="../assets/home/tjX2.png"
                          alt=""
                        />
                      </div>
                    </label>
                    <img
                      v-if="srcinfo == 0"
                      style="margin-top: 0.48rem"
                      class="confirmImges"
                      src="../assets/home/qrfkhX2.png"
                    />
                    <!-- 保存图片凭证上传到服务器 -->
                    <img
                      v-else
                      @click="voucher"
                      style="margin-top: 0.48rem"
                      class="confirmImges"
                      src="../assets/home/qrfklX2.png"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </van-pull-refresh>
    </div>
  </div>
</template>

<script>
let Base64 = require("js-base64").Base64;
import Clipboard from "clipboard";
import Recharge from "./Recharge.vue";
import {
  PaymentOrder,
  UploadAttachments,
  GetMerchantPhone,
} from "../api/business";
export default {
  components: { Recharge },
  props: {
    // 付款状态
    OrderStatus: {
      type: "",
      default: () => {
        return [
          {
            texts: "请付款",
            status: "请在29:59内完成付款，超时将自动取消交易",
          },
        ];
      },
    },
    // 商户电话
    telephone: {
      type: "",
      default: () => {
        return [
          {
            number: "15544466665",
          },
        ];
      },
    },
    // 用户购买金额和单价数量
    UserRecharge: {
      type: "",
      default: () => {
        return [
          {
            total: "1",
            UnitPrice: "1",
            quantity: "1",
          },
        ];
      },
    },
    // 判断是否显示隐藏一些字段
    hide: {
      type: Boolean,
      default: true,
    },
    // 卖家信息
    data: {
      type: "",
      default: () => {
        return [
          {
            itme: "付款方式",
            name: "王五四",
          },
          {
            itme: "收款人",
            name: "2",
            id: 1,
          },
          {
            itme: "银行卡号",
            name: "3",
            id: 1,
          },
          {
            itme: "开户银行",
            name: "4",
          },
          {
            itme: "开户支行",
            name: "5",
            id: 0,
          },
          {
            itme: "卖家姓名",
            name: "6",
          },
          {
            itme: "订单编号",
            name: "7",
            id: 1,
          },
          {
            itme: "下单时间",
            name: "8",
          },
        ];
      },
    },
  },
  data() {
    return {
      replace1: null,
      replace2: null,
      afetrgg: "",
      isLoading: false,
      title: "请付款",
      // 判断是否显示手机号
      replace: "",
      cellphonenumber: false,
      trede: true,
      // 上传图片的路径
      src: "",
      // 判断图片是否上传成功
      srcinfo: 0,
      // 判断是否填写凭证
      isinfo: false,
      // 判断是否显示成功显示
      bottoninfo: true,
      // 用户付款数据
      datainfo: "",
      // 上传服务器凭证
      credentials: "",
      // 剩余分钟
      time: "30.00",
      countTime: 180,
      interval: "",
      toLiveBtn: "30.00",
      timeriw: false,
      plasstime: 0,
      pleaseTi: "",
      timeDay: 0,
      timeHr: 0,
      timemin: 0,
      timeSec: 0,
      param: "",
    };
  },
  methods: {
    onRefresh() {
      if (this.countTime == 0) {
        this.$router.push({
          path: "/Cancell",
          name: "Cancell",
          id: this.$route.params.id,
        });
      }
      this.obtain();
      this.timeriw = true;
      this.isLoading = false;
    },
    routerTo() {
      var indexsd = localStorage.getItem("key");
      if (indexsd == 0) {
        this.$router.push({
          path: "/Home",
          name: "Describe",
        });
      } else {
        this.$router.push({
          path: "/Myrech",
          name: "Myrech",
          params: {
            index: 1,
          },
        });
      }
    },
    // 操作：点击了复制按钮
    handleCopyFun() {
      let clipboard = new Clipboard("#copy_text");
      clipboard.on("success", (e) => {
        this.$message({
          message: "复制成功",
          type: "success",
          center: true,
        });
        clipboard.destroy(); // 释放内存
      });
      clipboard.on("error", (e) => {
        // 不支持复制
        alert("该浏览器不支持自动复制");
        clipboard.destroy(); // 释放内存
      });
    },
    // 点击显示手机号弹出框
    number() {
      this.UpdataPhone();
      this.cellphonenumber = true;
    },
    // 隐藏手机号弹出框
    remvedia() {
      this.cellphonenumber = false;
    },
    // 上传图片并预览  这里
    afterRead(e) {
      const isLt2M = e.target.files[0].size / 1024 / 1024 < 10;
      if (!isLt2M) {
        this.$message({
          message: "图片尺寸过大!",
          type: "error",
          center: true,
        });
        e.target.value = "";
        return false;
      }
      let file = e.target.files[0];
      this.param = new FormData(); //创建form对象
      this.param.append("file", file, file.name); //通过append向form对象添加数据
      this.param.append("chunk", "0"); //添加form表单中其他数据
      UploadAttachments(this.param).then((response) => {
        if (response.data.message == "文件过大") {
          this.$message({
            message: "图片尺寸过大",
            type: "error",
            center: true,
          });
        }
        console.log("用户上传附件");
        console.log(response);
        this.credentials = response.data.data;
        if (this.credentials) {
          this.srcinfo = 1;
        }
      });
      let that = this;
      var reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = function (e) {
        that.src = this.result; //显示缩略图
        // 显示图片的路径
        // console.log(this.result);
      };
    },
    // 点击显示凭证弹出框
    isnick() {
      this.srcinfo = 0;
      this.isinfo = true;
      this.src = "";
      this.$refs.updata.value = null;
    },
    // 点击关闭遮罩层 点击div之外的 隐藏
    hibe(e) {
      if (!this.$refs.msk.contains(e.target)) {
        this.isinfo = false;
      }
    },
    // 只更新商家电话
    async UpdataPhone() {
      await GetMerchantPhone(this.$route.params.id).then((res) => {
        console.log("用户获取最新商家电话");
        console.log(res);
        this.replace = res.data.data.userPhone.replace(/[^\d]/g, "");
        if (res.data.data.userPhone2) {
          this.replace1 = res.data.data.userPhone2.replace(/[^\d]/g, "");
        } else {
          this.replace1 = "";
        }
        if (res.data.data.userPhone3) {
          this.replace2 = res.data.data.userPhone3.replace(/[^\d]/g, "");
        } else {
          this.replace2 = "";
        }
      });
    },
    // 获取用户详细信息
    async obtain() {
      if (!this.$route.params.id) {
        this.$route.params.id = localStorage.getItem("info");
      }
      await GetMerchantPhone(this.$route.params.id).then((res) => {
        console.log("用户获取订单详情");
        console.log(res);
        this.plasstime = res.data.data.validTime * 60;
        this.datainfo = res.data.data;
        this.replace = this.datainfo.userPhone.replace(/[^\d]/g, "");
        if (this.datainfo.userPhone2) {
          this.replace1 = this.datainfo.userPhone2.replace(/[^\d]/g, "");
        } else {
          this.replace1 = "";
        }
        if (this.datainfo.userPhone3) {
          this.replace2 = this.datainfo.userPhone3.replace(/[^\d]/g, "");
        } else {
          this.replace2 = "";
        }
        // console.log(this.replace);
      });
      if (!this.timeriw) {
        var date1 = new Date();
        date1 = Date.parse(date1);
        date1 = date1 / 1000;
        // 订单时间
        var date = new Date(this.datainfo.orderTime);
        // 有三种方式获取
        var date = Date.parse(date);
        var date = date / 1000;
        this.time = date1 - date;
        this.time = this.plasstime - this.time;
        this.countTime = this.time;
        if (this.countTime <= 0) {
          this.countTime = 0;
        }
        let day = parseInt(this.countTime / 60 / 60 / 24);
        let hr = parseInt((this.countTime / 60 / 60) % 24);
        let min = parseInt((this.countTime / 60) % 60);
        let sec = parseInt(this.countTime % 60);
        day = day > 9 ? day : "0" + day;
        hr = hr > 9 ? hr : "0" + hr;
        min = min > 9 ? min : "0" + min;
        sec = sec > 9 ? sec : "0" + sec;
        if (day <= 0) {
          this.timeDay = false;
        } else {
          this.timeDay = `${day}`;
        }
        if (hr <= 0) {
          this.timeHr = false;
        } else {
          this.timeHr = `${hr}`;
        }
        if (min <= 0) {
          this.timemin = false;
        } else {
          this.timemin = `${min}`;
        }
        this.timeSec = `${sec}`;
        const that = this;
        that.interval = setInterval(() => {
          if (that.countTime <= 0) {
            // 计时结束，清除缓存
            clearInterval(that.interval);
            this.onRefresh();
          } else {
            that.countTime--;
            let day = parseInt(that.countTime / 60 / 60 / 24);
            let hr = parseInt((that.countTime / 60 / 60) % 24);
            let min = parseInt((that.countTime / 60) % 60);
            let sec = parseInt(that.countTime % 60);
            day = day > 9 ? day : "0" + day;
            hr = hr > 9 ? hr : "0" + hr;
            min = min > 9 ? min : "0" + min;
            sec = sec > 9 ? sec : "0" + sec;
            if (day <= 0) {
              this.timeDay = false;
            } else {
              this.timeDay = `${day}`;
            }
            if (hr <= 0) {
              this.timeHr = false;
            } else {
              this.timeHr = `${hr}`;
            }
            if (min <= 0) {
              this.timemin = false;
            } else {
              this.timemin = `${min}`;
            }
            this.timeSec = `${sec}`;
          }
        }, 1000);
      }
    },
    // 上传图片凭证到服务器
    async voucher() {
      let cherdata = {
        orderNo: this.datainfo.orderNoStr,
        transactorProofPath: this.credentials,
        token: this.$store.state.token,
      };
      await PaymentOrder(cherdata).then((res) => {
        console.log("用户支付订单");
        console.log(res);
        // 上传凭证成功
        if (res.data.message == "提交凭证成功") {
          this.$router.push({
            path: "/Paid",
            name: "Paid",
            params: {
              id: this.$route.params.id,
            },
          });
        } else {
          this.$message({
            message: res.data.message,
            type: "info",
            center: true,
          });
          this.credentials = "";
          this.$router.push({
            path: "Myrech",
            name: "Myrech",
          });
        }
      });
    },
  },
  mounted() {
    this.obtain();
    // alert(localStorage.getItem("info"));
  },
};
</script>
<style>
.back-imageses {
  padding: 0.76rem 0.533333rem;
  background-image: url("../assets/home/eback2.png");
  background-repeat: no-repeat;
  background-size: cover;
}

.bott-imges {
  height: 1.986667rem;
  width: 100%;
  background-image: url("../assets/home/bjksX2.png");
  background-repeat: no-repeat;
  background-size: cover;
}

.botton {
  background-color: #f3e2a9;
  border-radius: 0px 0px 26px 26px;
  box-shadow: 1px 4px 0px rgba(0, 0, 0, 0.5);
}

.border-bottom {
  width: 100%;
  border-bottom: 1px dashed #af6f3a;
  padding: 0.333333rem 0;
}

.border-bottom:last-child {
  border-bottom: none;
}

.bordr-sold {
  border-bottom: 5px solid #af6f3a;
}

.confirmDetermine {
  background: #77b936;
  box-shadow: 0px -1px 2px 0px rgba(255, 255, 255, 0.3) inset,
    -1px -2px 0px rgba(249, 251, 115, 0.8);
  width: 1.826667rem;
  height: 0.573333rem;
  color: #ffd800;
  font-size: 15px;
}

.prompt-box-back-imgs {
  background-image: url("../assets/home/e1d.png");
  background-repeat: no-repeat;
  width: 6.213333rem;
  height: 4.16rem;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-size: 100%;
}

.PaidImages {
  background-image: url("../assets/home/edX2.png");
  background-repeat: no-repeat;
  width: 5.733333rem;
  height: auto;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-size: 100%;
  padding: 0.746667rem 0.8rem;
}

.confirmImges {
  width: 100%;
  height: 0.786667rem;
}

.UploadPictures {
  width: 3.253333rem;
  height: 3.52rem;
  background: #ffffff;
  position: relative;
}

.UploadPictures::after {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  width: 200%;
  height: 200%;
  border: 1px solid #af6f3a;
  transform: scale(0.5);
  transform-origin: 0 0;
  box-sizing: border-box;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.imese {
  margin-top: 0.4rem;
  max-width: 3.253333rem;
  max-height: 3.52rem;
}

.list {
  margin: 0 0.426667rem;
  box-sizing: border-box;
  width: auto !important;
}

.bordr-solde {
  margin: 0;
  border-bottom: 2px solid #af6f3a;
  padding: 0.333333rem 0.426667rem;
}

.Pleabordr {
  border: 1px solid #af6f3a;
}
</style>