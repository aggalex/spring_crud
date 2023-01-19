<template>
  <button @click="emit('click')"><slot/>
    <Transition class="transition" name="fade" mode="out-in">
      <span v-if="props.loading">
        <FlowerSpinner :size="30"/>
        <SemipolarSpinner :size="30"/>
        <RadarSpinner :size="30"/>
      </span>
    </Transition>
  </button>
</template>

<script setup lang="ts">
import {FlowerSpinner, SemipolarSpinner, RadarSpinner} from "epic-spinners";

interface Props {
  loading: Boolean
}

const props = defineProps<Props>()

const emit = defineEmits(['click'])
</script>

<style scoped lang="scss">
button {
  font-size: 1.5rem;

  & > span {
    margin-left: 1rem;
    display: flex;
    flex-direction: row;
    gap: 1rem;
  }
}
</style>