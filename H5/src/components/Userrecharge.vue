<template>
  <div>
    <NavBar></NavBar>

    <!-- 商户背景 -->
    <div
      v-for="(item, index) in data"
      :key="index"
      class="Merchant-back-img"
      @click="dialogVisibl(number, data, item.userId, index)"
      style="
        padding: 0.36rem 0.44rem;
        box-sizing: border-box;
        margin-bottom: 0.2rem;
      "
    >
      <!-- 小图标 -->
      <img src="@/assets/home/sdX2.png" class="icon-image" />
      <!-- 商户信息 -->
      <div @click="handleClose">
        <!-- 商户名称和单价 -->
        <div class="disp-space-bet">
          <!-- 商户名称 -->
          <div class="Merchant-title font-we-bold">
            {{ item.nickname }}
          </div>
          <!-- 商户单价 -->
          <div class="Merchant-title font-we-bold">单价</div>
        </div>
        <!-- 商户剩余数量和价格 -->
        <div class="disp-space-bet">
          <!-- 商户剩余数量 -->
          <div class="Merchant-quantity font-we-bold disp-flex">
            数量:
            <div class="Merchant-number">{{ item.saroNumber | siwen }}SARO</div>
          </div>
          <!-- 商户价格 -->
          <div class="Merchant-Price font-we-bold">
            ¥{{ item.rechargeUnitPrice }}
          </div>
        </div>
        <!-- 商户商品单价和充值按钮 -->
        <div class="disp-space-bet">
          <!-- 商户商品单价 -->
          <div class="Merchant-quantity font-we-bold disp-flex">
            限额:
            <div class="Merchant-number">
              ¥{{ item.minRecharge }}-¥{{ item.maxRecharge }}
            </div>
          </div>
          <!-- 充值按钮 -->
          <div class="Recharge font-we-bold">充值</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {};
</script>
<style>
</style>