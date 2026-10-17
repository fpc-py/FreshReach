const app = getApp()
import { getMyOrders, pickupOrder, deliverOrder } from '../../utils/api.js'

const STATUS_MAP = {
  'PENDING': '待支付',
  'PAID': '已支付',
  'CONFIRMED': '待取货',
  'PICKED_UP': '已取货',
  'DELIVERING': '配送中',
  'DELIVERED': '已送达',
  'COMPLETED': '已完成',
  'CANCELLED': '已取消'
}

Page({
  data: {
    orders: [],
    filteredOrders: [],
    currentTab: 'all'
  },

  onLoad() {
    this.checkAuth()
  },

  onShow() {
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
    }
  },

  loadOrders() {
    const userId = app.getUserId()
    if (!userId) return
    
    getMyOrders(userId)
      .then(res => {
        const orders = (res || []).map(order => ({
          ...order,
          statusText: STATUS_MAP[order.status] || order.status,
          createTimeText: this.formatTime(order.createTime)
        }))
        this.setData({ orders })
        this.filterOrders()
      })
      .catch(err => {
        console.error('获取订单失败', err)
      })
  },

  switchTab(e) {
    const tab = e.currentTarget.dataset.tab
    this.setData({ currentTab: tab })
    this.filterOrders()
  },

  filterOrders() {
    const { orders, currentTab } = this.data
    let filtered = orders
    
    if (currentTab !== 'all') {
      filtered = orders.filter(order => {
        if (currentTab === 'picking') {
          return order.status === 'CONFIRMED' || order.status === 'PAID'
        }
        if (currentTab === 'delivering') {
          return order.status === 'PICKED_UP' || order.status === 'DELIVERING'
        }
        if (currentTab === 'completed') {
          return order.status === 'COMPLETED' || order.status === 'DELIVERED'
        }
        return true
      })
    }
    
    this.setData({ filteredOrders: filtered })
  },

  goToDetail(e) {
    const order = e.currentTarget.dataset.order
    wx.navigateTo({
      url: '/pages/order-detail/order-detail?id=' + order.id
    })
  },

  handlePickup(e) {
    const order = e.currentTarget.dataset.order
    
    wx.showModal({
      title: '确认取货',
      content: '确定已取到货物吗？',
      confirmText: '确认取货',
      success: (res) => {
        if (res.confirm) {
          pickupOrder(order.id)
            .then(() => {
              wx.showToast({
                title: '取货成功',
                icon: 'success'
              })
              this.loadOrders()
            })
            .catch(err => {
              console.error('取货失败', err)
            })
        }
      }
    })
  },

  handleDeliver(e) {
    const order = e.currentTarget.dataset.order
    
    wx.showModal({
      title: '确认送达',
      content: '确定已送达目的地吗？',
      confirmText: '确认送达',
      success: (res) => {
        if (res.confirm) {
          deliverOrder(order.id)
            .then(() => {
              wx.showToast({
                title: '送达成功',
                icon: 'success'
              })
              this.loadOrders()
            })
            .catch(err => {
              console.error('送达失败', err)
            })
        }
      }
    })
  },

  formatTime(time) {
    if (!time) return ''
    const date = new Date(time)
    const month = (date.getMonth() + 1).toString().padStart(2, '0')
    const day = date.getDate().toString().padStart(2, '0')
    const hours = date.getHours().toString().padStart(2, '0')
    const minutes = date.getMinutes().toString().padStart(2, '0')
    return month + '-' + day + ' ' + hours + ':' + minutes
  }
})
