<template>
  <div class="login-container">
    <div class="login-header">
      <div class="logo">
        <span class="logo-text">鲜达骑手</span>
      </div>
      <p class="slogan">快速接单，轻松赚钱</p>
    </div>

    <div class="login-form">
      <div class="form-item">
        <label class="label">手机号</label>
        <input
          class="input"
          type="tel"
          v-model="form.phone"
          placeholder="请输入手机号"
          maxlength="11"
        />
      </div>

      <div class="form-item">
        <label class="label">密码</label>
        <input
          class="input"
          type="password"
          v-model="form.password"
          placeholder="请输入密码"
          maxlength="20"
          @keyup.enter="handleLogin"
        />
      </div>

      <button class="btn-login" @click="handleLogin" :disabled="loading">
        {{ loading ? '登录中...' : '登录' }}
      </button>
    </div>

    <div class="login-footer">
      <p class="tips">登录即表示同意《用户协议》和《隐私政策》</p>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useRiderStore } from '@/store/rider'
import { riderLogin } from '@/api/rider'
import { toast } from '@/utils/notify'

const router = useRouter()
const riderStore = useRiderStore()

const form = reactive({
  phone: '',
  password: ''
})

const loading = ref(false)

const handleLogin = async () => {
  if (!form.phone) {
    toast.warning('请输入手机号')
    return
  }
  if (!/^1[3-9]\d{9}$/.test(form.phone)) {
    toast.warning('手机号格式错误')
    return
  }
  if (!form.password) {
    toast.warning('请输入密码')
    return
  }
  if (form.password.length < 6) {
    toast.warning('密码长度不能少于6位')
    return
  }

  loading.value = true
  try {
    const res = await riderLogin(form)
    console.log('===== 登录接口返回原始数据 =====')
    console.log('res:', res)
    console.log('所有key:', Object.keys(res))
    
    const uid = res.userId ?? res.user_id ?? res.id ?? res.riderId ?? res.rider_id
    console.log('解析出的userId:', uid)
    
    const token = res.token || 'rider-token-' + uid
    riderStore.setToken(token)
    riderStore.setUserId(uid)
    riderStore.setRiderInfo(res)
    
    console.log('store.userId:', riderStore.userId)
    console.log('store.riderInfo:', riderStore.riderInfo)
    
    toast.success('登录成功')
    setTimeout(() => {
      router.push('/index')
    }, 500)
  } catch (e) {
    console.error('登录失败', e)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #FF6B35 0%, #FF8C5A 30%, #f5f5f5 60%);
  padding: 0 20px;
  display: flex;
  flex-direction: column;
}

.login-header {
  padding-top: 80px;
  text-align: center;
  margin-bottom: 60px;
}

.logo {
  width: 80px;
  height: 80px;
  background: #fff;
  border-radius: 50%;
  margin: 0 auto 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 20px rgba(255, 107, 53, 0.3);
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
  color: #FF6B35;
}

.slogan {
  font-size: 14px;
  color: #fff;
  opacity: 0.9;
  margin: 0;
}

.login-form {
  background: #fff;
  border-radius: 16px;
  padding: 30px 24px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
}

.form-item {
  margin-bottom: 24px;
}

.label {
  display: block;
  font-size: 14px;
  color: #333;
  margin-bottom: 10px;
  font-weight: 500;
}

.input {
  width: 100%;
  height: 44px;
  border: 1px solid #e5e5e5;
  border-radius: 8px;
  padding: 0 12px;
  font-size: 15px;
  box-sizing: border-box;
  outline: none;
  transition: border-color 0.2s;
}

.input:focus {
  border-color: #FF6B35;
}

.btn-login {
  width: 100%;
  height: 48px;
  background: linear-gradient(135deg, #FF6B35 0%, #FF8C5A 100%);
  color: #fff;
  border: none;
  border-radius: 24px;
  font-size: 16px;
  font-weight: 500;
  margin-top: 10px;
  cursor: pointer;
  transition: opacity 0.2s;
}

.btn-login:active {
  opacity: 0.8;
}

.btn-login:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.login-footer {
  margin-top: auto;
  padding: 30px 0;
  text-align: center;
}

.tips {
  font-size: 12px;
  color: #999;
  margin: 0;
}
</style>
