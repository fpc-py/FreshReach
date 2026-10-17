<template>
  <div class="page-container product-manage">
    <div class="page-header">
      <h1>商品管理</h1>
    </div>
    
    <!-- 操作栏 -->
    <div class="action-bar">
      <div class="filter-left">
        <el-select v-model="filterCategoryId" placeholder="全部分类" clearable style="width: 160px">
          <el-option
            v-for="cat in categories"
            :key="cat.id"
            :label="cat.categoryName"
            :value="cat.id"
          />
        </el-select>
        <el-input
          v-model="searchKeyword"
          placeholder="搜索商品名称"
          :prefix-icon="Search"
          clearable
          @input="handleSearch"
          style="width: 200px"
        />
        <el-radio-group v-model="filterStatus" @change="handleStatusChange">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="true">在售</el-radio-button>
          <el-radio-button label="false">已下架</el-radio-button>
        </el-radio-group>
      </div>
      <div class="action-right">
        <el-button @click="showCategoryDialog">分类管理</el-button>
        <el-button type="primary" @click="showAddDialog">
          <el-icon><Plus /></el-icon>
          新增商品
        </el-button>
      </div>
    </div>
    
    <!-- 商品列表 -->
    <div class="product-list">
      <el-table
        :data="products"
        v-loading="loading"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="name" label="商品信息" min-width="300">
          <template #default="{ row }">
            <div class="product-info">
              <el-image
                :src="row.image || defaultImage"
                fit="cover"
                class="product-image"
              >
                <template #error>
                  <div class="image-placeholder">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              <div class="product-detail">
                <div class="product-name">{{ row.name }}</div>
                <div class="product-desc">{{ row.description || '暂无描述' }}</div>
                <div class="product-category">
                  <el-tag size="small" type="info">{{ row.categoryName }}</el-tag>
                </div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格" width="120" align="center">
          <template #default="{ row }">
            <div class="price-info">
              <span class="current-price">¥{{ row.price }}</span>
              <span v-if="row.originalPrice > row.price" class="original-price">
                ¥{{ row.originalPrice }}
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" align="center">
          <template #default="{ row }">
            <span :class="{ 'low-stock': row.stock < 10 }">
              {{ row.stock }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.isOnSale ? 'success' : 'info'" size="small">
              {{ row.isOnSale ? '在售' : '已下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-button type="primary" link size="small" @click="handleEdit(row)">
                编辑
              </el-button>
              <el-switch
                v-model="row.isOnSale"
                size="small"
                inline-prompt
                active-text="上架"
                inactive-text="下架"
                @change="handleToggleStatus(row)"
              />
              <el-button type="danger" link size="small" @click="handleDelete(row)">
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>
    
    <!-- 商品表单弹窗 -->
    <ProductForm
      v-model="productFormVisible"
      :product="currentProduct"
      :categories="categories"
      @success="handleProductSuccess"
    />
    
    <!-- 分类管理弹窗 -->
    <el-dialog
      v-model="categoryDialogVisible"
      title="分类管理"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="category-list">
        <div v-for="cat in categories" :key="cat.id" class="category-item">
          <span v-if="editingCategoryId !== cat.id">{{ cat.categoryName }}</span>
          <el-input
            v-else
            v-model="editingCategoryName"
            size="small"
            style="width: 160px"
            @keyup.enter="handleSaveCategory(cat.id)"
          />
          <div class="category-actions">
            <template v-if="editingCategoryId !== cat.id">
              <el-button type="primary" link size="small" @click="startEditCategory(cat)">
                编辑
              </el-button>
              <el-button
                type="danger"
                link
                size="small"
                @click="handleDeleteCategory(cat.id)"
                :disabled="hasProductInCategory(cat.id)"
              >
                删除
              </el-button>
            </template>
            <template v-else>
              <el-button type="primary" link size="small" @click="handleSaveCategory(cat.id)">
                保存
              </el-button>
              <el-button type="info" link size="small" @click="cancelEditCategory">
                取消
              </el-button>
            </template>
          </div>
        </div>
      </div>
     <div class="add-category">
  <el-input
    v-model="newCategoryName"
    placeholder="输入新分类名称"
    style="width: 200px"
    @keyup.enter="handleAddCategory" 
  />
  <el-button 
    type="primary" 
    @click="handleAddCategory" 
    :disabled="!newCategoryName || !newCategoryName.trim()"
  >
    添加分类
  </el-button>
</div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Picture } from '@element-plus/icons-vue'
import { useProductStore } from '@/stores/product'
import ProductForm from '@/components/ProductForm.vue'

const productStore = useProductStore()

const defaultImage = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjRjVGNEY2Ii8+PHRleHQgZmlsbD0iIzg4ODg4OCIgZm9udC1mYW1pbHk9InNhbnMtc2VyaWYiIGZvbnQtc2l6ZT0iMjQiIHRleHQtYW5jaG9yPSJtaWRkbGUiIHg9IjUwJSIgeT0iNTAlIj7lm77niI48L3RleHQ+PC9zdmc+'

const filterCategoryId = ref('')
const filterStatus = ref('')
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)

const loading = computed(() => productStore.loading)
const products = computed(() => productStore.products)
const categories = computed(() => productStore.categories)
const total = computed(() => productStore.total)

// 商品表单
const productFormVisible = ref(false)
const currentProduct = ref(null)

// 分类管理
const categoryDialogVisible = ref(false)
const editingCategoryId = ref(null)
const editingCategoryName = ref('')
const newCategoryName = ref('')

// 加载数据
const fetchData = async () => {
  const params = {
    page: currentPage.value,
    pageSize: pageSize.value
  }
  
  if (filterCategoryId.value) {
    params.categoryId = filterCategoryId.value
  }
  
  if (filterStatus.value !== '') {
    params.isOnSale = filterStatus.value === 'true'
  }
  
  if (searchKeyword.value) {
    params.keyword = searchKeyword.value
  }
  
  await Promise.all([
    productStore.fetchProducts(params),
    productStore.fetchCategories()
  ])
}

// 搜索
let searchTimer = null
const handleSearch = () => {
  clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    currentPage.value = 1
    productStore.fetchProducts({
      page: currentPage.value,
      pageSize: pageSize.value,
      categoryId: filterCategoryId.value || undefined,
      isOnSale: filterStatus.value !== '' ? filterStatus.value === 'true' : undefined,
      keyword: searchKeyword.value || undefined
    })
  }, 300)
}

// 状态筛选
const handleStatusChange = () => {
  currentPage.value = 1
  handleSearch()
}

// 分页
const handleSizeChange = () => {
  currentPage.value = 1
  fetchData()
}

const handlePageChange = () => {
  fetchData()
}

// 显示新增弹窗
const showAddDialog = () => {
  currentProduct.value = null
  productFormVisible.value = true
}

// 编辑商品
const handleEdit = (product) => {
  currentProduct.value = product
  productFormVisible.value = true
}

// 商品操作成功
const handleProductSuccess = async (data) => {
  if (currentProduct.value?.id) {
    // 编辑
    const result = await productStore.updateProduct(currentProduct.value.id, data)
    if (result.success) {
      ElMessage.success('更新成功')
    } else {
      ElMessage.error(result.message)
    }
  } else {
    // 新增
    const result = await productStore.addProduct(data)
    if (result.success) {
      ElMessage.success('添加成功')
    } else {
      ElMessage.error(result.message)
    }
  }
  fetchData()
}

// 切换上下架
const handleToggleStatus = async (product) => {
  // product.isOnSale 是当前开关的状态（true/false）
  // 注意：el-switch 在 @change 触发时，v-model 绑定的值可能已经改变，
  // 但通常我们传入的是 row 对象，此时 row.isOnSale 已经是新值了。
  
  const result = await productStore.toggleProductStatus(product.id, product.isOnSale)
  
  if (result.success) {
    ElMessage.success(product.isOnSale ? '已上架' : '已下架')
  } else {
    ElMessage.error(result.message)
    // 如果失败，回滚开关状态
    product.isOnSale = !product.isOnSale
  }
}
// 删除商品
const handleDelete = async (product) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除商品「${product.name}」吗？此操作不可恢复。`,
      '删除确认',
      {
        confirmButtonText: '确认删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const result = await productStore.deleteProduct(product.id)
    if (result.success) {
      ElMessage.success('删除成功')
      fetchData()
    } else {
      ElMessage.error(result.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message)
    }
  }
}

// 分类管理弹窗
const showCategoryDialog = () => {
  categoryDialogVisible.value = true
}

// 检查分类下是否有商品
const hasProductInCategory = (categoryId) => {
  return products.value.some(p => p.categoryId === categoryId)
}

// 添加分类
const handleAddCategory = async () => {
  const name = newCategoryName.value?.trim()
  
  if (!name) {
    ElMessage.warning('请输入分类名称')
    return
  }
  
  const result = await productStore.addCategory({  categoryName: name  })
  
  if (result && result.success) {
    ElMessage.success('添加成功')
    newCategoryName.value = '' // 清空输入框
  } else {
    // 如果 store 中没有捕获错误，这里可能需要处理
    ElMessage.error(result?.message || '添加失败，请稍后重试')
  }
}

// 开始编辑分类
const startEditCategory = (cat) => {
  editingCategoryId.value = cat.id
  editingCategoryName.value = cat.categoryName
}

// 取消编辑分类
const cancelEditCategory = () => {
  editingCategoryId.value = null
  editingCategoryName.value = ''
}

// 保存分类
const handleSaveCategory = async (id) => {
  if (!editingCategoryName.value.trim()) {
    ElMessage.warning('分类名称不能为空')
    return
  }
  
  const result = await productStore.updateCategory(id, { categoryName: editingCategoryName.value.trim() })
  if (result.success) {
    ElMessage.success('更新成功')
    editingCategoryId.value = null
    editingCategoryName.value = ''
  } else {
    ElMessage.error(result.message)
  }
}

// 删除分类
const handleDeleteCategory = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除此分类吗？', '删除确认', {
      confirmButtonText: '确认删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const result = await productStore.deleteCategory(id)
    if (result.success) {
      ElMessage.success('删除成功')
      if (filterCategoryId.value === id) {
        filterCategoryId.value = ''
      }
    } else {
      ElMessage.error(result.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message)
    }
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.product-manage {
  min-width: 1024px;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #F8FAF9;
  border-radius: 8px;
  padding: 16px 20px;
  margin-bottom: 20px;
}

.filter-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.action-right {
  display: flex;
  gap: 12px;
}

.product-list {
  background: #F8FAF9;
  border-radius: 8px;
  padding: 20px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

/* 商品信息样式 */
.product-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.product-image {
  width: 64px;
  height: 64px;
  border-radius: 8px;
  flex-shrink: 0;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #F8FAF9;
  color: #94A3B8;
}

.product-detail {
  flex: 1;
  min-width: 0;
}

.product-name {
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 4px;
}

.product-desc {
  font-size: 12px;
  color: #94A3B8;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 4px;
}

.product-category {
  display: inline-block;
}

/* 价格样式 */
.price-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

.current-price {
  font-size: 14px;
  font-weight: 600;
  color: #E53935;
}

.original-price {
  font-size: 12px;
  color: #94A3B8;
  text-decoration: line-through;
}

.low-stock {
  color: #E53935;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 分类管理 */
.category-list {
  margin-bottom: 20px;
}

.category-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border-bottom: 1px solid #E8EDEB;
}

.category-item:last-child {
  border-bottom: none;
}

.category-actions {
  display: flex;
  gap: 8px;
}

.add-category {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid #E8EDEB;
}
</style>
