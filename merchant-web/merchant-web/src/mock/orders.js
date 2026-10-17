// // Mock 订单数据
// export const mockOrders = [
//   {
//     id: 'XD202401150001',
//     status: 'pending',
//     createTime: '2024-01-15 10:23:45',
//     finishTime: null,
//     totalAmount: 68.5,
//     deliveryFee: 3,
//     packagingFee: 2,
//     discountAmount: 5,
//     payAmount: 65.5,
//     address: {
//       name: '张先生',
//       phone: '138****5678',
//       province: '广东省',
//       city: '深圳市',
//       district: '南山区',
//       detail: '科技园南区A栋1203室'
//     },
//     riderName: null,
//     riderPhone: null,
//     remark: '少放辣，餐具多拿两份',
//     items: [
//       { name: '招牌酸菜鱼', price: 38, quantity: 1, specs: '微辣' },
//       { name: '米饭', price: 3, quantity: 2 },
//       { name: '柠檬水', price: 8, quantity: 1 },
//       { name: '凉拌黄瓜', price: 12, quantity: 1 }
//     ]
//   },
//   {
//     id: 'XD202401150002',
//     status: 'preparing',
//     createTime: '2024-01-15 10:35:12',
//     finishTime: null,
//     totalAmount: 45,
//     deliveryFee: 3,
//     packagingFee: 1,
//     discountAmount: 0,
//     payAmount: 49,
//     address: {
//       name: '李女士',
//       phone: '139****8765',
//       province: '广东省',
//       city: '深圳市',
//       district: '福田区',
//       detail: '中心商务大厦B座808'
//     },
//     riderName: null,
//     riderPhone: null,
//     remark: '',
//     items: [
//       { name: '宫保鸡丁盖饭', price: 28, quantity: 1 },
//       { name: '皮蛋瘦肉粥', price: 15, quantity: 1 }
//     ]
//   },
//   {
//     id: 'XD202401150003',
//     status: 'delivering',
//     createTime: '2024-01-15 11:02:33',
//     finishTime: null,
//     totalAmount: 89,
//     deliveryFee: 0,
//     packagingFee: 3,
//     discountAmount: 10,
//     payAmount: 82,
//     address: {
//       name: '王先生',
//       phone: '137****2345',
//       province: '广东省',
//       city: '深圳市',
//       district: '罗湖区',
//       detail: '东门步行街168号'
//     },
//     riderName: '刘骑手',
//     riderPhone: '158****1111',
//     remark: '请尽快送达，公司等着开会',
//     items: [
//       { name: '麻辣香锅(大份)', price: 58, quantity: 1, specs: '特辣' },
//       { name: '米饭', price: 3, quantity: 3 },
//       { name: '可乐', price: 6, quantity: 2 }
//     ]
//   },
//   {
//     id: 'XD202401150004',
//     status: 'completed',
//     createTime: '2024-01-15 09:15:22',
//     finishTime: '2024-01-15 09:48:56',
//     totalAmount: 56,
//     deliveryFee: 3,
//     packagingFee: 2,
//     discountAmount: 5,
//     payAmount: 56,
//     address: {
//       name: '赵女士',
//       phone: '136****9876',
//       province: '广东省',
//       city: '深圳市',
//       district: '宝安区',
//       detail: '宝安中心地铁站A出口'
//     },
//     riderName: '陈骑手',
//     riderPhone: '159****2222',
//     remark: '',
//     items: [
//       { name: '黄焖鸡米饭', price: 25, quantity: 2 },
//       { name: '卤蛋', price: 3, quantity: 2 }
//     ]
//   },
//   {
//     id: 'XD202401150005',
//     status: 'cancelled',
//     createTime: '2024-01-15 08:45:10',
//     finishTime: null,
//     totalAmount: 32,
//     deliveryFee: 3,
//     packagingFee: 1,
//     discountAmount: 0,
//     payAmount: 36,
//     address: {
//       name: '陈先生',
//       phone: '135****3456',
//       province: '广东省',
//       city: '深圳市',
//       district: '龙岗区',
//       detail: '坂田街道华为基地'
//     },
//     riderName: null,
//     riderPhone: null,
//     remark: '太远了，等不及了',
//     items: [
//       { name: '桂林米粉', price: 18, quantity: 1 },
//       { name: '油条', price: 5, quantity: 2 }
//     ],
//     cancelReason: '商家距离太远'
//   },
//   {
//     id: 'XD202401150006',
//     status: 'refunding',
//     createTime: '2024-01-14 20:30:00',
//     finishTime: null,
//     totalAmount: 128,
//     deliveryFee: 0,
//     packagingFee: 5,
//     discountAmount: 20,
//     payAmount: 113,
//     address: {
//       name: '周女士',
//       phone: '133****7654',
//       province: '广东省',
//       city: '深圳市',
//       district: '龙华区',
//       detail: '壹方天地A区3号楼1501'
//     },
//     riderName: '张骑手',
//     riderPhone: '157****3333',
//     remark: '',
//     items: [
//       { name: '烤鱼套餐', price: 98, quantity: 1, specs: '香辣味' },
//       { name: '烤生蚝', price: 15, quantity: 2 }
//     ],
//     refundReason: '菜品少了一样，烤生蚝没送到'
//   },
//   {
//     id: 'XD202401140007',
//     status: 'completed',
//     createTime: '2024-01-14 18:22:15',
//     finishTime: '2024-01-14 19:05:30',
//     totalAmount: 75,
//     deliveryFee: 3,
//     packagingFee: 2,
//     discountAmount: 8,
//     payAmount: 72,
//     address: {
//       name: '吴先生',
//       phone: '132****4567',
//       province: '广东省',
//       city: '深圳市',
//       district: '南山区',
//       detail: '深圳大学粤海门宿舍'
//     },
//     riderName: '李骑手',
//     riderPhone: '156****4444',
//     remark: '期末考试加油，多给点酱料',
//     items: [
//       { name: '红烧肉盖饭', price: 32, quantity: 1 },
//       { name: '酸辣土豆丝', price: 16, quantity: 1 },
//       { name: '紫菜蛋花汤', price: 12, quantity: 1 }
//     ]
//   },
//   {
//     id: 'XD202401140008',
//     status: 'pending',
//     createTime: '2024-01-14 12:10:05',
//     finishTime: null,
//     totalAmount: 42,
//     deliveryFee: 3,
//     packagingFee: 1,
//     discountAmount: 0,
//     payAmount: 46,
//     address: {
//       name: '孙女士',
//       phone: '131****6789',
//       province: '广东省',
//       city: '深圳市',
//       district: '光明区',
//       detail: '凤凰城地铁站商业街'
//     },
//     riderName: null,
//     riderPhone: null,
//     remark: '不要香菜',
//     items: [
//       { name: '牛肉面', price: 22, quantity: 1, specs: '加牛肉' },
//       { name: '小笼包', price: 12, quantity: 1 }
//     ]
//   }
// ]

// // Mock 统计数据
// export const mockStats = {
//   today: {
//     pendingCount: 3,
//     totalAmount: 1268.5,
//     orderCount: 28,
//     productOnSaleCount: 45
//   },
//   week: {
//     totalAmount: 8650.0,
//     orderCount: 186,
//     avgAmount: 46.5
//   },
//   month: {
//     totalAmount: 32800.0,
//     orderCount: 705,
//     avgAmount: 46.5
//   }
// }

// // 模拟 API 延迟
// const delay = (ms = 300) => new Promise(resolve => setTimeout(resolve, ms))

// // Mock API 实现
// export const mockApi = {
//   // 登录
//   login: async ({ phone, password }) => {
//     await delay()
//     if (phone === '13800138000' && password === '123456') {
//       return {
//         code: 200,
//         data: {
//           token: 'mock_token_' + Date.now(),
//           merchantInfo: {
//             id: 1,
//             name: '鲜达美食坊',
//             phone: '13800138000',
//             avatar: ''
//           }
//         }
//       }
//     }
//     return { code: 401, message: '手机号或密码错误' }
//   },

//   // 获取订单列表
//   getOrderList: async (params = {}) => {
//     await delay()
//     let list = [...mockOrders]
    
//     // 按状态筛选
//     if (params.status && params.status !== 'all') {
//       list = list.filter(order => order.status === params.status)
//     }
    
//     // 按订单号搜索
//     if (params.id) {
//       list = list.filter(order => order.id.includes(params.id))
//     }
    
//     // 按日期范围筛选
//     if (params.startDate) {
//       list = list.filter(order => order.createTime >= params.startDate)
//     }
//     if (params.endDate) {
//       list = list.filter(order => order.createTime <= params.endDate)
//     }
    
//     return {
//       code: 200,
//       data: {
//         list,
//         total: list.length
//       }
//     }
//   },

//   // 获取订单详情
//   getOrderDetail: async (id) => {
//     await delay()
//     const order = mockOrders.find(o => o.id === id)
//     if (order) {
//       return { code: 200, data: order }
//     }
//     return { code: 404, message: '订单不存在' }
//   },

//   // 接单
//   acceptOrder: async (id) => {
//     await delay()
//     const order = mockOrders.find(o => o.id === id)
//     if (order && order.status === 'pending') {
//       order.status = 'preparing'
//       return { code: 200, message: '接单成功' }
//     }
//     return { code: 400, message: '操作失败' }
//   },

//   // 拒单
//   rejectOrder: async (id, reason) => {
//     await delay()
//     const order = mockOrders.find(o => o.id === id)
//     if (order && order.status === 'pending') {
//       order.status = 'cancelled'
//       order.cancelReason = reason
//       return { code: 200, message: '已拒单' }
//     }
//     return { code: 400, message: '操作失败' }
//   },

//   // 完成备餐
//   finishPreparing: async (id) => {
//     await delay()
//     const order = mockOrders.find(o => o.id === id)
//     if (order && order.status === 'preparing') {
//       order.status = 'delivering'
//       return { code: 200, message: '已通知骑手取餐' }
//     }
//     return { code: 400, message: '操作失败' }
//   },

//   // 同意退款
//   agreeRefund: async (id) => {
//     await delay()
//     const order = mockOrders.find(o => o.id === id)
//     if (order && order.status === 'refunding') {
//       order.status = 'refunded'
//       return { code: 200, message: '已同意退款' }
//     }
//     return { code: 400, message: '操作失败' }
//   },

//   // 拒绝退款
//   rejectRefund: async (id, reason) => {
//     await delay()
//     const order = mockOrders.find(o => o.id === id)
//     if (order && order.status === 'refunding') {
//       order.status = 'rejected'
//       order.rejectReason = reason
//       return { code: 200, message: '已拒绝退款' }
//     }
//     return { code: 400, message: '操作失败' }
//   },

//   // 获取今日统计
//   getTodayStats: async () => {
//     await delay()
//     return { code: 200, data: mockStats.today }
//   },

//   // 获取日期范围统计
//   getStatsByDateRange: async (startDate, endDate) => {
//     await delay()
//     return { code: 200, data: mockStats.week }
//   },

//   // 获取店铺信息
//   getShopInfo: async () => {
//     await delay()
//     return {
//       code: 200,
//       data: {
//         id: 1,
//         name: '鲜达美食坊',
//         phone: '13800138000',
//         address: '广东省深圳市南山区科技园',
//         isOpen: true,
//         openTime: '09:00',
//         closeTime: '22:00',
//         deliveryFee: 3,
//         minAmount: 20,
//         notice: '欢迎光临，新客立减5元！'
//       }
//     }
//   },

//   // 更新店铺信息
//   updateShopInfo: async (data) => {
//     await delay()
//     return { code: 200, message: '更新成功' }
//   },

//   // 更新营业状态
//   updateShopStatus: async (isOpen) => {
//     await delay()
//     return { code: 200, message: isOpen ? '已营业' : '已休息' }
//   }
// }
