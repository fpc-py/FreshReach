<template>
  <div class="main-layout">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ 'is-collapsed': isCollapsed }">
      <div class="sidebar-header">
        <div class="logo">
          <svg width="36" height="36" viewBox="0 0 48 48" fill="none">
            <circle cx="24" cy="24" r="24" fill="#27AE60"/>
            <path d="M24 12C17.373 12 12 17.373 12 24C12 30.627 17.373 36 24 36C30.627 36 36 30.627 36 24C36 17.373 30.627 12 24 12ZM24 32C18.477 32 14 27.523 14 22C14 16.477 18.477 12 24 12C29.523 12 34 16.477 34 22C34 27.523 29.523 32 24 32Z" fill="white"/>
            <circle cx="24" cy="24" r="6" fill="white"/>
          </svg>
        </div>
        <div class="logo-text">
          <span class="brand-name">鲜达外卖</span>
          <span class="brand-sub">商家版</span>
        </div>
      </div>
      
      <nav class="sidebar-nav">
        <router-link
          v-for="item in menuItems"
          :key="item.path"
          :to="item.path"
          class="nav-item"
          :class="{ active: isActive(item.path) }"
        >
          <el-icon :size="20">
            <component :is="item.icon" />
          </el-icon>
          <span class="nav-text">{{ item.label }}</span>
          <em v-if="item.badge" class="nav-badge">{{ item.badge }}</em>
        </router-link>
      </nav>
      
      <div class="sidebar-footer">
        <div class="user-info">
          <el-avatar :size="36" style="background: #27AE60">
            {{ merchantName }}
          </el-avatar>
          <div class="user-detail">
            <span class="user-name">{{ merchantName }}</span>
            <span class="user-role">商家</span>
          </div>
        </div>
        <el-button type="danger" plain size="small" @click="handleLogout">
          退出登录
        </el-button>
      </div>
    </aside>
    
    <!-- 主内容区 -->
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
// import { HomeFilled, List, Goods, Setting, DataLine } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { useOrderStore } from '@/stores/order'
import { HomeFilled, List, Goods, Setting, DataLine, UserFilled } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const orderStore = useOrderStore()

const isCollapsed = ref(false)

// 现在 authStore 中有 merchantInfo 了
const merchantName = computed(() => authStore.merchantInfo?.name || '商家')



// 获取登录存在本地的角色 1管理员 0普通员工
const userRole = computed(() => {
  const valStr = localStorage.getItem('userRole')
  const val = Number(valStr)
  console.log('localStorage原始值：', valStr,'转数字后：',val)
  return val
})

// 待处理订单数量
const pendingCount = computed(() => {
  // 如果orders是undefined，替换为空数组[]，再执行filter
  const list = orderStore.orders || []
  return list.filter(o => o.status === 'pending').length
})

const menuItems = computed(() => [
  {
    path: '/dashboard',
    label: '工作台',
    icon: HomeFilled,
    badge: orderStore.orders?.filter(o => o.status === 'pending').length > 0 
      ? orderStore.orders.filter(o => o.status === 'pending').length 
      : undefined
  },
  {
    path: '/orders',
    label: '订单管理',
    icon: List,
    badge: orderStore.orders?.filter(o => o.status === 'pending').length > 0 
      ? orderStore.orders.filter(o => o.status === 'pending').length 
      : undefined
  },
  {
    path: '/products',
    label: '商品管理',
    icon: Goods
  },
  // ==========员工菜单单独处理，加条件过滤==========
  ...(userRole.value === 1 ? [{
    path: '/employee',
    label: '员工管理',
    icon: UserFilled
  }] : []),
  {
    path: '/settings',
    label: '营业设置',
    icon: Setting
  },
  {
    path: '/report',
    label: '数据报表',
    icon: DataLine
  }
])

const isActive = (path) => {
  return route.path.startsWith(path)
}

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '退出确认', {
      confirmButtonText: '确认退出',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 直接调用 store 的 logout
    // store 内部已经处理了清除 token 和 router.replace('/login')
    await authStore.logout()
    
    // 不需要再手动 router.push，也不需要在这里显示成功消息，因为页面已经跳转了
    // 如果希望跳转前显示，可以在 store 跳转前显示，但通常跳转后消息会消失
    
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '退出失败')
    }
  }
}

onMounted(async () => {
  await orderStore.fetchOrders()
})
</script>

<style scoped>
.main-layout {
  display: flex;
  min-height: 100vh;
  background: #FFFFFF;
}

/* 侧边栏 */
.sidebar {
  width: 220px;
  background: linear-gradient(180deg, #1E293B 0%, #0F172A 100%);
  display: flex;
  flex-direction: column;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  z-index: 100;
  transition: width 0.3s;
}

.sidebar.is-collapsed {
  width: 64px;
}

/* 侧边栏头部 */
.sidebar-header {
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 12px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo {
  flex-shrink: 0;
}

.logo-text {
  display: flex;
  flex-direction: column;
}

.brand-name {
  font-size: 16px;
  font-weight: 700;
  color: #FFFFFF;
}

.brand-sub {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
}

/* 导航菜单 */
.sidebar-nav {
  flex: 1;
  padding: 16px 12px;
  overflow-y: auto;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  margin-bottom: 4px;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  transition: all 0.3s;
  position: relative;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #FFFFFF;
}

.nav-item.active {
  background: #27AE60;
  color: #FFFFFF;
}

.nav-item.active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 24px;
  background: #FFFFFF;
  border-radius: 0 4px 4px 0;
}

.nav-text {
  margin-left: 12px;
  font-size: 14px;
  flex: 1;
}

.nav-badge {
  background: #E53935;
  color: #FFFFFF;
  font-size: 11px;
  padding: 2px 6px;
  border-radius: 10px;
  font-style: normal;
  min-width: 18px;
  text-align: center;
}

/* 侧边栏底部 */
.sidebar-footer {
  padding: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.user-detail {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 14px;
  color: #FFFFFF;
  font-weight: 500;
}

.user-role {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
}

/* 主内容区 */
.main-content {
  flex: 1;
  margin-left: 220px;
  min-height: 100vh;
  background: #FFFFFF;
}

/* 响应式 */
@media (max-width: 1024px) {
  .sidebar {
    width: 64px;
  }
  
  .logo-text,
  .nav-text,
  .user-detail {
    display: none;
  }
  
  .main-content {
    margin-left: 64px;
  }
  
  .nav-item {
    justify-content: center;
    padding: 12px;
  }
  
  .nav-item.active::before {
    display: none;
  }
  
  .sidebar-footer {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
}
</style>
