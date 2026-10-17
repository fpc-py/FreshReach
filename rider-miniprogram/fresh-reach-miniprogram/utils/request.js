const app = getApp()

const BASE_URL = 'http://localhost:8080/api'

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

const request = (options) => {
  return new Promise((resolve, reject) => {
    const token = app.globalData.token
    let data = options.data || {}
    let params = options.params || {}
    
    if (options.method && options.method.toUpperCase() !== 'GET') {
      data = camelToUnderscore(data)
    }
    params = camelToUnderscore(params)
    
    let url = BASE_URL + options.url
    const paramKeys = Object.keys(params)
    if (paramKeys.length > 0) {
      const queryStr = paramKeys.map(key => `${key}=${encodeURIComponent(params[key])}`).join('&')
      url += (url.indexOf('?') > -1 ? '&' : '?') + queryStr
    }

    wx.request({
      url: url,
      method: options.method || 'GET',
      data: data,
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? 'Bearer ' + token : ''
      },
      success: (res) => {
        const result = res.data
        if (result.code === 200) {
          const converted = underscoreToCamel(result.data)
          resolve(converted)
        } else {
          wx.showToast({
            title: result.msg || '请求失败',
            icon: 'none',
            duration: 2000
          })
          if (res.statusCode === 401) {
            app.logout()
            wx.reLaunch({
              url: '/pages/login/login'
            })
          }
          reject(new Error(result.msg || '请求失败'))
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
        wx.showToast({
          title: '网络错误',
          icon: 'none',
          duration: 2000
        })
        reject(err)
      }
    })
  })
}

export default request
