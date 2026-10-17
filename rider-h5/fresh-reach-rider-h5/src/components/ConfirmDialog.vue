<template>
  <Teleport to="body">
    <Transition name="dialog-fade">
      <div v-if="visible" class="dialog-mask" @click="handleMaskClick">
        <Transition name="dialog-scale">
          <div v-if="visible" class="dialog-content" @click.stop>
            <div v-if="title" class="dialog-title">{{ title }}</div>
            <div class="dialog-message">{{ message }}</div>
            <div class="dialog-buttons">
              <div v-if="showCancel" class="dialog-btn cancel" @click="handleCancel">{{ cancelText }}</div>
              <div class="dialog-btn confirm" @click="handleConfirm">{{ confirmText }}</div>
            </div>
          </div>
        </Transition>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  visible: Boolean,
  title: String,
  message: String,
  showCancel: {
    type: Boolean,
    default: true
  },
  cancelText: {
    type: String,
    default: '取消'
  },
  confirmText: {
    type: String,
    default: '确定'
  },
  maskClosable: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:visible', 'confirm', 'cancel'])

const handleConfirm = () => {
  emit('confirm')
  emit('update:visible', false)
}

const handleCancel = () => {
  emit('cancel')
  emit('update:visible', false)
}

const handleMaskClick = () => {
  if (props.maskClosable) {
    handleCancel()
  }
}
</script>

<style scoped>
.dialog-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9998;
  backdrop-filter: blur(2px);
}

.dialog-content {
  background: #fff;
  border-radius: 16px;
  width: 80%;
  max-width: 320px;
  padding: 28px 24px 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.dialog-title {
  font-size: 17px;
  font-weight: 600;
  color: #1a1a1a;
  text-align: center;
  margin-bottom: 12px;
}

.dialog-message {
  font-size: 15px;
  color: #666;
  line-height: 1.6;
  text-align: center;
  margin-bottom: 24px;
}

.dialog-buttons {
  display: flex;
  gap: 12px;
}

.dialog-btn {
  flex: 1;
  height: 44px;
  border-radius: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 15px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.dialog-btn:active {
  transform: scale(0.96);
}

.dialog-btn.cancel {
  background: #f5f5f5;
  color: #666;
}

.dialog-btn.cancel:hover {
  background: #e8e8e8;
}

.dialog-btn.confirm {
  background: linear-gradient(135deg, #FF6B35 0%, #FF8C5A 100%);
  color: #fff;
  font-weight: 500;
}

.dialog-btn.confirm:hover {
  opacity: 0.9;
}

.dialog-fade-enter-active,
.dialog-fade-leave-active {
  transition: opacity 0.3s ease;
}

.dialog-fade-enter-from,
.dialog-fade-leave-to {
  opacity: 0;
}

.dialog-scale-enter-active,
.dialog-scale-leave-active {
  transition: transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1), opacity 0.3s ease;
}

.dialog-scale-enter-from,
.dialog-scale-leave-to {
  opacity: 0;
  transform: scale(0.9);
}
</style>
