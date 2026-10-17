<template>
  <div class="register-container">
    <div class="register-card">
      <div class="header">
        <h2>商家入驻注册</h2>
        <p>欢迎加入鲜达外卖商家平台</p>
      </div>
      
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="rules"
        label-width="0"
        class="register-form"
      >
        <el-form-item prop="merchantName">
          <el-input
            v-model="registerForm.merchantName"
            placeholder="请输入店铺名称"
            prefix-icon="Shop"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="phone">
          <el-input
            v-model="registerForm.phone"
            placeholder="请输入手机号"
            prefix-icon="Iphone"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入登录账号"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码 (6-20位)"
            prefix-icon="Lock"
            show-password
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请确认密码"
            prefix-icon="Lock"
            show-password
            size="large"
          />
        </el-form-item>
        
        <el-button
          type="primary"
          size="large"
          class="submit-btn"
          :loading="loading"
          @click="handleRegister"
        >
          立即注册
        </el-button>
        
        <div class="footer-links">
          <span>已有账号？</span>
          <router-link to="/login" class="link">去登录</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Shop, Iphone, User, Lock } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = reactive({
  merchantName: '',
  phone: '',
  username: '',
  password: '',
  confirmPassword: ''
})

// 自定义验证规则：确认密码
const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

const rules = {
  merchantName: [{ required: true, message: '请输入店铺名称', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  username: [{ required: true, message: '请输入登录账号', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: 'blur' }]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 移除 confirmPassword，只发送后端需要的数据
        const { confirmPassword, ...params } = registerForm
        
        const result = await authStore.register(params)
        
        if (result.success) {
          ElMessage.success(result.message || '注册成功，请登录')
          router.push('/login')
        } else {
          ElMessage.error(result.message)
        }
      } catch (error) {
        ElMessage.error('注册失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.register-card {
  width: 400px;
  padding: 40px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.header {
  text-align: center;
  margin-bottom: 30px;
}

.header h2 {
  margin: 0 0 10px;
  color: #1E293B;
  font-size: 24px;
}

.header p {
  margin: 0;
  color: #64748B;
  font-size: 14px;
}

.register-form {
  margin-top: 20px;
}

.submit-btn {
  width: 100%;
  margin-top: 10px;
  background: #27AE60;
  border-color: #27AE60;
}

.submit-btn:hover {
  background: #219a52;
  border-color: #219a52;
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
</style>