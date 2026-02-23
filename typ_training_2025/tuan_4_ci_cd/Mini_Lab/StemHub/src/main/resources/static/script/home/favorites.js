// favorites.js - wire Like toggle (unfavorite) and download count in Favorites page
(function(){
  function ready(fn){ if(document.readyState==='loading'){document.addEventListener('DOMContentLoaded',fn);} else {fn();}}

  ready(function(){
    const grid = document.getElementById('favoritesGrid');

    // Unfavorite/Like toggle
    document.querySelectorAll('.like-btn').forEach(function(btn){
      btn.addEventListener('click', async function(e){
        e.preventDefault();
        const documentId = btn.getAttribute('data-document-id');
        const userId = btn.getAttribute('data-user-id');
        const liked = String(btn.getAttribute('data-liked')) === 'true';
        if(!userId){ window.location.href = '/auth/login'; return; }
        if(!documentId){ return; }
        const icon = btn.querySelector('.like-icon');
        const label = btn.querySelector('.like-label');
        const original = btn.innerHTML;
        btn.disabled = true;
        btn.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Đang xử lý...';
        try{
          const params = new URLSearchParams({ userId, documentId });
          const res = await fetch('/api/v1/actions/like', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params.toString(),
            redirect: 'follow'
          });
          if(!res.ok) throw new Error('Like request failed');
          // If it was liked, remove card from favorites
          const cardCol = btn.closest('[data-doc-id]');
          if(liked && cardCol){ cardCol.remove(); }
          // If grid is now empty, show empty state
          if(grid && grid.querySelectorAll('[data-doc-id]').length === 0){
            const empty = document.createElement('div');
            empty.className = 'text-center py-5';
            empty.innerHTML = '<div class="mb-3"><i class="fas fa-heart-broken fa-3x text-muted"></i></div>' +
                              '<h5 class="text-muted">Chưa có tài liệu yêu thích</h5>' +
                              '<p class="text-muted mb-3">Hãy khám phá và lưu những tài liệu hữu ích!</p>' +
                              '<a href="/search" class="btn btn-primary"><i class="fas fa-search me-2"></i>Khám phá tài liệu</a>';
            grid.parentElement.appendChild(empty);
            grid.remove();
          }
        } catch(err){
          console.error(err);
          btn.innerHTML = original;
          btn.disabled = false;
          alert('Không thể cập nhật yêu thích. Vui lòng thử lại.');
          return;
        }
        // Success: if still on page (not removed), toggle to liked/unliked state
        if(document.body.contains(btn)){
          btn.disabled = false;
          if(liked){
            // It was liked and now unliked, update to like state if not removed for some reason
            btn.classList.remove('btn-danger');
            btn.classList.add('btn-outline-danger');
            btn.setAttribute('data-liked','false');
            btn.innerHTML = '<i class="far fa-heart me-1 like-icon"></i><span class="like-label">Yêu thích</span>';
          } else {
            btn.classList.remove('btn-outline-danger');
            btn.classList.add('btn-danger');
            btn.setAttribute('data-liked','true');
            btn.innerHTML = '<i class="fas fa-heart me-1 like-icon"></i><span class="like-label">Bỏ yêu thích</span>';
          }
        }
      });
    });

    // Download count
    document.querySelectorAll('.download-btn').forEach(function(a){
      a.addEventListener('click', async function(e){
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
        // Proceed to actual file download
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

