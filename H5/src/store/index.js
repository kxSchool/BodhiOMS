
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
let accoutlist = ['token'];
// let accout = ['token'];
const store = new Vuex.Store({
    state: {
        Collection: 0,
        number: 0,
        // 数据请求token
        token: 0,
        id: 0,
    },
    mutations: {
        // 初始化数据
        initdata(state) {
            for (let name of accoutlist) {
                state[name] = localStorage.getItem(name);
            }
        },
        increment(state, tend) {
            state.Collection = tend
        },
        contact(state, number) {
            state.number = number
        },
        // 把token赋值到vuex,保存一下看看打印的是什么
        assignment(state, token) {
            state.token = token;
            localStorage.setItem('token', token)
        },
    }
})

export default store
