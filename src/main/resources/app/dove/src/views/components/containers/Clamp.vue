<template>
  <div class="clamp">
    <div></div>
    <div class="clamp-contents"><slot/></div>
    <div></div>
  </div>
</template>

<script>
export default {
  name: "Clamp"
}
</script>

<style scoped lang="scss">
.clamp {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: stretch;
  width: 100%;

  .clamp-contents {
    @media (min-width: 50rem) {
      width: min(44rem, 70vw);
    }

    @media (max-width: 50rem) {
      width: 100%;
    }
  }
}
</style>