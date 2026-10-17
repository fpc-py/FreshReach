<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-card class="filter-container" shadow="never">
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="姓名">
          <el-input
            v-model="searchName"
            placeholder="请输入用户名"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
        >新增员工</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <!-- 【修复】直接绑定 tableData，因为后端已经返回了当前页的数据 -->
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column label="ID" align="center" prop="id" width="100" />
      <el-table-column label="姓名" align="center" prop="name" width="120" />
      <el-table-column label="用户名" align="center" prop="username" width="120" />
      <el-table-column label="手机号" align="center" prop="phone" width="120" />
      <el-table-column label="性别" align="center" prop="sex" width="80">
        <template #default="scope">
          {{ scope.row.sex === '1' ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          {{ parseTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="250" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(scope.row)"
          >编辑</el-button>
           
          <!-- 【新增】删除按钮 -->
          <el-button
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
          <el-button
            v-if="scope.row.status === 1"
            link
            type="warning"
            icon="CircleClose"
            @click="handleStatusChange(scope.row, 0)"
          >禁用</el-button>
          
          <el-button
            v-if="scope.row.status === 0"
            link
            type="success"
            icon="CircleCheck"
            @click="handleStatusChange(scope.row, 1)"
          >启用</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="open"
      width="600px"
      append-to-body
    >
      <el-form ref="employeeRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名"  />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!isEdit">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio label="1">男</el-radio>
            <el-radio label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  addEmployee, 
  getEmployeeList, 
  updateEmployeeStatus, 
  getEmployeeById, 
  updateEmployee ,
  deleteEmployee
} from '@/api/employee'

const { proxy } = getCurrentInstance()

// 状态定义
const loading = ref(false)
const open = ref(false)
const total = ref(0)
const tableData = ref([]) // 存储后端返回的当前页数据
const dialogTitle = ref('')
const isEdit = ref(false)
const employeeRef = ref(null)

// 分页与搜索状态
const pageNum = ref(1)
const pageSize = ref(10)
const searchName = ref('')

const form = reactive({
  id: undefined,
  name: undefined,
  username: undefined,
  password: undefined,
  phone: undefined,
  sex: '1',
  idNumber: undefined
})

const rules = {
  name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
  username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
  password: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
  phone: [{ required: true, message: '手机号不能为空', trigger: 'blur' }]
}

// 【修复】获取分页数据
const getList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      userName: searchName.value
    }
      //  console.log('正在请求员工列表，参数:', params) // 【调试】打印参数
    const res = await getEmployeeList(params)
    
    // console.log('后端返回数据:', res) // 【调试】打印返回结果

    // 假设后端返回结构为 { code: 200, data: { records: [], total: 100 } }
    if (res.code === 200 && res.data) {
      tableData.value = res.data.records || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error(res.msg || '获取数据失败')
      tableData.value = []
      total.value = 0
    }
  } catch (err) {
    console.error(err)
    ElMessage.error('网络请求错误')
  } finally {
    loading.value = false
  }
}

// 【新增】删除处理函数
function handleDelete(row) {
  ElMessageBox.confirm(`确认要删除员工 "${row.name}" 吗？此操作不可恢复！`, '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return deleteEmployee(row.id)
  }).then(() => {
    ElMessage.success('删除成功')
    getList() // 刷新列表
  }).catch((err) => {
    if (err !== 'cancel') {
      console.error(err)
      ElMessage.error('删除失败')
    }
  })
}
// 页码改变
const handleCurrentChange = (val) => {
  pageNum.value = val
  getList()
}

// 每页条数改变
const handleSizeChange = (val) => {
  pageSize.value = val
  pageNum.value = 1
  getList()
}

// 搜索
function handleSearch() {
  pageNum.value = 1
  getList()
}

// 重置
function resetQuery() {
  searchName.value = ''
  handleSearch()
}

function handleAdd() {
  resetForm()
  isEdit.value = false
  open.value = true
  dialogTitle.value = '新增员工'
}

function handleUpdate(row) {
  resetForm() // 先清空旧数据
  isEdit.value = true
  const id = row.id
  
  getEmployeeById(id).then(res => {
    if (res.code === 200 && res.data) {
      // 【修复1】使用 Object.assign 更新 reactive 对象，而不是直接赋值
      // 注意：不要写 form.value = ...，因为 form 是 reactive 不是 ref
      Object.assign(form, res.data)
      
      // 【修复2】处理性别字段，确保是字符串类型，以便 el-radio 能正确识别
      // 假设后端返回 1 或 0，我们将其转为字符串 '1' 或 '0'
      if (res.data.sex !== undefined && res.data.sex !== null) {
        form.sex = String(res.data.sex)
      } else {
        form.sex = '1' // 默认值
      }
      
      dialogTitle.value = '修改员工'
      open.value = true
    } else {
      ElMessage.error(res.msg || '获取员工信息失败')
    }
  }).catch(err => {
    console.error('回显员工信息失败', err)
    ElMessage.error('网络请求错误')
  })
}
function handleStatusChange(row, status) {
  const text = status === 1 ? '启用' : '禁用'
  ElMessageBox.confirm(`确认要${text}员工"${row.name}"吗?`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return updateEmployeeStatus(row.id, status)
  }).then(() => {
    ElMessage.success(`${text}成功`)
    getList()
  }).catch(() => {})
}

function submitForm() {
  employeeRef.value.validate(valid => {
    if (valid) {
      if (form.id !== undefined) {
        updateEmployee(form).then(response => {
          ElMessage.success('修改成功')
          open.value = false
          getList()
        })
      } else {
        addEmployee(form).then(response => {
          ElMessage.success('新增成功')
          open.value = false
          getList()
        })
      }
    }
  })
}

function cancel() {
  open.value = false
  resetForm()
}

function resetForm() {
  form.id = undefined
  form.name = undefined
  form.username = undefined
  form.password = undefined
  form.phone = undefined
  form.sex = '1'
  form.idNumber = undefined
  if (employeeRef.value) {
    employeeRef.value.resetFields()
  }
}

function parseTime(time) {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString()
}

// 初始化
getList()
</script>

<style scoped>
.filter-container {
  margin-bottom: 10px;
}
.mb8 {
  margin-bottom: 8px;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>