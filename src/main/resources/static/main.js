document.addEventListener("DOMContentLoaded", function() {
    let currentSlide = 0;
    const slides = document.querySelectorAll(".slide");
    const totalSlides = slides.length;
    const indicatorsContainer = document.querySelector('.slider-indicators');

    // 인디케이터 생성
    for(let i = 0; i < totalSlides; i++) {
        const indicator = document.createElement('div');
        indicator.classList.add('indicator');
        indicator.addEventListener('click', function() {
            updateSlide(i);
        });
        indicatorsContainer.appendChild(indicator);
    }

    const indicators = document.querySelectorAll(".indicator");

    function updateSlide(slideIndex) {
        slides[currentSlide].style.opacity = 0;
        indicators[currentSlide].classList.remove('active');
        currentSlide = slideIndex;
        slides[currentSlide].style.opacity = 1;
        indicators[currentSlide].classList.add('active');
    }

    document.getElementById('prevSlide').addEventListener('click', function() {
        let prevSlide = (currentSlide - 1 + totalSlides) % totalSlides;
        updateSlide(prevSlide);
    });

    document.getElementById('nextSlide').addEventListener('click', function() {
        let nextSlide = (currentSlide + 1) % totalSlides;
        updateSlide(nextSlide);
    });

    // 첫 번째 슬라이드의 인디케이터를 활성화합니다.
    indicators[currentSlide].classList.add('active');

    // 자동 슬라이드
    setInterval(function() {
        let nextSlide = (currentSlide + 1) % totalSlides;
        updateSlide(nextSlide);
    }, 3000);
});





