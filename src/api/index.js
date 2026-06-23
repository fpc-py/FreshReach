import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建 axios 实例
const instance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('merchant_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
instance.interceptors.response.use(
  (response) => {
    const res = response.data
    
    if (res.code !== 200 && res.code !== undefined) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    
    return res
  },
  (error) => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          ElMessage.error('登录已过期，请重新登录')
          localStorage.removeItem('merchant_token')
          router.push('/login')
          break
        case 403:
          ElMessage.error('没有权限访问')
          break
        case 404:
          ElMessage.error('请求资源不存在')
          break
        case 500:
          ElMessage.error('服务器错误')
          break
        default:
          // ElMessage.error(error.message || '网络错误')
      }
    } else {
      // ElMessage.error('网络连接失败')
    }
    return Promise.reject(error)
  }
)

export default instance

// 导出请求方法
export const request = (config) => {
  return instance(config)
}

export const get = (url, params = {}, config = {}) => {
  return instance.get(url, { params, ...config })
}

export const post = (url, data = {}, config = {}) => {
  return instance.post(url, data, config)
}

export const put = (url, data = {}, config = {}) => {
  return instance.put(url, data, config)
}

export const del = (url, params = {}, config = {}) => {
  return instance.delete(url, { params, ...config })
}
