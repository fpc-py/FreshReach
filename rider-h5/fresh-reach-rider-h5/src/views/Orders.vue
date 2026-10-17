<template>
  <div class="page-container">
    <div class="tabs">
      <div 
        :class="['tab-item', currentTab === 'all' ? 'active' : '']"
        @click="switchTab('all')"
      >
        全部
      </div>
      <div 
        :class="['tab-item', currentTab === 'picking' ? 'active' : '']"
        @click="switchTab('picking')"
      >
        待取货
      </div>
      <div 
        :class="['tab-item', currentTab === 'delivering' ? 'active' : '']"
        @click="switchTab('delivering')"
      >
        配送中
      </div>
      <div 
        :class="['tab-item', currentTab === 'completed' ? 'active' : '']"
        @click="switchTab('completed')"
      >
        已完成
      </div>
    </div>

    <div class="order-list" v-if="filteredOrders.length > 0">
      <div class="order-card card" v-for="order in filteredOrders" :key="order.id" @click="goToDetail(order)">
        <div class="order-header">
          <span class="order-id">订单号: {{ order.id }}</span>
          <span :class="['order-status', getStatusClass(order.status)]">
            {{ getStatusText(order.status) }}
          </span>
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
          <div class="order-info">
            <span class="delivery-fee">配送费 ¥{{ order.deliveryFee }}</span>
            <span class="order-time">{{ formatTime(order.createTime) }}</span>
          </div>
          <div class="order-actions">
            <button 
              v-if="order.status === 'CONFIRMED' || order.status === 'PAID'" 
              class="btn-action btn-primary"
              @click.stop="handlePickup(order)"
            >
              确认取货
            </button>
            <button 
              v-if="order.status === 'PICKED_UP' || order.status === 'DELIVERING'" 
              class="btn-action btn-primary"
              @click.stop="handleDeliver(order)"
            >
              确认送达
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="empty-state" v-else>
      <span class="empty-icon">📋</span>
      <span class="empty-text">暂无订单</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useRiderStore } from '@/store/rider'
import { getMyOrders, pickupOrder, deliverOrder } from '@/api/rider'
import { toast, confirm } from '@/utils/notify'

const router = useRouter()
const riderStore = useRiderStore()

const orders = ref([])
const currentTab = ref('all')

const filteredOrders = computed(() => {
  if (currentTab.value === 'all') {
    return orders.value
  }
  return orders.value.filter(order => {
    if (currentTab.value === 'picking') {
      return order.status === 'CONFIRMED' || order.status === 'PAID'
    }
    if (currentTab.value === 'delivering') {
      return order.status === 'PICKED_UP' || order.status === 'DELIVERING'
    }
    if (currentTab.value === 'completed') {
      return order.status === 'COMPLETED' || order.status === 'DELIVERED'
    }
    return true
  })
})

onMounted(() => {
  loadOrders()
})

const loadOrders = async () => {
  const userId = riderStore.userId
  if (!userId) {
    router.push('/login')
    return
  }
  try {
    const res = await getMyOrders(userId)
    orders.value = res || []
  } catch (e) {
    console.error('获取订单失败', e)
  }
}

const switchTab = (tab) => {
  currentTab.value = tab
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

const goToDetail = (order) => {
  router.push('/order-detail/' + order.id)
}

const handlePickup = async (order) => {
  const ok = await confirm({
    title: '确认取货',
    message: '确定已取到货物吗？',
    confirmText: '确认取货'
  })
  if (ok) {
    try {
      await pickupOrder(order.id)
      toast.success('取货成功')
      loadOrders()
    } catch (e) {
      console.error('取货失败', e)
    }
  }
}

const handleDeliver = async (order) => {
  const ok = await confirm({
    title: '确认送达',
    message: '确定已送达目的地吗？',
    confirmText: '确认送达'
  })
  if (ok) {
    try {
      await deliverOrder(order.id)
      toast.success('送达成功')
      loadOrders()
    } catch (e) {
      console.error('送达失败', e)
    }
  }
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const month = (date.getMonth() + 1).toString().padStart(2, '0')
  const day = date.getDate().toString().padStart(2, '0')
  const hours = date.getHours().toString().padStart(2, '0')
  const minutes = date.getMinutes().toString().padStart(2, '0')
  return month + '-' + day + ' ' + hours + ':' + minutes
}
</script>

<style scoped>
.page-container {
  padding: 0 0 80px 0;
  min-height: 100vh;
  overflow-y: auto;
}

.tabs {
  display: flex;
  background: #fff;
  padding: 0 10px;
  position: sticky;
  top: 0;
  z-index: 10;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 15px 0;
  font-size: 14px;
  color: #666;
  position: relative;
  cursor: pointer;
}

.tab-item.active {
  color: #FF6B35;
  font-weight: bold;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 3px;
  background: #FF6B35;
  border-radius: 2px;
}

.order-list {
  padding: 15px;
}

.card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.order-card {
  margin-bottom: 15px;
  cursor: pointer;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.order-id {
  font-size: 13px;
  color: #666;
}

.order-status {
  font-size: 13px;
  font-weight: 500;
}

.status-pending {
  color: #faad14;
}

.status-paid {
  color: #1890ff;
}

.status-confirmed {
  color: #FF6B35;
}

.status-picked {
  color: #722ed1;
}

.status-delivering {
  color: #13c2c2;
}

.status-delivered,
.status-completed {
  color: #52c41a;
}

.status-cancelled {
  color: #999;
}

.order-address {
  margin-bottom: 12px;
}

.address-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 4px 0;
}

.dot {
  width: 8px;
  height: 8px;
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
  height: 16px;
  background: #e5e5e5;
  margin-left: 3px;
}

.address-content {
  flex: 1;
}

.address-label {
  display: block;
  font-size: 11px;
  color: #999;
  margin-bottom: 2px;
}

.address-detail {
  font-size: 13px;
  color: #333;
  line-height: 1.4;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid #f0f0f0;
}

.order-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.delivery-fee {
  font-size: 14px;
  font-weight: bold;
  color: #FF6B35;
}

.order-time {
  font-size: 11px;
  color: #999;
}

.order-actions {
  display: flex;
  gap: 10px;
}

.btn-action {
  padding: 6px 16px;
  font-size: 12px;
  border-radius: 16px;
  border: none;
  cursor: pointer;
}

.btn-primary {
  background: linear-gradient(135deg, #FF6B35 0%, #FF8C5A 100%);
  color: #fff;
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
}
</style>
