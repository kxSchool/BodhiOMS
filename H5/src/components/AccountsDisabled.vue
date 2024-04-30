<template>
  <!-- 首页背景图 -->
  <div class="back-imagees">
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
        <div class="margin-auto font-we-bold title" style="color: #2b2c2f">
          SARO
        </div>
      </div>
    </div>
    <div id="App">
      <div class="centerimg" style="z-index: 100">
        <img
          src="@/assets/home/updata.png"
          style="
            height: 1.986667rem;
            width: 2.693333rem;
            margin-bottom: 0.133333rem;
          "
        />
        <div class="nav-tiel" style="padding-bottom: 5px; white-space: nowrap">
          您的商家账户已被禁用
        </div>
      </div>
    </div>
    <img src="@/assets/home/backimg.png" class="imges" />
  </div>
</template>

<script>
export default {
  name: "GamerdealAccountexception",

  data() {
    return {};
  },
  methods: {
    routerTo() {
      //  失败后跳转回游戏
      window.location.href = 'uniwebview://game?action="close"';
    },
  },
};
</script>

<style scoped>
.imges {
  position: fixed;
  z-index: 0;
  width: 100%;
  height: 100vh;
}

.nav-tiel {
  color: #f3e2a9;
  font-size: 32px;
  font-weight: bold;
}
.centerimg {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
}
</style>