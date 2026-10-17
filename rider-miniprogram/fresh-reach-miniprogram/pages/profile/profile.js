const app = getApp()
import { updateRiderStatus } from '../../utils/api.js'

Page({
  data: {
    riderInfo: {},
    isOnline: false,
    usernameFirst: ''
  },

  onLoad() {
    this.checkAuth()
  },

  onShow() {
    const riderInfo = app.globalData.riderInfo || {}
    this.setData({
      riderInfo,
      isOnline: app.globalData.isOnline,
      usernameFirst: riderInfo.username ? riderInfo.username.charAt(0) : '骑'
    })
  },

  checkAuth() {
    if (!app.isLoggedIn()) {
      wx.redirectTo({
        url: '/pages/login/login'
      })
    }
  },

  toggleStatus() {
    const userId = app.getUserId()
    if (!userId) {
      wx.showToast({
        title: '请先登录或用户信息不完整',
        icon: 'none'
      })
      wx.redirectTo({
        url: '/pages/login/login'
      })
      return
    }

    const newStatus = !this.data.isOnline
    const statusValue = newStatus ? 1 : 0

    updateRiderStatus(statusValue, userId)
      .then(() => {
        this.setData({ isOnline: newStatus })
        app.setOnline(newStatus)
        wx.showToast({
          title: newStatus ? '已上线' : '已下线',
          icon: 'success'
        })
      })
      .catch(err => {
        console.error('切换状态失败', err)
      })
  },

  goToIncome() {
    wx.navigateTo({
      url: '/pages/income/income'
    })
  },

  goToOrders() {
    wx.switchTab({
      url: '/pages/orders/orders'
    })
  },

  handleLogout() {
    wx.showModal({
      title: '退出登录',
      content: '确定要退出登录吗？',
      confirmText: '退出',
      confirmColor: '#FF4D4F',
      success: (res) => {
        if (res.confirm) {
          app.logout()
          wx.showToast({
            title: '已退出登录',
            icon: 'success'
          })
          setTimeout(() => {
            wx.reLaunch({
              url: '/pages/login/login'
            })
          }, 1500)
        }
      }
    })
  }
})
