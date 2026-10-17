<template>
  <div id="app" class="app-container">
    <router-view v-slot="{ Component }">
      <transition name="fade" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
    
    <nav class="tab-bar" v-if="showTabBar">
      <router-link to="/index" class="tab-item">
        <span class="tab-icon">📦</span>
        <span class="tab-text">抢单</span>
      </router-link>
      <router-link to="/orders" class="tab-item">
        <span class="tab-icon">📋</span>
        <span class="tab-text">订单</span>
      </router-link>
      <router-link to="/profile" class="tab-item">
        <span class="tab-icon">👤</span>
        <span class="tab-text">我的</span>
      </router-link>
    </nav>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const showTabBar = computed(() => {
  const tabPages = ['/index', '/orders', '/profile']
  return tabPages.includes(route.path)
})
</script>

<style scoped>
.app-container {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: #fff;
  display: flex;
  border-top: 1px solid #eee;
  z-index: 100;
  padding-bottom: env(safe-area-inset-bottom);
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-decoration: none;
  color: #999;
  font-size: 12px;
  gap: 4px;
}

.tab-item.router-link-active {
  color: #FF6B35;
}

.tab-icon {
  font-size: 24px;
}

.tab-text {
  font-size: 12px;
}
</style>
