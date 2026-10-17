import axios from 'axios'
import { useRiderStore } from '@/store/rider'
import { toast } from '@/utils/notify'

const service = axios.create({
  baseURL: '/api',
  timeout: 15000
})

const underscoreToCamel = (obj) => {
  if (Array.isArray(obj)) {
    return obj.map(underscoreToCamel)
  } else if (obj !== null && typeof obj === 'object') {
    return Object.keys(obj).reduce((acc, key) => {
      const camelKey = key.replace(/_([a-z])/g, (_, c) => c.toUpperCase())
      acc[camelKey] = underscoreToCamel(obj[key])
      return acc
    }, {})
  }
  return obj
}

const camelToUnderscore = (obj) => {
  if (Array.isArray(obj)) {
    return obj.map(camelToUnderscore)
  } else if (obj !== null && typeof obj === 'object') {
    return Object.keys(obj).reduce((acc, key) => {
      const underscoreKey = key.replace(/([A-Z])/g, '_$1').toLowerCase()
      acc[underscoreKey] = camelToUnderscore(obj[key])
      return acc
    }, {})
  }
  return obj
}

service.interceptors.request.use(
  (config) => {
    const riderStore = useRiderStore()
    if (riderStore.token) {
      config.headers['Authorization'] = 'Bearer ' + riderStore.token
    }
    if (config.data && typeof config.data === 'object') {
      console.log('===== 请求拦截器 =====')
      console.log('原始data:', config.data)
      config.data = camelToUnderscore(config.data)
      console.log('转换后data:', config.data)
    }
    if (config.params && typeof config.params === 'object') {
      console.log('原始params:', config.params)
      config.params = camelToUnderscore(config.params)
      console.log('转换后params:', config.params)
    }
    return config
  },
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response) => {
    const res = response.data
    console.log('===== 响应拦截器 =====')
    console.log('原始data:', res.data)
    if (res.code === 200) {
      const converted = underscoreToCamel(res.data)
      console.log('转换后data:', converted)
      return converted
    } else {
      toast.error(res.msg || '请求失败')
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
  },
  (error) => {
    console.error('Response error:', error)
    let message = '网络错误'
    if (error.response) {
      message = error.response.data?.msg || '请求失败'
      if (error.response.status === 401) {
        const riderStore = useRiderStore()
        riderStore.logout()
        window.location.href = '#/login'
      }
    }
    toast.error(message)
    return Promise.reject(error)
  }
)

export default service
