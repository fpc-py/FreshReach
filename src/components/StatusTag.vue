<template>
  <el-tag :type="tagType" :class="['status-tag', `status-${status}`]" :disable-transitions="true">
    <span class="status-dot"></span>
    {{ statusText }}
  </el-tag>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  status: {
    type: String,
    required: true
  }
})

const statusConfig = {
  pending: { text: '待接单', type: 'warning' },
  preparing: { text: '备餐中', type: 'primary' },
  delivering: { text: '配送中', type: 'success' },
  completed: { text: '已完成', type: 'info' },
  cancelled: { text: '已取消', type: 'danger' },
  refunded: { text: '已退款', type: 'info' },
  refunding: { text: '退款中', type: 'warning' },
  rejected: { text: '退款驳回', type: 'danger' }
}

const statusText = computed(() => statusConfig[props.status]?.text || props.status)
const tagType = computed(() => statusConfig[props.status]?.type || 'info')
</script>

<style scoped>
.status-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  border: none;
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: currentColor;
}

.status-pending {
  background-color: #FFF3E0;
  color: #FF7D34;
}

.status-preparing {
  background-color: #E3F2FD;
  color: #42A5F5;
}

.status-delivering {
  background-color: #E8F5E9;
  color: #27AE60;
}

.status-completed {
  background-color: #ECEFF1;
  color: #94A3B8;
}

.status-cancelled {
  background-color: #FFEBEE;
  color: #E53935;
}

.status-refunding {
  background-color: #FFFDE7;
  color: #F9A825;
}

.status-refunded {
  background-color: #ECEFF1;
  color: #94A3B8;
}

.status-rejected {
  background-color: #FFEBEE;
  color: #E53935;
}
</style>
