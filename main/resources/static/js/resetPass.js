
    let countdown;
    let timeLeft = 0;
    let userIdentifier = '';

    function showMessage(type, text) {
    const messageEl = document.getElementById(`${type}Message`);
    messageEl.textContent = text;
    messageEl.style.display = 'block';

    const types = ['error', 'success', 'info'];
    types.filter(t => t !== type).forEach(t => {
    document.getElementById(`${t}Message`).style.display = 'none';
});
}

    function goToStep(stepNumber) {
    document.querySelectorAll('.form-step').forEach(step => step.classList.remove('active'));
    document.getElementById(`step${stepNumber}`).classList.add('active');

    document.querySelectorAll('.step').forEach((step, index) => {
    step.classList.remove('active', 'completed');
    if (index + 1 === stepNumber) step.classList.add('active');
    else if (index + 1 < stepNumber) step.classList.add('completed');
});
}

    // Send OTP
    async function sendOTP() {
        userIdentifier = document.getElementById('userIdentifier').value.trim();
        if (!userIdentifier) {
            showMessage('error', 'Please enter your email or username');
            return;
        }

        showMessage('info', 'Sending OTP to your email...');

        try {
            const response = await fetch(`${BASE_PATH}api/PasswordReset/send-otp`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({ userIdentifier })
            });

            const data = await response.json();
            console.log("Response Data:", data);

            if (data.success) {  // <-- use JSON 'success' field
                showMessage('success', data.message || 'OTP sent successfully!');
                goToStep(2);
                startTimer(data.expiry);
            } else {
                showMessage('error', data.message || 'User not found!');
            }
        } catch (err) {
            console.log(err);
            showMessage('error', 'Server error! Try again later.');
        }

    }

    // Timer for OTP
    function startTimer(time) {
        timeLeft = time;
        updateTimer();
        clearInterval(countdown);

        countdown = setInterval(() => {
            timeLeft--;
            updateTimer();
            if (timeLeft <= 0) {
                clearInterval(countdown);
                showMessage('error', 'OTP expired. Please request a new one.');
            }
        }, 1000);
    }

    function updateTimer() {
    document.getElementById('timer').textContent = `00:${timeLeft.toString().padStart(2,'0')}`;
}

    // Verify OTP
    async function verifyOTP() {
        const otp = document.getElementById('otp').value.trim();

        if (!otp || otp.length !== 6) {
            showMessage('error', 'Enter a valid 6-digit OTP');
            return;
        }

        if (timeLeft <= 0) {
            showMessage('error', 'OTP expired. Please request a new one.');
            return;
        }

        showMessage('info', 'Verifying OTP...');

        try {
            const response = await fetch(`${BASE_PATH}api/PasswordReset/verify-otp`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({userIdentifier, otp})
            });

            const data = await response.json();

            if (response.ok && data.success) {
                showMessage('success', 'OTP verified successfully!');
                goToStep(3);
                timeLeft += 300;
            } else {
                showMessage('error', data.message || 'Invalid OTP');
            }
        } catch (err) {
            showMessage('error', 'Server error! Try again later.');
        }
    }

    async function resendOTP() {
        if (timeLeft > 0) {
            showMessage('info', 'Resending OTP...');
        }

        try {
            if (userIdentifier == null && userIdentifier === '' ) return;
            const response = await fetch(`${BASE_PATH}api/PasswordReset/send-otp`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({userIdentifier})
            });

            const data = await response.json();
            console.log("Response Data of resend code :", data);

            if (data.success) {  // <-- use JSON 'success' field
                showMessage('success', data.message || 'OTP sent successfully!');
                startTimer(data.expiry);
            } else {
                showMessage('error', data.message || 'User not found!');
            }
        } catch (err) {
            console.log(err);
            showMessage('error', 'Server error! Try again later.');
        }
    }

    // Update password
    async function updatePassword() {
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        if (!newPassword) {
            showMessage('error', 'Enter new password');
            return;
        }
        if (newPassword !== confirmPassword) {
            showMessage('error', 'Passwords do not match');
            return;
        }
        if (newPassword.length < 8) {
            showMessage('error', 'Password must be at least 8 characters');
            return;
        }
        // ✅ Must contain: lowercase, uppercase, digit, special char
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/;
        if (!passwordRegex.test(newPassword)) {
            showMessage(
                'error',
                'Password must include at least 1 uppercase, 1 lowercase, 1 digit, and 1 special character');
            return;
        }

        showMessage('info', 'Updating password...');

        try {
            const response = await fetch(`${BASE_PATH}profileController/api/forget-password`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({userIdentifier, newPassword, confirmPassword})
            });

            const data = await response.json();

            if (response.ok && data.success) {
                showMessage('success', data.message || 'Password updated successfully! Redirecting to login...');
                setTimeout(() => window.location.href = `${BASE_PATH}login`, 2000);
            } else {
                showMessage('error', data.message || 'Failed to update password!');
            }
        } catch (err) {
            showMessage('error', 'Server error! Try again later.');
        }
    }

    // Password strength check
    document.getElementById('newPassword').addEventListener('input', function() {
    const password = this.value;
    const strengthBar = document.getElementById('passwordStrength');
    let strength = 0;
    if (password.length >= 8) strength += 25;
    if (/[A-Z]/.test(password)) strength += 25;
    if (/[0-9]/.test(password)) strength += 25;
    if (/[^A-Za-z0-9]/.test(password)) strength += 25;
    strengthBar.style.width = strength + '%';
    strengthBar.style.backgroundColor = strength < 50 ? '#e53935' : strength < 75 ? '#ffb300' : '#43a047';
});
