<template>
  <div style="height: 1.066667rem">
    <div class="disp-align-center" style="height: 1.066667rem">
      <van-icon
        size="20"
        name="arrow-left"
        style="position: absolute; left: 0.266666rem"
        @click="routerTo"
      />
      <div class="margin-auto font-we-bold title" style="color: #2b2c2f">
        {{ title }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      default: "SARO",
    },
  },
  data() {
    return {};
  },
  methods: {
    routerTo() {
      this.$router.go(-1);
    },
  },
};
</script>
<style>
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
</style>