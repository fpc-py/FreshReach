<template>
  <Teleport to="body">
    <Transition name="toast-fade">
      <div v-if="visible" class="toast-mask" :class="position">
        <div class="toast-content" :class="type">
          <div v-if="type === 'success'" class="toast-icon">✓</div>
          <div v-else-if="type === 'error'" class="toast-icon">✕</div>
          <div v-else-if="type === 'warning'" class="toast-icon">!</div>
          <div v-else class="toast-icon">ℹ</div>
          <div class="toast-message">{{ message }}</div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  visible: Boolean,
  message: String,
  type: {
    type: String,
    default: 'info'
  },
  position: {
    type: String,
    default: 'center'
  },
  duration: {
    type: Number,
    default: 2000
  }
})

const emit = defineEmits(['update:visible'])

watch(() => props.visible, (val) => {
  if (val && props.duration > 0) {
    setTimeout(() => {
      emit('update:visible', false)
    }, props.duration)
  }
})
</script>

<style scoped>
.toast-mask {
  position: fixed;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
  z-index: 9999;
  pointer-events: none;
}

.toast-mask.center {
  top: 50%;
  transform: translateY(-50%);
}

.toast-mask.top {
  top: 80px;
}

.toast-mask.bottom {
  bottom: 100px;
}

.toast-content {
  background: rgba(0, 0, 0, 0.8);
  color: #fff;
  padding: 14px 24px;
  border-radius: 12px;
  font-size: 14px;
  max-width: 80%;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  min-width: 120px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.toast-content.success {
  background: linear-gradient(135deg, #52c41a 0%, #389e0d 100%);
}

.toast-content.error {
  background: linear-gradient(135deg, #ff4d4f 0%, #cf1322 100%);
}

.toast-content.warning {
  background: linear-gradient(135deg, #faad14 0%, #d48806 100%);
}

.toast-icon {
  font-size: 32px;
  font-weight: bold;
  line-height: 1;
}

.toast-message {
  line-height: 1.5;
}

.toast-fade-enter-active,
.toast-fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.toast-fade-enter-from,
.toast-fade-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}

.center.toast-fade-enter-from,
.center.toast-fade-leave-to {
  transform: translateY(-50%) scale(0.9);
}
</style>
