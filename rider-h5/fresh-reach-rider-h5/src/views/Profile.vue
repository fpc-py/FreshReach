<template>
  <div class="page-container">
    <div class="profile-header">
      <div class="avatar">
        <span class="avatar-text">{{ riderInfo.username ? riderInfo.username.charAt(0) : '骑' }}</span>
      </div>
      <div class="user-info">
        <span class="username">{{ riderInfo.username || '骑手用户' }}</span>
        <span class="phone">{{ riderInfo.phone }}</span>
      </div>
      <div :class="['status-badge', isOnline ? 'online' : 'offline']">
        {{ isOnline ? '在线' : '离线' }}
      </div>
    </div>

    <div class="status-card card">
      <div class="status-item">
        <span class="status-label">工作状态</span>
        <div class="status-control">
          <span :class="['status-text', isOnline ? 'online' : 'offline']">
            {{ isOnline ? '已上线' : '已下线' }}
          </span>
          <label class="switch">
            <input type="checkbox" :checked="isOnline" @change="toggleStatus" />
            <span class="slider"></span>
          </label>
        </div>
      </div>
    </div>

    <div class="menu-list">
      <div class="menu-item card" @click="goToIncome">
        <div class="menu-left">
          <span class="menu-icon">💰</span>
          <span class="menu-title">收入统计</span>
        </div>
        <span class="menu-arrow">›</span>
      </div>

      <div class="menu-item card" @click="goToOrders">
        <div class="menu-left">
          <span class="menu-icon">📋</span>
          <span class="menu-title">我的订单</span>
        </div>
        <span class="menu-arrow">›</span>
      </div>

      <div class="menu-item card">
        <div class="menu-left">
          <span class="menu-icon">📞</span>
          <span class="menu-title">联系客服</span>
        </div>
        <span class="menu-arrow">›</span>
      </div>

      <div class="menu-item card">
        <div class="menu-left">
          <span class="menu-icon">⚙️</span>
          <span class="menu-title">设置</span>
        </div>
        <span class="menu-arrow">›</span>
      </div>
    </div>

    <div class="logout-section">
      <button class="btn-logout" @click="handleLogout">退出登录</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useRiderStore } from '@/store/rider'
import { updateRiderStatus } from '@/api/rider'
import { toast, confirm } from '@/utils/notify'

const router = useRouter()
const riderStore = useRiderStore()

const riderInfo = computed(() => riderStore.riderInfo || {})
const isOnline = ref(riderStore.isOnline)

onMounted(() => {
  if (!riderStore.token) {
    router.push('/login')
  }
})

const toggleStatus = async (e) => {
  const userId = riderStore.userId
  console.log('toggleStatus - userId:', userId)
  if (!userId) {
    toast.warning('请先登录或用户信息不完整')
    router.push('/login')
    return
  }
  try {
    await updateRiderStatus(e.target.checked ? 1 : 0, userId)
    isOnline.value = e.target.checked
    riderStore.setOnline(e.target.checked)
    toast.success(e.target.checked ? '已上线' : '已下线')
  } catch (err) {
    console.error('切换状态失败', err)
    e.target.checked = isOnline.value
  }
}

const goToIncome = () => {
  router.push('/income')
}

const goToOrders = () => {
  router.push('/orders')
}

const handleLogout = async () => {
  const ok = await confirm({
    title: '退出登录',
    message: '确定要退出登录吗？',
    confirmText: '退出',
    confirmType: 'danger'
  })
  if (ok) {
    riderStore.logout()
    toast.success('已退出登录')
    router.push('/login')
  }
}
</script>

<style scoped>
.page-container {
  padding: 0 0 80px 0;
  min-height: 100vh;
  background: #f5f5f5;
  overflow-y: auto;
}

.profile-header {
  background: linear-gradient(135deg, #FF6B35 0%, #FF8C5A 100%);
  padding: 40px 20px 60px;
  display: flex;
  align-items: center;
  position: relative;
}

.avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
}

.avatar-text {
  font-size: 28px;
  color: #fff;
  font-weight: bold;
}

.user-info {
  flex: 1;
}

.username {
  display: block;
  font-size: 18px;
  color: #fff;
  font-weight: bold;
  margin-bottom: 4px;
}

.phone {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.8);
}

.status-badge {
  padding: 4px 14px;
  border-radius: 20px;
  font-size: 12px;
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
}

.status-badge.online {
  background: rgba(82, 196, 26, 0.3);
}

.status-card {
  margin: -30px 15px 15px;
  position: relative;
  z-index: 10;
}

.card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.status-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.status-label {
  font-size: 15px;
  color: #333;
  font-weight: 500;
}

.status-control {
  display: flex;
  align-items: center;
  gap: 12px;
}

.status-text {
  font-size: 14px;
  font-weight: 500;
}

.status-text.online {
  color: #52c41a;
}

.status-text.offline {
  color: #999;
}

.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 28px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: .4s;
  border-radius: 28px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 22px;
  width: 22px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: .4s;
  border-radius: 50%;
}

input:checked + .slider {
  background-color: #FF6B35;
}

input:checked + .slider:before {
  transform: translateX(22px);
}

.menu-list {
  padding: 0 15px;
}

.menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 16px;
  margin-bottom: 12px;
  cursor: pointer;
}

.menu-item:active {
  background: #f9f9f9;
}

.menu-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.menu-icon {
  font-size: 22px;
}

.menu-title {
  font-size: 15px;
  color: #333;
}

.menu-arrow {
  font-size: 24px;
  color: #ccc;
}

.logout-section {
  padding: 30px 15px;
  margin-top: 20px;
}

.btn-logout {
  width: 100%;
  height: 44px;
  background: #fff;
  color: #FF4D4F;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  cursor: pointer;
}

.btn-logout:active {
  background: #f9f9f9;
}
</style>
