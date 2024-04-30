// 商家 封装APi接口
import axios from '../main'
// Business
// 商家获取token
export function BusinessGettoKeen(query) {
    return axios({
        url: '/trans/receive/userAccessToken/' + query,
        method: 'GET',
    })
}

// 商家校验是否有交易密码
export function BusinessValidatePassword() {
    return axios({
        url: '/trans/checkHas/transPwd',
        method: 'GET',
        headers: {
            token: localStorage.getItem("Authorization"),
        }
    })
}

// 商家获取信息
export function BusinessInformation(data) {
    return axios({
        url: '/trans/merchant/index', data,
        method: 'POST',
        headers: {
            token: localStorage.getItem("Authorization"),
        }
    })
}

// 商家效验游戏端合法性
export function BusinessValidityValidity(data) {
    return axios({
        url: '/trans/checkUser', data,
        method: 'POST',
        headers: {
            token: localStorage.getItem("Authorization"),
        }
    })
}

// 效验资金密码是否正确
export function BusinessValidateFundassword(data) {
    return axios({
        data: data,
        url: '/trans/check/transPwd',
        method: 'POST',
    })
}

// 效验验证码
export function ValidationCode(data) {
    return axios({
        data: data,
        url: '/trans/check/message',
        method: 'POST',
    })
}

// 商家获取订单列表
export function OrderList(data) {
    return axios({
        data: data,
        url: '/trans/merchant/getOrderList',
        method: 'POST',
    })
}

// 商家获取订单详情
export function OrderDetails(data) {
    return axios({
        url: '/trans/merchant/getOrderInfo/' + data,
        method: 'POST',
    })
}

// 商家修改充值和卖出单价
export function ModifyUnitPrice(data) {
    return axios({
        data: data,
        url: 'trans/updateUser',
        method: 'PUT',
    })
}

// 商家设置电话
export function SetUpPhone(data) {
    return axios({
        data: data,
        url: '/trans/updateUser',
        method: 'PUT',
    })
}

// 商家关闭订单
export function CloseOrder(data) {
    return axios({
        data: data,
        url: '/trans/merchant/closeOrder',
        method: 'PUT',
    })
}

// 商家回收订单
export function RecycleOrder(data) {
    return axios({
        data: data,
        url: '/trans/merchant/operateOrder',
        method: 'PUT',
    })
}

// 商家放行订单
export function ReleaseOrder(data) {
    return axios({
        data: data,
        url: '/trans/merchant/confirmOrder',
        method: 'PUT',
    })
}

// 商家获取订单交易凭证
export function GetVoucher(data) {
    return axios({
        url: '/trans/merchant/getOrderProof/' + data,
        method: 'POST',
    })
}