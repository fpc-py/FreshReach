<template>
  <div class="page-container shop-setting">
    <div class="page-header">
      <h1>营业设置</h1>
    </div>
    
    <div class="settings-content">
      <!-- 营业状态 -->
      <div class="setting-card">
        <div class="setting-header">
          <div class="setting-title">
            <h3>营业状态</h3>
            <p>开启后，店铺将接收新的外卖订单</p>
          </div>
          <el-switch
            v-model="formData.isOpen"
            size="large"
            inline-prompt
            active-text="营业中"
            inactive-text="休息中"
            @change="handleToggleStatus"
          />
        </div>
        <div class="status-indicator" :class="{ 'is-open': formData.isOpen }">
          <span class="indicator-dot"></span>
          {{ formData.isOpen ? '当前正在营业' : '当前暂停营业' }}
        </div>
      </div>
      
      <!-- 营业时间 -->
      <div class="setting-card">
        <div class="setting-header">
          <div class="setting-title">
            <h3>营业时间</h3>
            <p>设置店铺的每日营业时间范围</p>
          </div>
        </div>
        <div class="setting-form">
          <el-form :model="formData" label-width="100px">
            <el-form-item label="开始时间">
              <el-time-select
                v-model="formData.openTime"
                start="00:00"
                end="23:59"
                step="00:30"
                placeholder="选择开始时间"
                style="width: 200px"
              />
            </el-form-item>
            <el-form-item label="结束时间">
              <el-time-select
                v-model="formData.closeTime"
                start="00:00"
                end="23:59"
                step="00:30"
                placeholder="选择结束时间"
                style="width: 200px"
              />
            </el-form-item>
          </el-form>
        </div>
      </div>
      
      <!-- 配送设置 -->
      <div class="setting-card">
        <div class="setting-header">
          <div class="setting-title">
            <h3>配送设置</h3>
            <p>配置外卖配送的相关费用和条件</p>
          </div>
        </div>
        <div class="setting-form">
          <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
            <el-form-item label="配送费" prop="deliveryFee">
              <el-input-number
                v-model="formData.deliveryFee"
                :min="0"
                :max="100"
                :precision="2"
                :controls="false"
                style="width: 200px"
              >
                <template #suffix>元</template>
              </el-input-number>
            </el-form-item>
            <el-form-item label="起送价" prop="minAmount">
              <el-input-number
                v-model="formData.minAmount"
                :min="0"
                :max="1000"
                :precision="2"
                :controls="false"
                style="width: 200px"
              >
                <template #suffix>元</template>
              </el-input-number>
              <span class="form-tip">订单金额达到起送价才能下单</span>
            </el-form-item>
          </el-form>
        </div>
      </div>
      
      <!-- 店铺公告 -->
      <div class="setting-card">
        <div class="setting-header">
          <div class="setting-title">
            <h3>店铺公告</h3>
            <p>展示在店铺首页的公告信息</p>
          </div>
        </div>
        <div class="setting-form">
          <el-input
            v-model="formData.notice"
            type="textarea"
            :rows="4"
            placeholder="请输入店铺公告，如：新客立减5元、满30减10等优惠信息"
            maxlength="200"
            show-word-limit
            style="width: 500px"
          />
        </div>
      </div>
      
      <!-- 保存按钮 -->
      <div class="setting-actions">
        <el-button type="primary" size="large" @click="handleSave" :loading="saving">
          保存设置
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useShopStore } from '@/stores/shop'

const shopStore = useShopStore()
const formRef = ref(null)
const saving = ref(false)

const formData = reactive({
  isOpen: true,
  openTime: '09:00',
  closeTime: '22:00',
  deliveryFee: 3,
  minAmount: 20,
  notice: ''
})

const formRules = {
  deliveryFee: [
    { required: true, message: '请输入配送费', trigger: 'blur' }
  ],
  minAmount: [
    { required: true, message: '请输入起送价', trigger: 'blur' }
  ]
}

// 加载店铺信息
const loadShopInfo = async () => {
  await shopStore.fetchShopInfo()
  const info = shopStore.shopInfo
  Object.assign(formData, {
    isOpen: info.isOpen,
    openTime: info.openTime,
    closeTime: info.closeTime,
    deliveryFee: info.deliveryFee,
    minAmount: info.minAmount,
    notice: info.notice
  })
}

// 切换营业状态
const handleToggleStatus = async (isOpen) => {
  const result = await shopStore.updateShopStatus(isOpen)
  if (result.success) {
    ElMessage.success(isOpen ? '已设置为营业中' : '已设置为休息中')
  } else {
    ElMessage.error(result.message)
    formData.isOpen = !isOpen
  }
}

// 保存设置
const handleSave = async () => {
  try {
    await formRef.value.validate()
    saving.value = true
    
    const result = await shopStore.updateShopInfo({
      openTime: formData.openTime,
      closeTime: formData.closeTime,
      deliveryFee: formData.deliveryFee,
      minAmount: formData.minAmount,
      notice: formData.notice
    })
    
    if (result.success) {
      ElMessage.success('设置已保存')
    } else {
      ElMessage.error(result.message)
    }
  } catch (error) {
    // 验证失败
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadShopInfo()
})
</script>

<style scoped>
.shop-setting {
  min-width: 1024px;
}

.settings-content {
  max-width: 800px;
}

.setting-card {
  background: #F8FAF9;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
}

.setting-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.setting-title h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 4px;
}

.setting-title p {
  font-size: 13px;
  color: #94A3B8;
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: #FFFFFF;
  border-radius: 8px;
  font-size: 14px;
  color: #94A3B8;
}

.status-indicator.is-open {
  color: #27AE60;
}

.indicator-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #94A3B8;
}

.status-indicator.is-open .indicator-dot {
  background: #27AE60;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.setting-form {
  padding-left: 16px;
}

.form-tip {
  margin-left: 12px;
  font-size: 12px;
  color: #94A3B8;
}

.setting-actions {
  margin-top: 24px;
}
</style>
