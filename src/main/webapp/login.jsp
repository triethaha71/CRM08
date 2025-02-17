<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập hệ thống</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" integrity="sha512-9usAa10IRO0HhonpyAIVpjrylPvoDwiPUiKdWk5t3PyolY1cOd4DSE0Ga+ri4AuTroPR5aQvXU9xC6qOPnzFeg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <style>
        body {
            background: linear-gradient(135deg, #71b7e6, #9b59b6); /* Gradient background */
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; /* Modern font */
            margin: 0; /* Reset default margin */
        }

        .login-container {
            width: 400px;
            background-color: #fff;
            padding: 40px;
            border-radius: 12px; /* Rounded corners */
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2); /* Stronger shadow */
            transition: transform 0.3s ease-in-out; /* Smooth transition */
        }

        .login-container:hover {
            transform: translateY(-5px); /* Slight lift on hover */
        }

        .login-title {
            text-align: center;
            margin-bottom: 30px;
            color: #333; /* Darker text */
            font-size: 2rem; /* Larger title */
            font-weight: 500; /* Semi-bold */
            letter-spacing: 1px; /* Spaced letters */
        }

        .form-group {
            margin-bottom: 25px; /* More spacing */
        }

        .form-control {
            border-radius: 8px;
            padding: 14px; /* Larger input */
            border: 1px solid #ddd; /* Lighter border */
            font-size: 1rem;
            transition: border-color 0.3s ease; /* Border transition */
        }

        .form-control:focus {
            border-color: #555; /* Darker border on focus */
            box-shadow: none; /* Remove default shadow */
            outline: none; /* Remove outline */
        }

        .btn-primary {
            width: 100%;
            padding: 14px;
            border-radius: 8px;
            background-color: #5cb85c; /* Green button */
            border-color: #5cb85c;
            font-size: 1.1rem;
            font-weight: 500;
            transition: background-color 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #449d44; /* Darker green */
            border-color: #419641;
        }

        .remember-me {
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            font-size: 0.9rem;
            color: #777;
        }

        .remember-me input[type="checkbox"] {
            margin-right: 8px;
        }

        .error-message {
            color: #d9534f;
            margin-top: 15px;
            text-align: center;
            font-size: 0.9rem;
        }

        /* Icon styling */
        .login-title i {
            margin-right: 8px;
            color: #666;
        }

        .form-group i {
            margin-right: 10px;
            color: #888;
        }

        /* Placeholder styling */
        ::-webkit-input-placeholder { /* Chrome/Opera/Safari */
            color: #aaa;
            font-style: italic;
        }

        ::-moz-placeholder { /* Firefox 19+ */
            color: #aaa;
            font-style: italic;
        }

        :-ms-input-placeholder { /* IE 10+ */
            color: #aaa;
            font-style: italic;
        }

        :-moz-placeholder { /* Firefox 18- */
            color: #aaa;
            font-style: italic;
        }
    </style>
</head>
<body>

    <div class="login-container">
        <h3 class="login-title">
            <i class="fas fa-user-circle"></i> ĐĂNG NHẬP HỆ THỐNG
        </h3>
        <form action="login" method="post">
            <div class="form-group">
                <i class="fas fa-envelope"></i>
                <label for="email">Email</label>
                <input type="email" class="form-control" id="email" name="email" value="${email}" placeholder="Nhập email">
            </div>
            <div class="form-group">
                <i class="fas fa-lock"></i>
                <label for="password">Mật khẩu</label>
                <input type="password" class="form-control" id="password" name="password" value="${password}" placeholder="Nhập mật khẩu">
            </div>
            <div class="form-group form-check">
                <input type="checkbox" class="form-check-input" name="remember-me" id="remember-me">
                <label class="form-check-label" for="remember-me">Ghi nhớ</label>
            </div>
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-sign-in-alt"></i> Đăng nhập
            </button>
            <div class="error-message">${message}</div>
        </form>
    </div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>
</html>