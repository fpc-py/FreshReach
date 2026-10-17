import { get, post, put, del } from './index'

// 获取订单列表
export const getOrderList = (params) => {
  return get('/orders', params)
}

// 获取订单详情
export const getOrderDetail = (id) => {
  return get(`/orders/${id}`)
}

// 接单
export const acceptOrder = (id) => {
  return put(`/orders/${id}/accept`)
}

// 拒单
export const rejectOrder = (id, cancelReason) => {
  return put(`/orders/${id}/reject`, { cancelReason })
}

// 完成备餐
export const finishPreparing = (id) => {
  return put(`/orders/${id}/prepare-done`)
}

// 同意退款
export const agreeRefund = (id) => {
  return put(`/orders/${id}/refund/agree`)
}

// 拒绝退款
export const rejectRefund = (id, cancelReason) => {
  return put(`/orders/${id}/refund/reject`, { cancelReason })
}
