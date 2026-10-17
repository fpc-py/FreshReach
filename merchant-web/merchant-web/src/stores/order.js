import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
// import { mockApi } from '@/mock/orders'
// import { getOrderList, getOrderDetail, acceptOrder, rejectOrder, finishPreparing, agreeRefund, rejectRefund } from '@/api/order'
import{ getTodayStats, getStatsByDateRange} from '@/api/shop'
import { getOrderList, getOrderDetail, acceptOrder as apiAcceptOrder, rejectOrder as apiRejectOrder, finishPreparing as apiFinishPreparing, agreeRefund as apiAgreeRefund, rejectRefund as apiRejectRefund } from '@/api/order'

export const useOrderStore = defineStore('order', () => {
  const orders = ref([])
  const currentOrder = ref(null)
  const loading = ref(false)
  const total = ref(0)
  const stats = ref({
    pendingCount: 0,
    totalAmount: 0,
    orderCount: 0,
    productOnSaleCount: 0
  })

  // 状态映射
  const statusMap = {
    pending: '待接单',
    preparing: '备餐中',
    delivering: '配送中',
    completed: '已完成',
    cancelled: '已取消',
    refunded: '已退款',
    refunding: '退款中',
    rejected: '退款驳回'
  }

  const statusOptions = [
    { label: '全部', value: 'all' },
    { label: '待接单', value: 'pending' },
    { label: '备餐中', value: 'preparing' },
    { label: '配送中', value: 'delivering' },
    { label: '已完成', value: 'completed' },
    { label: '已取消', value: 'cancelled' },
    { label: '退款中', value: 'refunding' }
  ]

  // 获取订单列表
  const fetchOrders = async (params = {}) => {
    loading.value = true
    try {
      const requestParams = { ...params }
      // 如果 status 为 'all' 或 undefined，删除该字段，不传给后端，避免后端误解析
      if (!requestParams.status || requestParams.status === 'all') {
        delete requestParams.status
      }
      const res = await getOrderList(requestParams)
      if (res.code === 200) {
        orders.value = res.data.records
        total.value = res.data.total
      }
    } finally {
      loading.value = false
    }
  }

  // 获取订单详情
  const fetchOrderDetail = async (id) => {
    loading.value = true
    try {
           const res = await getOrderDetail(id)

      if (res.code === 200) {
        currentOrder.value = res.data
        return res.data
      }
    } finally {
      loading.value = false
    }
  }

  // 接单
  const acceptOrder = async (id) => {
        const res = await apiAcceptOrder(id)

    if (res.code === 200) {
      const order = orders.value.find(o => o.id === id)
      if (order) order.status = 'preparing'
      return { success: true }
    }
    return { success: false, message: res.message }
  }

  // 拒单
  const rejectOrder = async (id, cancelReason) => {
        const res = await apiRejectOrder(id, cancelReason)

    if (res.code === 200) {
      const order = orders.value.find(o => o.id === id)
      if (order) {
        order.status = 'cancelled'
        order.cancelReason = cancelReason
      }
      return { success: true }
    }
    return { success: false, message: res.message }
  }

  // 完成备餐
  const finishPreparing = async (id) => {
        const res = await apiFinishPreparing(id)

    if (res.code === 200) {
      const order = orders.value.find(o => o.id === id)
      if (order) order.status = 'delivering'
      return { success: true }
    }
    return { success: false, message: res.message }
  }

  // 同意退款
  const agreeRefund = async (id) => {
        const res = await apiAgreeRefund(id)

    if (res.code === 200) {
      const order = orders.value.find(o => o.id === id)
      if (order) order.status = 'refunded'
      return { success: true }
    }
    return { success: false, message: res.message }
  }

  // 拒绝退款
  const rejectRefund = async (id, cancelReason) => {
        const res = await apiRejectRefund(id, cancelReason)

    if (res.code === 200) {
      const order = orders.value.find(o => o.id === id)
      if (order) {
        order.status = 'rejected'
        order.rejectReason = cancelReason
      }
      return { success: true }
    }
    return { success: false, message: res.message }
  }

  // 获取今日统计
  const fetchTodayStats = async () => {
       const res = await getTodayStats()

    if (res.code === 200) {
      const data = Array.isArray(res.data) ? res.data[0] : res.data
      stats.value = {
        pendingCount: stats.value.pendingCount,
        totalAmount: data.total_amount || 0,
        orderCount: data.total_orders || 0,
        productOnSaleCount: stats.value.productOnSaleCount
      }
    }
  }

  // 获取日期范围统计
  const fetchStatsByDateRange = async (startDate, endDate) => {
       const res = await getStatsByDateRange(startDate, endDate)

    if (res.code === 200) {
      return res.data
    }
    return null
  }

  return {
    orders,
    currentOrder,
    loading,
    total,
    stats,
    statusMap,
    statusOptions,
    fetchOrders,
    fetchOrderDetail,
    acceptOrder,
    rejectOrder,
    finishPreparing,
    agreeRefund,
    rejectRefund,
    fetchTodayStats,
    fetchStatsByDateRange
  }
})
