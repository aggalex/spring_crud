<template>
  <li @click="emits('click')">
    <div class="icon"></div>
    <span><slot/></span>
  </li>
</template>

<script setup lang="ts">
const emits = defineEmits(["click"])

const props = defineProps({
  icon: String
});
</script>

<style scoped lang="scss">
@import "../../../assets/scss/constants";
li {
  padding: .5rem;
  margin: .5rem;
  border-radius: 6px;

  transition: background-color 200ms ease-out;

  &:hover {
    background: $foreground;
    cursor: pointer;
  }

  &:active {
    background: $accent;
  }

  &:not(:first-child) {
    margin-top: 1rem;
  }
}
</style>