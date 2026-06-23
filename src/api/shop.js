import { get, post, put } from './index'

// 登录
export const login = (data) => {
  return post('/auth/login', data)
}

// 退出登录
export const logout = () => {
  return post('/auth/logout')
}

// 获取店铺信息
export const getShopInfo = () => {
  return get('/shop/info')
}

// 更新店铺信息
export const updateShopInfo = (data) => {
  return put('/shop/info', data)
}

// 更新营业状态
export const updateShopStatus = (isOpen) => {
  return put('/shop/status', { is_open: isOpen })
}

// 获取今日统计
export const getTodayStats = () => {
  return get('/shop/stats/today')
}

// 获取日期范围统计
export const getStatsByDateRange = (startDate, endDate) => {
  return get('/shop/stats/range', { start_date: startDate, end_date: endDate })
}
