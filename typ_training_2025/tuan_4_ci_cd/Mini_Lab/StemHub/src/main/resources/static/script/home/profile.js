// Wire like/unlike and download actions on the profile page
(function() {
  function onDomReady(cb){ if(document.readyState==='loading'){document.addEventListener('DOMContentLoaded',cb);} else {cb();}}

  onDomReady(function(){
    // Like toggle for each card
    document.querySelectorAll('.like-btn').forEach(function(btn){
      btn.addEventListener('click', async function(e){
        e.preventDefault();
        const documentId = btn.getAttribute('data-document-id');
        let userId = btn.getAttribute('data-user-id') || (window.CURRENT_USER_ID ? String(window.CURRENT_USER_ID) : '');
        let liked = String(btn.getAttribute('data-liked')) === 'true';
        if(!userId){ window.location.href = '/auth/login'; return; }
        if(!documentId){ return; }
        const icon = btn.querySelector('.like-icon');
        const label = btn.querySelector('.like-label');
        const originalIcon = icon ? icon.className : '';
        const originalText = label ? label.textContent : '';
        btn.disabled = true;
        if(icon) icon.className = 'fas fa-spinner fa-spin me-1';
        if(label) label.textContent = 'Đang xử lý...';
        try{
          const params = new URLSearchParams({ userId, documentId });
          const res = await fetch('/api/v1/actions/like', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params.toString(),
            redirect: 'follow'
          });
          if(!res.ok) throw new Error('Like request failed');
          // toggle state
          if(liked){
            btn.classList.remove('btn-danger');
            btn.classList.add('btn-outline-danger');
            if(icon) icon.className = 'far fa-heart me-1';
            if(label) label.textContent = 'Yêu thích';
            btn.setAttribute('data-liked','false');
            liked = false;
          } else {
            btn.classList.remove('btn-outline-danger');
            btn.classList.add('btn-danger');
            if(icon) icon.className = 'fas fa-heart me-1';
            if(label) label.textContent = 'Bỏ yêu thích';
            btn.setAttribute('data-liked','true');
            liked = true;
          }
        } catch(err){
          console.error(err);
          if(icon) icon.className = originalIcon;
          if(label) label.textContent = originalText || 'Yêu thích';
          alert('Không thể cập nhật yêu thích. Vui lòng thử lại.');
        } finally {
          btn.disabled = false;
        }
      });
    });

    // Download count tracking
    document.querySelectorAll('.download-btn').forEach(function(a){
      a.addEventListener('click', async function(e){
        // count then proceed
        e.preventDefault();
        const documentId = a.getAttribute('data-document-id');
        const fileUrl = a.getAttribute('data-file-url') || a.getAttribute('href');
        if(!documentId || !fileUrl){ window.open(a.getAttribute('href'),'_blank'); return; }
        const original = a.innerHTML;
        a.classList.add('disabled');
        a.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Đang tải...';
        try{
          const params = new URLSearchParams({ documentId });
          await fetch('/api/v1/actions/download', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params.toString(),
            redirect: 'follow'
          });
        } catch(err){ console.warn('Download count failed', err); }
        // proceed to actual file
        try{
          const link = document.createElement('a');
          link.href = fileUrl;
          link.target = '_blank';
          link.rel = 'noopener';
          link.download = '';
          document.body.appendChild(link);
          link.click();
          link.remove();
        } catch(openErr){ window.open(fileUrl,'_blank'); }
        a.classList.remove('disabled');
        a.innerHTML = original;
      });
    });
  });
})();
