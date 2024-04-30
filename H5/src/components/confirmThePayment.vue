<template>
  <div>
    <!-- 首页背景图 -->
    <div class="back-images">
         <!-- 头部标题组件 -->
      <NavBar></NavBar>
      <div class="disp-space-bet" style="color: #fae8a6">
        <!-- 请付款 -->
        <div
          class="font-we-bold"
          v-for="(item, index) in OrderStatus"
          :key="index"
        >
          <div style="font-size: 20px; margin-bottom: 0.28rem">
            {{ item.texts }}
          </div>
          <!-- 付款超时时间 -->
          <div style="font-size: 16px; width: 5.72rem">
            {{ item.status }}
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
          <div style="font-size: 15px" class="font-we-bold">联系对方</div>
        </div>
      </div>
      <!-- 付款的详细信息 -->
      <div style="margin-top: 0.493333rem">
        <div
          class="bott-imges disp-space-bet"
          style="padding: 0.333333rem; box-sizing: border-box"
        >
          <img
            src="../assets/home/SARO@2x.png"
            style="width: 1.333333rem; height: 1.32rem"
          />
          <div
            class="font-we-bold"
            style="font-size: 15px"
            v-for="(item, index) in UserRecharge"
            :key="index"
          >
            <div
              style="
                display: flex;
                justify-content: end;
                color: #ffbe32;
                font-size: 21px;
              "
            >
              ¥{{ item.total }}.00
            </div>
            <div style="color: #fbe7a6; font-size: 14px">
              <span>单价:¥{{ item.UnitPrice }}</span>
              <span style="margin-left: 0.266667rem"
                >数量:{{ item.quantity }}SARO</span
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
            v-for="(item, index) in data"
            :key="index"
            :class="item.id == 0 ? 'bordr-solde' : ''"
            class="border-bottom font-we-bold disp-space-bet list"
            style="color: #b06f39; font-size: 15px"
          >
            {{ item.itme }}
            <div style="color: #5d3129">
              {{ item.name }}
              <span
                v-show="item.id == 1"
                style="margin-left: 0.066667rem; color: #7abd37"
                id="copy_text"
                :data-clipboard-text="item.name"
                @click="handleCopyFun"
                >复制</span
              >
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
              在转账过程中请勿备注充值SARO等信息，防止汇
              款被拦截，银行卡被冻结等问题
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
        <div class="disp-space-bet" v-show="imgInfo">
          <img
            @click="Closeorder"
            class="User-images"
            src="../assets/home/guanbi@2x.png"
            alt=""
          />
          <img
            @click="voucher"
            class="User-images"
            src="../assets/home/chakan@2x.png"
            alt=""
          />
          <img
            @click="Release"
            class="User-images"
            src="../assets/home/fangxing@2x.png"
            alt=""
          />
        </div>
      </div>
    </div>
    <!-- 提示遮罩层 -->
    <div v-show="cellphonenumber == true" style="display: none">
      <div :class="[cellphonenumber ? 'popContainer' : '']" @touchmove.prevent>
        <div class="prompt-box-back-imgs" v-show="cellphonenumber == true">
          <!-- 提示框文字 用户收款银行 -->
          <div class="flex-even">
            <div
              class="prompt-title font-we-bold text-center"
              style="flex-direction: column"
            >
              <div class="text-center">联系电话</div>
              <div
                v-for="(item, index) in telephone"
                :key="index"
                style="margin-top: 0.266667rem"
              >
                {{ item.number }}
              </div>
            </div>
            <div
              class="disp-ju-center wed-100"
              v-for="(item, index) in telephone"
              :key="index"
            >
              <div class="confirmDetermine disp-ju-center">
                <a :href="'tel:' + item.number">
                  <img
                    class="cancel-img"
                    src="../assets/home/queren.png"
                    alt=""
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
    <!-- 确认收款并放行 -->
    <div v-show="Releasese">
      <div v-show="cellphonenumber == true" style="display: none">
        <div
          :class="[cellphonenumber ? 'popContainer' : '']"
          @touchmove.prevent
        >
          <div class="prompt-box-back-imgs" v-show="cellphonenumber == true">
            <!-- 提示框文字 用户收款银行 -->
            <div class="flex-even">
              <div
                class="prompt-title font-we-bold text-center"
                style="flex-direction: column"
              >
                <div class="text-center"></div>
                <div style="margin-top: 0.266667rem">确认已收款并放行</div>
              </div>
              <div
                class="disp-ju-center wed-100"
                v-for="(item, index) in telephone"
                :key="index"
              >
                <div class="confirmDetermine disp-ju-center">
                  <img
                    @click="confirm"
                    class="cancel-img"
                    src="../assets/home/queren.png"
                    alt=""
                  />
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
    </div>
    <!-- 确认关闭订单 -->
    <div v-show="Closeorderin">
      <div v-show="cellphonenumber == true" style="display: none">
        <div
          :class="[cellphonenumber ? 'popContainer' : '']"
          @touchmove.prevent
        >
          <div class="prompt-box-back-imgs" v-show="cellphonenumber == true">
            <!-- 提示框文字 用户收款银行 -->
            <div class="flex-even">
              <div
                class="prompt-title font-we-bold text-center"
                style="flex-direction: column; padding: 0 0.773333rem"
              >
                <div class="text-center">确认关闭订单</div>
                <div
                  style="
                    margin-top: 0.266667rem;
                    color: #58381c;
                    font-size: 0.373333rem;
                  "
                >
                  关闭订单后SARO将不会放行给对方，请谨慎操作。
                </div>
              </div>
              <div
                class="disp-ju-center wed-100"
                v-for="(item, index) in telephone"
                :key="index"
              >
                <div class="confirmDetermine disp-ju-center">
                  <img
                    @click="close"
                    class="cancel-img"
                    src="../assets/home/queren.png"
                    alt=""
                  />
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
    </div>
    <div>
      <!-- 提示遮罩层 -->
      <div v-show="isinfo" @click="hibe($event)">
        <div :class="[trede ? 'popContainer' : '']">
          <!-- 确认付款图片凭证 -->
          <div class="PaidImages" ref="msk">
            <div class="disp-colu-cen font-we-bold">
              <div style="color: #5d3129; font-size: 16px">确认已付款</div>
              <div style="color: #d14330; font-size: 15px; margin-top: 0.4rem">
                若未付款点击确认已付款，平台核实后将有可能对您的账号进行禁用或封号处理
              </div>
              <div class="disp-colu-cen">
                <div
                  style="color: #5d3129; font-size: 15px; margin-top: 0.4rem"
                >
                  请上传您的付款凭证
                </div>
                <label for="upload">
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
                  <img v-show="src" class="imese" :src="src" alt="" srcset="" />
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
                <router-link to="/Paid" v-else>
                  <img
                    style="margin-top: 0.48rem"
                    class="confirmImges"
                    src="../assets/home/qrfklX2.png"
                  />
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
let Base64 = require("js-base64").Base64;
import Recharge from "./Recharge.vue";
// 点击复制
import Clipboard from "clipboard";
export default {
  components: { Recharge },

  props: {
    // 付款状态
    OrderStatus: {
      type: "",
      default: () => {
        return [
          {
            texts: "交易关闭",
            status: "您的订单已被卖家关闭，若有疑问，请及时联系卖家",
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
    // 判断是否显示凭证和放行
    imgInfo: {
      type: Boolean,
      default: false,
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
      Closeorderin: false,
      Releasese: false,
      title: "请付款",
      // 判断是否显示手机号
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
    };
  },
  methods: {
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
      this.Releasese = false;
      this.Closeorderin = false;
      this.cellphonenumber = true;
    },
    // 隐藏手机号弹出框
    remvedia() {
      this.cellphonenumber = false;
    },
    // 上传图片并预览
    afterRead() {
      let that = this;
      let file = this.$refs.updata.files[0];
      //   console.log(file);
      var reader = new FileReader();
      reader.readAsDataURL(file);
      this.srcinfo = 1;
      reader.onload = function (e) {
        //   console.log(e);
        that.src = this.result; //显示缩略图
        // 显示图片的路径
        // console.log(this.result);
      };
    },
    // 点击显示凭证弹出框
    isnick() {
      this.isinfo = true;
    },
    // 点击关闭遮罩层 点击div之外的 隐藏
    hibe(e) {
      if (!this.$refs.msk.contains(e.target)) {
        this.isinfo = false;
      }
    },
    // 点击放行显示弹窗
    Release() {
      this.cellphonenumber = true;
      this.Releasese = true;
      this.Closeorderin = false;
    },
    // 点击关闭订单
    Closeorder() {
      this.Closeorderin = true;
      this.Releasese = false;
      this.cellphonenumber = true;
    },
    // 点击跳转到凭证页面
    voucher() {
      this.$router.push({
        path: "/Voucher",
      });
    },
    // 确定关闭订单
    close() {
      this.$router.push({
        path: "/Closur",
      });
    },
    // 确定收款并放行
    confirm() {
      this.$router.push({
        path: "/Complete",
      });
    },
  },
};
</script>
<style>
.back-images {
  height: 100vh;
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
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  /* height: 9.773333rem; */
  background-color: #f3e2a9;
  border-radius: 0px 0px 26px 26px;
  box-shadow: 1px 4px 0px rgba(0, 0, 0, 0.5);
}

.border-bottom {
  width: 100%;
  border-bottom: 1px dashed #af6f3a;
  /* padding: 0.333333rem 0; */
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
  /* padding: 0.333333rem 0.426667rem; */
}

.User-images {
  margin-top: 0.866667rem;
  width: 2.88rem;
  height: 1.04rem;
}
</style>