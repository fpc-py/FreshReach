<template>
  <div class="stat-card" :style="{ borderLeftColor: color }">
    <div class="stat-icon" :style="{ backgroundColor: iconBg }">
      <el-icon :size="24" :color="color">
        <component :is="icon" />
      </el-icon>
    </div>
    <div class="stat-content">
      <div class="stat-value" :style="{ color: color }">{{ value }}</div>
      <div class="stat-label">{{ label }}</div>
    </div>
    <div v-if="$slots.extra" class="stat-extra">
      <slot name="extra" />
    </div>
  </div>
</template>

<script setup>
defineProps({
  icon: {
    type: [String, Object],
    required: true
  },
  value: {
    type: [String, Number],
    required: true
  },
  label: {
    type: String,
    required: true
  },
  color: {
    type: String,
    default: '#27AE60'
  },
  iconBg: {
    type: String,
    default: '#E8F5E9'
  }
})
</script>

<style scoped>
.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: #FFFFFF;
  border-radius: 8px;
  border-left: 4px solid;
  box-shadow: 0 2px 8px rgba(39, 174, 96, 0.08);
  transition: box-shadow 0.3s;
}

.stat-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.stat-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 56px;
  height: 56px;
  border-radius: 12px;
  margin-right: 16px;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  line-height: 1.2;
}

.stat-label {
  font-size: 14px;
  color: #94A3B8;
  margin-top: 4px;
}

.stat-extra {
  margin-left: 16px;
}
</style>
