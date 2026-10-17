App({
  globalData: {
    baseUrl: 'http://localhost:8080/api',
    token: '',
    userId: null,
    riderInfo: null,
    isOnline: false
  },

  onLaunch() {
    const token = wx.getStorageSync('token')
    const userId = wx.getStorageSync('riderUserId')
    const riderInfo = wx.getStorageSync('riderInfo')
    const isOnline = wx.getStorageSync('isOnline')
    
    if (token) {
      this.globalData.token = token
    }
    if (userId) {
      this.globalData.userId = userId
    }
    if (riderInfo) {
      this.globalData.riderInfo = riderInfo
    }
    if (isOnline) {
      this.globalData.isOnline = isOnline
    }
  },

  setToken(token) {
    this.globalData.token = token
    wx.setStorageSync('token', token)
  },

  setUserId(id) {
    const uid = id != null ? Number(id) : null
    this.globalData.userId = uid
    if (uid != null) {
      wx.setStorageSync('riderUserId', uid)
    } else {
      wx.removeStorageSync('riderUserId')
    }
  },

  setRiderInfo(info) {
    this.globalData.riderInfo = info
    if (info) {
      wx.setStorageSync('riderInfo', info)
      const uid = this.extractUserId(info)
      if (uid != null) {
        this.setUserId(uid)
      }
    } else {
      wx.removeStorageSync('riderInfo')
    }
  },

  setOnline(status) {
    this.globalData.isOnline = status
    wx.setStorageSync('isOnline', status)
  },

  extractUserId(info) {
    if (!info) return null
    const id = info.userId ?? info.user_id ?? info.id ?? info.riderId ?? info.rider_id
    return id != null ? Number(id) : null
  },

  logout() {
    this.globalData.token = ''
    this.globalData.userId = null
    this.globalData.riderInfo = null
    this.globalData.isOnline = false
    wx.removeStorageSync('token')
    wx.removeStorageSync('riderUserId')
    wx.removeStorageSync('riderInfo')
    wx.removeStorageSync('isOnline')
  },

  isLoggedIn() {
    return !!this.globalData.token
  },

  getUserId() {
    return this.globalData.userId
  }
})
