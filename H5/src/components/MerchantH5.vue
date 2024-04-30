<template>
  <div>
    <!-- 首页背景图 -->
    <div class="back-imageseseee">
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
          <div
            class="margin-auto font-we-bold title"
            style="color: #2b2c2f; padding-left: 1.066667rem"
          >
            用户充值
          </div>
          <div
            class="margin-auto font-we-bold title"
            style="color: #919191; padding-right: 1.066667rem"
            @click="userSell"
          >
            用户卖出
          </div>
        </div>
      </div>
   
        <!-- 充值和卖出按钮 -->
        <div class="disp-space-bet" style="padding: 0.493333rem 0.413333rem">
          <div
            v-for="(item, index) in Myimg"
            :key="index"
            class="disp-flex"
            style="display: flex; justify-content: space-between"
          >
            <div style="position: relative">
              <img
                :src="imgindex == index ? item.img : item.images"
                alt=""
                style="width: 2.133333rem; height: 0.826667rem"
                @click="Myimgclick(index, item.status)"
              />
              <!-- 待收款红色角标 -->
              <div
                :class="[
                  indexin == 0 ? 'display-none' : '',
                  'border-red',
                  'disp-ju-center',
                ]"
                v-show="item.id == 1"
              >
                {{ indexin }}
              </div>
              <!-- 待放行红色角标 -->
              <div
                :class="[
                  TobereLeased == 0 ? 'display-none' : '',
                  'border-red',
                  'disp-ju-center',
                ]"
                v-show="item.id == 2"
              >
                {{ TobereLeased }}
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
            :class="[Judgment ? 'Nodata' : 'back-colorrese']"
            style="padding: 0.373333rem 0.306666rem; position: relative;overflow: hidden;"
          >
              <!-- 暂无数据 -->
              <div v-show="Judgment" class="nodePOst">
                <img class="nodeimg" src="../assets/home/Nodata.png" alt="" />
                <div
                  style="font-size: 0.426667rem; color: #f3e2a9"
                  class="font-we-bold"
                >
                  暂无数据
                </div>
              </div>
             <van-pull-refresh
        v-model="isLoading"
        success-text="刷新成功"
        @refresh="onRefresh"
        style="overflow: visible"
      >
            <!-- 上拉加载组件使用 -->

            <van-list
              v-model="loading"
              :finished="finished"
              finished-text="没有更多了"
              @load="onLoad"
              :immediate-check="false"
            >
              <!-- 商户背景 -->

              <div v-for="(items, index) in list" :key="index">
                <!-- 渲染数据 -->
                <van-cell
                  v-if="imgindex == 0 ? true : imgindex == items.orderStatus"
                  @click="MyrouterTo(items.orderId, items.orderStatus)"
                  class="Merchant-back-img"
                  style="
                    padding: 0.36rem 0.44rem;
                    box-sizing: border-box;
                    margin-bottom: 0.266667rem;
                    background-color: transparent !important;
                    line-height: inherit !important;
                    overflow: initial !important;
                  "
                >
                  <!-- 小图标 -->
                  <img src="@/assets/home/sdX2.png" class="icon-image" />
                  <!-- 商户信息 -->
                  <div>
                    <!-- 商户名称和单价 -->
                    <div class="disp-space-bet">
                      <!-- 商户名称 -->
                      <div class="Merchant-title font-we-bold">SARO</div>
                      <!-- 商户名称 -->
                      <div class="Merchant-title font-we-bold">
                        {{ items.realName }}
                      </div>
                      <!-- 商户单价 -->
                      <div
                        v-if="items.orderStatus == 1"
                        class="Merchant-title font-we-bold"
                      >
                        待收款>
                      </div>
                      <div
                        v-else-if="items.orderStatus == 2"
                        class="Merchant-title font-we-bold"
                      >
                        待放行>
                      </div>
                      <div
                        v-else-if="items.orderStatus == 3"
                        class="Merchant-title font-we-bold"
                      >
                        已完成>
                      </div>
                      <div
                        v-else-if="items.orderStatus == 4"
                        class="Merchant-title font-we-bold"
                      >
                        已取消>
                      </div>
                      <div
                        v-else-if="items.orderStatus == 5"
                        class="Merchant-title font-we-bold"
                      >
                        已关闭>
                      </div>
                    </div>
                    <!-- 用户购买商品订单时间 -->
                    <div id="date">
                      {{ items.orderTimeStr | moment }}
                    </div>
                    <!-- 商户剩余数量和价格 -->
                    <div class="disp-space-bet" style="margin-top: 0.133333rem">
                      <!-- 商户剩余数量 -->
                      <div
                        class="Merchant-quantity font-we-bold disp-flex Mycolor"
                      >
                        单价(CNY)
                      </div>
                      <div
                        class="Merchant-quantity font-we-bold disp-flex Mycolor"
                      >
                        数量(SARO)
                      </div>
                      <div
                        class="Merchant-quantity font-we-bold disp-flex Mycolor"
                      >
                        交易总额(CNY)
                      </div>
                    </div>
                    <!-- 商户商品单价和充值按钮 -->
                    <div
                      class="disp-space-bet"
                      style="
                        margin-top: 0.133333rem;
                        margin-top: 0.066667rem !important;
                      "
                    >
                      <!-- 商户商品单价 -->
                      <div class="Merchant-quantity font-we-bold disp-flex">
                        {{ items.orderUnitPrice }}
                      </div>
                      <!-- 商户商品单价 -->
                      <div class="Merchant-quantity font-we-bold disp-flex">
                        {{ items.saroNumber | siwen }}
                      </div>
                      <!-- 商户商品单价 -->
                      <div
                        class="Merchant-quantity font-we-bold disp-flex total"
                      >
                        {{ items.payAmount | amount }}
                      </div>
                    </div>
                  </div>
                </van-cell>
              </div>
          
            </van-list>
      </van-pull-refresh>

          </div>
        </div>
    </div>
  </div>
</template>

<script>
import Recharge from "./Recharge.vue";
// 点击复制
import { mapState } from "vuex";
import { OrderList } from "../api/user";
export default {
  components: { Recharge },
  data() {
    return {
      state: false,
      list: [], //存储列表数据
      loading: false, //控制加载中loading的状态
      finished: false, //控制数据加载结束的状态
      total: 0, //总数据条数
      page: 1, //表示请求的第几页的数据
      pagesize: 10, //每页显示多少条数据
      isloading: false, //上拉获取数据节流
      isLoading: false,
      Judgment: false,
      data: "",
      merstatus: "",
      // 用户id
      index: 17,
      indexStatus: 0,
      // 图片切换索引
      imgindex: 0,
      indexin: 0,
      TobereLeased: 0,
      // 付款状态  是这个对吧嗯  对了
      Myimg: [
        {
          img: require("../assets/home/qbxz.png"),
          images: require("../assets/home/qbwxzx2.png"),
          status: "", // 你去看后台状态对应传入值0 1 23
        },
        {
          img: require("../assets/home/dskxz.png"),
          images: require("../assets/home/daishoukkuan.png"),
          id: 1,
          status: 1, // 你去看后台状态对应传入值0 1 23
        },
        {
          img: require("../assets/home/dfxxz.png"),
          images: require("../assets/home/dai.png"),
          status: 2, // 你去看后台状态对应传入值0 1 23
          id: 2,
        },
        {
          img: require("../assets/home/ywcxz.png"),
          images: require("../assets/home/ywcwxz.png"),
          status: 3, // 你去看后台状态对应传入值0 1 23
        },
      ],
    };
  },
  methods: {
    // 切换到卖出
    userSell() {
      this.$router.push({
        path: "/userSellList",
        name: "userSellList",
      });
      localStorage.setItem("sellerIndex", 0);
    },
    // 搜索一下
    async onLoad() {
      let loaddata = {
        orderStatus: this.merstatus,
        page: this.page, //页码
        size: this.pagesize, //每页加载条数
        orderType: 1,
        token: this.$store.state.token,
      };
      // 用户获取订单列表
      await OrderList(loaddata).then((res) => {
        console.log("获取订单列表");
        console.log(res);
        let datalist = res.data.data.list;
        if (this.page === 1) {
          this.list = [];
        }
        this.list = this.list.concat(datalist);
        // 加载状态结束
        this.loading = false;
        // 判断是否全部加载完毕
        if (res.data.data.list.length) {
          this.page++;
        } else {
          this.finished = true;
        }
        // 待收款状态的条数
        this.indexin = res.data.data.waitPayNum;
        // 待放行状态的条数
        this.TobereLeased = res.data.data.waitReleasedNum;
        if (this.merstatus == "" && this.list.length == 0) {
          this.Judgment = true;
        }
        this.indexStatus = this.list.filter(
          (item) => item.orderStatus == this.merstatus
        ).length;
      });
    },
    onRefresh() {
      this.page = 1;
      Promise.all([this.onLoad()]).then((res) => {
        this.finished = false;
        if (this.list.length != 0 && this.merstatus != "") {
          this.Judgment = false;
        } else if (this.merstatus == "" && this.list.length == 0) {
          this.Judgment = true;
        } else if (this.merstatus == 1 && this.list.length == 0) {
          this.Judgment = true;
        } else if (this.merstatus == 2 && this.list.length == 0) {
          this.Judgment = true;
        } else if (this.merstatus == 3 && this.list.length == 0) {
          this.Judgment = true;
        } else if (this.merstatus == "" && this.list.length != 0) {
          this.Judgment = false;
        } else if (this.merstatus == 1 && this.list.length != 0) {
          this.Judgment = false;
        } else if (this.merstatus == 2 && this.list.length != 0) {
          this.Judgment = false;
        } else if (this.merstatus == 3 && this.list.length != 0) {
          this.Judgment = false;
        }
      });
      this.isLoading = false;
    },
    routerTo() {
      localStorage.setItem("businessindex", 0);
      this.$route.meta.keepAlive = false;
      this.$router.push({
        path: "/Merchan",
        name: "Merchan",
      });
    },
    // 点击切换付款状态
    Myimgclick(id, tabitem, index) {
      if (id == 1 || id == 2 || id == 3) {
        localStorage.setItem("sellerIndex", 1);
      } else {
        localStorage.setItem("sellerIndex", 0);
      }
      this.merstatus = tabitem;
      localStorage.setItem("businessindex", id);
      this.imgindex = id;
      this.$route.meta.keepAlive = true;
      this.page = 1;
      Promise.all([this.onLoad()]).then((res) => {
        this.finished = false;
        if (this.list.length != 0 && id == 0) {
          this.Judgment = false;
        } else if (id == 1 && this.indexStatus != 0) {
          this.Judgment = false;
        } else if (id == 2 && this.indexStatus != 0) {
          this.Judgment = false;
        } else if (id == 3 && this.indexStatus != 0) {
          this.Judgment = false;
        } else if (id == 1 && this.indexStatus == 0) {
          this.Judgment = true;
        } else if (id == 2 && this.indexStatus == 0) {
          this.Judgment = true;
        } else if (id == 3 && this.indexStatus == 0) {
          this.Judgment = true;
        } else if (this.list.length == 0 && id == 0) {
          this.Judgment = true;
        }
      });
    },
    // 点击订单跳到付款详情界面
    async MyrouterTo(id, status) {
      // 跳转到代付款页面
      if (status == 1) {
        localStorage.setItem("info", id);
        this.$router.push({
          path: "/COllect",
          name: "COllect",
          params: {
            id: id,
          },
        });
      }
      // 跳转到待放行页面
      else if (status == 2) {
        localStorage.setItem("info", id);
        this.$router.push({
          path: "/UserRech",
          name: "UserRech",
          params: {
            id: id,
          },
        });
      }

      // 跳转到已完成页面
      else if (status == 3) {
        localStorage.setItem("info", id);
        this.$router.push({
          path: "/Complete",
          name: "Complete",
          params: {
            id: id,
          },
        });
      }
      // 跳转到已取消页面
      else if (status == 4) {
        localStorage.setItem("info", id);
        this.$router.push({
          path: "/Rechar",
          name: "Rechar",
          params: {
            id: id,
          },
        });
      }
      // 跳转到关闭页面
      else if (status == 5) {
        localStorage.setItem("info", id);
        this.$router.push({
          path: "/Closur",
          name: "Closur",
          params: {
            id: id,
          },
        });
      }
    },
  },
  filters: {
    formatDate: function (value) {
      var date = new Date();
      //date.setTime(value);
      var month = date.getMonth() + 1;
      var hours = date.getHours();
      if (hours < 10) hours = "0" + hours;
      var minutes = date.getMinutes();
      if (minutes < 10) minutes = "0" + minutes;
      var time =
        date.getFullYear() +
        "-" +
        month +
        "-" +
        date.getDate() +
        " " +
        hours +
        ":" +
        minutes;
      return time;
    },
  },
  mounted() {
    // // 页面一加载把当前页面设置为缓存
    if (localStorage.getItem("sellerIndex") != 1) {
      this.onLoad();
    }
    switch (
      this.$route.params.index &&
      Number(localStorage.getItem("businessindex"))
    ) {
      case 1:
        this.Myimgclick(1, 1, 2);
        break;
      case 2:
        this.Myimgclick(2, 2);
        break;
      case 3:
        this.Myimgclick(3, 3);
        break;
      default:
    }
  },
  computed: {
    ...mapState(["token"]),
  },
};
</script>
<style>
.back-imageseseee {
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

.title {
  font-size: 32px;
  color: #2b2c2f;
}

.disp-align-center {
  background-color: #fff;
  position: fixed;
  width: 100%;
  z-index: 100;
}

.image {
  width: auto !important;
}

#date {
  font-size: 26px;
  color: #af6f3a;
  font-weight: bold;
}

.Merchant-quantity {
  color: #af6f3a;
  font-size: 26px;
}

.Merchant-quantity {
  color: #5d3129;
}

.total {
  color: #d14330 !important;
}

.border-red {
  min-width: 0.533333rem;
  min-height: 0.533333rem;
  max-height: 1.066667rem;
  max-width: 1.066667rem;
  border-radius: 100%;
  color: #f3e3a9;
  font-size: 0.346667rem;
  font-weight: bold;
  background: #d3492f;
  border: 2px solid #d69e57;
  position: absolute;
  top: -0.4rem;
  right: -0.16rem;
}

.Mycolor {
  color: #af6f3a !important;
}

.van-cell::after {
  position: unset !important;
}

.van-cell__value {
  position: initial !important;
  overflow: inherit !important;
}

.back-colorrese {
  background-color: #b0703b;
  height: 100%;
  border-radius: 0.08rem;
  opacity: 1;
  box-shadow: 0px 0px 3px 5px rgba(141, 83, 43, 0.45) inset;
  margin: auto;
}

.hieght {
  height: 100vh !important;
}

.van-list__finished-text {
  position: absolute;
  bottom: 0%;
  left: 50%;
  transform: translate(-50%);
}
</style>