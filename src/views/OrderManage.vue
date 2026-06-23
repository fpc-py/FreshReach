<template>
  <div class="page-container order-manage">
    <div class="page-header">
      <h1>订单管理</h1>
    </div>
    
    <!-- 筛选区域 -->
    <div class="filter-section">
      <el-tabs v-model="activeStatus" @tab-change="handleTabChange">
        <el-tab-pane
          v-for="tab in statusTabs"
          :key="tab.value"
          :label="tab.label"
          :name="tab.value"
        >
          <template #label>
            <span class="tab-label">
              {{ tab.label }}
              <em v-if="tab.count !== undefined && tab.count > 0">{{ tab.count }}</em>
            </span>
          </template>
        </el-tab-pane>
      </el-tabs>
      
      <div class="filter-actions">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          @change="handleDateChange"
          style="width: 260px"
        />
        <el-input
          v-model="searchKeyword"
          placeholder="搜索订单号"
          :prefix-icon="Search"
          clearable
          @keyup.enter="handleSearch"
          style="width: 200px"
        />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
      </div>
    </div>
    
    <!-- 订单列表 -->
    <div class="order-list">
      <OrderTable
        :orders="orders"
        :loading="loading"
        @accept="handleAccept"
        @reject="showRejectDialog"
        @finishPreparing="handleFinishPreparing"
        @view="showOrderDetail"
        @agreeRefund="handleAgreeRefund"
        @rejectRefund="showRejectRefundDialog"
      />
      
      <div class="pagination-wrapper">
               <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />

      </div>
    </div>
    
    <!-- 订单详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="订单详情"
      width="600px"
      :close-on-click-modal="false"
    >
      <div v-if="currentOrder" class="order-detail">
        <div class="detail-section">
          <h4>基本信息</h4>
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="订单号">{{ currentOrder.id }}</el-descriptions-item>
            <el-descriptions-item label="下单时间">{{ currentOrder.createTime }}</el-descriptions-item>
            <el-descriptions-item label="订单状态">
              <StatusTag :status="currentOrder.status" />
            </el-descriptions-item>
            <el-descriptions-item v-if="currentOrder.riderName" label="配送骑手">
              {{ currentOrder.riderName }} ({{ currentOrder.riderPhone }})
            </el-descriptions-item>
          </el-descriptions>
        </div>
        
        <div class="detail-section">
          <h4>商品清单</h4>
          <div class="product-list">
            <div v-for="(item, index) in currentOrder.items" :key="index" class="product-item">
              <div class="product-info">
                <span class="product-name">{{ item.name }}</span>
                <span v-if="item.specs" class="product-specs">({{ item.specs }})</span>
              </div>
              <div class="product-price">¥{{ item.price }} × {{ item.quantity }}</div>
            </div>
          </div>
        </div>
        
        <div class="detail-section">
          <h4>收货信息</h4>
          <div class="address-info">
            <p><strong>收货人：</strong>{{ currentOrder.address.name }}</p>
            <p><strong>联系电话：</strong>{{ currentOrder.address.phone }}</p>
            <p><strong>收货地址：</strong>{{ currentOrder.address.province }}{{ currentOrder.address.city }}{{ currentOrder.address.district }}{{ currentOrder.address.detail }}</p>
          </div>
        </div>
        
        <div v-if="currentOrder.remark" class="detail-section">
          <h4>用户备注</h4>
          <div class="remark-info">{{ currentOrder.remark }}</div>
        </div>
        
        <div class="detail-section">
          <h4>金额明细</h4>
          <div class="amount-detail">
            <div class="amount-row">
              <span>商品金额</span>
              <span>¥{{ (currentOrder.totalAmount || 0).toFixed(2) }}</span>
            </div>
            <div class="amount-row">
              <span>配送费</span>
              <span>¥{{ (currentOrder.deliveryFee || 0).toFixed(2) }}</span>
            </div>
            <div class="amount-row">
              <span>包装费</span>
              <span>¥{{ (currentOrder.packagingFee || 0).toFixed(2) }}</span>
            </div>
            <div v-if="currentOrder.discountAmount > 0" class="amount-row discount">
              <span>优惠金额</span>
              <span>-¥{{ (currentOrder.discountAmount || 0).toFixed(2) }}</span>
            </div>
            <div class="amount-row total">
              <span>实付金额</span>
              <span>¥{{ (currentOrder.payAmount || 0).toFixed(2) }}</span>
            </div>
          </div>
        </div>
        
        <div v-if="currentOrder.cancelReason" class="detail-section">
          <h4>取消原因</h4>
          <div class="reason-info">{{ currentOrder.cancelReason }}</div>
        </div>
        
        <div v-if="currentOrder.refundReason" class="detail-section">
          <h4>退款原因</h4>
          <div class="reason-info">{{ currentOrder.refundReason }}</div>
        </div>
      </div>
    </el-dialog>
    
    <!-- 拒单弹窗 -->
    <el-dialog
      v-model="rejectDialogVisible"
      title="拒单"
      width="450px"
      :close-on-click-modal="false"
    >
      <el-form ref="rejectFormRef" :model="rejectForm" :rules="rejectFormRules" label-width="80px">
        <el-form-item label="拒单原因" prop="reason">
          <el-input
            v-model="rejectForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入拒单原因，将告知用户"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleReject" :loading="actionLoading">确认拒单</el-button>
      </template>
    </el-dialog>
    
    <!-- 拒绝退款弹窗 -->
    <el-dialog
      v-model="rejectRefundDialogVisible"
      title="拒绝退款"
      width="450px"
      :close-on-click-modal="false"
    >
      <el-form ref="rejectRefundFormRef" :model="rejectRefundForm" :rules="rejectRefundRules" label-width="80px">
        <el-form-item label="拒绝原因" prop="reason">
          <el-input
            v-model="rejectRefundForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入拒绝退款原因"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectRefundDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRejectRefund" :loading="actionLoading">确认拒绝</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { useOrderStore } from '@/stores/order'
import OrderTable from '@/components/OrderTable.vue'
import StatusTag from '@/components/StatusTag.vue'

const route = useRoute()
const orderStore = useOrderStore()

const activeStatus = ref('all')
const dateRange = ref([])
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = computed(() => orderStore.total)
const orders = computed(() => orderStore.orders)
const loading = computed(() => orderStore.loading)

// 状态标签及数量
const statusTabs = computed(() => {
  // 兜底为空数组，防止undefined调用filter报错
  const orderList = orderStore.orders || []
  const pendingCount = orderList.filter(o => o.status === 'pending').length
  const preparingCount = orderList.filter(o => o.status === 'preparing').length
  const deliveringCount = orderList.filter(o => o.status === 'delivering').length
  const completedCount = orderList.filter(o => o.status === 'completed').length
  const cancelledCount = orderList.filter(o => o.status === 'cancelled').length
  const refundingCount = orderList.filter(o => o.status === 'refunding').length
  const allCount = orderList.length

  return [
    { label: '全部', value: 'all', count: allCount },
    { label: '待接单', value: 'pending', count: pendingCount },
    { label: '备餐中', value: 'preparing', count: preparingCount },
    { label: '配送中', value: 'delivering', count: deliveringCount },
    { label: '已完成', value: 'completed', count: completedCount },
    { label: '已取消', value: 'cancelled', count: cancelledCount },
    { label: '退款中', value: 'refunding', count: refundingCount },
  ]
})

// 订单详情相关
const detailDialogVisible = ref(false)
const currentOrder = ref(null)

// 拒单相关
const rejectDialogVisible = ref(false)
const rejectFormRef = ref(null)
const rejectForm = reactive({ reason: '' })
const rejectFormRules = {
  reason: [
    { required: true, message: '请输入拒单原因', trigger: 'blur' }
  ]
}
const currentRejectOrder = ref(null)

// 拒绝退款相关
const rejectRefundDialogVisible = ref(false)
const rejectRefundFormRef = ref(null)
const rejectRefundForm = reactive({ reason: '' })
const rejectRefundRules = {
  reason: [
    { required: true, message: '请输入拒绝原因', trigger: 'blur' }
  ]
}
const currentRejectRefundOrder = ref(null)

const actionLoading = ref(false)

// 加载订单列表
const fetchOrders = async () => {
  const params = {
    status: activeStatus.value === 'all' ? undefined : activeStatus.value,
    pageNum: currentPage.value,
    pageSize: pageSize.value
  }

  
  if (dateRange.value && dateRange.value.length === 2) {
    params.startDate = dateRange.value[0] + ' 00:00:00'
    params.endDate = dateRange.value[1] + ' 23:59:59'
  }
  
  if (searchKeyword.value) {
    params.id = searchKeyword.value
  }
  
  await orderStore.fetchOrders(params)
}

// Tab切换
const handleTabChange = () => {
  currentPage.value = 1
  fetchOrders()
}

// 日期范围变化
const handleDateChange = () => {
  currentPage.value = 1
  fetchOrders()
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchOrders()
}

// 分页
const handleSizeChange = () => {
  currentPage.value = 1
  fetchOrders()
}

const handlePageChange = () => {
  fetchOrders()
}

// 查看订单详情
const showOrderDetail = async (order) => {
  currentOrder.value = order
  detailDialogVisible.value = true
}

// 接单
const handleAccept = async (order) => {
  try {
    await ElMessageBox.confirm('确认接下此订单？', '接单确认', {
      confirmButtonText: '确认接单',
      cancelButtonText: '取消',
      type: 'success'
    })
    
    actionLoading.value = true
    const result = await orderStore.acceptOrder(order.id)
    
    if (result.success) {
      ElMessage.success('已接单，请及时备餐')
    } else {
      ElMessage.error(result.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message)
    }
  } finally {
    actionLoading.value = false
  }
}

// 显示拒单弹窗
const showRejectDialog = (order) => {
  currentRejectOrder.value = order
  rejectForm.reason = ''
  rejectDialogVisible.value = true
}

// 确认拒单
const handleReject = async () => {
  try {
    await rejectFormRef.value.validate()
    
    actionLoading.value = true
    const result = await orderStore.rejectOrder(currentRejectOrder.value.id, rejectForm.reason)
    
    if (result.success) {
      ElMessage.success('已拒单')
      rejectDialogVisible.value = false
    } else {
      ElMessage.error(result.message)
    }
  } catch (error) {
    // 验证失败
  } finally {
    actionLoading.value = false
  }
}

// 完成备餐
const handleFinishPreparing = async (order) => {
  try {
    await ElMessageBox.confirm('确认已完成备餐？', '备餐完成', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'success'
    })
    
    actionLoading.value = true
    const result = await orderStore.finishPreparing(order.id)
    
    if (result.success) {
      ElMessage.success('已通知骑手取餐')
    } else {
      ElMessage.error(result.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message)
    }
  } finally {
    actionLoading.value = false
  }
}

// 同意退款
const handleAgreeRefund = async (order) => {
  try {
    await ElMessageBox.confirm('确认同意此退款申请？退款将原路返回给用户', '同意退款', {
      confirmButtonText: '确认退款',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    actionLoading.value = true
    const result = await orderStore.agreeRefund(order.id)
    
    if (result.success) {
      ElMessage.success('已同意退款')
    } else {
      ElMessage.error(result.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message)
    }
  } finally {
    actionLoading.value = false
  }
}

// 显示拒绝退款弹窗
const showRejectRefundDialog = (order) => {
  currentRejectRefundOrder.value = order
  rejectRefundForm.reason = ''
  rejectRefundDialogVisible.value = true
}

// 确认拒绝退款
const handleRejectRefund = async () => {
  try {
    await rejectRefundFormRef.value.validate()
    
    actionLoading.value = true
    const result = await orderStore.rejectRefund(currentRejectRefundOrder.value.id, rejectRefundForm.reason)
    
    if (result.success) {
      ElMessage.success('已拒绝退款')
      rejectRefundDialogVisible.value = false
    } else {
      ElMessage.error(result.message)
    }
  } catch (error) {
    // 验证失败
  } finally {
    actionLoading.value = false
  }
}

// 监听路由参数
watch(() => route.query.status, (newStatus) => {
  if (newStatus) {
    activeStatus.value = newStatus
  }
}, { immediate: true })

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.order-manage {
  min-width: 1024px;
}

.filter-section {
  background: #F8FAF9;
  border-radius: 8px;
  padding: 16px 20px;
  margin-bottom: 20px;
}

.tab-label {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.tab-label em {
  background: #E53935;
  color: #FFFFFF;
  font-size: 11px;
  padding: 2px 6px;
  border-radius: 10px;
  font-style: normal;
  min-width: 18px;
  text-align: center;
}

.filter-actions {
  display: flex;
  gap: 12px;
  margin-top: 16px;
  flex-wrap: wrap;
}

.order-list {
  background: #F8FAF9;
  border-radius: 8px;
  padding: 20px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

/* 订单详情样式 */
.order-detail {
  max-height: 60vh;
  overflow-y: auto;
}

.detail-section {
  margin-bottom: 20px;
}

.detail-section h4 {
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #E8EDEB;
}

.product-list {
  background: #F8FAF9;
  border-radius: 8px;
  padding: 12px;
}

.product-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px dashed #E8EDEB;
}

.product-item:last-child {
  border-bottom: none;
}

.product-info {
  display: flex;
  align-items: center;
  gap: 4px;
}

.product-name {
  font-size: 14px;
  color: #475569;
}

.product-specs {
  font-size: 12px;
  color: #94A3B8;
}

.product-price {
  font-size: 14px;
  color: #475569;
}

.address-info p {
  font-size: 14px;
  color: #475569;
  line-height: 1.8;
}

.remark-info,
.reason-info {
  font-size: 14px;
  color: #E53935;
  padding: 12px;
  background: #FFEBEE;
  border-radius: 8px;
}

.amount-detail {
  background: #F8FAF9;
  border-radius: 8px;
  padding: 12px;
}

.amount-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  font-size: 14px;
  color: #475569;
}

.amount-row.discount {
  color: #27AE60;
}

.amount-row.total {
  border-top: 1px solid #E8EDEB;
  margin-top: 8px;
  padding-top: 12px;
  font-weight: 600;
  color: #E53935;
}
</style>
