// src/api/common.js
import request from '@/utils/request' // 或者你的 axios 实例路径

export const uploadImage = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/api/common/upload', // 根据实际后端部署路径调整
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}