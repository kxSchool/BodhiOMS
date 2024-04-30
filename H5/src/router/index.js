import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import User from '../components/Usercollection.vue'
import Number from '../components/number.vue'
import tranpass from '../components/transactionPassword.vue'
import Recharge from '../components/Rechargeamount.vue'
import Myrech from '../components/Myrecharge.vue'
import Paid from '../components/Paid.vue'
import Succeed from '../components/succeeded.vue'
import Salecomp from '../components/Salecompleted.vue'
import Cancell from '../components/cancellation.vue'
import Transac from '../components/transaction.vue'
import Please from '../components/PleasePay.vue'
import Buyerss from '../components/BuyerssellPleasePay.vue'
import Merchan from '../components/MerchanHome.vue'
import SaroUni from '../components/MerchantSaroUnitPrice.vue'
import Teleph from '../components/MerchantTelephone.vue'
import Bank from '../components/MerchantAddBankCard.vue'
import MerH5 from '../components/MerchantH5.vue'
import COllect from '../components/UserRechargeCollection.vue'
import Paymen from '../components/Paymentbyseller.vue'
import Rechar from '../components/RechargeOrderCancellation.vue'
import Sellcan from '../components/Sellcancel.vue'
import UserRech from '../components/UserRechargeRelease.vue'
import Complete from '../components/UserRechargeComplete.vue'
import Closur from '../components/TransactionClosure.vue'
import Voucher from '../components/voucher.vue'
import lson from '../components/IsnoTset.vue'
import cellhone from '../components/cellhoneverificationcode.vue'
import userSellList from '../components/userSellList.vue'
import BuyesList from '../components/BuyersellList.vue'
import Buyersell from '../components/Buyersell.vue'
import BUylesan from '../components/Buyeralesancellation.vue'
import Buyerorder from '../components/Buyerorderomplet.vue'
import Error from '../components/AccounTexception.vue'
import SalesC from '../components/SalesClosed.vue'
import Merchantsad from '../components/Merchantsalesclosed.vue'
import Accou from '../components/AccountsDisabled.vue'
Vue.use(VueRouter)

const routes = [
  // path路由路径 comp组件名字
  {
    path: '/',
    redirect: '/Home'
  },
  {
    path: '/Home', component: Home, name: 'Describe',
  },
  // 账户异常提示
  {
    path: '/Error', component: Error, name: 'Error',
  },
  // 跳转到用户绑定银行卡页面
  { path: '/User', component: User, },
  // 跳转到用户绑定手机号页面
  { path: '/Number', component: Number, },
  // 跳转到充值金额页面
  { path: '/Recharge', component: Recharge, name: 'Recharge', },
  // 跳转到出售金额页面
  { path: '/Buyersell', component: Buyersell, name: 'Buyersell', },
  // 跳转到设置资金密码页面
  { path: '/tranpass', component: tranpass, name: 'tranpass', },
  // 跳转到未绑定资金密码页面
  { path: '/lson', component: lson, name: 'lson', },
  // 跳转到忘记资金密码页面
  { path: '/cellhone', component: cellhone, name: 'cellhone', },
  // 跳转到用户充值详情页面
  {
    path: '/Myrech', component: Myrech, name: 'Myrech',
  },
  // 跳转到商家账户被禁用页面
  {
    path: '/Accou', component: Accou, name: 'Accou',
  },
  // // 跳转到用户确认收款页面
  // { path: '/Confirm', component: Confirm, },
  // 跳转到用户卖出页面
  { path: '/BuyesList', component: BuyesList, name: 'BuyesList', },
  // 跳转到用户卖出待付款页面
  { path: '/Buyerss', component: Buyerss, name: 'Buyerss', },
  // 跳转到用户卖出订单取消页面
  { path: '/BUylesan', component: BUylesan, name: 'BUylesan', },
  // 跳转到用户卖出订单关闭页面
  { path: '/SalesC', component: SalesC, name: 'SalesC', },
  // 跳转到用户卖出订单完成页面
  { path: '/Buyerorder', component: Buyerorder, name: 'Buyerorder', },
  // 跳转到用户请付款页面
  { path: '/Please', component: Please, name: 'Please', },
  // 跳转到用户已付款页面
  { path: '/Paid', component: Paid, name: 'Paid', },
  // 跳转到用户充值成功页面
  { path: '/Succeed', component: Succeed, name: 'Succeed', },
  // 跳转到用户取消充值页面
  { path: '/Cancell', component: Cancell, name: 'Cancell', },
  // 跳转到用户交易关闭页面
  { path: '/Transac', component: Transac, name: 'Transac', },
  // 跳转到商家H5首页页面
  { path: '/Merchan', component: Merchan, name: 'Merchan', },
  // 跳转到商家设置SARO单价页面
  { path: '/SaroUni', component: SaroUni, name: 'SaroUni' },
  // 跳转到商家设置联系电话页面
  { path: '/Teleph', component: Teleph, },
  // 跳转到商家设置收款银行卡页面
  { path: '/Bank', component: Bank, },
  // 跳转到商家用户充值页面
  { path: '/MerH5', component: MerH5, name: 'MerH5' },
  // 跳转到商家用户充值待收款页面
  { path: '/COllect', component: COllect, name: 'COllect', },
  // 跳转到商家用户订单取消页面
  { path: '/Rechar', component: Rechar, name: 'Rechar', },
  // 跳转到商家查看凭证页面
  { path: '/Voucher', component: Voucher, name: 'Voucher', },
  // 跳转到商家用户待放行页面
  { path: '/UserRech', component: UserRech, name: 'UserRech', },
  // 跳转到商家用户充值完成页面
  { path: '/Complete', component: Complete, name: 'Complete', },
  // 跳转到商家用户订单关闭页面
  { path: '/Closur', component: Closur, name: 'Closur', },
  // 跳转到商户卖出列表页面
  { path: '/userSellList', component: userSellList, name: 'userSellList' },
  // 跳转到商家卖出订单代付款页面
  { path: '/Paymen', component: Paymen, name: 'Paymen', },
  // 跳转到商家卖出完成页面
  { path: '/Salecomp', component: Salecomp, name: 'Salecomp', },
  // 跳转到商家卖出取消页面
  { path: '/Sellcan', component: Sellcan, name: 'Sellcan', },
  // 跳转到商家卖出关闭页面
  { path: '/Merchantsad', component: Merchantsad, name: 'Merchantsad', },
]

const router = new VueRouter({
  routes
})

export default router
