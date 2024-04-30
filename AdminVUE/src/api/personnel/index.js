import request from '@/utils/request';

// 获取订单列表数据
export const transReport = (params) => {
    return request({
        data: params,
        url: '/system/transReport',
        method: 'GET'
    })
}