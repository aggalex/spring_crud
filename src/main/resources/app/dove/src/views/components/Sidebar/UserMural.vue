<template>
  <div class="images">

    <button class="edit-button fab-style centered-icon">
      <Icon>add_a_photo</Icon>
    </button>
    <div class="user-backdrop">
      <div class="overlay"></div>
    </div>
    <div class="user-box">
      <Overlay v-if="info !== null" class="icon-section">
        <img class="overlay">
        <button class="centered-icon overlay top">
          <Icon class="image-icon">add_a_photo</Icon>
        </button>
      </Overlay>
      <button v-if="info !== null" class="user-info align-left">
        <b>{{ info?.username }}</b>
        <br>
        {{ info?.email }}
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import Icon from "@/views/components/Icon.vue";
import Overlay from "@/views/components/containers/Overlay.vue";
import {useUserStore} from "@/store/userStore";
import {computed} from "vue";
import type {UserInfoDto} from "@/api/user/dto/UserInfoDto";

let userStore = useUserStore();

let info = computed(() => userStore.info as UserInfoDto | null)

</script>

<style scoped lang="scss">
@import "../../../assets/scss/constants";

$icon-size: 5rem;

.images {
  position: relative;
  height: 12rem;

  .edit-button {
    position: absolute;
    top: 0;
    right: 0;
    z-index: 1;
  }

  .menu-button {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 300;
  }

  .user-backdrop {
    background: $accent;
    position: absolute;
    top: 0;
    height: 100%;
    width: 100%;
    margin-bottom: 1rem;

    .overlay {
      position: relative;
      top: 70%;
      height: 30%;
      background: linear-gradient(to top,
          transparentize($background-special, 0) 30%,
          transparentize($background-special, 1)
      );
    }
  }

  .user-box {
    position: absolute;
    bottom: 1rem;
    width: 100%;
    height: $icon-size;
    display: flex;
    flex-direction: row;
    justify-content: space-evenly;
    max-height: $icon-size;
    align-items: flex-end;

    .icon-section {
      height: $icon-size;
      width: $icon-size;

      & > * {
        height: $icon-size;
        width: $icon-size;
        border-radius: 100%;
        border: 6px $background-special solid;
        padding: 0;
      }

      .top {
        z-index: 350;
      }

      button {
        opacity: 0;

        &:hover {
          opacity: 100%;
        }
      }

      img {
        background: $accent;
      }
    }

    .user-info {
      overflow: hidden;
      white-space: nowrap;
      display: block;
      text-overflow: ellipsis;
      width: 30%;
    }
  }
}
</style>