<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import Sidebar from "@/views/layouts/Sidebar.vue";
import Icon from "@/views/components/Icon.vue";
import Searchbar from "@/views/components/Searchbar.vue";
import Clamp from "@/views/components/containers/Clamp.vue";
</script>

<template>
  <div class="main-box">
    <Sidebar/>
    <main>
      <div class="router-box">
        <RouterView class="router-view" v-slot="{ Component, route }">
          <Transition :name=" route.meta.transition || 'fade' " mode="out-in">
            <KeepAlive include="HomeView">
              <Component :is="Component"/>
            </KeepAlive>
          </Transition>
        </RouterView>
      </div>
    </main>
  </div>
</template>

<style lang="scss">
@import "assets/scss/constants";
@import "assets/scss/main.scss";

div.main-box {

  @media (min-width: 50rem) {
    display: flex;
    flex-direction: row;
    align-items: stretch;
    justify-content: space-between;
    margin: 0;
  }

  @media (max-width: 50rem) {
    & > * {
      position: absolute;
      top: 0;
      left: 0;
    }

    .router-box {
      width: 100%;
    }
  }

  main {
    position: relative;
    max-height: 100vh;
    width: 100%;

    .router-box {
      position: relative;

      @media (max-width: 50rem) {
        :first-child {
          margin-top: 0;
        }
      }
    }
  }
}
</style>

<style scoped lang="scss">

</style>
