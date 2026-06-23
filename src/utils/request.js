import axios from 'axios'
import { getToken, removeToken } from './token'
import { ElMessage } from 'element-plus'
import router from '@/router'

const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000
})

// 请求拦截器：自动携带Token
service.interceptors.request.use(config => {
  const token = getToken()
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

// 响应拦截器：统一处理登录过期、业务错误
service.interceptors.response.use(
  res =>{
    const data = res.data
  if(data.code !== 200){
    ElMessage.error(data.msg)
    return Promise.reject(data)
  }
  return data},
  err => {
    const status = err.response?.status
    // 401 Token过期，强制跳转登录页
    if (status === 401) {
      removeToken()
      ElMessage.error('登录已失效，请重新登录')
      router.push('/login')
    }
    else if(status === 403){
      ElMessage.error('没有权限访问')
    }
     else {
      ElMessage.error('服务器异常')
    }
    return Promise.reject(err)
  }
)
export default service