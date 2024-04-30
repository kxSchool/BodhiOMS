import {request} from "./request";
//登录
function loginApi(name,pwd){
    return request({
        method:"post",
        url:"login",
        data:{
            username:name,
            password:pwd
        }
    })
}
//一级菜单
function menusApi(){
   return request({
    method:"get",
    url:"menus",
   })
}
//用户
function userInfoApi(data){
    return request({
        method:"get",
        url:"users",
        params:data
    })
}


export default{
    loginApi,
    menusApi,
    userInfoApi
}