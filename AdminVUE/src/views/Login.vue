<template>
  <div class="login_container">
  <canvas v-if="this.biu" id="canvas"></canvas>
    <div class="login_box">
        
      <!-- 头像 -->
      <div class="avatar_box">
         <div class="animate__animated animate__tada" animate__infinite infinite>
              <img class="logoimg" src="../assets/logo1.png" />
          </div>
        <h2 class="title_tit">企鹅农场SARO交易管理平台</h2>
      </div>
      <!-- 登录表单区域 -->

      <el-form :rules="loginFormRules" class="login_form" :model="loginForm" ref="loginFormRef">
        <!-- name -->
        <el-form-item prop="userName">
          <el-input prefix-icon="el-icon-user-solid" v-model="loginForm.userName" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <!-- pwd -->
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-s-goods" v-model="loginForm.password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input prefix-icon="el-icon-eleme" v-model="loginForm.code" placeholder="请输入验证码"></el-input>
        </el-form-item>
        <el-form-item class="btns">
          <el-button type="primary" class="login" @click="login">登录</el-button>
          <!-- <el-button type="info" @click="loginReset">重置</el-button> -->
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
import { login} from '@/api/login'
export default {
  data() {
    return {
      biu:true,
      windowWidth: document.documentElement.clientWidth,  //实时屏幕宽度
      windowHeight: document.documentElement.clientHeight,   //实时屏幕高度

      loginForm: {
        userName: '',
        password: '',
        code: ''
      },
      //表单验证规则
      loginFormRules: {
        userName: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 16, message: '长度在 6 到 16 个字符', trigger: 'blur' }
        ]
      }
    }
  },

 watch: {
      windowHeight (val) {
        let that = this;
        console.log(document.documentElement.clientHeight)
        this.biu = false
        this.$router.go(0)
        console.log("实时屏幕高度：",val, that.windowHeight );
      },
      windowWidth (val) {
        let that = this;
        console.log(document.documentElement.clientHeight)
        this.biu = false
        this.$router.go(0)
        console.log("实时屏幕宽度：",val, that.windowWeight );
      }
    },


   mounted(){
        var canvas = document.getElementById('canvas'),
        ctx = canvas.getContext('2d'),
        w = canvas.width = document.documentElement.clientWidth,
        h = canvas.height = document.documentElement.clientHeight,
          
        hue = 217,
        stars = [],
        count = 0,
        maxStars = 1400;

      // Thanks @jackrugile for the performance tip! http://codepen.io/jackrugile/pen/BjBGoM
      // Cache gradient
      var canvas2 = document.createElement('canvas'),
          ctx2 = canvas2.getContext('2d');
          canvas2.width = 100;
          canvas2.height = 100;
      var half = canvas2.width/2,
          gradient2 = ctx2.createRadialGradient(half, half, 0, half, half, half);
          gradient2.addColorStop(0.025, '#fff');
          gradient2.addColorStop(0.1, 'hsl(' + hue + ', 61%, 33%)');
          gradient2.addColorStop(0.25, 'hsl(' + hue + ', 64%, 6%)');
          gradient2.addColorStop(1, 'transparent');

          ctx2.fillStyle = gradient2;
          ctx2.beginPath();
          ctx2.arc(half, half, half, 0, Math.PI * 2);
          ctx2.fill();

      // End cache

      function random(min, max) {
        if (arguments.length < 2) {
          max = min;
          min = 0;
        }
        
        if (min > max) {
          var hold = max;
          max = min;
          min = hold;
        }

        return Math.floor(Math.random() * (max - min + 1)) + min;
      }

      var Star = function() {

        this.orbitRadius = random(w / 2 - 50);
        this.radius = random(100, this.orbitRadius) / 10;
        this.orbitX = w / 2;
        this.orbitY = h / 2;
        this.timePassed = random(0, maxStars);
        this.speed = random(this.orbitRadius) / 900000;
        this.alpha = random(2, 10) / 10;

        count++;
        stars[count] = this;
      }

      Star.prototype.draw = function() {
        var x = Math.sin(this.timePassed + 1) * this.orbitRadius + this.orbitX,
            y = Math.cos(this.timePassed) * this.orbitRadius/2 + this.orbitY,
            twinkle = random(10);

        if (twinkle === 1 && this.alpha > 0) {
          this.alpha -= 0.05;
        } else if (twinkle === 2 && this.alpha < 1) {
          this.alpha += 0.05;
        }

        ctx.globalAlpha = this.alpha;
          ctx.drawImage(canvas2, x - this.radius / 2, y - this.radius / 2, this.radius, this.radius);
        this.timePassed += this.speed;
      }

      for (var i = 0; i < maxStars; i++) {
        new Star();
      }

      function animation() {
          ctx.globalCompositeOperation = 'source-over';
          ctx.globalAlpha = 0.8;
          ctx.fillStyle = 'hsla(' + hue + ', 64%, 6%, 1)';
          ctx.fillRect(0, 0, w, h)
        
        ctx.globalCompositeOperation = 'lighter';
        for (var i = 1, l = stars.length; i < l; i++) {
          stars[i].draw();
        };  
        
        window.requestAnimationFrame(animation);
      }

      animation();


      var that = this;
        // <!--把window.onresize事件挂在到mounted函数上-->
        window.onresize = () => {
            return (() => {
              window.fullHeight = document.documentElement.clientHeight;
              window.fullWidth = document.documentElement.clientWidth;
              that.windowHeight = window.fullHeight;  // 高
              that.windowWidth = window.fullWidth; // 宽
            })()
          };
  },

  methods: {
    loginReset() {
      this.$refs.loginFormRef.resetFields()
    },
     login() {
      this.$refs.loginFormRef.validate(async (valid) => {
        if (!valid) return
        const res = await login(this.loginForm)
        if (res.code === 1) {
          localStorage.setItem('userName', this.loginForm.userName)
          localStorage.setItem('token', res.data.token)
          this.$router.push({ path: '/home' })
        } else {
            this.$message.error(res.message)
        }
      })
    },

  }
}
</script>
<style lang="less" scoped>
.login_container {
 background: #060e1b;
  height: 100vh;
}
.login_box {
  width: 450px;
  height: 350px;
  background-color: #fff;
  border-radius: 3px;
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: -150px;
  margin-left: -225px;

  .avatar_box {
    text-align: center;
  }
  .logoimg {
    height: 130px;
    width: 130px;
    border-radius: 50%;
    padding: 10px;
    position: absolute;
    left: 50%;
    top: -50%;
    transform: translate(-50%, -50%);
    // background-color: #fff;

  }
}
.btns {
  display: flex;
  justify-content: flex-end;
}
.login_form {
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 0 50px;
  box-sizing: border-box;
}
.title_tit{
    margin-top: 30px;
}
.login {
  width: 350px;
}
.animate__animated.animate__tada {
    position: absolute;
    left: 50%;
    top: -30%;
    transform: translate(-50%, -50%);
  --animate-duration: 2s;
  animation-iteration-count:infinite;

}

</style>
