<template>
<div class="card">
  <slot/>
</div>
</template>

<script setup lang="ts">

</script>

<style scoped lang="scss">
@import "../../../assets/scss/constants";
@import "../../../assets/scss/shadowed";

.card {
  border-radius: 6px;
  margin: 12px 12px 24px;
  padding: 12px 24px;
  background-color: $background-special;
  transition-duration: 300ms !important;
  transition-timing-function: ease-in-out !important;
  transition-property:
      background-color,
      filter,
      border-bottom-color,
      border-top-color,
      border-left-color,
      border-right-color,
      height
      !important;

  &:not(.flattened) {
    @include Shadowed;
  }

  &.flattened, &.fill-view {
    background-color: transparent;
  }

  &.flattened:not(.fill-view)  {
    border: 1px $foreground solid;
  }

  &.fill-view {
    margin: 0;
    border: none;
  }
}
</style>