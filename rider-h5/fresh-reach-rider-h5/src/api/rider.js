import request from '@/utils/request'

export const riderLogin = (data) => {
  return request({
    url: '/rider/login',
    method: 'POST',
    data: {
      ...data,
      role: 3
    }
  })
}

export const updateRiderStatus = (status, id) => {
  return request({
    url: '/rider/status',
    method: 'PUT',
    params: {
      status: status,
      id: id
    }
  })
}

export const getAvailableOrders = () => {
  return request({
    url: '/rider/available-orders',
    method: 'GET'
  })
}

export const grabOrder = (data) => {
  return request({
    url: '/rider/grab',
    method: 'PUT',
    data
  })
}

export const getMyOrders = (id) => {
  return request({
    url: '/rider/orders',
    method: 'GET',
    params: { id }
  })
}

export const pickupOrder = (id) => {
  return request({
    url: `/rider/orders/${id}/pickup`,
    method: 'PUT'
  })
}

export const deliverOrder = (id) => {
  return request({
    url: `/rider/orders/${id}/deliver`,
    method: 'PUT'
  })
}

export const getIncomeStats = (id) => {
  return request({
    url: '/rider/income/stats',
    method: 'GET',
    params: { id }
  })
}
