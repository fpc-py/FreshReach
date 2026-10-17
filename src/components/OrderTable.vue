<template>
  <div class="order-table">
    <el-table
      :data="orders"
      v-loading="loading"
      stripe
      style="width: 100%"
      @row-click="handleRowClick"
      row-class-name="order-row"
    >
      <el-table-column prop="id" label="订单号" width="160">
        <template #default="{ row }">
          <span class="order-id">{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="160" />
    <el-table-column label="商品" min-width="200">
  <template #default="{ row }">
    <div class="product-summary">
      <!-- 兜底：row.items不存在则赋值空数组 -->
      <div 
        v-for="(item, index) in (row.items || []).slice(0, 2)" 
        :key="index" 
        class="product-item"
      >
        {{ item.name }}
        <template v-if="item.specs">（{{ item.specs }}）</template>
        × {{ item.quantity }}
      </div>
      <!-- length也要兜底 -->
      <div v-if="(row.items || []).length > 2" class="product-more">
        还有 {{ (row.items || []).length - 2 }} 件商品...
      </div>
    </div>
  </template>
</el-table-column>
      <el-table-column prop="payAmount" label="总金额" width="100" align="right">
        <template #default="{ row }">
          <span class="amount">¥{{ row.payAmount != null ? row.payAmount.toFixed(2) : '0.00' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <StatusTag :status="row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="{ row }">
          <div class="action-buttons">
            <!-- 待接单 -->
            <template v-if="row.status === 'pending'">
              <el-button type="success" size="small" @click.stop="$emit('accept', row)">
                接单
              </el-button>
              <el-button type="danger" size="small" @click.stop="$emit('reject', row)">
                拒单
              </el-button>
            </template>
            <!-- 备餐中 -->
            <template v-else-if="row.status === 'preparing'">
              <el-button type="success" size="small" @click.stop="$emit('finishPreparing', row)">
                完成备餐
              </el-button>
            </template>
            <!-- 配送中 -->
            <template v-else-if="row.status === 'delivering'">
              <span class="text-aux">等待送达</span>
            </template>
            <!-- 已完成 -->
            <template v-else-if="row.status === 'completed'">
              <el-button type="primary" size="small" plain @click.stop="$emit('view', row)">
                查看详情
              </el-button>
            </template>
            <!-- 已取消 -->
            <template v-else-if="row.status === 'cancelled'">
              <el-button type="info" size="small" plain @click.stop="$emit('view', row)">
                查看详情
              </el-button>
            </template>
            <!-- 退款中 -->
            <template v-else-if="row.status === 'refunding'">
              <el-button type="success" size="small" @click.stop="$emit('agreeRefund', row)">
                同意退款
              </el-button>
              <el-button type="danger" size="small" @click.stop="$emit('rejectRefund', row)">
                拒绝退款
              </el-button>
            </template>
            <!-- 其他状态 -->
            <template v-else>
              <el-button type="primary" size="small" plain @click.stop="$emit('view', row)">
                查看详情
              </el-button>
            </template>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import StatusTag from './StatusTag.vue'

defineProps({
  orders: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  }
})

defineEmits([
  'accept', 'reject', 'finishPreparing', 'view', 'agreeRefund', 'rejectRefund'
])

const handleRowClick = (row) => {
  // 行点击事件可以在父组件中处理
}
</script>

<style scoped>
.order-table {
  width: 100%;
}

.order-id {
  font-family: 'Monaco', 'Menlo', monospace;
  font-size: 13px;
  color: #475569;
}

.product-summary {
  text-align: left;
}

.product-item {
  font-size: 13px;
  color: #475569;
  line-height: 1.6;
}

.product-more {
  font-size: 12px;
  color: #94A3B8;
  margin-top: 2px;
}

.amount {
  font-weight: 600;
  color: #E53935;
  font-size: 14px;
}

.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.text-aux {
  font-size: 12px;
  color: #94A3B8;
}

:deep(.order-row) {
  cursor: pointer;
}

:deep(.order-row:hover) {
  background-color: #F8FAF9;
}
</style>
