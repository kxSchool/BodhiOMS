import request from '@/utils/request';

export const getOrderList = (params) => {
    return request({
        data: params,
        url: '/system/getOrderList',
        method: 'POST'
    })
}


export const editOrder = (params) => {
    return request({
        data: params,
        url: '/system/editOrder',
        method: 'PUT'
    })
}