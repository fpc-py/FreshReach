// src/stores/auth.js
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { loginApi, logoutApi,registerApi,getCaptchaApi } from '@/api/auth'
import router from '@/router'
import { 
  getToken, setToken, removeToken, 
  getUserInfo, setUserInfo, removeUserInfo 
} from '@/utils/token'
import { ElMessage } from 'element-plus'

export const useAuthStore = defineStore('auth', () => {
  // 1. 状态初始化使用工具函数
  const token = ref(getToken() || '')
  const merchantInfo = ref(getUserInfo() || null) // 对应 MainLayout 中的 merchantInfo

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  
  // 验证码相关状态
  const captchaKey = ref('')
  const captchaImg = ref('')



  // 获取验证码
  const getCaptcha = async () => {
    try {
      const res = await getCaptchaApi()
      // console.log(res)
      if (res.code === 200 || res.success) {
        captchaImg.value = res.data.img_base64
        captchaKey.value = res.data.code_key
      }
      // loading.value = false
    } catch (error) {
      console.error('Get captcha failed:', error)
    }
  }
  // 动作: 登录
  const login = async (loginForm) => {
  //前置判断key是否有效
  if (!captchaKey.value) {
    ElMessage.warning("请点击验证码图片重新加载")
    return
  }
  const params = {
    phone: loginForm.phone,
    password: loginForm.password,
    captcha_code: loginForm.captchaCode,
    code_key: captchaKey.value,
    img_base64: captchaImg.value
  }
  try{
const res = await loginApi(params)

    // 假设后端返回结构为 { code: 200, data: { token, user } }
      if (res.code === 200 || res.success) {
        const newToken = res.data.token ?? 0
        const newUser = res.data.user
        // 安全取值，user不存在自动赋值0普通员工
const role = res.data.role
localStorage.setItem('userRole', role)
        token.value = newToken
        merchantInfo.value = newUser
        
        setToken(newToken)
        setUserInfo(newUser)
        
        return { success: true }
      }else {
        return { success: false, message: res.message || '登录失败' }
      }
  }
      catch (error) {
      return { success: false, message: error.message || '网络错误' }
    }
  //后续token存储逻辑
}

  
  

  // 动作: 登出
 const logout = async () => {
    try {
      // 尝试调用后端接口，即使失败也要清除本地状态
      await logoutApi().catch(e => console.warn('Logout API failed', e))
    } finally {
      // 2. 清除本地状态 (使用工具函数)
      token.value = ''
      merchantInfo.value = null
      removeToken()
      removeUserInfo()

       captchaKey.value = ''
      captchaImg.value = ''
      
      // 3. 跳转登录页
      // 使用 replace 防止用户点击浏览器后退按钮回到受保护页面
      router.replace('/login')
    }
  }

  const register = async (registerForm) => {
    // try {
      const res = await registerApi(registerForm)
      if (res.code === 200 || res.success) {
        return { success: true, message: '注册成功' }
      } else {
        return { success: false, message: res.message || '注册失败' }
      }
    }
    //  catch (error) {
    //   return { success: false, message: error.message || '网络错误' }
    // }
  // }

  return {
    token,
    merchantInfo, // 导出 merchantInfo 供组件使用
     captchaKey,
    captchaImg,
    isLoggedIn,
    getCaptcha, 
    login,
    logout,
    register
    }
})