<template>
  <div>
    <!-- 首页背景图 -->
    <div class="back-imagese">
      <NavBar></NavBar>

      <div style="height: 1.066667rem">
        <div class="disp-align-center" style="height: 1.066667rem">
          <van-icon size="20" name="arrow-left" style="position: absolute; left: 0.266666rem" @click="routerTo" />
          <div class="margin-auto font-we-bold title" style="color: #2b2c2f; padding-left: 1.066667rem">
            我的充值
          </div>
          <div class="margin-auto font-we-bold title" @click="userSell"
            style="color: #919191; padding-right: 1.066667rem">
            我的卖出
          </div>
        </div>
      </div>

      <!-- 充值和卖出按钮 -->
      <div class="disp-space-bet" style="padding: 0.493333rem 0.413333rem">
        <div v-for="(item, index) in Myimg" :key="index" class="disp-flex font-sizetw"
          style="flex: 1; justify-content: space-between">
          <div style="position: relative">
            <img :src="imgindex == index ? item.img : item.images" alt=""
              style="width: 2.133333rem; height: 0.826667rem" @click="Myimgclick(index, item.status)" />
            <div :class="[
                  indexin == 0 ? 'display-none' : '',
                  'border-red',
                  'disp-ju-center',
                ]" v-show="item.id">
              {{ indexin }}
            </div>
          </div>
        </div>
      </div>
      <!-- 充值商户列表 -->

      <div style="
            padding: 0 0.546667rem 0.48rem 0.546667rem;
            box-sizing: border-box;
          " class="backimages">
        <div :class="[Judgment ? 'Nodata' : 'back-color']" style="
              padding: 0.373333rem 0.306666rem;
              height: 100%;
              position: relative;
              overflow: hidden;
            ">
               <div v-show="Judgment" class="nodePOst">
                <img class="nodeimg" src="../assets/home/Nodata.png" alt="" />
                <div style="font-size: 0.426667rem; color: #f3e2a9" class="font-we-bold">
                  暂无数据
                </div>
              </div>
          <van-pull-refresh v-model="isLoading" success-text="刷新成功" @refresh="onRefresh" style="overflow: visible">
            <!-- 上拉加载组件使用 -->
            <van-list v-model="loading" :finished="finished" finished-text="没有更多了" @load="onLoad"
              :immediate-check="false">
              <!-- 商户背景 -->
              <div v-for="(items, index) in list" :key="index">
                <van-cell v-if="imgindex == 0 ? true : imgindex == items.orderStatus"
                  @click="MyrouterTo(items.orderId, items.orderStatus)" class="Merchant-back-img" style="
                    padding: 0.36rem 0.44rem;
                    box-sizing: border-box;
                    margin-bottom: 0.266667rem;
                    background-color: transparent !important;
                    line-height: inherit !important;
                    overflow: initial !important;
                  ">
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
                        {{ items.nickname }}
                      </div>
                      <!-- 商户单价 -->
                      <div v-if="items.orderStatus == 1" class="Merchant-title font-we-bold">
                        待付款>
                      </div>
                      <div v-else-if="items.orderStatus == 2" class="Merchant-title font-we-bold">
                        待放行>
                      </div>
                      <div v-else-if="items.orderStatus == 3" class="Merchant-title font-we-bold">
                        已完成>
                      </div>
                      <div v-else-if="items.orderStatus == 4" class="Merchant-title font-we-bold">
                        已取消>
                      </div>
                      <div v-else-if="items.orderStatus == 5" class="Merchant-title font-we-bold">
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
                      <div class="Merchant-quantity font-we-bold disp-flex Mycolor">
                        单价(CNY)
                      </div>
                      <div class="Merchant-quantity font-we-bold disp-flex Mycolor">
                        数量(SARO)
                      </div>
                      <div class="Merchant-quantity font-we-bold disp-flex Mycolor">
                        交易总额(CNY)
                      </div>
                    </div>
                    <!-- 商户商品单价和充值按钮 -->
                    <div class="disp-space-bet" style="
                        margin-top: 0.133333rem;
                        margin-top: 0.066667rem !important;
                      ">
                      <!-- 商户商品单价 -->
                      <div class="Merchant-quantity font-we-bold disp-flex">
                        {{ items.orderUnitPrice }}
                      </div>
                      <!-- 商户商品数量 -->
                      <div class="Merchant-quantity font-we-bold disp-flex">
                        {{ items.saroNumber | iskes }}
                      </div>
                      <!-- 商户商品总额 -->
                      <div class="Merchant-quantity font-we-bold disp-flex total">
                        {{ items.payAmount | amount }}
                      </div>
                    </div>
                  </div>
                </van-cell>
              </div>
              <!-- 暂无数据 -->
           
            </van-list>
          </van-pull-refresh>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Recharge from "./Recharge.vue";
  import { mapState } from "vuex";
  import { SellOrder } from "../api/business";
  export default {
    components: { Recharge },
    data() {
      return {
        list: [], //存储列表数据
        loading: false, //控制加载中loading的状态
        finished: false, //控制数据加载结束的状态
        total: 0, //总数据条数
        page: 1, //表示请求的第几页的数据
        pagesize: 10, //每页显示多少条数据
        isLoading: false,
        data: "",
        // 用户id
        index: 17,
        merstatus: "",
        // 图片切换索引
        imgindex: 0,
        indexin: 0,
        // 判断是否有数据
        Judgment: false,
        datalist: "",
        // 付款状态
        Myimg: [
          {
            img: require("../assets/home/qbxz.png"),
            images: require("../assets/home/qbwxzx2.png"),
            status: "", // 你去看后台状态对应传入值0 1 23
          },
          {
            img: require("../assets/home/dfkxz.png"),
            images: require("../assets/home/dfkwxz.png"),
            id: 1,
            status: 1, // 你去看后台状态对应传入值0 1 23
          },
          {
            img: require("../assets/home/dfxxz.png"),
            images: require("../assets/home/dai.png"),
            status: 2, // 你去看后台状态对应传入值0 1 23
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
        localStorage.setItem("first", 0);
        this.$router.push({
          path: "/BuyesList",
          name: "BuyesList",
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
      // 这是订单列表页面
      routerTo() {
        localStorage.setItem("Myindex", 0);
        this.$router.push({
          // 返回到首页
          path: "/Home",
          name: "Describe",
        });
        // 返回后把当前页面设置为不缓存
      },
      // 点击订单跳到付款详情界面
      async MyrouterTo(id, status) {
        // 跳转到代付款页面
        if (status == 1) {
          localStorage.setItem("info", id);
          this.$router.push({
            path: "/Please",
            name: "Please",
            params: {
              id: id,
            },
          });
          localStorage.setItem("key", 1);
        }
        // 跳转到待放行页面
        else if (status == 2) {
          localStorage.setItem("info", id);
          this.$router.push({
            path: "/Paid",
            name: "Paid",
            params: {
              id: id,
            },
          });
        }

        // 跳转到已完成页面
        else if (status == 3) {
          this.$router.push({
            path: "/Succeed",
            name: "Succeed",
            params: {
              id: id,
            },
          });
        }
        // 跳转到已取消页面
        else if (status == 4) {
          this.$router.push({
            path: "/Cancell",
            name: "Cancell",
            params: {
              id: id,
            },
          });
        }
        // 跳转到一关闭页面
        else if (status == 5) {
          this.$router.push({
            path: "/Transac",
            name: "Transac",
            params: {
              id: id,
            },
          });
        }
      },
      // 用户获取订单列表
      async onLoad() {
        let loaddata = {
          orderStatus: this.merstatus,
          page: this.page, //页码
          size: this.pagesize, //每页加载条数
          orderType: 1, //这个是充值状态
          token: this.$store.state.token, //token值
        };
        await SellOrder(loaddata).then((res) => {
          this.datalist = res.data.data.list;
          console.log("用户的订单列表数据");
          console.log(res.data);
          if (this.page === 1) {
            this.list = [];
          }
          this.list = this.list.concat(this.datalist);
          // 加载状态结束
          this.loading = false;
          // 判断是否全部加载完毕
          if (res.data.data.list.length) {
            this.page++;
          } else {
            this.finished = true;
          }
          // 每个数据状态的条数
          this.indexin = res.data.data.waitPayNum;
          if (this.merstatus == "" && this.list.length == 0) {
            this.Judgment = true;
          }
          this.indexStatus = this.list.filter(
            (item) => item.orderStatus == this.merstatus
          ).length;
        });
      },
      // 点击切换付款状态
      Myimgclick(id, tabitem, index) {
        if (id == 1 || id == 2 || id == 3) {
          localStorage.setItem("first", 1);
        } else {
          localStorage.setItem("first", 0);
        }
        // this.$route.meta.keepAlive = true;
        this.merstatus = tabitem;
        // 每个数据状态的条数
        this.imgindex = id;
        localStorage.setItem("Myindex", id);
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
    },
    filters: {
      formatDate: function (value) {
        var date = new Date();
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
      if (localStorage.getItem("first") != 1) {
        this.onLoad();
      }
      switch (
      this.$route.params.index &&
      Number(localStorage.getItem("Myindex"))
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
  .back-imagese {
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
    color: #d14330;
  }

  .display-none {
    display: none !important;
  }

  .Mycolor {
    color: #af6f3a !important;
  }

  .nodeimg {
    width: 1.786667rem;
    height: 2.053333rem;
  }

  .nodePOst {
    position: absolute;
    left: 50%;
    top: 35%;
    transform: translate(-50%, -50%);
  }

  /* 判断是否有数据 */
  .Nodata {
    background-color: #b0703b;
    border-radius: 0.08rem;
    opacity: 1;
    box-shadow: 0 0 0.04rem 0.066667rem rgb(141 83 43 / 45%) inset;
    margin: auto;
    height: 100vh !important;
  }

  .van-cell::after {
    position: unset !important;
  }

  .van-cell__value {
    position: initial !important;
    overflow: inherit !important;
  }

  .van-list__finished-text {
    bottom: -0.6rem !important;
  }
</style>