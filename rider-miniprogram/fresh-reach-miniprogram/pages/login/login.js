const app = getApp()
import { riderLogin } from '../../utils/api.js'

Page({
  data: {
    phone: '',
    password: '',
    loading: false
  },

  onLoad() {
    if (app.isLoggedIn()) {
      wx.switchTab({
        url: '/pages/index/index'
      })
    }
  },

  onPhoneInput(e) {
    this.setData({
      phone: e.detail.value
    })
  },

  onPasswordInput(e) {
    this.setData({
      password: e.detail.value
    })
  },

  handleLogin() {
    const { phone, password } = this.data

    if (!phone) {
      wx.showToast({
        title: '请输入手机号',
        icon: 'none'
      })
      return
    }
    if (!/^1[3-9]\d{9}$/.test(phone)) {
      wx.showToast({
        title: '手机号格式错误',
        icon: 'none'
      })
      return
    }
    if (!password) {
      wx.showToast({
        title: '请输入密码',
        icon: 'none'
      })
      return
    }
    if (password.length < 6) {
      wx.showToast({
        title: '密码长度不能少于6位',
        icon: 'none'
      })
      return
    }

    this.setData({ loading: true })

    riderLogin({ phone, password })
      .then(res => {
        console.log('登录返回:', res)
        
        const uid = res.userId ?? res.user_id ?? res.id ?? res.riderId ?? res.rider_id
        const token = res.token || ('rider-token-' + uid)
        
        app.setToken(token)
        app.setUserId(uid)
        app.setRiderInfo(res)
        
        wx.showToast({
          title: '登录成功',
          icon: 'success',
          duration: 1500
        })
        
        setTimeout(() => {
          wx.switchTab({
            url: '/pages/index/index'
          })
        }, 1500)
      })
      .catch(err => {
        console.error('登录失败:', err)
      })
      .finally(() => {
        this.setData({ loading: false })
      })
  }
})
