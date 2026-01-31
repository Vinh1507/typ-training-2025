window.addEventListener('DOMContentLoaded', () => {
    const avatarSection = document.querySelector('.avatar');
    const avatarImg = document.querySelector('.avatar img');
    const audio = document.getElementById('song');
    if (!audio || !avatarImg) {
        console.error('Không tìm thấy audio hoặc avatar image');
        return;
    }
    audio.addEventListener('play', () => {
        avatarImg.classList.add('spinning');
    });

    audio.addEventListener('pause', () => {
        avatarImg.classList.remove('spinning');
    });

    avatarSection.addEventListener('mouseenter', () => {
        const playPromise = audio.play();

        if (playPromise !== undefined) {
            playPromise.then(() => {
                console.log('Audio đã phát');
            }).catch((error) => {
                console.error('Audio không thể phát:', error);
            });
        }   
    });

    avatarSection.addEventListener('mouseleave', () => {
        audio.pause();
        audio.currentTime = 0;
    });

    avatarImg.addEventListener('click', (e) => {
        e.stopPropagation();
        
        if (audio.paused) {
            audio.play()
        } else {
            audio.pause();
        }
    });
});