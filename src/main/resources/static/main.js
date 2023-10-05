let currentSlide = 0;
const slides = document.querySelectorAll(".slide");
const totalSlides = slides.length;

setInterval(function() {
    slides[currentSlide].style.opacity = 0;
    currentSlide = (currentSlide + 1) % totalSlides;
    slides[currentSlide].style.opacity = 1;
}, 3000); // 3초 간격으로 이미지 전환