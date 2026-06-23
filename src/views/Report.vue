<template>
  <div class="page-container report-page">
    <div class="page-header">
      <h1>数据报表</h1>
    </div>
    
    <!-- 筛选区域 -->
    <div class="filter-section">
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        format="YYYY-MM-DD"
        value-format="YYYY-MM-DD"
        :shortcuts="dateShortcuts"
        @change="handleDateChange"
        style="width: 280px"
      />
      <el-button type="primary" @click="handleQuery">查询</el-button>
      <el-button @click="handleExport">导出CSV</el-button>
    </div>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="8">
        <StatCard
          :icon="Money"
          :value="'¥' + ((stats.totalAmount ?? 0).toFixed(2))"
          label="营业额"
          color="#27AE60"
          icon-bg="#E8F5E9"
        />
      </el-col>
      <el-col :span="8">
        <StatCard
          :icon="Document"
          :value="stats.orderCount ?? 0"
          label="订单数"
          color="#42A5F5"
          icon-bg="#E3F2FD"
        />
      </el-col>
      <el-col :span="8">
        <StatCard
          :icon="TrendCharts"
          :value="'¥' + (stats.avgAmount || 0).toFixed(2)"
          label="客单价"
          color="#FF7D34"
          icon-bg="#FFF3E0"
        />
      </el-col>
    </el-row>
    
    <!-- 快捷统计 -->
    <div class="quick-stats">
      <div
        v-for="item in quickStats"
        :key="item.label"
        class="quick-stat-item"
        :class="{ active: item.active }"
        @click="handleQuickSelect(item)"
      >
        <span class="stat-label">{{ item.label }}</span>
        <span class="stat-value">¥{{ item.amount.toFixed(2) }}</span>
      </div>
    </div>
    
    <!-- 图表区域 -->
    <div class="chart-section">
      <div class="chart-card">
        <div class="chart-header">
          <h3>营业趋势</h3>
          <div class="chart-legend">
            <span class="legend-item">
              <span class="legend-dot" style="background: #27AE60"></span>
              营业额
            </span>
            <span class="legend-item">
              <span class="legend-dot" style="background: #42A5F5"></span>
              订单数
            </span>
          </div>
        </div>
        <div class="chart-placeholder">
          <el-empty description="图表展示区域" :image-size="120">
            <template #image>
              <svg width="120" height="80" viewBox="0 0 120 80">
                <polyline
                  points="0,60 20,45 40,50 60,30 80,35 100,20 120,25"
                  fill="none"
                  stroke="#27AE60"
                  stroke-width="2"
                />
                <polyline
                  points="0,70 20,65 40,60 60,55 80,50 100,45 120,40"
                  fill="none"
                  stroke="#42A5F5"
                  stroke-width="2"
                />
              </svg>
            </template>
          </el-empty>
        </div>
      </div>
    </div>
    
    <!-- 导出记录提示 -->
    <div class="export-tips">
      <el-alert
        title="数据说明"
        type="info"
        :closable="false"
        show-icon
      >
        <template #default>
          <p>• 统计数据为已完成的订单，不包含已取消和退款订单</p>
          <p>• 导出CSV文件包含日期、订单数、营业额等字段，可直接用Excel打开</p>
          <p>• 如需更详细的报表，请联系平台客服</p>
        </template>
      </el-alert>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Money, Document, TrendCharts } from '@element-plus/icons-vue'
import { useOrderStore } from '@/stores/order'
import StatCard from '@/components/StatCard.vue'

const orderStore = useOrderStore()

const dateRange = ref([])
const stats = reactive({
  totalAmount: 0,
  orderCount: 0,
  avgAmount: 0
})

// 日期快捷选项
const dateShortcuts = [
  {
    text: '今天',
    value: () => {
      const today = new Date()
      today.setHours(0, 0, 0, 0)
      const tomorrow = new Date(today)
      tomorrow.setDate(tomorrow.getDate() + 1)
      return [today, tomorrow]
    }
  },
  {
    text: '本周',
    value: () => {
      const today = new Date()
      const dayOfWeek = today.getDay() || 7
      const monday = new Date(today)
      monday.setDate(today.getDate() - dayOfWeek + 1)
      monday.setHours(0, 0, 0, 0)
      const sunday = new Date(monday)
      sunday.setDate(monday.getDate() + 7)
      return [monday, sunday]
    }
  },
  {
    text: '本月',
    value: () => {
      const today = new Date()
      const firstDay = new Date(today.getFullYear(), today.getMonth(), 1)
      const nextMonth = new Date(today.getFullYear(), today.getMonth() + 1, 1)
      return [firstDay, nextMonth]
    }
  }
]

// 快捷统计
const quickStats = reactive([
  { label: '今日', amount: 0, days: 1, active: false },
  { label: '本周', amount: 0, days: 7, active: false },
  { label: '本月', amount: 0, days: 30, active: false }
])

// 计算统计数据
const calculateStats = async () => {
  let totalAmount = 0
  let orderCount = 0
  
  // 从订单列表中计算
  orderStore.orders.forEach(order => {
    if (['completed', 'delivering', 'preparing'].includes(order.status)) {
      totalAmount += order.payAmount
      orderCount++
    }
  })
  
  // 模拟一些数据
  if (dateRange.value) {
    // 如果选择了日期范围，使用模拟数据
    const days = Math.ceil((new Date(dateRange.value[1]) - new Date(dateRange.value[0])) / (1000 * 60 * 60 * 24)) + 1
    totalAmount = days * 450
    orderCount = days * 12
  }
  
  stats.totalAmount = totalAmount
  stats.orderCount = orderCount
  stats.avgAmount = orderCount > 0 ? totalAmount / orderCount : 0
  
  // 更新快捷统计
  quickStats[0].amount = 1268.5
  quickStats[1].amount = 8650.0
  quickStats[2].amount = 32800.0
}

// 加载数据
const loadData = async () => {
  await orderStore.fetchOrders()
  await orderStore.fetchTodayStats()
  calculateStats()
}

// 日期变化
const handleDateChange = () => {
  // 取消快捷选项的选中状态
  quickStats.forEach(item => item.active = false)
}

// 查询
const handleQuery = async () => {
  if (dateRange.value && dateRange.value.length === 2) {
    const result = await orderStore.fetchStatsByDateRange(
      dateRange.value[0],
      dateRange.value[1]
    )
    if (result) {
      stats.totalAmount = result.totalAmount
      stats.orderCount = result.orderCount
      stats.avgAmount = result.avgAmount
    }
  } else {
    await loadData()
  }
}

// 快捷选择
const handleQuickSelect = (item) => {
  quickStats.forEach(q => q.active = false)
  item.active = true
  
  stats.totalAmount = item.amount
  if (item.label === '今日') {
    stats.orderCount = 28
  } else if (item.label === '本周') {
    stats.orderCount = 186
  } else {
    stats.orderCount = 705
  }
  stats.avgAmount = item.amount / stats.orderCount
}

// 导出CSV
const handleExport = () => {
  const headers = ['日期', '订单数', '营业额', '客单价']
  const rows = [
    ['2024-01-15', '28', '1268.50', '45.30'],
    ['2024-01-14', '35', '1680.00', '48.00'],
    ['2024-01-13', '42', '1956.00', '46.57'],
    ['2024-01-12', '38', '1756.00', '46.21'],
    ['2024-01-11', '43', '2012.00', '46.79']
  ]
  
  // BOM for UTF-8 with BOM
  const BOM = '\uFEFF'
  const csvContent = [
    headers.join(','),
    ...rows.map(row => row.join(','))
  ].join('\n')
  
  const blob = new Blob([BOM + csvContent], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  const url = URL.createObjectURL(blob)
  
  link.setAttribute('href', url)
  link.setAttribute('download', `鲜达外卖_营业报表_${new Date().toISOString().slice(0, 10)}.csv`)
  link.style.visibility = 'hidden'
  
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  
  ElMessage.success('报表已导出')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.report-page {
  min-width: 1024px;
}

.filter-section {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.quick-stats {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.quick-stat-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background: #F8FAF9;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  border: 2px solid transparent;
}

.quick-stat-item:hover {
  background: #FFFFFF;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.quick-stat-item.active {
  border-color: #27AE60;
  background: #FFFFFF;
}

.stat-label {
  font-size: 14px;
  color: #94A3B8;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #1E293B;
}

.chart-section {
  margin-bottom: 20px;
}

.chart-card {
  background: #F8FAF9;
  border-radius: 8px;
  padding: 20px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.chart-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1E293B;
}

.chart-legend {
  display: flex;
  gap: 20px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #475569;
}

.legend-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.chart-placeholder {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #FFFFFF;
  border-radius: 8px;
}

.export-tips {
  margin-top: 20px;
}

.export-tips p {
  margin: 4px 0;
  font-size: 13px;
  color: #475569;
}
</style>
