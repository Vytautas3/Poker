<template>
  <div class="d-flex flex-column align-items-center">
    <b-img class="" :src="require('@/assets/' + imgName )"></b-img>
    <h3 class="text-center">{{ name }}</h3>
    <div v-if="player" class="d-flex">
      <playing-card v-for="card in player.cards" :rank="card.rank" :suit="card.suit"/>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import {Component, Prop} from 'vue-property-decorator'
import PlayingCard from "@/components/PlayingCard.vue";
import {CardHand} from "@/types";

@Component({
  components: {PlayingCard}
})
export default class Player extends Vue {
  @Prop()
  imgName: string

  @Prop()
  name: string

  @Prop()
  player: CardHand

}

</script>

<style scoped lang="scss">

</style>
