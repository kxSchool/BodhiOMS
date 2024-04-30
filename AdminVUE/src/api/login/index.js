import request from '@/utils/request';
export const login = (params) => {
    return request({
        data: params,
        url: '/system/login',
        method: 'POST'
    })
}