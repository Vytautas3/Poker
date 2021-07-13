<template>
  <div>
    <poker></poker>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import Poker from './components/Poker.vue'

@Component({
  components: { Poker }
})
export default class App extends Vue {}
</script>

<style>

</style>
