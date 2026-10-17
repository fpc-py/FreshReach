import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getShopInfo, updateShopInfo as apiUpdateShopInfo, updateShopStatus as apiUpdateShopStatus } from '@/api/shop'

export const useShopStore = defineStore('shop', () => {
  const shopInfo = ref({
    id: 1,
    name: '鲜达美食坊',
    phone: '13800138000',
    address: '广东省深圳市南山区科技园',
    isOpen: true,
    openTime: '09:00',
    closeTime: '22:00',
    deliveryFee: 3,
    minAmount: 20,
    notice: '欢迎光临，新客立减5元！'
  })
  const loading = ref(false)

  // 获取店铺信息
  const fetchShopInfo = async () => {
    loading.value = true
    try {
      const res = await getShopInfo()
      if (res.code === 200) {
        const data = Array.isArray(res.data) ? res.data[0] : res.data
        shopInfo.value = {
          id: data.id,
          name: data.name,
          address: data.address,
          isOpen: data.is_open === 1 || data.is_open === true,
          openTime: data.open_time,
          closeTime: data.close_time,
          deliveryFee: data.delivery_fee,
          minAmount: data.min_amount,
          notice: data.notice
        }
      }
    } finally {
      loading.value = false
    }
  }

  // 更新店铺信息
  const updateShopInfo = async (data) => {
    const res = await apiUpdateShopInfo(data)
    if (res.code === 200) {
      shopInfo.value = { ...shopInfo.value, ...data }
      return { success: true }
    }
    return { success: false, message: res.message }
  }

  // 更新营业状态
  const updateShopStatus = async (isOpen) => {
    const res = await apiUpdateShopStatus(isOpen)
    if (res.code === 200) {
      shopInfo.value.isOpen = isOpen
      return { success: true }
    }
    return { success: false, message: res.message }
  }

  return {
    shopInfo,
    loading,
    fetchShopInfo,
    updateShopInfo,
    updateShopStatus
  }
})
