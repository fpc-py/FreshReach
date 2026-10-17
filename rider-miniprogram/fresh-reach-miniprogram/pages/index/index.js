const app = getApp()
import { getAvailableOrders, grabOrder, updateRiderStatus } from '../../utils/api.js'

Page({
  data: {
    orders: [],
    loading: false,
    isOnline: false
  },

  onLoad() {
    this.checkAuth()
  },

  onShow() {
    this.setData({
      isOnline: app.globalData.isOnline
    })
    if (app.isLoggedIn()) {
      this.loadOrders()
    }
  },

  onPullDownRefresh() {
    this.loadOrders()
    wx.stopPullDownRefresh()
  },

  checkAuth() {
    if (!app.isLoggedIn()) {
      wx.redirectTo({
        url: '/pages/login/login'
      })
      return
    }
    const userId = app.getUserId()
    if (!userId) {
      if (app.globalData.riderInfo) {
        app.setRiderInfo(app.globalData.riderInfo)
      }
      if (!app.getUserId()) {
        app.logout()
        wx.redirectTo({
          url: '/pages/login/login'
        })
      }
    }
  },

  loadOrders() {
    if (!app.isLoggedIn()) return
    
    this.setData({ loading: true })
    getAvailableOrders()
      .then(res => {
        const orders = (res || []).map(order => ({
          ...order,
          createTimeText: this.formatTime(order.createTime)
        }))
        this.setData({ orders })
      })
      .catch(err => {
        console.error('获取可抢订单失败', err)
      })
      .finally(() => {
        this.setData({ loading: false })
      })
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

  handleGrab(e) {
    const order = e.currentTarget.dataset.order
    const userId = app.getUserId()
    
    if (!userId) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      })
      wx.redirectTo({
        url: '/pages/login/login'
      })
      return
    }
    if (!this.data.isOnline) {
      wx.showToast({
        title: '请先上线',
        icon: 'none'
      })
      return
    }

    wx.showModal({
      title: '抢单确认',
      content: '确定要抢这个订单吗？',
      confirmText: '立即抢单',
      success: (res) => {
        if (res.confirm) {
          grabOrder({
            id: userId,
            orderId: order.id
          })
            .then(() => {
              wx.showToast({
                title: '抢单成功',
                icon: 'success'
              })
              this.loadOrders()
            })
            .catch(err => {
              console.error('抢单失败', err)
            })
        }
      }
    })
  },

  formatTime(time) {
    if (!time) return ''
    const date = new Date(time)
    const hours = date.getHours().toString().padStart(2, '0')
    const minutes = date.getMinutes().toString().padStart(2, '0')
    return hours + ':' + minutes
  }
})
