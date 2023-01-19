<template>
  <View>
    <div class="about">
      <h1>This is an about page</h1>
    </div>
  </View>
</template>

<style>
@media (min-width: 64rem) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
<script setup lang="ts">
import View from "@/views/components/containers/View.vue";
</script>