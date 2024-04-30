// 用户 封装 api接口
import axios from '../main'

// 用户获取token
export function GettoKeen(query) {
    return axios({
        url: '/trans/receive/userAccessToken/' + query,
        method: 'GET',
    })
}

// 用户获取商家列表
export function MerchantList(data) {
    return axios({
        url: '/trans/merchant/list', data,
        method: 'POST',
        headers: {
            token: localStorage.getItem("Authorization"),
        }
    })
}
// 用户获取卖出商家列表
export function ListofSellers() {
    return axios({
        url: '/trans/recovery/merchant/list',
        method: 'GET',
        headers: {
            token: localStorage.getItem("Authorization"),
        }
    })
}
// 用户获取电话和银行卡信息
export function ListTeLephone(data) {
    return axios({
        url: '/trans/merchant', data,
        method: 'POST',
        headers: {
            token: localStorage.getItem("Authorization"),
        }
    })
}
// 用户效验电话银行卡信息
export function ValidationTelephone(data) {
    return axios({
        url: '/trans/check/merchant', data,
        method: 'POST',
        headers: {
            token: localStorage.getItem("Authorization"),
        }
    })
}
// 用户检验游戏端用户合法性
export function ValidityValidity(data) {
    return axios({
        url: '/trans/checkUser', data,
        method: 'POST',
        headers: {
            token: localStorage.getItem("Authorization"),
        }
    })
}

// 用户获取最新商家电话或获取订单详情
export function GetMerchantPhone(data) {
    return axios({
        url: '/trans/user/getOrderInfo/ ' + data,
        method: 'POST',
    })
}

// 用户卖出检查商户单价是否有变动
export function ValidationUnitPrice(data) {
    return axios({
        data: data,
        url: '/trans/check/price',
        method: 'POST',
    })
}

// 用户充值下单
export function RechargeOrder(data) {
    return axios({
        data: data,
        url: '/trans/user/createRechargeOrder',
        method: 'POST',
    })
}

// 用户获取卖出的订单列表
export function SellOrder(data) {
    return axios({
        data: data,
        url: '/trans/user/getOrderList',
        method: 'POST',
    })
}

// 用户点击获取验证码
export function GetVerificationCode(data) {
    return axios({
        data: data,
        url: '/trans/user/message',
        method: 'POST',
    })
}

// 用户支付订单
export function PaymentOrder(data) {
    return axios({
        data: data,
        url: '/trans/user/paymentRechargeOrder',
        method: 'PUT',
    })
}

// 用户上传附件
export function UploadAttachments(data) {
    return axios({
        data: data,
        url: '/trans/upload',
        method: 'POST',
    })
}

// 用户下单获取信息
export function pickUpInformation(data) {
    return axios({
        data: data,
        url: '/trans/merchant/info',
        method: 'POST',
    })
}

// 用户设置资金密码
export function SetFundPassword(data) {
    return axios({
        data: data,
        url: '/trans/edit/transPwd',
        method: 'POST',
    })
}
