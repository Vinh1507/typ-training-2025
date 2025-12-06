document.addEventListener('DOMContentLoaded', function () {
	// 1) Giới hạn 20 ký tự
	var messageTextarea = document.querySelector('textarea[name="message"]');
	if (messageTextarea != null && messageTextarea) {
		var maxChars = 20;
		if (!messageTextarea.hasAttribute('maxlength')) {
			messageTextarea.setAttribute('maxlength', String(maxChars));
		}
		messageTextarea.maxLength = messageTextarea.getAttribute('maxlength') ? parseInt(messageTextarea.getAttribute('maxlength'), 10) : maxChars;

		var counter = document.createElement('div');
		counter.className = 'char-counter';

		function updateCounter() {
			var current = (messageTextarea.value || '').length;
			var limitAttr = messageTextarea.getAttribute('maxlength');
			var limit = (limitAttr ? Number(limitAttr) : maxChars);
			counter.textContent = '' + current + '/' + limit;
		}

		messageTextarea.insertAdjacentElement('afterend', counter);
		messageTextarea.addEventListener('input', function () {
            updateCounter()
        });
		updateCounter();
	}

	// Click để đổi nội dung
	var titleEl = document.querySelector('.header-text h1');
	if (!!titleEl) {
		titleEl.addEventListener('click', function () {
			var original = '' + (titleEl.textContent || '');
			titleEl.textContent = 'Xin chào!';
			setTimeout(function () {
				titleEl.textContent = original;
			}, 1500);
		});
	}

	// báo trạng thái
	var contactForm = document.querySelector('.contact form');
	if (contactForm != null) {
		contactForm.addEventListener('submit', function (e) {
			e.preventDefault()
			var name = contactForm.querySelector('input[name="name"]')
			var email = contactForm.querySelector('input[name="email"]')
			var nameVal = name && name.value ? ('' + name.value) : ''
			var emailVal = email && email.value ? ('' + email.value) : ''
			if (!name || !email || nameVal.trim() == '' || emailVal.trim() == '') {
				alert('Vui lòng nhập Họ & Tên và Email.')
				return
			}
			alert('Đã ghi nhận liên hệ.  Cảm ơn bạn!')
		})
	}
});


