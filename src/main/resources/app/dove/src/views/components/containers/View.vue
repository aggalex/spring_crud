<template>
  <div id="scrolled-window" class="scrolled">
    <Clamp>
      <slot/>
    </Clamp>
  </div>
</template>

<script setup lang="ts">
import Clamp from "./Clamp.vue";
import {onActivated, onDeactivated, ref} from "vue";

interface Props {
  keepScroll?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  keepScroll: false
})

if (props.keepScroll) {
  const scroll = ref(0)

  onActivated(() => document
      .getElementById("scrolled-window")
      ?.scrollTo(0, scroll.value))

  onDeactivated(() => scroll.value = document
      .getElementById("scrolled-window")
      ?.scrollTop || 0)
}
</script>

<style scoped>
.scrolled {
  overflow-y: scroll;
  max-height: 100vh;
}
</style>