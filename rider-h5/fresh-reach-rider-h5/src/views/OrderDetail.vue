<template>
  <div class="page-container" v-if="order">
    <div class="status-header" :class="getStatusClass(order.status)">
      <span class="status-text">{{ getStatusText(order.status) }}</span>
      <span class="status-tip" v-if="order.status === 'CONFIRMED' || order.status === 'PAID'">请尽快前往取货</span>
      <span class="status-tip" v-else-if="order.status === 'PICKED_UP' || order.status === 'DELIVERING'">正在配送中，请尽快送达</span>
    </div>

    <div class="section card">
      <div class="section-title">配送信息</div>
      <div class="address-block">
        <div class="address-item">
          <div class="dot shop-dot"></div>
          <div class="address-content">
            <span class="address-type">取货点</span>
            <span class="address-detail">{{ order.detail || '商家地址' }}</span>
          </div>
        </div>
        <div class="address-line"></div>
        <div class="address-item">
          <div class="dot user-dot"></div>
          <div class="address-content">
            <span class="address-type">收货点</span>
            <span class="address-detail">{{ order.province }}{{ order.city }}{{ order.district }}{{ order.detail }}</span>
            <span class="receiver" v-if="order.name">{{ order.name }} {{ order.phone }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="section card">
      <div class="section-title">订单信息</div>
      <div class="info-row">
        <span class="info-label">订单号</span>
        <span class="info-value">{{ order.id }}</span>
      </div>
      <div class="info-row">
        <span class="info-label">下单时间</span>
        <span class="info-value">{{ formatTime(order.createTime) }}</span>
      </div>
      <div class="info-row" v-if="order.finishTime">
        <span class="info-label">完成时间</span>
        <span class="info-value">{{ formatTime(order.finishTime) }}</span>
      </div>
      <div class="info-row" v-if="order.remark">
        <span class="info-label">备注</span>
        <span class="info-value">{{ order.remark }}</span>
      </div>
    </div>

    <div class="section card">
      <div class="section-title">费用明细</div>
      <div class="info-row">
        <span class="info-label">商品金额</span>
        <span class="info-value">¥{{ order.totalAmount }}</span>
      </div>
      <div class="info-row">
        <span class="info-label">配送费</span>
        <span class="info-value text-primary">¥{{ order.deliveryFee }}</span>
      </div>
      <div class="info-row total-row">
        <span class="info-label">实付金额</span>
        <span class="info-value text-bold">¥{{ order.payAmount }}</span>
      </div>
    </div>

    <div class="bottom-bar">
      <button
        v-if="order.status === 'CONFIRMED' || order.status === 'PAID'"
        class="btn-action btn-full"
        @click="handlePickup"
      >
        确认取货
      </button>
      <button
        v-else-if="order.status === 'PICKED_UP' || order.status === 'DELIVERING'"
        class="btn-action btn-full"
        @click="handleDeliver"
      >
        确认送达
      </button>
      <button
        v-else
        class="btn-action btn-full btn-disabled"
        disabled
      >
        订单已完成
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { pickupOrder, deliverOrder, getMyOrders } from '@/api/rider'
import { useRiderStore } from '@/store/rider'
import { toast, confirm } from '@/utils/notify'

const route = useRoute()
const router = useRouter()
const riderStore = useRiderStore()

const order = ref(null)

onMounted(() => {
  loadOrderDetail()
})

const loadOrderDetail = async () => {
  const orderId = route.params.id
  const userId = riderStore.userId
  if (!userId) {
    router.push('/login')
    return
  }
  try {
    const orders = await getMyOrders(userId)
    order.value = orders.find(o => o.id === orderId)
    if (!order.value) {
      toast.warning('订单不存在')
      router.back()
    }
  } catch (e) {
    console.error('加载订单失败', e)
  }
}

const getStatusText = (status) => {
  const statusMap = {
    'PENDING': '待支付',
    'PAID': '已支付',
    'CONFIRMED': '待取货',
    'PICKED_UP': '已取货',
    'DELIVERING': '配送中',
    'DELIVERED': '已送达',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消'
  }
  return statusMap[status] || status
}

const getStatusClass = (status) => {
  const classMap = {
    'PENDING': 'status-pending',
    'PAID': 'status-paid',
    'CONFIRMED': 'status-confirmed',
    'PICKED_UP': 'status-picked',
    'DELIVERING': 'status-delivering',
    'DELIVERED': 'status-delivered',
    'COMPLETED': 'status-completed',
    'CANCELLED': 'status-cancelled'
  }
  return classMap[status] || 'status-default'
}

const formatTime = (time) => {
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

const handlePickup = async () => {
  const ok = await confirm({
    title: '确认取货',
    message: '确定已取到货物吗？',
    confirmText: '确认取货'
  })
  if (ok) {
    try {
      await pickupOrder(order.value.id)
      toast.success('取货成功')
      setTimeout(() => {
        router.back()
      }, 1000)
    } catch (e) {
      console.error('取货失败', e)
    }
  }
}

const handleDeliver = async () => {
  const ok = await confirm({
    title: '确认送达',
    message: '确定已送达目的地吗？',
    confirmText: '确认送达'
  })
  if (ok) {
    try {
      await deliverOrder(order.value.id)
      toast.success('送达成功')
      setTimeout(() => {
        router.back()
      }, 1000)
    } catch (e) {
      console.error('送达失败', e)
    }
  }
}
</script>

<style scoped>
.page-container {
  padding: 0 0 100px 0;
  min-height: 100vh;
  overflow-y: auto;
}

.status-header {
  padding: 40px 20px;
  text-align: center;
  color: #fff;
}

.status-header.status-pending {
  background: linear-gradient(135deg, #faad14 0%, #ffc53d 100%);
}

.status-header.status-paid,
.status-header.status-confirmed {
  background: linear-gradient(135deg, #FF6B35 0%, #FF8C5A 100%);
}

.status-header.status-picked,
.status-header.status-delivering {
  background: linear-gradient(135deg, #1890ff 0%, #40a9ff 100%);
}

.status-header.status-delivered,
.status-header.status-completed {
  background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
}

.status-header.status-cancelled {
  background: linear-gradient(135deg, #999 0%, #bbb 100%);
}

.status-text {
  display: block;
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 8px;
}

.status-tip {
  font-size: 13px;
  opacity: 0.9;
}

.card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.section {
  margin: 15px;
  padding: 20px 16px;
}

.section-title {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
}

.address-block {
  background: #fafafa;
  border-radius: 8px;
  padding: 16px;
}

.address-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 5px 0;
}

.dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  margin-top: 5px;
  flex-shrink: 0;
}

.shop-dot {
  background: #FF6B35;
}

.user-dot {
  background: #52c41a;
}

.address-line {
  width: 1px;
  height: 20px;
  background: #e5e5e5;
  margin-left: 4px;
}

.address-content {
  flex: 1;
}

.address-type {
  display: block;
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.address-detail {
  font-size: 14px;
  color: #333;
  line-height: 1.5;
}

.receiver {
  display: block;
  font-size: 13px;
  color: #666;
  margin-top: 6px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
}

.info-row:last-child {
  border-bottom: none;
}

.info-label {
  font-size: 14px;
  color: #666;
}

.info-value {
  font-size: 14px;
  color: #333;
}

.total-row {
  padding-top: 16px;
  margin-top: 8px;
  border-top: 2px solid #f0f0f0;
  border-bottom: none !important;
}

.text-primary {
  color: #FF6B35;
  font-weight: bold;
}

.text-bold {
  font-weight: bold;
  font-size: 16px !important;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 12px 20px;
  padding-bottom: calc(12px + env(safe-area-inset-bottom));
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.05);
}

.btn-action {
  height: 44px;
  border: none;
  border-radius: 22px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
}

.btn-full {
  width: 100%;
  background: linear-gradient(135deg, #FF6B35 0%, #FF8C5A 100%);
  color: #fff;
}

.btn-full:active {
  opacity: 0.8;
}

.btn-disabled {
  background: #ccc;
  color: #fff;
  cursor: not-allowed;
}
</style>
