<template>
  <div class="page-container">
    <div class="status-bar card">
      <div class="status-info">
        <span class="status-label">当前状态</span>
        <span :class="['status-value', isOnline ? 'online' : 'offline']">
          {{ isOnline ? '在线' : '离线' }}
        </span>
      </div>
      <label class="switch">
        <input type="checkbox" :checked="isOnline" @change="toggleStatus" />
        <span class="slider"></span>
      </label>
    </div>

    <div class="section-header">
      <span class="section-title">可抢订单</span>
      <span class="refresh-btn" @click="loadOrders">刷新</span>
    </div>

    <div class="order-list" v-if="orders.length > 0">
      <div class="order-card card" v-for="order in orders" :key="order.id">
        <div class="order-header">
          <span class="order-id">订单号: {{ order.id }}</span>
          <span class="delivery-fee">配送费 ¥{{ order.deliveryFee }}</span>
        </div>

        <div class="order-address">
          <div class="address-item">
            <div class="dot shop-dot"></div>
            <div class="address-content">
              <span class="address-label">取货地址</span>
              <span class="address-detail">{{ order.detail || '商家地址' }}</span>
            </div>
          </div>
          <div class="address-line"></div>
          <div class="address-item">
            <div class="dot user-dot"></div>
            <div class="address-content">
              <span class="address-label">收货地址</span>
              <span class="address-detail">{{ order.province }}{{ order.city }}{{ order.district }}{{ order.detail }}</span>
            </div>
          </div>
        </div>

        <div class="order-footer">
          <span class="order-time">{{ formatTime(order.createTime) }}</span>
          <button class="btn-grab" @click="handleGrab(order)">立即抢单</button>
        </div>
      </div>
    </div>

    <div class="empty-state" v-else>
      <span class="empty-icon">📦</span>
      <span class="empty-text">暂无可抢订单</span>
      <span class="empty-tip">点击刷新获取最新订单</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useRiderStore } from '@/store/rider'
import { getAvailableOrders, grabOrder, updateRiderStatus } from '@/api/rider'
import { toast, confirm } from '@/utils/notify'

const router = useRouter()
const riderStore = useRiderStore()

const orders = ref([])
const loading = ref(false)
const isOnline = ref(riderStore.isOnline)

onMounted(() => {
  if (!riderStore.userId && riderStore.token) {
    console.log('检测到token但无userId，尝试从riderInfo修复')
    if (riderStore.riderInfo) {
      riderStore.setRiderInfo(riderStore.riderInfo)
    }
    if (!riderStore.userId) {
      console.log('修复失败，清除登录状态')
      riderStore.logout()
      router.push('/login')
      return
    }
  }
  loadOrders()
})

const loadOrders = async () => {
  loading.value = true
  try {
    const res = await getAvailableOrders()
    orders.value = res || []
  } catch (e) {
    console.error('获取可抢订单失败', e)
  } finally {
    loading.value = false
  }
}

const toggleStatus = async (e) => {
  const userId = riderStore.userId
  console.log('toggleStatus - userId:', userId, 'riderInfo:', riderStore.riderInfo)
  if (!userId) {
    toast.warning('请先登录或用户信息不完整')
    router.push('/login')
    return
  }
  const status = e.target.checked ? 1 : 0
  try {
    await updateRiderStatus(status, userId)
    isOnline.value = e.target.checked
    riderStore.setOnline(e.target.checked)
    toast.success(e.target.checked ? '已上线' : '已下线')
  } catch (e) {
    console.error('切换状态失败', e)
    e.target.checked = isOnline.value
  }
}

const handleGrab = async (order) => {
  const userId = riderStore.userId
  if (!userId) {
    toast.warning('请先登录')
    router.push('/login')
    return
  }
  if (!isOnline.value) {
    toast.warning('请先上线')
    return
  }

  const ok = await confirm({
    title: '抢单确认',
    message: '确定要抢这个订单吗？',
    confirmText: '立即抢单'
  })
  if (ok) {
    try {
      await grabOrder({
        id: userId,
        orderId: order.id
      })
      toast.success('抢单成功')
      loadOrders()
    } catch (e) {
      console.error('抢单失败', e)
    }
  }
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const hours = date.getHours().toString().padStart(2, '0')
  const minutes = date.getMinutes().toString().padStart(2, '0')
  return hours + ':' + minutes
}
</script>

<style scoped>
.page-container {
  padding: 15px;
  padding-bottom: 80px;
  min-height: 100vh;
  overflow-y: auto;
}

.status-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.status-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.status-label {
  font-size: 14px;
  color: #666;
}

.status-value {
  font-size: 16px;
  font-weight: bold;
}

.status-value.online {
  color: #52c41a;
}

.status-value.offline {
  color: #999;
}

.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 28px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: .4s;
  border-radius: 28px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 22px;
  width: 22px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: .4s;
  border-radius: 50%;
}

input:checked + .slider {
  background-color: #FF6B35;
}

input:checked + .slider:before {
  transform: translateX(22px);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 5px;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.refresh-btn {
  font-size: 13px;
  color: #FF6B35;
  cursor: pointer;
}

.order-card {
  margin-bottom: 15px;
}

.card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.order-id {
  font-size: 13px;
  color: #666;
}

.delivery-fee {
  font-size: 16px;
  font-weight: bold;
  color: #FF6B35;
}

.order-address {
  margin-bottom: 15px;
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

.address-label {
  display: block;
  font-size: 12px;
  color: #999;
  margin-bottom: 3px;
}

.address-detail {
  font-size: 14px;
  color: #333;
  line-height: 1.4;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.order-time {
  font-size: 12px;
  color: #999;
}

.btn-grab {
  background: linear-gradient(135deg, #FF6B35 0%, #FF8C5A 100%);
  color: #fff;
  border: none;
  border-radius: 20px;
  padding: 8px 20px;
  font-size: 13px;
  cursor: pointer;
  transition: opacity 0.2s;
}

.btn-grab:active {
  opacity: 0.8;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.empty-icon {
  font-size: 60px;
  margin-bottom: 15px;
}

.empty-text {
  font-size: 15px;
  color: #666;
  margin-bottom: 8px;
}

.empty-tip {
  font-size: 12px;
  color: #999;
}
</style>
