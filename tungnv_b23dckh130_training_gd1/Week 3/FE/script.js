document.addEventListener('DOMContentLoaded', () => {
    const introduction = document.getElementById('introduction');
    const avatarImg = document.getElementsByClassName('avatar')[0];
    const audio = document.getElementById('song');
    
    if (!audio || !avatarImg) {
        console.error('Không tìm thấy audio hoặc avatar image');
        return;
    }
    
    audio.addEventListener('play', () => {
        avatarImg.classList.add('spinning-avatar');
    });

    audio.addEventListener('pause', () => {
        avatarImg.classList.remove('spinning-avatar');
    });

    introduction.addEventListener('mouseenter', () => {
        const playPromise = audio.play();
        
        if (playPromise !== undefined) {
            playPromise.then(() => {
                console.log('Audio đã phát');
            }).catch((error) => {
                console.error('Audio không thể phát:', error);
            });
        }
    });

    introduction.addEventListener('mouseleave', () => {
        audio.pause();
        audio.currentTime = 0;
    });

    avatarImg.addEventListener('click', (e) => {
        e.stopPropagation();
        
        if (audio.paused) {
            audio.play();
        } else {
            audio.pause();
        }
    });
});

const scrollToTopBtn = document.createElement('button');
scrollToTopBtn.id = 'scrollToTop';
scrollToTopBtn.innerHTML = '↑';
document.body.appendChild(scrollToTopBtn);

window.addEventListener('scroll', () => {
    if (window.scrollY > 300) {
        scrollToTopBtn.classList.add('show');
    } else {
        scrollToTopBtn.classList.remove('show');
    }
});

scrollToTopBtn.addEventListener('click', () => {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
});