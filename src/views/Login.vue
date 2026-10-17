<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <div class="logo">
          <svg width="48" height="48" viewBox="0 0 48 48" fill="none">
            <circle cx="24" cy="24" r="24" fill="#27AE60"/>
            <path d="M24 12C17.373 12 12 17.373 12 24C12 30.627 17.373 36 24 36C30.627 36 36 30.627 36 24C36 17.373 30.627 12 24 12ZM24 32C18.477 32 14 27.523 14 22C14 16.477 18.477 12 24 12C29.523 12 34 16.477 34 22C34 27.523 29.523 32 24 32Z" fill="white"/>
            <circle cx="24" cy="24" r="6" fill="white"/>
          </svg>
        </div>
        <h1 class="title">鲜达外卖 · 商家管理</h1>
        <p class="subtitle">登录您的商家账号</p>
      </div>
      
      <el-form
        ref="formRef"
        :model="loginForm"
        :rules="formRules"
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="phone">
          <el-input
            v-model="loginForm.phone"
            placeholder="请输入手机号"
            size="large"
            :prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            :prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
               <!-- 新增验证码部分 -->
        <el-form-item prop="captchaCode">
          <div class="captcha-group">
            <el-input
              v-model="loginForm.captchaCode"
              placeholder="请输入验证码"
              prefix-icon="Key"
              size="large"
              style="flex: 1; margin-right: 10px;"
            />
            <div class="captcha-img-wrapper" @click="refreshCaptcha">
              <img 
                v-if="authStore.captchaImg" 
                :src="authStore.captchaImg" 
                alt="验证码" 
                class="captcha-img"
              />
              <span v-else class="loading-text">加载中...</span>
            </div>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-button"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>

      </el-form>
   <div class="footer-links">
       <span>还没有账号？</span>
      <router-link to="/register" class="link">立即注册</router-link>
   </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted} from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'


const router = useRouter()
const authStore = useAuthStore()

const formRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  phone: '',
  password: '',
  captchaCode: ''
})

const formRules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20位', trigger: 'blur' }
  ],
   captchaCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}
// 刷新验证码
const refreshCaptcha = () => {
  authStore.getCaptcha()
  loginForm.captchaCode = '' // 清空已输入的验证码
}


const handleLogin = async () => {
  try {
    await formRef.value.validate()
    loading.value = true
    
    const result = await authStore.login(loginForm)
    
    if (result.success) {
      ElMessage.success('登录成功')
      router.push('/dashboard')
    } else {
      ElMessage.error(result.message || '登录失败')
    }
  } catch (error) {
    ElMessage.error(error.message || '登录失败')
   // 验证失败
  } finally {
    loading.value = false
  }
}
// 页面加载时获取验证码
onMounted(() => {
  refreshCaptcha()
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #27AE60 0%, #1E8449 100%);
}

.login-box {
  width: 420px;
  padding: 40px;
  background: #FFFFFF;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo {
  margin-bottom: 16px;
}

.title {
  font-size: 24px;
  font-weight: 700;
  color: #1E293B;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 14px;
  color: #94A3B8;
}

.login-form {
  margin-bottom: 24px;
}

.login-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  background: #27AE60;
  border-color: #27AE60;
}

.login-button:hover {
  background: #2ECC71;
  border-color: #2ECC71;
}

.login-footer {
  text-align: center;
}

.tips {
  font-size: 12px;
  color: #94A3B8;
}
.footer-links {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
  color: #64748B;
}

.link {
  color: #27AE60;
  text-decoration: none;
  margin-left: 5px;
  font-weight: 500;
}

.link:hover {
  text-decoration: underline;
}

/* 验证码样式 */
.captcha-group {
  display: flex;
  align-items: center;
  width: 100%;
}

.captcha-img-wrapper {
  width: 110px;
  height: 40px; /* 与 el-input size=large 高度匹配 */
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f5f7fa;
  transition: border-color 0.3s;
}

.captcha-img-wrapper:hover {
  border-color: #409eff;
}

.captcha-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.loading-text {
  font-size: 12px;
  color: #909399;
}
</style>
