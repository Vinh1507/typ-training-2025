document.addEventListener('DOMContentLoaded', function() {
    const dragDropArea = document.getElementById('dragDropArea');
    const fileInput = document.getElementById('fileInput');
    const selectedFile = document.getElementById('selectedFile');
    const progressBar = document.getElementById('progressBar');
    const thumbnailInput = document.getElementById('thumbnailInput');
    const thumbnailPreview = document.getElementById('thumbnailPreview');
    const uploadForm = document.getElementById('uploadForm');
    const submitBtn = uploadForm ? uploadForm.querySelector('button[type="submit"]') : null;
    const fileStatusSelect = document.querySelector('select[id*="file_status"]');
    const priceField = document.getElementById('priceField');
    const authorIdInput = document.getElementById('authorId');
    const authorSavedInfo = document.getElementById('authorSavedInfo');
    const authorSavedValue = document.getElementById('authorSavedValue');
    const authorIdFieldWrap = document.getElementById('authorIdFieldWrap');
    const changeAuthorIdBtn = document.getElementById('changeAuthorIdBtn');

    // --- Helper functions (placed before usage to avoid scope/hoist issues) ---
    function formatFileSize(bytes) {
        if (bytes === 0) return '0 Bytes';
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    }

    function displaySelectedFile(file) {
        const fileName = file.name;
        const fileSize = formatFileSize(file.size);
        if (selectedFile) {
            const fileNameEl = selectedFile.querySelector('.file-name');
            const fileSizeEl = selectedFile.querySelector('.file-size');
            if (fileNameEl) fileNameEl.textContent = fileName;
            if (fileSizeEl) fileSizeEl.textContent = fileSize;
            selectedFile.style.display = 'block';
        }
        if (dragDropArea) {
            const content = dragDropArea.querySelector('.upload-content');
            if (content) {
                content.innerHTML = `
                    <i class="fas fa-check-circle upload-icon" style="color: #28a745;"></i>
                    <h4 style="color: #28a745;">Tệp đã được chọn!</h4>
                    <p class="text-muted">Click để chọn tệp khác</p>
                `;
            }
        }
    }

    function handleThumbnailPreview(e) {
        const file = e.target.files && e.target.files[0];
        if (file && thumbnailPreview) {
            const reader = new FileReader();
            reader.onload = function(ev) {
                const img = thumbnailPreview.querySelector('img');
                if (img) {
                    img.src = ev.target.result;
                    thumbnailPreview.style.display = 'block';
                }
            };
            reader.readAsDataURL(file);
        }
    }

    function resetUploadArea() {
        if (selectedFile) selectedFile.style.display = 'none';
        if (dragDropArea) {
            const content = dragDropArea.querySelector('.upload-content');
            if (content) {
                content.innerHTML = `
                    <i class="fas fa-cloud-upload-alt upload-icon"></i>
                    <h4>Kéo thả tệp vào đây hoặc click để chọn</h4>
                    <p class="text-muted">Hỗ trợ: PDF, DOC/DOCX, PPT/PPTX, XLS/XLSX, TXT, JPG/PNG/JPEG/WEBP/GIF/BMP</p>
                    <p class="text-muted">Nếu không phải PDF, bắt buộc chọn hình thu nhỏ</p>
                    <p class="text-muted">Kích thước tối đa: 500MB</p>
                `;
            }
        }
    }

    // Check authentication on page load (supports JWT or session-based)
    checkAuthenticationStatus();

    function isSessionAuthenticated() {
        // If server rendered a hidden input with userId or any element gated by sec:authorize,
        // treat as authenticated via session
        return !!(authorIdInput && authorIdInput.value);
    }

    function checkAuthenticationStatus() {
        const token = localStorage.getItem('token');
        const sessionAuth = isSessionAuthenticated();

        if (!token && !sessionAuth) {
            // Neither JWT nor session auth is present
            showAuthWarning();
            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.title = 'Bạn cần đăng nhập để tải lên tài liệu';
            }
        } else {
            // Authenticated by either method
            if (submitBtn) {
                submitBtn.disabled = false;
                submitBtn.title = '';
            }
            // Let backend resolve authorId from JWT/session if missing
            if (authorIdInput && !authorIdInput.value) {
                authorIdInput.value = '';
            }
        }
    }

    function showAuthWarning() {
        // Check if warning already exists
        const existingWarning = document.querySelector('.auth-warning-banner');
        if (existingWarning) return;

        // Create warning banner
        const warning = document.createElement('div');
        warning.className = 'alert alert-warning auth-warning-banner';
        warning.innerHTML = `
            <i class="fas fa-exclamation-triangle me-2"></i>
            Bạn cần đăng nhập để tải lên tài liệu. 
            <a href="/auth/login" class="alert-link">Đăng nhập ngay</a>
        `;

        // Insert at the top of upload container
        const uploadContainer = document.querySelector('.upload-container');
        if (uploadContainer) {
            uploadContainer.insertBefore(warning, uploadContainer.firstChild);
        }
    }

    // Initialize price field visibility
    updatePriceVisibility();

    // Load authorId from localStorage if present (best-effort; optional)
    try {
        const savedAuthorId = localStorage.getItem('authorId');
        if (!isSessionAuthenticated() && savedAuthorId && authorIdInput) {
            authorIdInput.value = savedAuthorId;
            if (authorSavedInfo && authorSavedValue && authorIdFieldWrap) {
                authorSavedValue.textContent = savedAuthorId;
                authorSavedInfo.style.display = 'block';
                authorIdFieldWrap.style.display = 'none';
            }
        }
    } catch (e) {
        // ignore storage errors
    }

    if (changeAuthorIdBtn && authorIdFieldWrap && authorSavedInfo) {
        changeAuthorIdBtn.addEventListener('click', () => {
            authorSavedInfo.style.display = 'none';
            authorIdFieldWrap.style.display = 'block';
            if (authorIdInput) authorIdInput.focus();
        });
    }

    // Add event listener for file status change
    if (fileStatusSelect) {
        fileStatusSelect.addEventListener('change', updatePriceVisibility);
    }

    // Drag and drop functionality
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        if (dragDropArea) {
            dragDropArea.addEventListener(eventName, preventDefaults, false);
        }
        document.body.addEventListener(eventName, preventDefaults, false);
    });

    if (dragDropArea) {
        ['dragenter', 'dragover'].forEach(eventName => {
            dragDropArea.addEventListener(eventName, highlight, false);
        });
        ['dragleave', 'drop'].forEach(eventName => {
            dragDropArea.addEventListener(eventName, unhighlight, false);
        });
        dragDropArea.addEventListener('drop', handleDrop, false);
        // Only trigger file input when clicking directly on drag drop area, not its children
        dragDropArea.addEventListener('click', function(e) {
            if (e.target === dragDropArea || e.target.closest('.upload-content')) {
                fileInput && fileInput.click();
            }
        });
    }

    if (fileInput) fileInput.addEventListener('change', handleFiles);

    // Thumbnail preview functionality
    if (thumbnailInput) {
        thumbnailInput.addEventListener('change', handleThumbnailPreview);
    }

    // Remove thumbnail button
    const removeThumbnailBtn = document.getElementById('removeThumbnail');
    if (removeThumbnailBtn) {
        removeThumbnailBtn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            if (thumbnailInput) {
                thumbnailInput.value = '';
            }
            if (thumbnailPreview) {
                thumbnailPreview.style.display = 'none';
            }
        });
    }

    // Check if thumbnail is required for non-PDF files
    function checkThumbnailRequired() {
        const file = fileInput && fileInput.files && fileInput.files[0];
        const errorEl = document.getElementById('thumbnailError');
        const sectionEl = document.getElementById('thumbnailSection');
        if (!file) {
            if (errorEl) errorEl.style.display = 'none';
            return true; // No file, let other validations handle
        }

        const ext = file.name.split('.').pop().toLowerCase();
        const isPdf = ext === 'pdf';

        if (!isPdf) {
            const hasThumbnail = thumbnailInput && thumbnailInput.files && thumbnailInput.files.length > 0;
            if (!hasThumbnail) {
                if (errorEl) errorEl.style.display = 'block';
                if (sectionEl) sectionEl.scrollIntoView({ behavior: 'instant', block: 'center' });
                alert('Tài liệu không phải PDF yêu cầu bắt buộc phải có hình thu nhỏ. Trang sẽ tải lại ngay.');
                // Use a slight delay with location.replace to guarantee navigation
                setTimeout(() => { window.location.replace(window.location.href); }, 50);
                return false;
            }
        }

        if (errorEl) errorEl.style.display = 'none';
        return true;
    }

    // Live toggle thumbnail error when user selects thumbnail after picking non-PDF
    if (thumbnailInput) {
        thumbnailInput.addEventListener('change', function() {
            // If a non-PDF file is chosen and now thumbnail exists, hide error
            const file = fileInput && fileInput.files && fileInput.files[0];
            if (file) checkThumbnailRequired();
        });
    }

    // Improve submit button UX during validation
    function setSubmitBusy(busy, labelWhenReady = 'Tải lên') {
        if (!submitBtn) return;
        submitBtn.disabled = !!busy;
        submitBtn.innerHTML = busy
            ? '<i class="fas fa-spinner fa-spin me-2"></i>Đang kiểm tra...'
            : `<i class="fas fa-upload me-2"></i>${labelWhenReady}`;
    }

    // Form submission with validation
    if (uploadForm) {
        uploadForm.addEventListener('submit', function(e) {
            e.preventDefault();
            setSubmitBusy(true);

            const file = fileInput && fileInput.files && fileInput.files[0];
            const category = document.getElementById('category');
            const courseName = document.getElementById('courseName');

            let errorMsg = '';
            if (!file) errorMsg = 'Vui lòng chọn tệp tài liệu.';
            else if (!category || !category.value) errorMsg = 'Vui lòng chọn danh mục.';
            else if (!courseName || !courseName.value.trim()) errorMsg = 'Vui lòng nhập tên môn học/khoá học.';

            if (errorMsg) {
                alert(errorMsg);
                setSubmitBusy(false);
                return;
            }

            if (!checkThumbnailRequired()) {
                setSubmitBusy(false);
                return;
            }

            // Submit via fetch. Use JWT if available, otherwise rely on session cookie
            submitWithAuth();
        });
    }

    async function submitWithAuth() {
        const token = localStorage.getItem('token');

        try {
            const formData = new FormData();

            // Add file
            if (fileInput && fileInput.files[0]) {
                formData.append('file', fileInput.files[0]);
            }

            // Add form fields
            formData.append('title', document.getElementById('title').value);
            formData.append('category', document.getElementById('category').value);
            formData.append('courseName', document.getElementById('courseName').value);
            formData.append('description', document.getElementById('description').value);

            // Add thumbnail if selected
            const thumbnailFile = thumbnailInput && thumbnailInput.files ? thumbnailInput.files[0] : null;
            if (thumbnailFile) {
                formData.append('thumbnail', thumbnailFile);
            }

            const headers = {};
            if (token) {
                headers['Authorization'] = `Bearer ${token}`;
            }

            const response = await fetch('/document/upload', {
                method: 'POST',
                headers,
                body: formData,
                credentials: 'same-origin' // send session cookies when present
            });

            if (response.ok) {
                if (submitBtn) {
                    submitBtn.innerHTML = '<i class="fas fa-check me-2"></i>Tải lên thành công!';
                    submitBtn.classList.remove('btn-primary');
                    submitBtn.classList.add('btn-success');
                }
                setTimeout(() => { window.location.href = '/'; }, 1500);
            } else {
                // Attempt to parse error payload; if not JSON, fallback to text
                let message = 'Có lỗi xảy ra khi tải lên';
                try { const data = await response.json(); message = data.detail || data.message || message; }
                catch { try { message = await response.text() || message; } catch {} }
                throw new Error(message);
            }
        } catch (error) {
            console.error('Upload error:', error);
            alert('Lỗi: ' + error.message);
            if (submitBtn) {
                submitBtn.disabled = false;
                submitBtn.innerHTML = '<i class="fas fa-upload me-2"></i>Tải lên';
                submitBtn.classList.remove('btn-success');
                submitBtn.classList.add('btn-primary');
            }
        }
    }

    function updatePriceVisibility() {
        if (fileStatusSelect && priceField) {
            const selectedValue = fileStatusSelect.value;
            const priceInput = priceField.querySelector('input');
            
            if (selectedValue === '1') { // For sales
                priceField.style.display = 'block';
                if (priceInput) {
                    priceInput.required = true;
                    priceInput.min = '1000';
                }
            } else { // Free
                priceField.style.display = 'none';
                if (priceInput) {
                    priceInput.required = false;
                    priceInput.value = '0';
                }
            }
        }
    }

    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }

    function highlight(e) {
        if (dragDropArea) dragDropArea.classList.add('drag-over');
    }

    function unhighlight(e) {
        if (dragDropArea) dragDropArea.classList.remove('drag-over');
    }

    function handleDrop(e) {
        const dt = e.dataTransfer;
        const files = dt.files;
        if (fileInput) fileInput.files = files;
        handleFiles();
    }

    function handleFiles() {
        if (!fileInput) return;
        const files = fileInput.files;
        if (files.length > 0) {
            const file = files[0];
            
            // Validate file size (500MB)
            if (file.size > 500 * 1024 * 1024) {
                alert('Kích thước tệp quá lớn! Vui lòng chọn tệp nhỏ hơn 500MB.');
                fileInput.value = '';
                resetUploadArea();
                return;
            }
            
            // Validate file type - Allow multiple formats
            const allowedExts = ['.pdf','.doc','.docx','.ppt','.pptx','.xls','.xlsx','.txt','.md','.jpg','.jpeg','.png','.gif','.bmp','.webp'];
            const fileExtension = '.' + file.name.split('.').pop().toLowerCase();
            
            if (!allowedExts.includes(fileExtension)) {
                alert('Định dạng không được hỗ trợ. Hãy chọn: PDF, DOC/DOCX, PPT/PPTX, XLS/XLSX, TXT, JPG/PNG/JPEG/WEBP/GIF/BMP.');
                fileInput.value = '';
                resetUploadArea();
                return;
            }
            
            displaySelectedFile(file);
        }
    }


    // Auto-resize textarea
    const textareas = document.querySelectorAll('textarea');
    textareas.forEach(textarea => {
        textarea.addEventListener('input', function() {
            this.style.height = 'auto';
            this.style.height = Math.min(this.scrollHeight, 200) + 'px';
        });
    });
});

// Global function for price visibility (called from form onchange)
function updatePriceVisibility() {
    const fileStatusSelect = document.querySelector('select[id*="file_status"]');
    const priceField = document.getElementById('priceField');
    
    if (fileStatusSelect && priceField) {
        const selectedValue = fileStatusSelect.value;
        const priceInput = priceField.querySelector('input');
        
        if (selectedValue === '1') { // For sales
            priceField.style.display = 'block';
            if (priceInput) {
                priceInput.required = true;
                priceInput.min = '1000';
            }
        } else { // Free
            priceField.style.display = 'none';
            if (priceInput) {
                priceInput.required = false;
                priceInput.value = '0';
            }
        }
    }
}