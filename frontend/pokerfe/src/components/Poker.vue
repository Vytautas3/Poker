<template>
  <div class="d-flex justify-content-between">
    <player img-name="boy-avatar.png" name="Player1" :player="this.$store.state.player1"></player>
    <div class="d-flex flex-column align-self-center">
      <b-button @click="dealHands" variant="success">Start</b-button>
      <b-button v-if="this.$store.state.player1.cards" @click="result" variant="danger" class="mt-2">Results</b-button>
    </div>
    <player img-name="man-avatar.png" name="Player2" :player="this.$store.state.player2"></player>
    <b-modal
      id="results"
      title="Results"
      header-bg-variant="success"
      header-text-variant="white"
      header-class="justify-content-center"
      body-class="text-center"
      footer-class="justify-content-center"
      ok-variant="success"
      hide-header-close
      no-close-on-backdrop
      ok-only
      ok-title="Play Again"
      @ok="playAgain()"
    >
      {{ resultText }}
    </b-modal>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import {Component} from 'vue-property-decorator'
import PokerService from '../api/api'
import {Card} from '@/types'
import PlayingCard from "@/components/PlayingCard.vue";
import Player from "@/components/Player.vue";

@Component({
  components: {PlayingCard, Player}
})
export default class Poker extends Vue {
  suits = ['hearts', 'diamonds', 'clubs', 'spades']
  ranks = ['2', '3', '4', '5', '6', '7', '8', '9', '10', 'J', 'Q', 'K', 'A']

  deck: Card[]

  resultText: string = ''

  dealHands(): void {
    this.$store.commit('changePlayer1', {cards: this.getHand(5)})
    this.$store.commit('changePlayer2', {cards: this.getHand(5)})
  }

  playAgain(): void {
    this.$store.commit('changePlayer1', {})
    this.$store.commit('changePlayer2', {})
  }

  result(): void {
    const data = []
    data.push(this.$store.state.player1)
    data.push(this.$store.state.player2)
    PokerService.results(data).then(data => {
      this.resultText = data.data
      this.$bvModal.show('results')
    })
  }

  getHand(n: number): Card[] {
    const result: Card[] = [{
      suit: '',
      rank: ''
    }]
    let len = this.deck.length
    const taken = new Array(len)
    if (n > len) {
      throw new RangeError('getRandom: more elements taken than available')
    }
    while (n--) {
      const x = Math.floor(Math.random() * len)
      result[n] = this.deck[x in taken ? taken[x] : x]
      taken[x] = --len in taken ? taken[len] : len
    }
    return result
  }

  mounted(): void {
    this.createDeck()
  }

  createDeck(): void {
    this.deck = [{
      suit: '',
      rank: ''
    }]
    for (let i = 0; i < this.suits.length; i++) {
      for (let j = 0; j < this.ranks.length; j++) {
        const card = {
          suit: '',
          rank: ''
        }
        card.suit = this.suits[i]
        card.rank = this.ranks[j]
        this.deck.push(card)
      }
    }
    this.deck.shift()
    console.log(this.deck)
  }
}

</script>

<style scoped lang="scss">

</style>
