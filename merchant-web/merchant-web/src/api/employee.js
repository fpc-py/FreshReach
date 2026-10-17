import request from '@/utils/request' // Assuming you have a configured axios instance

/**
 * Add new employee
 * @param {Object} data - Employee details
 */
export function addEmployee(data) {
  return request({
    url: '/employee',
    method: 'post',
    data
  })
}

/**
 * Get employee list with pagination
 * @param {Object} params - Query parameters (page, pageSize, name, etc.)
 */
export function getEmployeeList(params) {
  return request({
    url: '/employee/page',
    method: 'get',
    params
  })
}

/**
 * Enable or Disable employee account
 * @param {Number} id - Employee ID
 * @param {Number} status - 1 for enable, 0 for disable
 */
export function updateEmployeeStatus(id, status) {
  return request({
    url: `/employee/status/${status}`,
    method: 'post',
    params: { id }
  })
}

/**
 * Get employee details by ID
 * @param {Number} id - Employee ID
 */
export function getEmployeeById(id) {
  return request({
    url: `/employee/${id}`,
    method: 'get'
  })
}

/**
 * Update employee information
 * @param {Object} data - Updated employee details
 */
export function updateEmployee(data) {
  return request({
    url: '/employee',
    method: 'put',
    data
  })
}

export function deleteEmployee(id) {
  return request({
    url: `/employee/${id}`,
    method: 'delete'
  })
}