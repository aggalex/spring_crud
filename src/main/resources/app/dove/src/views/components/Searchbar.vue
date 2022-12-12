<template>
<Card class="searchbar">
  <Icon class="hide-on-small">search</Icon>
  <input placeholder="Search">
</Card>
</template>

<script setup lang="ts">
import Card from "@/views/components/containers/Card.vue";
import Icon from "@/views/components/Icon.vue";
</script>

<style scoped lang="scss">
@import "@/assets/scss/constants.scss";
@import "@/assets/scss/main.scss";


@media (max-width: 50rem) {
  .hide-on-small {
    visibility: hidden;
  }
}

.searchbar {
  //@include MainSized;

  position: sticky;
  top: 1rem;
  background: transparentize(white, 0.9);
  backdrop-filter: blur(10px);
  z-index: 2;
  display: flex;
  align-items: center;
  gap: 1rem;
  flex-direction: row;
}

input {
  width: 100%;
}
</style>