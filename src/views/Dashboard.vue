<template>
  <div class="page-container">
    <div class="page-header">
      <h1>工作台</h1>
    </div>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <StatCard
          :icon="Clock"
          :value="stats.pendingCount ?? 0"
          label="待处理订单"
          color="#FF7D34"
          icon-bg="#FFF3E0"
        />
      </el-col>
      <el-col :span="6">
        <StatCard
          :icon="Money"
          :value="'¥' + ((stats.totalAmount ?? 0).toFixed(2))"
          label="今日营业额"
          color="#27AE60"
          icon-bg="#E8F5E9"
        />
      </el-col>
      <el-col :span="6">
        <StatCard
          :icon="Document"
          :value="stats.orderCount ?? 0"
          label="今日订单数"
          color="#42A5F5"
          icon-bg="#E3F2FD"
        />
      </el-col>
      <el-col :span="6">
        <StatCard
          :icon="Goods"
          :value="stats.productOnSaleCount ?? 0"
          label="在售商品数"
          color="#FFD143"
          icon-bg="#FFFDE7"
        />
      </el-col>
    </el-row>
    
    <!-- 快捷操作和最近订单 -->
    <el-row :gutter="20">
      <el-col :span="14">
        <div class="card">
          <div class="card-header">
            <span class="card-title">最近订单</span>
            <el-button type="primary" link @click="$router.push('/orders')">
              查看全部
            </el-button>
          </div>
          <OrderTable
            :orders="recentOrders"
            :loading="loading"
            @view="handleViewOrder"
            @accept="handleAcceptOrder"
            @reject="handleRejectOrder"
            @finishPreparing="handleFinishPreparing"
          />
        </div>
      </el-col>
      
      <el-col :span="10">
        <div class="card">
          <div class="card-header">
            <span class="card-title">营业状态</span>
          </div>
          <div class="shop-status">
            <div class="status-info">
              <div class="status-indicator" :class="{ 'is-open': shopInfo?.isOpen }">
                <span class="indicator-dot"></span>
                {{ shopInfo?.isOpen ? '营业中' : '休息中' }}
              </div>
              <p class="status-desc">
                {{ shopInfo?.isOpen ? '店铺正在接收订单' : '暂停接单，用户无法下单' }}
              </p>
            </div>
            <el-switch
              v-model="shopInfo.isOpen"
              size="large"
              inline-prompt
              active-text="营业"
              inactive-text="休息"
              @change="handleToggleStatus"
            />
          </div>
        </div>
        
        <div class="card quick-actions">
          <div class="card-header">
            <span class="card-title">快捷操作</span>
          </div>
          <div class="action-grid">
            <div class="action-item" @click="$router.push('/orders?status=pending')">
              <el-icon :size="28" color="#FF7D34"><Clock /></el-icon>
              <span>待处理订单</span>
              <em v-if="stats.pendingCount > 0">{{ stats.pendingCount }}</em>
            </div>
            <div class="action-item" @click="$router.push('/products')">
              <el-icon :size="28" color="#27AE60"><Goods /></el-icon>
              <span>商品管理</span>
            </div>
            <div class="action-item" @click="$router.push('/settings')">
              <el-icon :size="28" color="#42A5F5"><Setting /></el-icon>
              <span>营业设置</span>
            </div>
            <div class="action-item" @click="$router.push('/report')">
              <el-icon :size="28" color="#FFD143"><DataLine /></el-icon>
              <span>数据报表</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Clock, Money, Document, Goods, Setting, DataLine } from '@element-plus/icons-vue'
import { useOrderStore } from '@/stores/order'
import { useShopStore } from '@/stores/shop'
import StatCard from '@/components/StatCard.vue'
import OrderTable from '@/components/OrderTable.vue'

const orderStore = useOrderStore()
const shopStore = useShopStore()

const loading = ref(false)

const stats = computed(() => orderStore.stats)
const shopInfo = computed(() => shopStore.shopInfo)
const recentOrders = computed(() => orderStore.orders.slice(0, 5))

// 初始化数据
const initData = async () => {
  loading.value = true
  try {
    await Promise.all([
      orderStore.fetchTodayStats(),
      orderStore.fetchOrders(),
      shopStore.fetchShopInfo()
    ])
  } finally {
    loading.value = false
  }
}

// 切换营业状态
const handleToggleStatus = async (isOpen) => {
  const result = await shopStore.updateShopStatus(isOpen)
  if (result.success) {
    ElMessage.success(isOpen ? '已设置为营业中' : '已设置为休息中')
  } else {
    ElMessage.error(result.message)
  }
}

// 查看订单
const handleViewOrder = (order) => {
  // 可以跳转到订单详情或打开详情弹窗
}

// 接单
const handleAcceptOrder = async (order) => {
  const result = await orderStore.acceptOrder(order.id)
  if (result.success) {
    ElMessage.success('已接单')
  } else {
    ElMessage.error(result.message)
  }
}

// 拒单
const handleRejectOrder = (order) => {
  // 显示拒单弹窗
}

// 完成备餐
const handleFinishPreparing = async (order) => {
  const result = await orderStore.finishPreparing(order.id)
  if (result.success) {
    ElMessage.success('已通知骑手取餐')
  } else {
    ElMessage.error(result.message)
  }
}

onMounted(() => {
  initData()
})
</script>

<style scoped>
.stats-row {
  margin-bottom: 20px;
}

.card {
  background: #F8FAF9;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.card-title {
  font-size: 16px;
  font-weight: 700;
  color: #1E293B;
}

.shop-status {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #FFFFFF;
  border-radius: 8px;
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #94A3B8;
}

.status-indicator.is-open {
  color: #27AE60;
}

.indicator-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #94A3B8;
}

.status-indicator.is-open .indicator-dot {
  background: #27AE60;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.status-desc {
  font-size: 13px;
  color: #94A3B8;
  margin-top: 4px;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background: #FFFFFF;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
}

.action-item:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.action-item span {
  font-size: 14px;
  color: #475569;
  margin-top: 8px;
}

.action-item em {
  position: absolute;
  top: 8px;
  right: 8px;
  background: #E53935;
  color: #FFFFFF;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 10px;
  font-style: normal;
}
</style>
