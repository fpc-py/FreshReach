// src/utils/token.js
const TOKEN_KEY = 'merchant_token'
const USER_INFO_KEY = 'merchant_info'

export const getToken = () => localStorage.getItem(TOKEN_KEY)
export const setToken = (token) => localStorage.setItem(TOKEN_KEY, token)
export const removeToken = () => localStorage.removeItem(TOKEN_KEY)

export const getUserInfo = () => {
  const dataStr = localStorage.getItem(USER_INFO_KEY)
  // 为空 / 为undefined字符串，直接返回null
  if (!dataStr || dataStr === 'undefined') {
    return null
  }
  return JSON.parse(dataStr)
}
// export const getUserInfo = () => JSON.parse(localStorage.getItem(USER_INFO_KEY) || 'null')
export const setUserInfo = (info) => localStorage.setItem(USER_INFO_KEY, JSON.stringify(info))
export const removeUserInfo = () => localStorage.removeItem(USER_INFO_KEY)