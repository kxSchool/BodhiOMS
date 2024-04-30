import request from '@/utils/request';
// 获取商家列表
export const getUserList = (params) => {
    return request({
        data: params,
        url: '/system/getUserList',
        method: 'POST'
    })
}

// 编辑商家接口
export const editUserList = (params) => {
    return request({
        data: params,
        url: '/system/editUserList',
        method: 'POST'
    })
}


//查看是否能升级为商家
export const checkUserRole = (params) => {
    return request({
        data: params,
        url: '/system/checkUserRole',
        method: 'POST'
    })
}
//升级为商家
export const upgradeUser = (params) => {
    return request({
        data: params,
        url: '/system/upgradeUser',
        method: 'POST'
    })
}