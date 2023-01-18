<template>
<Card class="editor no-padding">
  <input class="large no-border padded" placeholder="Title" v-model="state.title">
  <hr>
  <div class="body">
    <div class="scroll">
      <span class="transparent-template">
        <EditorContent class="textarea padded" :editor="state.editor"/>
      </span>
      <div class="space"></div>
    </div>
    <span class="button-section">
      <button @click="transform().toggleBold().run()"><Icon>format_bold</Icon></button>
      <button @click="transform().toggleItalic().run()"><Icon>format_italic</Icon></button>
      <button @click="transform().toggleStrike().run()"><Icon>format_strikethrough</Icon></button>
      <button @click="transform().toggleHeading({level: 3}).run()"><Icon>title</Icon></button>
      <button @click="transform().toggleCode().run()"><Icon>code</Icon></button>
      <button @click="transform().toggleCodeBlock().run()"><Icon>code_blocks</Icon></button>
      <button @click="transform().toggleBulletList().run()"><Icon>format_list_bulleted</Icon></button>
      <span class="horizontal-spacer transparent-template"></span>
      <button @click="post"><Icon>send</Icon> post</button>
    </span>
  </div>
</Card>
</template>

<script setup lang="ts">
import Card from "@/views/components/containers/Card.vue";
import Icon from "@/views/components/Icon.vue";
import {Commands, Editor, EditorContent} from "@tiptap/vue-3";
import {onMounted, reactive} from "vue";
import {StarterKit} from "@tiptap/starter-kit";
import {Placeholder} from "@tiptap/extension-placeholder";
import type {PostDto} from "@/api/post/dto/PostDto";

const emit = defineEmits(['submit'])

const state = reactive({
  editor: null as any,
  title: ""
})

function post() {
  emit('submit', {
    title: state.title,
    body: state.editor?.getHTML()
  })

  state.editor.commands.clearContent()
  state.title = ""
}

onMounted(() => {
  state.editor = new Editor({
    editorProps: {
      attributes: {
        class: 'hidden-template',
      },
    },
    extensions: [
        StarterKit,
        Placeholder.configure({
          emptyEditorClass: 'is-editor-empty',
          placeholder: 'Share your thoughts...'
        })
    ]
  })
})

const transform = () => state.editor?.chain().focus();
</script>

<style scoped lang="scss">
@import "@/assets/scss/constants.scss";

.editor {
  display: flex;
  flex-direction: column;
}

.body {
  width: auto;
  display: flex;
  justify-content: stretch;
  flex-direction: column;
  position: relative;

  .scroll {
    max-height: 20rem;
    overflow-y: scroll;

    .space {
      height: 2rem;
    }
  }

  .textarea {
    display: block;
    border: none;
    overflow-y: scroll;
    width: auto;
    margin-left: 1rem;
    margin-right: 1rem;
  }

  .button-section {
    position: absolute;
    bottom: 0;
    display: flex;
    flex-direction: row;
    width: 100%;
    box-sizing: border-box;
    border-radius: 6px;
    padding: 0 2rem;
    background: linear-gradient(
      0deg,
      $background-special 50%,
      transparent 100%
    ) !important;

    & > * {
      padding: .25rem;
      margin: .25rem;
    }
  }
}
</style>