<template>
<div class="container-overlay">
  <slot/>
</div>
</template>

<script>
export default {
  name: "Overlay"
}
</script>

<style lang="scss">
.container-overlay {
  position: relative;

  .overlay {
    position: absolute;
    top: 0;
    left: 0;
  }
}
</style>