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
    order: null,
    orderId: ''
  },

  onLoad(options) {
    this.setData({ orderId: options.id || '' })
    this.checkAuth()
  },

  onShow() {
    if (app.isLoggedIn() && this.data.orderId) {
      this.loadOrderDetail()
    }
  },

  checkAuth() {
    if (!app.isLoggedIn()) {
      wx.redirectTo({
        url: '/pages/login/login'
      })
    }
  },

  loadOrderDetail() {
    const userId = app.getUserId()
    const orderId = this.data.orderId
    if (!userId || !orderId) return

    wx.showLoading({ title: '加载中...' })

    getMyOrders(userId)
      .then(res => {
        wx.hideLoading()
        const orders = res || []
        const order = orders.find(o => o.id === orderId)
        if (order) {
          const enrichedOrder = {
            ...order,
            statusText: STATUS_MAP[order.status] || order.status,
            createTimeText: this.formatTime(order.createTime),
            finishTimeText: this.formatTime(order.finishTime)
          }
          this.setData({ order: enrichedOrder })
        } else {
          wx.showToast({
            title: '订单不存在',
            icon: 'none'
          })
          setTimeout(() => {
            wx.navigateBack()
          }, 1000)
        }
      })
      .catch(err => {
        wx.hideLoading()
        console.error('加载订单失败', err)
      })
  },

  handlePickup() {
    const order = this.data.order
    if (!order) return

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
              setTimeout(() => {
                wx.navigateBack()
              }, 1000)
            })
            .catch(err => {
              console.error('取货失败', err)
            })
        }
      }
    })
  },

  handleDeliver() {
    const order = this.data.order
    if (!order) return

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
              setTimeout(() => {
                wx.navigateBack()
              }, 1000)
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
    const year = date.getFullYear()
    const month = (date.getMonth() + 1).toString().padStart(2, '0')
    const day = date.getDate().toString().padStart(2, '0')
    const hours = date.getHours().toString().padStart(2, '0')
    const minutes = date.getMinutes().toString().padStart(2, '0')
    const seconds = date.getSeconds().toString().padStart(2, '0')
    return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds
  }
})
