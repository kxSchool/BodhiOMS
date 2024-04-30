<template>
    <div class="page">        
        <div class="nav-bar">
           <div class="nav_Blanck" v-if="isApp"></div>
                   
        </div>
        <div class="c-h-b">
            <div class="nav_Blanck"></div>
            <div class="c-h-bg"></div>
        </div>
        
    </div>
</template>

<script>
import { Icon } from "vant";

export default {
    name: "NavBar",
    components: {
        "van-icon": Icon,
    },
    computed: {
      
    },
    watch: {
        
    },
    data() {
        return {
            
            isApp:true,
        };
    },
    created(){
        
        
    },
    methods: {
        
        handleBack() {
            if(this.isOut){//返回APP
               window.location.href = 'uniwebview://game?action="close"';
            }else{//返回上一页
                this.$router.back()
            }
            
            
            
        },
    },
};
</script>
<style lang="less" >
.nav-bar {
  position: fixed;
  top: 0PX;
  left: 0;
  width: 100%;
  z-index: 99;
  background: #fff;
  .van-ellipsis {
        font-size: 28PX;
        font-weight: 400;
        color: #4d4d4c;
  }
    
}
.nav {
    padding: 0 15PX;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 50PX;    
    .nav-icon {
        position: absolute;
        left: 0PX;
        display: flex;
        align-items: center;
        flex: 1;
        .back {
            width: 20PX;
            height: 20PX;
            margin-left: 20PX;
        }
        .out {
            width: 20PX;
            height: 20PX;
            margin-left: 20PX;
            display: inline-block;
        }
    }
    .nav-title {
        font-size: 16PX;
        font-weight: 600;
        text-align: center;
        color: #151311;
        line-height: 22PX;
    }
    .right-nav{
        position: absolute;
        right: 20PX;
        display: flex;
        align-items: center;
        flex: 1;
        .deleteIcon{
            font-size: 14PX;
            font-weight: bold;
            color:#ff5f3f;
        }
        .orderIcon{
            color:#4D4D4C;
            font-size: 14PX;
            font-weight: bold;
            img {
                width: 15PX;
                height: 17PX;
                margin-right: 5PX;
            }
        }
    }
    
}
.c-h-bg{
    background: #fff;
    z-index: 98;
    height: 48PX;   
    width: 100%;
    top: 0PX;
    position: absolute;
}
.nav_Blanck{
    top: 0PX;
    height: 50PX;
    width: 100%;
}
</style>
