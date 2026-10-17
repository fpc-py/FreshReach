<template>
  <div class="page-container">
    <div class="income-header">
      <div class="income-card">
        <span class="income-label">累计总收入</span>
        <span class="income-total">¥{{ incomeStats.totalIncome || '0.00' }}</span>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-item card">
        <span class="stat-value">{{ incomeStats.todayOrderCount || 0 }}</span>
        <span class="stat-label">今日完成订单</span>
      </div>
      <div class="stat-item card">
        <span class="stat-value">¥{{ incomeStats.todayIncome || '0.00' }}</span>
        <span class="stat-label">今日收入</span>
      </div>
    </div>

    <div class="section-title">
      <span>收入明细</span>
    </div>

    <div class="income-list" v-if="orderList.length > 0">
      <div class="income-item card" v-for="item in orderList" :key="item.id">
        <div class="income-info">
          <span class="income-order">订单号: {{ item.id }}</span>
          <span class="income-time">{{ formatTime(item.finishTime || item.createTime) }}</span>
        </div>
        <span class="income-amount">+¥{{ item.deliveryFee }}</span>
      </div>
    </div>

    <div class="empty-state" v-else>
      <span class="empty-icon">💰</span>
      <span class="empty-text">暂无收入记录</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useRiderStore } from '@/store/rider'
import { getIncomeStats, getMyOrders } from '@/api/rider'

const router = useRouter()
const riderStore = useRiderStore()

const incomeStats = ref({
  todayOrderCount: 0,
  todayIncome: '0.00',
  totalIncome: '0.00'
})
const orderList = ref([])

onMounted(() => {
  loadData()
})

const loadData = async () => {
  const riderInfo = riderStore.riderInfo
  if (!riderInfo) {
    router.push('/login')
    return
  }
  try {
    const [stats, orders] = await Promise.all([
      getIncomeStats(riderInfo.userId),
      getMyOrders(riderInfo.userId)
    ])
    incomeStats.value = stats || incomeStats.value
    orderList.value = (orders || []).filter(o => 
      o.status === 'COMPLETED' || o.status === 'DELIVERED'
    )
  } catch (e) {
    console.error('加载数据失败', e)
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
  padding: 0 15px 15px;
  min-height: 100vh;
  padding-bottom: 80px;
  overflow-y: auto;
}

.income-header {
  margin: 0 -15px;
  padding: 30px 20px;
  background: linear-gradient(135deg, #FF6B35 0%, #FF8C5A 100%);
}

.income-card {
  text-align: center;
  color: #fff;
}

.income-label {
  display: block;
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 10px;
}

.income-total {
  font-size: 36px;
  font-weight: bold;
}

.stats-grid {
  display: flex;
  gap: 15px;
  margin-top: -20px;
  margin-bottom: 20px;
  position: relative;
  z-index: 10;
}

.card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.stat-item {
  flex: 1;
  text-align: center;
  padding: 20px 12px;
}

.stat-value {
  display: block;
  font-size: 24px;
  font-weight: bold;
  color: #FF6B35;
  margin-bottom: 6px;
}

.stat-label {
  font-size: 12px;
  color: #666;
}

.section-title {
  padding: 15px 8px;
  font-size: 15px;
  font-weight: bold;
  color: #333;
}

.income-list {
  padding-bottom: 15px;
}

.income-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  margin-bottom: 10px;
}

.income-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.income-order {
  font-size: 14px;
  color: #333;
}

.income-time {
  font-size: 12px;
  color: #999;
}

.income-amount {
  font-size: 18px;
  font-weight: bold;
  color: #52c41a;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
}

.empty-icon {
  font-size: 50px;
  margin-bottom: 15px;
}

.empty-text {
  font-size: 14px;
  color: #999;
}
</style>
