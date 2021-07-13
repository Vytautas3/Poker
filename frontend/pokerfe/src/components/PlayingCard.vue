<template>
  <div class="card" :class="red() ? 'red' : ''">
    <div class="tl-rank">{{ rank }}</div>
    <div class="tl-suit" v-html="suitHTMLCharacter()"></div>
    <div class="br-rank">{{ rank }}</div>
    <div class="br-suit" v-html="suitHTMLCharacter()"></div>
    <div class="m-suit" v-html="suitHTMLCharacter()"></div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import {Component, Prop} from 'vue-property-decorator'

@Component({
  components: {}
})
export default class PlayingCard extends Vue {
  @Prop()
  rank: string

  @Prop()
  suit: string

  red(): boolean {
    return this.suit === 'hearts' || this.suit === 'diamonds'
  }

  suitHTMLCharacter() {
    if (this.suit === 'diamonds') return '&diams;'
    return `&${this.suit.toLowerCase()}\;`
  }

}

</script>

<style scoped lang="scss">
.card {
  display: inline-block;
  margin: 10px;
  padding: 5px;
  box-shadow: 0 2px 3px 1px #bbb;
  border-radius: 5px;

  width: 90px;
  height: 120px;

}

.tl-rank {
  position: relative;
  top: 0;
  left: 0;
}

.tl-suit {
  position: relative;
  top: -5px;
  left: 0;
}

.m-suit {
  position: relative;
  top: -75px;
  left: 23px;
  font-size: 45px;
}

.br-rank {
  position: relative;
  top: 40px;
  left: 0;
  transform: rotate(180deg);
}

.br-suit {
  position: relative;
  top: 0;
  left: 0;
  transform: rotate(180deg);
}

.red {
  color: red;
}
</style>
