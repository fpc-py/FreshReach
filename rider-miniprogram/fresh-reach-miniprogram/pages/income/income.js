const app = getApp()
import { getIncomeStats, getMyOrders } from '../../utils/api.js'

Page({
  data: {
    periods: ['今日', '本周', '本月'],
    periodIndex: 0,
    periodText: '今日',
    stats: {
      totalAmount: 0,
      totalOrders: 0,
      completedOrders: 0,
      totalDeliveryFee: 0,
      todayOrders: 0
    },
    incomeList: []
  },

  onLoad() {
    this.checkAuth()
    this.loadStats()
  },

  onShow() {
    this.loadStats()
  },

  checkAuth() {
    if (!app.isLoggedIn()) {
      wx.redirectTo({
        url: '/pages/login/login'
      })
    }
  },

  onPeriodChange(e) {
    const index = Number(e.detail.value)
    this.setData({
      periodIndex: index,
      periodText: this.data.periods[index]
    })
    this.loadStats()
  },

  loadStats() {
    const userId = app.getUserId()
    if (!userId) return

    getIncomeStats(userId)
      .then(res => {
        this.setData({
          stats: {
            totalAmount: res.totalAmount || 0,
            totalOrders: res.totalOrders || 0,
            completedOrders: res.completedOrders || 0,
            totalDeliveryFee: res.totalDeliveryFee || res.totalAmount || 0,
            todayOrders: res.todayOrders || 0
          }
        })
      })
      .catch(err => {
        console.error('获取收入统计失败', err)
      })

    this.loadIncomeList()
  },

  loadIncomeList() {
    const userId = app.getUserId()
    if (!userId) return

    getMyOrders(userId)
      .then(res => {
        const list = (res || [])
          .filter(order => order.status === 'COMPLETED' || order.status === 'DELIVERED')
          .map(order => ({
            id: order.id,
            orderId: order.id,
            amount: order.deliveryFee || 0,
            timeText: this.formatTime(order.updateTime || order.createTime)
          }))
          .sort((a, b) => new Date(b.timeText) - new Date(a.timeText))
        
        this.setData({ incomeList: list })
      })
      .catch(err => {
        console.error('获取收入明细失败', err)
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
