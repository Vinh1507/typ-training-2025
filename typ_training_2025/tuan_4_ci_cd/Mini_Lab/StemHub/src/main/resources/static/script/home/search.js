document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchInput');
    const realTimeResults = document.getElementById('realTimeResults');
    const realTimeList = document.getElementById('realTimeList');
    const mainResults = document.getElementById('mainResults');
    
    let searchTimeout;
    let isSearching = false;

    // Real-time search with debounce
    searchInput.addEventListener('input', function() {
        const query = this.value.trim();
        
        // Clear previous timeout
        clearTimeout(searchTimeout);
        
        // Hide real-time results if query is empty
        if (!query) {
            realTimeResults.style.display = 'none';
            return;
        }
        
        // Set new timeout for debounce (500ms)
        searchTimeout = setTimeout(() => {
            performRealTimeSearch(query);
        }, 500);
    });

    // Handle form submission
    const searchForm = document.querySelector('.search-form');
    searchForm.addEventListener('submit', function(e) {
        // Allow form submission even without search query
        // Users can search by categories only
        
        // Set flag for scroll after reload
        localStorage.setItem('scrollToResults', '1');
    });

    // Perform real-time search
    function performRealTimeSearch(query) {
        if (isSearching) return;
        
        isSearching = true;
        showLoading();
        
        fetch(`/api/search/?q=${encodeURIComponent(query)}`)
            .then(response => response.json())
            .then(data => {
                isSearching = false;
                displayRealTimeResults(data.files, query);
            })
            .catch(error => {
                isSearching = false;
                console.error('Search error:', error);
                showMessage('Có lỗi xảy ra khi tìm kiếm', 'error');
            });
    }

    // Display real-time search results
    function displayRealTimeResults(files, query) {
        if (files.length === 0) {
            realTimeResults.style.display = 'none';
            return;
        }

        realTimeList.innerHTML = '';
        
        files.forEach(file => {
            const item = document.createElement('div');
            item.className = 'real-time-item';
            item.innerHTML = `
                <div class="file-thumbnail">
                    <img src="/static/images/default.webp" alt="${file.title}" class="img-fluid" style="width: 50px; height: 50px; object-fit: cover; border-radius: 8px;">
                </div>
                <div class="file-info">
                    <div class="file-title">${highlightQuery(file.title, query)}</div>
                    <div class="file-meta">
                        <span class="category-badge">${file.category}</span>
                        <span class="author">bởi ${file.author}</span>
                        <span class="status ${file.status === 'Free' ? 'free' : 'paid'}">${file.status}</span>
                    </div>
                </div>
            `;
            
            // Add click event to navigate to file detail
            item.addEventListener('click', () => {
                window.location.href = file.url;
            });
            
            realTimeList.appendChild(item);
        });
        
        realTimeResults.style.display = 'block';
        
        // Auto scroll to real-time results if they appear below the fold
        const realTimeResultsRect = realTimeResults.getBoundingClientRect();
        if (realTimeResultsRect.bottom > window.innerHeight) {
            realTimeResults.scrollIntoView({ 
                behavior: 'smooth', 
                block: 'nearest',
                inline: 'nearest'
            });
        }
    }

    // Highlight search query in results
    function highlightQuery(text, query) {
        if (!query) return text;
        const regex = new RegExp(`(${query})`, 'gi');
        return text.replace(regex, '<mark>$1</mark>');
    }

    // Show loading state
    function showLoading() {
        realTimeList.innerHTML = `
            <div class="text-center py-3">
                <div class="loading"></div>
                <p class="text-muted mt-2">Đang tìm kiếm...</p>
            </div>
        `;
        realTimeResults.style.display = 'block';
    }

    // Show message
    function showMessage(message, type = 'info') {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type === 'error' ? 'danger' : type} alert-dismissible fade show`;
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        const container = document.querySelector('.search-results .container');
        container.insertBefore(alertDiv, container.firstChild);
        
        // Auto remove after 5 seconds
        setTimeout(() => {
            if (alertDiv.parentNode) {
                alertDiv.remove();
            }
        }, 5000);
    }

    // Multi-select category functionality
    let selectedCategories = new Set();
    
    // Initialize selected categories from template
    document.querySelectorAll('.category-tag').forEach(tag => {
        const categoryName = tag.dataset.category;
        selectedCategories.add(categoryName);
        
        // Check corresponding checkbox
        const checkbox = document.querySelector(`input[data-category="${categoryName}"]`);
        if (checkbox) {
            checkbox.checked = true;
        }
    });

    // Handle category checkbox selection
    window.handleCategoryCheckbox = function(checkbox) {
        const categoryName = checkbox.dataset.category;
        const isParent = !checkbox.dataset.parent;
        
        if (checkbox.checked) {
            // Add category
            selectedCategories.add(categoryName);
            
            // Create tag element
            const tag = document.createElement('span');
            tag.className = 'category-tag';
            tag.dataset.category = categoryName;
            tag.innerHTML = `
                ${categoryName}
                <button type="button" class="tag-remove" onclick="removeCategory('${categoryName}')">
                    <i class="fas fa-times"></i>
                </button>
            `;
            
            // Add to selected categories container
            const container = document.getElementById('selectedCategories');
            container.appendChild(tag);
            
            // Add hidden input
            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'categories';
            hiddenInput.value = categoryName;
            hiddenInput.dataset.category = categoryName;
            
            document.getElementById('categoryInputs').appendChild(hiddenInput);
            
            // If parent is selected, uncheck all children
            if (isParent) {
                const parentName = checkbox.dataset.category;
                const childCheckboxes = document.querySelectorAll(`input[data-parent="${parentName}"]`);
                childCheckboxes.forEach(childCheckbox => {
                    if (childCheckbox.checked) {
                        childCheckbox.checked = false;
                        selectedCategories.delete(childCheckbox.dataset.category);
                        
                        // Remove child tag
                        const childTag = document.querySelector(`.category-tag[data-category="${childCheckbox.dataset.category}"]`);
                        if (childTag) {
                            childTag.remove();
                        }
                        
                        // Remove child hidden input
                        const childInput = document.querySelector(`input[data-category="${childCheckbox.dataset.category}"]`);
                        if (childInput) {
                            childInput.remove();
                        }
                    }
                });
            }
            
            // If child is selected, uncheck parent
            if (!isParent) {
                const parentName = checkbox.dataset.parent;
                const parentCheckbox = document.querySelector(`input[data-category="${parentName}"]`);
                if (parentCheckbox && parentCheckbox.checked) {
                    parentCheckbox.checked = false;
                    selectedCategories.delete(parentName);
                    
                    // Remove parent tag
                    const parentTag = document.querySelector(`.category-tag[data-category="${parentName}"]`);
                    if (parentTag) {
                        parentTag.remove();
                    }
                    
                    // Remove parent hidden input
                    const parentInput = document.querySelector(`input[data-category="${parentName}"]`);
                    if (parentInput) {
                        parentInput.remove();
                    }
                }
            }
            
        } else {
            // Remove category
            selectedCategories.delete(categoryName);
            
            // Remove tag element
            const tag = document.querySelector(`.category-tag[data-category="${categoryName}"]`);
            if (tag) {
                tag.remove();
            }
            
            // Remove hidden input
            const hiddenInput = document.querySelector(`input[data-category="${categoryName}"]`);
            if (hiddenInput) {
                hiddenInput.remove();
            }
        }
        
        // Update filter status
        updateFilterStatus();
    };

    // Add category function (for backward compatibility)
    window.addCategory = function(categoryName) {
        // This function is kept for backward compatibility but now uses the new dropdown system
        console.log('addCategory called for:', categoryName);
    };

    // Remove category function
    window.removeCategory = function(categoryName) {
        selectedCategories.delete(categoryName);
        
        // Remove tag element
        const tag = document.querySelector(`.category-tag[data-category="${categoryName}"]`);
        if (tag) {
            tag.remove();
        }
        
        // Remove hidden input
        const hiddenInput = document.querySelector(`input[data-category="${categoryName}"]`);
        if (hiddenInput) {
            hiddenInput.remove();
        }
        
        // Uncheck corresponding checkbox
        const checkbox = document.querySelector(`input[data-category="${categoryName}"]`);
        if (checkbox) {
            checkbox.checked = false;
        }
        
        // Update filter status
        updateFilterStatus();
    };



    // Clear all categories
    window.clearAllCategories = function() {
        selectedCategories.clear();
        document.getElementById('selectedCategories').innerHTML = '';
        document.getElementById('categoryInputs').innerHTML = '';
        
        // Uncheck all checkboxes
        const checkboxes = document.querySelectorAll('.category-checkbox');
        checkboxes.forEach(checkbox => {
            checkbox.checked = false;
        });
        
        // Update filter status
        updateFilterStatus();
    };

    // Filter form submission - remove auto-submit
    const filterSelects = document.querySelectorAll('.search-filters select');
    filterSelects.forEach(select => {
        select.addEventListener('change', function() {
            // Remove auto-submit, user must click search button
            // Just add visual feedback for active filters
            updateFilterStatus();
        });
    });
    
    // Update filter status visual feedback
    function updateFilterStatus() {
        const form = document.querySelector('.search-form');
        const hasCategories = selectedCategories.size > 0;
        const hasStatus = document.querySelector('select[name="status"]').value !== '';
        const hasQuery = document.getElementById('searchInput').value.trim() !== '';
        
        if (hasCategories || hasStatus || hasQuery) {
            form.classList.add('has-filters');
        } else {
            form.classList.remove('has-filters');
        }
    }
    
    // Update filter status when categories change
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('tag-remove') || e.target.closest('.tag-remove')) {
            setTimeout(updateFilterStatus, 100);
        }
    });
    
    // Initialize filter status on page load
    updateFilterStatus();
    
    // Add keyboard support for category dropdown
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            // Close category dropdown on Escape
            const dropdownBtn = document.querySelector('.category-dropdown-btn');
            if (dropdownBtn) {
                const bsDropdown = bootstrap.Dropdown.getInstance(dropdownBtn);
                if (bsDropdown) {
                    bsDropdown.hide();
                }
            }
        }
    });
    
    // Add visual feedback when adding categories
    window.addCategoryWithFeedback = function(categoryName) {
        addCategory(categoryName);
        
        // Show brief success message
        const container = document.getElementById('selectedCategories');
        const feedback = document.createElement('div');
        feedback.className = 'category-feedback';
        feedback.innerHTML = `<i class="fas fa-check"></i> Đã thêm "${categoryName}"`;
        feedback.style.cssText = `
            position: absolute;
            top: -30px;
            right: 0;
            background: #28a745;
            color: white;
            padding: 0.25rem 0.5rem;
            border-radius: 15px;
            font-size: 0.75rem;
            opacity: 0;
            transform: translateY(10px);
            transition: all 0.3s ease;
            z-index: 1000;
        `;
        
        container.style.position = 'relative';
        container.appendChild(feedback);
        
        // Animate in
        setTimeout(() => {
            feedback.style.opacity = '1';
            feedback.style.transform = 'translateY(0)';
        }, 10);
        
        // Remove after 2 seconds
        setTimeout(() => {
            feedback.style.opacity = '0';
            feedback.style.transform = 'translateY(-10px)';
            setTimeout(() => {
                if (feedback.parentNode) {
                    feedback.remove();
                }
            }, 300);
        }, 2000);
    };
    
    // Enhanced search input behavior
    searchInput.addEventListener('input', function() {
        updateFilterStatus();
    });

    // Keyboard navigation for real-time results
    let selectedIndex = -1;
    const realTimeItems = [];

    searchInput.addEventListener('keydown', function(e) {
        const items = realTimeList.querySelectorAll('.real-time-item');
        
        if (items.length === 0) return;
        
        switch(e.key) {
            case 'ArrowDown':
                e.preventDefault();
                selectedIndex = Math.min(selectedIndex + 1, items.length - 1);
                updateSelection(items);
                break;
            case 'ArrowUp':
                e.preventDefault();
                selectedIndex = Math.max(selectedIndex - 1, -1);
                updateSelection(items);
                break;
            case 'Enter':
                e.preventDefault();
                if (selectedIndex >= 0 && items[selectedIndex]) {
                    items[selectedIndex].click();
                } else {
                    // Submit form if no item is selected
                    searchForm.submit();
                }
                break;
            case 'Escape':
                realTimeResults.style.display = 'none';
                selectedIndex = -1;
                break;
        }
    });

    function updateSelection(items) {
        items.forEach((item, index) => {
            if (index === selectedIndex) {
                item.style.backgroundColor = '#e3f2fd';
                item.style.borderLeft = '3px solid #006056';
            } else {
                item.style.backgroundColor = '';
                item.style.borderLeft = '';
            }
        });
    }

    // Click outside to close real-time results
    document.addEventListener('click', function(e) {
        if (!realTimeResults.contains(e.target) && e.target !== searchInput) {
            realTimeResults.style.display = 'none';
            selectedIndex = -1;
        }
    });

    // Report file function
    window.reportFile = function() {
        if (confirm('Bạn có chắc chắn muốn báo cáo tài liệu này?')) {
            alert('Cảm ơn bạn đã báo cáo. Chúng tôi sẽ xem xét trong thời gian sớm nhất.');
        }
    };

    // Khi bấm nút tìm kiếm
    window.beforeSubmitScroll = function() {
        localStorage.setItem('scrollToResults', '1');
    };

    // Khi trang load, kiểm tra xem có cần scroll không
    window.addEventListener('load', function() {
        if (localStorage.getItem('scrollToResults') === '1') {
            // Đợi một chút để đảm bảo DOM đã load hoàn toàn
            setTimeout(() => {
                scrollToSearchResults();
                localStorage.removeItem('scrollToResults');
                
                // Thêm hiệu ứng loading cho search results
                const searchResults = document.querySelector('.search-results');
                if (searchResults) {
                    searchResults.classList.add('loading');
                    setTimeout(() => {
                        searchResults.classList.remove('loading');
                    }, 1000);
                }
            }, 300);
        }
    });
        
    // Scroll to search results function
    function scrollToSearchResults() {
        const searchResults = document.querySelector('.search-results');
        if (searchResults) {
            // Calculate offset to account for fixed header if any
            const offset = 80; // Adjust this value based on your header height
            const elementPosition = searchResults.getBoundingClientRect().top;
            const offsetPosition = elementPosition + window.pageYOffset - offset;
            
            // Smooth scroll to results
            window.scrollTo({
                top: offsetPosition,
                behavior: 'smooth'
            });
            
            // Add highlight effect to the results header after scroll
            setTimeout(() => {
                const resultsHeader = document.querySelector('.results-header');
                if (resultsHeader) {
                    resultsHeader.classList.add('highlight');
                    
                    setTimeout(() => {
                        resultsHeader.classList.remove('highlight');
                    }, 1000);
                }
            }, 500);
            
            // Add visual feedback for the scroll button
            const scrollButton = event?.target;
            if (scrollButton && scrollButton.classList.contains('btn-outline-info')) {
                scrollButton.style.transform = 'scale(0.95)';
                setTimeout(() => {
                    scrollButton.style.transform = '';
                }, 200);
            }
        }
    }

    // Initialize search input focus
    searchInput.focus();
    
    // Add search suggestions
    const suggestions = [
        'Toán học', 'Vật lý', 'Hóa học', 'Sinh học', 
        'Công nghệ', 'Kỹ thuật', 'Bài giảng', 'Thí nghiệm',
        'Giáo án', 'Đề thi', 'Tài liệu', 'STEM'
    ];
    
    let suggestionIndex = 0;
    
    // Auto-complete functionality
    searchInput.addEventListener('input', function() {
        const query = this.value.toLowerCase();
        if (query.length < 2) return;
        
        const matchingSuggestion = suggestions.find(s => 
            s.toLowerCase().includes(query) && s.toLowerCase() !== query
        );
        
        if (matchingSuggestion) {
            // Show suggestion (you can implement this as a dropdown)
            // For now, we'll just log it
            console.log('Suggestion:', matchingSuggestion);
        }
    });

    // Add search analytics
    function trackSearch(query, results) {
        // You can implement analytics tracking here
        console.log(`Search: "${query}" returned ${results} results`);
    }

    // Enhanced search with filters
    const filterForm = document.querySelector('.search-form');
    const filterInputs = filterForm.querySelectorAll('input, select');
    
    filterInputs.forEach(input => {
        input.addEventListener('change', function() {
            // Add visual feedback for active filters
            const activeFilters = Array.from(filterInputs)
                .filter(input => input.value && input.value !== '')
                .length;
            
            if (activeFilters > 0) {
                filterForm.classList.add('has-filters');
            } else {
                filterForm.classList.remove('has-filters');
            }
        });
    });

    // Add search history (localStorage)
    function saveSearchHistory(query) {
        let history = JSON.parse(localStorage.getItem('searchHistory') || '[]');
        history = history.filter(item => item !== query);
        history.unshift(query);
        history = history.slice(0, 10); // Keep only last 10 searches
        localStorage.setItem('searchHistory', JSON.stringify(history));
    }

    function getSearchHistory() {
        return JSON.parse(localStorage.getItem('searchHistory') || '[]');
    }

    // Show search history on focus
    searchInput.addEventListener('focus', function() {
        const history = getSearchHistory();
        if (history.length > 0 && !this.value) {
            // You can implement a dropdown to show search history
            console.log('Search history:', history);
        }
    });
}); 