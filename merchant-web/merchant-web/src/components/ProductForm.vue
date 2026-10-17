<template>
  <el-dialog
    v-model="visible"
    :title="isEdit ? '编辑商品' : '新增商品'"
    width="600px"
    :close-on-click-modal="false"
    @closed="handleClosed"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-form-item label="商品名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入商品名称" maxlength="50" show-word-limit />
      </el-form-item>
      
      <el-form-item label="商品分类" prop="categoryId">
        <el-select v-model="formData.categoryId" placeholder="请选择分类" style="width: 100%">
          <el-option
            v-for="cat in categories"
            :key="cat.id"
            :label="cat.categoryName"
            :value="cat.id"
          />
        </el-select>
      </el-form-item>
      
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="价格" prop="price">
            <el-input-number
              v-model="formData.price"
              :min="0"
              :precision="2"
              :controls="false"
              placeholder="0.00"
              style="width: 100%"
            >
              <template #prefix>¥</template>
            </el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原价" prop="originalPrice">
            <el-input-number
              v-model="formData.originalPrice"
              :min="0"
              :precision="2"
              :controls="false"
              placeholder="0.00"
              style="width: 100%"
            >
              <template #prefix>¥</template>
            </el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="库存" prop="stock">
        <el-input-number
          v-model="formData.stock"
          :min="0"
          :max="9999"
          :controls="false"
          placeholder="0"
          style="width: 100%"
        />
      </el-form-item>
      
      <el-form-item label="商品描述" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入商品描述"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>
      
      <el-form-item label="商品规格">
        <div class="specs-container">
          <div v-for="(spec, specIndex) in formData.specs" :key="specIndex" class="spec-item">
            <el-input v-model="spec.name" placeholder="规格名称" style="width: 120px" />
            <el-select
              v-model="spec.options"
              multiple
              filterable
              allow-create
              default-first-option
              placeholder="输入选项后回车"
              style="flex: 1"
            >
              <el-option
                v-for="opt in spec.options"
                :key="opt"
                :label="opt"
                :value="opt"
              />
            </el-select>
            <el-button type="danger" link @click="removeSpec(specIndex)">删除</el-button>
          </div>
          <el-button type="primary" link @click="addSpec">+ 添加规格</el-button>
        </div>
      </el-form-item>
      
    <el-form-item label="商品图片">
    <el-upload
      class="product-image-uploader"
      :show-file-list="false"
      :before-upload="handleBeforeUpload"
      :http-request="handleUpload"
      :disabled="uploading" 
    >
      <!-- 4. 添加上传中的加载状态显示 -->
      <div v-if="uploading" class="uploader-loading">
        <el-icon class="is-loading"><Plus /></el-icon>
      </div>
      <img v-else-if="formData.image" :src="formData.image" class="product-image" />
      <el-icon v-else class="uploader-icon"><Plus /></el-icon>
    </el-upload>
    <div class="upload-tip">支持 JPG、PNG 格式，大小不超过 2MB</div>
  </el-form-item>
    </el-form>
    
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">
        {{ isEdit ? '保存' : '添加' }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import OSS from 'ali-oss' // 1. 引入 OSS SDK
import { getOssPolicy } from '@/api/product' // 2. 引入获取签名的 API
import { uploadImage } from '@/api/common'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  product: {
    type: Object,
    default: null
  },
  categories: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

const formRef = ref(null)
const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const isEdit = computed(() => !!props.product?.id)

const defaultFormData = {
  name: '',
  categoryId: null,
  price: null,
  originalPrice: null,
  stock: 0,
  description: '',
  specs: [],
  image: '',
  isOnSale: true
}

const formData = reactive({ ...defaultFormData })

watch(() => props.product, (newVal) => {
  if (newVal) {
    Object.assign(formData, {
      name: newVal.name || '',
      categoryId: newVal.categoryId || null,
      price: newVal.price || null,
      originalPrice: newVal.originalPrice || null,
      stock: newVal.stock || 0,
      description: newVal.description || '',
      specs: newVal.specs ? [...newVal.specs] : [],
      image: newVal.image || '',
      isOnSale: newVal.isOnSale !== undefined ? newVal.isOnSale : true
    })
  } else {
    Object.assign(formData, { ...defaultFormData })
  }
}, { immediate: true })

const formRules = {
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' }
  ],
  stock: [
    { required: true, message: '请输入库存', trigger: 'blur' }
  ]
}

const submitting = ref(false)
const uploading = ref(false) // 新增：控制上传加载状态
const addSpec = () => {
  formData.specs.push({ name: '', options: [] })
}

const removeSpec = (index) => {
  formData.specs.splice(index, 1)
}

const handleBeforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2
  
  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB')
    return false
  }
  return true
}


const handleUpload = async (options) => {
  const { file } = options
  uploading.value = true
  
  try {
    // 调用后端上传接口
    const res = await uploadImage(file)
    
    // 假设后端返回格式: { code: 200, msg: "成功", data: "https://oss.../xxx.jpg" }
    if (res.code === 200 || res.status === 200) {
      // 注意：根据你的 Result<String> 结构，URL 通常在 res.data 中
      const imageUrl = res.data 
      
      formData.image = imageUrl
      ElMessage.success('图片上传成功')
      options.onSuccess(res) // 通知 el-upload 成功
    } else {
      throw new Error(res.msg || '上传失败')
    }
  } catch (error) {
    console.error('Upload error:', error)
    ElMessage.error(error.message || '图片上传失败')
    options.onError(error) // 通知 el-upload 失败
  } finally {
    uploading.value = false
  }
}



const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true
    
    // 【核心修改】字段映射：将前端表单字段转换为后端期望的字段
    const submitData = {
      productName: formData.name,           // name -> productName
      coverImage: formData.image,           // image -> coverImage
      salePrice: formData.price,            // price -> salePrice
      marketPrice: formData.originalPrice,  // originalPrice -> marketPrice
      stock: formData.stock,
      description: formData.description,
      categoryId: formData.categoryId,
      status: formData.isOnSale ? 1 : 0,    // 如果表单中有 isOnSale，转换为 status (1/0)
      // 如果有 specs，根据后端需求决定是否发送，通常后端可能不直接接收 specs 或格式不同
      // specs: formData.specs.filter(s => s.name && s.options.length > 0) 
    }

    // 如果是编辑模式，需要带上 id
    if (isEdit.value && props.product?.id) {
      submitData.id = props.product.id
    }
    
    emit('success', submitData)
    visible.value = false
  } catch (error) {
    console.error('Form validation failed:', error)
  } finally {
    submitting.value = false
  }
}

const handleClosed = () => {
  formRef.value?.resetFields()
  Object.assign(formData, { ...defaultFormData })
}
</script>

<style scoped>
.specs-container {
  width: 100%;
}

.spec-item {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.product-image-uploader {
  width: 120px;
  height: 120px;
  border: 1px dashed #DCDFE6;
  border-radius: 8px;
  cursor: pointer;
  overflow: hidden;
  transition: border-color 0.2s;
}

.product-image-uploader:hover {
  border-color: #27AE60;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.uploader-icon {
  font-size: 28px;
  color: #8C939D;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-tip {
  font-size: 12px;
  color: #94A3B8;
  margin-top: 8px;
}
</style>

