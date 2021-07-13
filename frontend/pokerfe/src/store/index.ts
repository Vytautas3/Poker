import Vue from 'vue'
import Vuex from 'vuex'
import {Card, CardHand} from "@/types";

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    player1: new class implements CardHand {
      cards: Card[];
    },
    player2: new class implements CardHand {
      cards: Card[];
    }
  },
  mutations: {
    changePlayer1(state: any, cardHand: CardHand) {
      state.player1 = cardHand;
    },

    changePlayer2(state: any, cardHand: CardHand) {
      state.player2 = cardHand;
    }
  },
  actions: {},
  modules: {}
})
