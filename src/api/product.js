import { get, post, put, del } from './index'

// 获取商品分类列表
export const getCategories = () => {
  return get('/products/categories')
}

// 新增分类
export const addCategory = (data) => {
  return post('/products/categories', data)
}

// 更新分类
export const updateCategory = (id, data) => {
  return put(`/products/categories/${id}`, data)
}

// 删除分类
export const deleteCategory = (id) => {
  return del(`/products/categories/${id}`)
}

// 获取商品列表
export const getProductList = (params) => {
  return get('/products', params)
}

// 获取商品详情
export const getProductDetail = (id) => {
  return get(`/products/${id}`)
}

// 新增商品
export const addProduct = (data) => {
  return post('/products', data)
}

// 更新商品
export const updateProduct = (id, data) => {
  return put(`/products/${id}`, data)
}

// 删除商品
export const deleteProduct = (id) => {
  return del(`/products/${id}`)
}

// 上下架商品

export const toggleProductStatus = (id, status) => {
  // status: 1 为上架, 0 为下架
  return put(`/products/${id}/status`, { status })
}

// 获取 OSS 上传签名
export const getOssPolicy = () => {
  return get('/oss/policy')
}