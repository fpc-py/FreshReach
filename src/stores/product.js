import { defineStore } from 'pinia'
import { ref } from 'vue'
// 1. 引入真实的 API 方法，而不是 mock 数据
import { 
  getCategories, 
  addCategory as apiAddCategory, 
  updateCategory as apiUpdateCategory, 
  deleteCategory as apiDeleteCategory,
  getProductList, 
  addProduct as apiAddProduct, 
  updateProduct as apiUpdateProduct, 
  deleteProduct as apiDeleteProduct,
  toggleProductStatus as apiToggleProductStatus
} from '@/api/product'

export const useProductStore = defineStore('product', () => {
  // 状态定义
  const products = ref([])
  const categories = ref([])
  const total = ref(0)
  const loading = ref(false)

  // --- 分类相关操作 ---

  // 获取分类列表
  const fetchCategories = async () => {
    try {
      // 调用真实接口
      const res = await getCategories()
      // 假设后端返回格式为 { code: 200, data: [...] }
      if (res.code === 200) {
        categories.value = res.data
      }
    } catch (error) {
      console.error('获取分类失败:', error)
    }
  }

  // 新增分类
  const addCategory = async (data) => {
    try {
      const res = await apiAddCategory(data)
      if (res.code === 200) {
        await fetchCategories() // 成功后刷新列表
        return { success: true }
      }
      return { success: false, message: res.message || '添加失败' }
    } catch (error) {
      return { success: false, message: error.message || '网络错误' }
    }
  }

  // 更新分类
  const updateCategory = async (id, data) => {
    try {
      const res = await apiUpdateCategory(id, data)
      if (res.code === 200) {
        await fetchCategories() // 成功后刷新列表
        return { success: true }
      }
      return { success: false, message: res.message || '更新失败' }
    } catch (error) {
      return { success: false, message: error.message || '网络错误' }
    }
  }

  // 删除分类
  const deleteCategory = async (id) => {
    try {
      const res = await apiDeleteCategory(id)
      if (res.code === 200) {
        await fetchCategories() // 成功后刷新列表
        return { success: true }
      }
      return { success: false, message: res.message || '删除失败' }
    } catch (error) {
      return { success: false, message: error.message || '网络错误' }
    }
  }

  // --- 商品相关操作 ---


  // 获取商品列表
  const fetchProducts = async (params = {}) => {
    loading.value = true
    try {
      const res = await getProductList(params)
      if (res.code === 200) {
        // 1. 适配分页结构：后端返回的是 records，前端期望 list
        const rawList = res.data.records || []
        const backendTotal = res.data.total || 0 

                // 创建一个分类 ID 到名称的映射表，提高查找效率
        const categoryMap = {}
        categories.value.forEach(cat => {
          categoryMap[cat.id] = cat.categoryName
        
        })

        
        // 2. 数据字段映射：将后端字段转换为前端组件使用的字段
        products.value = rawList.map(item => ({
          id: item.id,
          name: item.productName,           // productName -> name
          image: item.coverImage,           // coverImage -> image
          description: item.description,
          categoryId: item.categoryId,
          categoryName: categoryMap[item.categoryId] || '未分类',          // 后端没返回分类名，暂时默认，或通过 categoryId 关联 categories 查找
          price: item.salePrice || 0,           // 后端返回数据示例中没看到 price，如果有请保留，否则给默认值
          originalPrice: item.marketPrice || 0,
          stock: item.stock || 0,           // 后端返回数据示例中没看到 stock，如果有请保留，否则给默认值
          isOnSale: item.status === 1,      // status: 1 -> isOnSale: true
          createTime: item.createTime,
          updateTime: item.updateTime
        }))
        
        total.value = backendTotal
      }
    } catch (error) {
      console.error('获取商品列表失败:', error)
      products.value = []
      total.value = 0
    } finally {
      loading.value = false
    }
  }

  // 新增商品
  const addProduct = async (data) => {
    try {
      const res = await apiAddProduct(data)
      if (res.code === 200) {
        return { success: true }
      }
      return { success: false, message: res.message || '添加失败' }
    } catch (error) {
      return { success: false, message: error.message || '网络错误' }
    }
  }

  // 更新商品
  const updateProduct = async (id, data) => {
    try {
      const res = await apiUpdateProduct(id, data)
      if (res.code === 200) {
        return { success: true }
      }
      return { success: false, message: res.message || '更新失败' }
    } catch (error) {
      return { success: false, message: error.message || '网络错误' }
    }
  }

  // 删除商品
  const deleteProduct = async (id) => {
    try {
      const res = await apiDeleteProduct(id)
      if (res.code === 200) {
        return { success: true }
      }
      return { success: false, message: res.message || '删除失败' }
    } catch (error) {
      return { success: false, message: error.message || '网络错误' }
    }
  }

  // 切换上下架状态
  const toggleProductStatus = async (id, isOnSale) => {
    try {
      // 将布尔值转换为后端期望的 Integer: true -> 1, false -> 0
      const status = isOnSale ? 1 : 0
      const res = await apiToggleProductStatus(id, status)
      
      if (res.code === 200) {
        return { success: true }
      }
      return { success: false, message: res.msg || '操作失败' }
    } catch (error) {
      return { success: false, message: error.message || '网络错误' }
    }
  }

  // 暴露给组件使用的方法和状态
  return {
    products,
    categories,
    total,
    loading,
    fetchCategories,
    addCategory,
    updateCategory,
    deleteCategory,
    fetchProducts,
    addProduct,
    updateProduct,
    deleteProduct,
    toggleProductStatus
  }
})