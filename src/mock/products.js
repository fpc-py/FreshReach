// Mock 商品数据
export const mockCategories = [
  { id: 1, name: '招牌推荐', sort: 1 },
  { id: 2, name: '热卖套餐', sort: 2 },
  { id: 3, name: '主食米饭', sort: 3 },
  { id: 4, name: '风味小吃', sort: 4 },
  { id: 5, name: '饮品汤粥', sort: 5 }
]

export const mockProducts = [
  {
    id: 1,
    name: '招牌酸菜鱼',
    categoryId: 1,
    categoryName: '招牌推荐',
    price: 38,
    originalPrice: 48,
    stock: 99,
    isOnSale: true,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg',
    description: '精选鲜活黑鱼，配以四川老坛酸菜，酸辣开胃',
    specs: [
      { name: '辣度', options: ['微辣', '中辣', '特辣'] }
    ]
  },
  {
    id: 2,
    name: '宫保鸡丁盖饭',
    categoryId: 3,
    categoryName: '主食米饭',
    price: 28,
    originalPrice: 32,
    stock: 50,
    isOnSale: true,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg',
    description: '经典川味，鸡丁香嫩，花生酥脆',
    specs: []
  },
  {
    id: 3,
    name: '麻辣香锅(大份)',
    categoryId: 2,
    categoryName: '热卖套餐',
    price: 58,
    originalPrice: 68,
    stock: 30,
    isOnSale: true,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg',
    description: '多种食材随心搭配，麻辣鲜香',
    specs: [
      { name: '辣度', options: ['微辣', '中辣', '特辣'] }
    ]
  },
  {
    id: 4,
    name: '黄焖鸡米饭',
    categoryId: 3,
    categoryName: '主食米饭',
    price: 25,
    originalPrice: 28,
    stock: 45,
    isOnSale: true,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg',
    description: '酱香浓郁，鸡肉软糯，配米饭绝了',
    specs: []
  },
  {
    id: 5,
    name: '柠檬水',
    categoryId: 5,
    categoryName: '饮品汤粥',
    price: 8,
    originalPrice: 10,
    stock: 100,
    isOnSale: true,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg',
    description: '新鲜柠檬现切，清爽解腻',
    specs: []
  },
  {
    id: 6,
    name: '凉拌黄瓜',
    categoryId: 4,
    categoryName: '风味小吃',
    price: 12,
    originalPrice: 15,
    stock: 60,
    isOnSale: true,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg',
    description: '清脆爽口，夏日必备',
    specs: []
  },
  {
    id: 7,
    name: '烤鱼套餐',
    categoryId: 2,
    categoryName: '热卖套餐',
    price: 98,
    originalPrice: 118,
    stock: 15,
    isOnSale: true,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg',
    description: '新鲜烤鱼，配多种配菜',
    specs: [
      { name: '口味', options: ['香辣味', '蒜香味', '酱香味'] }
    ]
  },
  {
    id: 8,
    name: '桂林米粉',
    categoryId: 3,
    categoryName: '主食米饭',
    price: 18,
    originalPrice: 22,
    stock: 0,
    isOnSale: false,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg',
    description: '正宗桂林风味，米粉爽滑',
    specs: []
  },
  {
    id: 9,
    name: '小笼包',
    categoryId: 4,
    categoryName: '风味小吃',
    price: 12,
    originalPrice: 15,
    stock: 40,
    isOnSale: true,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg',
    description: '皮薄馅大，汤汁鲜美',
    specs: []
  },
  {
    id: 10,
    name: '皮蛋瘦肉粥',
    categoryId: 5,
    categoryName: '饮品汤粥',
    price: 15,
    originalPrice: 18,
    stock: 35,
    isOnSale: true,
    image: 'https://img.yzcdn.cn/vant/cat.jpeg',
    description: '绵密粥底，配料丰富',
    specs: []
  }
]

// 模拟 API 延迟
const delay = (ms = 300) => new Promise(resolve => setTimeout(resolve, ms))

// Mock API 实现
export const mockProductApi = {
  // 获取分类列表
  getCategories: async () => {
    await delay()
    return { code: 200, data: mockCategories }
  },

  // 新增分类
  addCategory: async (data) => {
    await delay()
    const newCategory = {
      id: Math.max(...mockCategories.map(c => c.id)) + 1,
      ...data,
      sort: mockCategories.length + 1
    }
    mockCategories.push(newCategory)
    return { code: 200, data: newCategory }
  },

  // 更新分类
  updateCategory: async (id, data) => {
    await delay()
    const index = mockCategories.findIndex(c => c.id === id)
    if (index !== -1) {
      mockCategories[index] = { ...mockCategories[index], ...data }
      return { code: 200, message: '更新成功' }
    }
    return { code: 404, message: '分类不存在' }
  },

  // 删除分类
  deleteCategory: async (id) => {
    await delay()
    const index = mockCategories.findIndex(c => c.id === id)
    if (index !== -1) {
      mockCategories.splice(index, 1)
      return { code: 200, message: '删除成功' }
    }
    return { code: 404, message: '分类不存在' }
  },

  // 获取商品列表
  getProductList: async (params = {}) => {
    await delay()
    let list = [...mockProducts]
    
    // 按分类筛选
    if (params.categoryId) {
      list = list.filter(p => p.categoryId === params.categoryId)
    }
    
    // 按名称搜索
    if (params.keyword) {
      list = list.filter(p => p.name.includes(params.keyword))
    }
    
    // 按上下架状态筛选
    if (params.isOnSale !== undefined && params.isOnSale !== '') {
      list = list.filter(p => p.isOnSale === params.isOnSale)
    }
    
    return {
      code: 200,
      data: {
        list,
        total: list.length
      }
    }
  },

  // 获取商品详情
  getProductDetail: async (id) => {
    await delay()
    const product = mockProducts.find(p => p.id === id)
    if (product) {
      return { code: 200, data: product }
    }
    return { code: 404, message: '商品不存在' }
  },

  // 新增商品
  addProduct: async (data) => {
    await delay()
    const category = mockCategories.find(c => c.id === data.categoryId)
    const newProduct = {
      id: Math.max(...mockProducts.map(p => p.id)) + 1,
      ...data,
      categoryName: category ? category.name : '',
      stock: data.stock || 0,
      isOnSale: data.isOnSale !== false
    }
    mockProducts.push(newProduct)
    return { code: 200, data: newProduct }
  },

  // 更新商品
  updateProduct: async (id, data) => {
    await delay()
    const index = mockProducts.findIndex(p => p.id === id)
    if (index !== -1) {
      const category = mockCategories.find(c => c.id === data.categoryId)
      mockProducts[index] = {
        ...mockProducts[index],
        ...data,
        categoryName: category ? category.name : mockProducts[index].categoryName
      }
      return { code: 200, message: '更新成功' }
    }
    return { code: 404, message: '商品不存在' }
  },

  // 删除商品
  deleteProduct: async (id) => {
    await delay()
    const index = mockProducts.findIndex(p => p.id === id)
    if (index !== -1) {
      mockProducts.splice(index, 1)
      return { code: 200, message: '删除成功' }
    }
    return { code: 404, message: '商品不存在' }
  },

  // 切换商品上下架状态
  toggleProductStatus: async (id, isOnSale) => {
    await delay()
    const product = mockProducts.find(p => p.id === id)
    if (product) {
      product.isOnSale = isOnSale
      return { code: 200, message: isOnSale ? '已上架' : '已下架' }
    }
    return { code: 404, message: '商品不存在' }
  }
}
