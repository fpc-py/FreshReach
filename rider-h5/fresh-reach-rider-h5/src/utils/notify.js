import { createVNode, render } from 'vue'
import Toast from '@/components/Toast.vue'
import ConfirmDialog from '@/components/ConfirmDialog.vue'

let toastInstance = null
let toastContainer = null
let toastTimer = null

const createToastContainer = () => {
  if (!toastContainer) {
    toastContainer = document.createElement('div')
    document.body.appendChild(toastContainer)
  }
  return toastContainer
}

const clearToast = () => {
  if (toastTimer) {
    clearTimeout(toastTimer)
    toastTimer = null
  }
  if (toastContainer && toastInstance) {
    render(null, toastContainer)
    toastInstance = null
  }
}

export const toast = {
  show(message, options = {}) {
    const container = createToastContainer()
    const duration = options.duration ?? 2000
    
    clearToast()
    
    const vnode = createVNode(Toast, {
      visible: true,
      message,
      type: options.type || 'info',
      position: options.position || 'center',
      duration: 0
    })
    
    render(vnode, container)
    toastInstance = vnode
    
    if (duration > 0) {
      toastTimer = setTimeout(() => {
        clearToast()
      }, duration)
    }
  },
  
  success(message, options = {}) {
    this.show(message, { ...options, type: 'success' })
  },
  
  error(message, options = {}) {
    this.show(message, { ...options, type: 'error' })
  },
  
  warning(message, options = {}) {
    this.show(message, { ...options, type: 'warning' })
  },
  
  info(message, options = {}) {
    this.show(message, { ...options, type: 'info' })
  }
}

let dialogInstance = null
let dialogContainer = null

const createDialogContainer = () => {
  if (!dialogContainer) {
    dialogContainer = document.createElement('div')
    document.body.appendChild(dialogContainer)
  }
  return dialogContainer
}

export const confirm = (options) => {
  return new Promise((resolve) => {
    const container = createDialogContainer()
    
    if (dialogInstance) {
      render(null, container)
    }
    
    const handleConfirm = () => {
      render(null, container)
      dialogInstance = null
      resolve(true)
    }
    
    const handleCancel = () => {
      render(null, container)
      dialogInstance = null
      resolve(false)
    }
    
    const vnode = createVNode(ConfirmDialog, {
      visible: true,
      title: options.title || '提示',
      message: options.message || '',
      showCancel: options.showCancel !== false,
      cancelText: options.cancelText || '取消',
      confirmText: options.confirmText || '确定',
      maskClosable: options.maskClosable || false,
      onConfirm: handleConfirm,
      onCancel: handleCancel,
      'onUpdate:visible': (val) => {
        if (!val) {
          handleCancel()
        }
      }
    })
    
    render(vnode, container)
    dialogInstance = vnode
  })
}

export default {
  install(app) {
    app.config.globalProperties.$toast = toast
    app.config.globalProperties.$confirm = confirm
  }
}
