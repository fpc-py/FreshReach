import { defineStore } from 'pinia'

const extractUserId = (info) => {
  if (!info) return null
  const id = info.userId ?? info.user_id ?? info.id ?? info.riderId ?? info.rider_id
  return id != null ? Number(id) : null
}

const savedUserId = localStorage.getItem('riderUserId')
const savedRiderInfo = localStorage.getItem('riderInfo')
const parsedRiderInfo = savedRiderInfo ? JSON.parse(savedRiderInfo) : null

let initialUserId = null
if (savedUserId) {
  initialUserId = Number(savedUserId)
} else if (parsedRiderInfo) {
  initialUserId = extractUserId(parsedRiderInfo)
}

export const useRiderStore = defineStore('rider', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userId: initialUserId,
    riderInfo: parsedRiderInfo,
    isOnline: false
  }),
  getters: {
    isLoggedIn: (state) => !!state.token
  },
  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },
    setUserId(id) {
      const uid = id != null ? Number(id) : null
      this.userId = uid
      if (uid != null) {
        localStorage.setItem('riderUserId', String(uid))
      } else {
        localStorage.removeItem('riderUserId')
      }
    },
    setRiderInfo(info) {
      this.riderInfo = info
      if (info) {
        localStorage.setItem('riderInfo', JSON.stringify(info))
        const uid = extractUserId(info)
        if (uid != null) {
          this.setUserId(uid)
        }
      } else {
        localStorage.removeItem('riderInfo')
      }
    },
    setOnline(status) {
      this.isOnline = status
    },
    logout() {
      this.token = ''
      this.userId = null
      this.riderInfo = null
      this.isOnline = false
      localStorage.removeItem('token')
      localStorage.removeItem('riderUserId')
      localStorage.removeItem('riderInfo')
    }
  }
})
