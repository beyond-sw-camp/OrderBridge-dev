<script setup>

function tryDemo() {
  // 데모 버튼 클릭 시 처리할 로직
  alert("Demo 버튼 클릭");
}

</script>

<template>
  <div class="hero-container">
    <div class="hero-left">
<!--      <img src="@/images/mainImage.jpg" alt="Clouds" class="hero-image" />-->
    </div>
    <div class="hero-right">
      <span class="hero-title">
        You have a <br />
        reason, We have a <br />
        <span class="highlight">
          <span class="char" style="color: #2b3a83;">s</span>
          <span class="char" style="color: #e20000;">o</span>
          <span class="char" style="color: #ff5ad0;">l</span>
          <span class="char" style="color: #f5871f;">u</span>
          <span class="char" style="color: #3fcb2c;">t</span>
          <span class="char" style="color: #5b3df1;">i</span>
          <span class="char" style="color: #b0cffd;">o</span>
          <span class="char" style="color: #5b3df1;">n</span>
        </span>
      </span>
      <p class="hero-description">
        오더브릿지 주문 관리 시스템은 <br />
        안전하고 차별적인 서비스를 제공합니다.
      </p>
      <button class="hero-button" @click="tryDemo">TRY DEMO</button>
    </div>
  </div>
</template>

<style scoped>

.hero-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fdf4e4; /* 배경색 */
  height: 100vh;
  padding: 0 5%;
}

.hero-left {
  flex: 1;
}

.hero-image {
  width: 100%;
  height: auto;
  object-fit: cover;
  border-radius: 10px;
}

.hero-right {
  flex: 1;
  text-align: right;
}

 .hero-title {
   font-size: 3.5rem;
   font-weight: bold;
   margin-bottom: 1rem;
   line-height: 1.4;
   font-family: 'Abhaya Libre', serif; /* 글씨체 적용 */
 }

.hero-description {
  font-size: 1.2rem;
  color: #000000;
  margin-bottom: 2rem;
  font-family: 'Alkalami', serif;
}

button {
  position: relative;
  display: inline-block;
  cursor: pointer;
  outline: none;
  border: 0;
  vertical-align: middle;
  text-decoration: none;
  font-size: inherit;
  font-family: inherit;
}

button.hero-button {
  font-family: 'Abhaya Libre', serif;
  font-size: 1.1rem;
  background-color: #d8e9ff;
  border: 2px solid black;
  text-transform: uppercase;
  padding: 1em 2em;
  border-radius: 15px;
  transform-style: preserve-3d;
  transition: transform 150ms cubic-bezier(0, 0, 0.58, 1), background 150ms cubic-bezier(0, 0, 0.58, 1);
}
button.hero-button::before {
  position: absolute;
  content: "";
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  margin-left: 10px;
  background-color: #fdf4e4;;
  border-radius: inherit;
  box-shadow: 0 0 0 2px black;
  transform: translate3d(0, 0.75em, -1em);
  transition: transform 150ms cubic-bezier(0, 0, 0.58, 1), box-shadow 150ms cubic-bezier(0, 0, 0.58, 1);
}

.hero-button:hover {
  background-color: #FFFDF8;
}

</style>
