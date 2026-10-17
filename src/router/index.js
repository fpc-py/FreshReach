import { createRouter, createWebHistory } from 'vue-router'
import { getToken} from '@/utils/token'
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '工作台', requiresAuth: true }
      },
      {
        path: 'orders',
        name: 'OrderManage',
        component: () => import('@/views/OrderManage.vue'),
        meta: { title: '订单管理', requiresAuth: true }
      },
      {
        path: 'products',
        name: 'ProductManage',
        component: () => import('@/views/ProductManage.vue'),
        meta: { title: '商品管理', requiresAuth: true }
      },
      {
        path: 'settings',
        name: 'ShopSetting',
        component: () => import('@/views/ShopSetting.vue'),
        meta: { title: '营业设置', requiresAuth: true }
      },
      {
        path: 'report',
        name: 'Report',
        component: () => import('@/views/Report.vue'),
        meta: { title: '数据报表', requiresAuth: true }
      },
      {
  path: 'employee',
  name: 'Employee',
  component: () => import('@/views/employee/index.vue'),
  meta: { title: '员工管理',requiresAuth: true,
        requiresAdmin: true // 【新增】标记该路由需要管理员权限
  }
}
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = getToken()

  if (to.meta.requiresAuth && !token) {
    next('/login')
    return
  }
  
  if (to.path === '/login' && token) {
    next('/dashboard')
    return
  }
  
  if (to.path === '/register' && token) {
    next('/dashboard')
    return
  }

  // 管理员权限验证
  if (to.meta.requiresAdmin) {
    const userRole = localStorage.getItem('userRole')
    if (userRole != 1) {
      next('/dashboard')
      return
    }
  }

  next()
})

export default router
